```
 ,-----. ,--.                                             ,--.                              
'  .--./ |  ,---.   ,--,--.  ,---.   ,--,--. ,-----.      |  |  ,--,--. ,--.  ,--.  ,--,--. 
|  |     |  .-.  | ' ,-.  | | .-. | ' ,-.  | '-----' ,--. |  | ' ,-.  |  \  `'  /  ' ,-.  | 
'  '--'\ |  | |  | \ '-'  | | '-' ' \ '-'  |         |  '-'  / \ '-'  |   \    /   \ '-'  | 
 `-----' `--' `--'  `--`--' |  |-'   `--`--'          `-----'   `--`--'    `--'     `--`--'
```

Unofficial Java package for Chapa Payment Gateway.

## Features
- You can now implement `IChapaClient` interface and create your own custom implementation
  to use your favorite HTTP client.
- Includes split payment feature added by Chapa. You can now get list of supported banks, create
  sub-account and perform a split payment. See [Split Payment](https://developer.chapa.co/docs/split-payment/) documentation for more details.
- Additional utility methods to help you to generate a convenient token for your transactions, to map json string
  to `PostData` object etc.

## Table of Contents
1. [Documentation](#documentation)
2. [Installation](#installation)
3. [Usage](#usage)
4. [Contribution](#contribution)
5. [Example](#example)
6. [License](#license)

## Documentation
Visit official [Chapa's API Documentation](https://developer.chapa.co/docs)
## Installation
 Add the below maven dependency to your `pom.xml` file.
```xml
    <dependency>
      <groupId>com.github.amenski</groupId>
      <artifactId>chapa</artifactId>
      <version>1.0.1</version>
    </dependency>
```
Or add the below gradle dependency to your `build.gradle` file.
```groovy
    implementation 'com.github.amenski:chapa:1.0.0'
```

## Usage

> **Note** : This doc might not fully cover chapa-api. Please refer to the chapa developer doc for more. And contributions are welcome too. Thanks.


Instantiate a `Chapa` class.
```java       
Chapa chapa = new Chapa.ChapaBuilder()
        .client(new ChapaClient()) // --> default implementation
        .secretKey("secret-key")
        .build();
```
Or if you want to use your own implementation, implement the methods from `IChapaClient` interface.
```java
import com.github.amenski.client.ChapaClient;

public class MyCustomChapaClient implements IChapaClient {
  ...
}


Chapa chapa = new Chapa(new MyCustomChapaClient(), "secrete-key");
```

## Retries (with ExponentialBackoff)
To add retries to clients, use `RetrierRetrofitClientProvider`. This class allows configuring 3 retries if a call fails.
Provides retrofit client to make calls to chapa api. <br>
Retry will be done with an <a href="https://en.wikipedia.org/wiki/Exponential_backoff">ExponentialBackoff</a> strategy.<br>
<pre>
Example usage:
public class CustomChapaClient implements IChapaClient {
    private String baseUrl = "https://api.chapa.co/v1/";
    private ChapaClientApi chapaClientApi;
    .
    .
    private void buildApiClient() {
         if (isBlank(baseUrl)) throw new ChapaException("Unable to create a client. Api baseUrl can't be empty");
         chapaClientApi = new RetrierRetrofitClientProvider().buildClient(ChapaClientApi.class, baseUrl);
    }
}
</pre>


To initialize a transaction, you simply need to specify your information by either using our `PostData` class.

```java
Customization customization = new Customization()
        .setTitle("E-commerce")
        .setDescription("It is time to pay")
        .setLogo("https://mylogo.com/log.png");
PostData postData = new PostData()
        .setAmount("100")
        .setCurrency("ETB")
        .setFirstName("Abebe")
        .setLastName("Bikila")
        .setEmail("abebe@bikila.com")
        .setTxRef(UUID.randomUUID().toString())
        .setCallbackUrl("https://chapa.co")
        .setReturnUrl("https://chapa.co")
        .setSubAccountId("testSubAccountId")
        .setCustomization(customization);
        
        ...
        
        chapa.initialize(postData);
