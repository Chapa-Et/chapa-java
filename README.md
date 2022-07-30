# Chapa-Java

[![BUILD](https://github.com/yaphet17/chapa-java/actions/workflows/maven.yml/badge.svg)](https://github.com/yaphet17/chapa-java/actions/workflows/maven.yml/) [![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/yaphet17/chapa-java.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/yaphet17/chapa-java/context:java) [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT) 

Unofficial Java package for Chapa Payment Gateway.
## Documentation
Visit official [Chapa's API Documentation](https://developer.chapa.co/docs)
## Installation
 Add the below maven dependency to your `pom.xml` file.
 ```xml
    <dependency>
      <groupId>io.github.yaphet17</groupId>
      <artifactId>chapa</artifactId>
      <version>1.0.0</version>
    </dependency>
```
## Usage

Instantiate a `Chapa` class.
```java       
Chapa chapa = new Chapa("you-secrete-key");
```
To initialize transaction, you can specify your information by either using our `PostData` class

```java
PostData formData = PostData.builder()
        .amount(new BigDecimal("100"))
        .currency( "ETB")
        .first_name("Yafet")
        .last_name("Berhanu")
        .email("yafetberhanu3@gmail.com")
        .tx_ref(transactionRef)
        .callback_url("https://chapa.co")
        .customization_title("I love e-commerce")
        .customization_description("It is time to pay")
        .customization_logo("My logo")
        .build();
```
Or, you can use a string JSON data
```java
 String formData = " {
    'amount': '100',
    'currency': 'ETB',
    'email': 'abebe@bikila.com',
    'first_name': 'Abebe',
    'last_name': 'Bikila',
    'tx_ref': 'tx-myecommerce12345',
    'callback_url': 'https://chapa.co',
    'customization[title]': 'I love e-commerce',
    'customization[description]': 'It is time to pay'
  }"
}
```
Intitialize payment
```java
String reponseString = chapa.initialize(formData).asString(); // get reponse in a string JSON format
Map<String, String> responseMap = chapa.initialize(formData).asMap(); // get reponse as a Map object 
```
Verify payment
```java
String reponseString = chapa.verify("tx-myecommerce12345").asString(); // get reponse in a string JSON format
Map<String, String> responseMap = chapa.verify("tx-myecommerce12345").asMap(); // get reponse as a Map object 
```