```
Or, as a string JSON data.
```java 
String postDataString = " { " +
        "'amount': '100', " +
        "'currency': 'ETB'," +
        "'email': 'abebe@bikila.com'," +
        "'first_name': 'Abebe'," +
        "'last_name': 'Bikila'," +
        "'tx_ref': 'tx-myecommerce12345'," +
        "'callback_url': 'https://chapa.co'," +
        "'subaccount[id]': 'ACCT_xxxxxxxxx'," +
        "'customizations':{" +
        "       'customization[title]':'E-commerce'," +
        "       'customization[description]':'It is time to pay'," +
        "       'customization[logo]':'https://mylogo.com/log.png'" +
        "   }" +
        " }";

        chapa.initialize(postDataString)
```
Intitialize payment
```java
InitializeResponseData responseData = chapa.initialize(postData) 
```
Verify payment
```java
VerifyResponseData actualResponseData = chapa.verify("tx-ref"); 
```
Get list of banks
```java
List<Bank> banks = chapa.getBanks();
```
To create a subaccount, you can specify your information by either using our `Subaccount` class.
```java
SubAccount subAccount = new SubAccountDto()
        .setBusinessName("Abebe Suq")
        .setAccountName("Abebe Bikila")
        .setAccountNumber("0123456789")
        .setBankCode("001")
        .setSplitType(SplitTypeEnum.PERCENTAGE)
        .setSplitValue(0.2);
        
        ...
        
        SubAccountResponseData response = chapa.createSubAccount(subAccountDto);
```
Or, you can use a string JSON data.
```java
String subAccount = " { " +
        "'business_name': 'Abebe Suq', " +
        "'account_name': 'Abebe Bikila'," +
        "'account_number': '0123456789'," +
        "'bank_code': '96e41186-29ba-4e30-b013-2ca36d7e7025'," +
        "'split_type': 'percentage'," +
        "'split_value': '0.2'" +
        " }";

        ...

        SubAccountResponseData actualResponse = chapa.createSubAccount(subAccount);
```
Create subaccount
```java
 SubAccountResponseData actualResponse = chapa.createSubAccount(subAccountDto);
```
## Example
```java
package com.github.amenski.chapa;

import com.github.amenski.Chapa;
import com.github.amenski.client.ChapaClient;
import com.github.amenski.ChapaException;
import com.github.amenski.model.Customization;
import com.github.amenski.model.PostData;
import com.github.amenski.model.ResponseBanks;
import com.github.amenski.model.SplitTypeEnum;
import com.github.amenski.model.SubAccountDto;

import java.util.UUID;

public class ChapaExample {

    public static void main(String[] args) {
        Chapa chapa = new Chapa.ChapaBuilder()
                .client(new ChapaClient())
                .secretKey("CHASECK_TEST-...")
                .build();

        Customization customization = new Customization()
                .setTitle("E-commerce")
                .setDescription("It is time to pay")
                .setLogo("https://mylogo.com/log.png");
        PostData postData = new PostData()
                .setAmount("100")
                .setCurrency("ETB")
                .setFirstName("Abebe")
                .setLastName("Bikila")
                .setEmail("abebe@bikila.com")
                .setTxRef(UUID.randomUUID().toString())
                .setCallbackUrl("https://chapa.co")
                .setReturnUrl("https://chapa.co")
                .setSubAccountId("testSubAccountId")
                .setCustomization(customization);

        SubAccountDto subAccountDto = new SubAccountDto()
                .setBusinessName("Abebe Suq")
                .setAccountName("Abebe Bikila")
                .setAccountNumber("0123456789")
                .setBankCode("853d0598-9c01-41ab-ac99-48eab4da1513")
                .setSplitType(SplitTypeEnum.PERCENTAGE)
                .setSplitValue(0.2);

        // list of banks
        ResponseBanks banks = chapa.getBanks();
        if ((banks == null || banks.getData() == null)) {
            System.out.println("Create SubAccount response: " + banks);
        } else {
            banks.getData().forEach(System.out::println);
        }
        // create subaccount
        System.out.println("Create SubAccount response: " + chapa.createSubAccount(subAccountDto));
        // initialize payment
        System.out.println("Initialize response: " + chapa.initialize(postData));
        // verify payment
        System.out.println("Verify response: " + chapa.verify(postData.getTxRef()));
    }
}
```
## Contribution
Please feel free to open an issue or pull request.

## License
MIT