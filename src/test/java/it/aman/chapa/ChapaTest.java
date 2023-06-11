package it.aman.chapa;

import it.aman.chapa.client.IChapaClient;
import it.aman.chapa.exception.ChapaException;
import it.aman.chapa.model.*;
import it.aman.chapa.utility.Util;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ChapaTest {

    private PostData postData;
    private String postDataString;
    private SubAccountDto subAccountDto;

    @Mock private IChapaClient chapaClient;
    private Chapa chapa;

    @BeforeEach
    void setUp() {
        chapa = new Chapa(chapaClient, "");
        Customization customization = new Customization()
                .setTitle("E-commerce")
                .setDescription("It is time to pay")
                .setLogo("https://mylogo.com/log.png");
        postData = new PostData()
                .setAmount(new BigDecimal("100"))
                .setCurrency("ETB")
                .setFirstName("Abebe")
                .setLastName("Bikila")
                .setEmail("abebe@bikila.com")
                .setTxRef(Util.generateToken())
                .setCallbackUrl("https://chapa.co")
                .setReturnUrl("https://chapa.co")
                .setSubAccountId("testSubAccountId")
                .setCustomization(customization);
        postDataString = " { " +
                "'amount': '100', " +
                "'currency': 'ETB'," +
                "'email': 'abebe@bikila.com'," +
                "'first_name': 'Abebe'," +
                "'last_name': 'Bikila'," +
                "'tx_ref': 'tx-myecommerce12345'," +
                "'callback_url': 'https://chapa.co'," +
                "'return_url': 'https://chapa.co'," +
                "'subaccount[id]': 'testSubAccountId'," +
                "'customizations':{'customization[title]':'E-commerce','customization[description]':'It is time to pay','customization[logo]':'https://mylogo.com/log.png'}" +
                " }";
        subAccountDto = new SubAccountDto()
                .setBusinessName("Abebe Suq")
                .setAccountName("Abebe Bikila")
                .setAccountNumber("0123456789")
                .setBankCode("001")
                .setSplitType(SplitTypeEnum.PERCENTAGE)
                .setSplitValue(0.2);
    }

    @Test
    public void initializeTransaction_Fail() throws ChapaException {
        // verify
        Assertions.assertThrows(ChapaException.class, () -> chapa.initialize(new PostData()));
    }

    @Test
    public void initializeTransaction_success() throws ChapaException {
        // when
        InitializeResponseData.Data data = new InitializeResponseData.Data();
        data.setCheckOutUrl("");
        InitializeResponseData response = new InitializeResponseData("","","", 200, data);
        when(chapaClient.initialize(anyString(), anyMap())).thenReturn(response);

        // verify
        InitializeResponseData responseData = new Chapa(chapaClient, "").initialize(postData);

        Assertions.assertNotNull(responseData);
        Assertions.assertNotNull(responseData.getData().getCheckOutUrl());
    }

    @Test
    public void initializeTransaction_success2() throws ChapaException {
        // when
        InitializeResponseData.Data data = new InitializeResponseData.Data();
        data.setCheckOutUrl("");
        InitializeResponseData response = new InitializeResponseData("","","", 200, data);
        when(chapaClient.initialize(anyString(), anyString())).thenReturn(response);

        // verify
        InitializeResponseData responseData = new Chapa(chapaClient, "").initialize(postDataString);

        Assertions.assertNotNull(responseData);
        Assertions.assertNotNull(responseData.getData().getCheckOutUrl());
    }


    @Test
    public void getBank_success() throws ChapaException {
        // when
        when(chapaClient.getBanks(anyString())).thenReturn(Collections.singletonList(new Bank()));

        // verify
        List<Bank> responseData = new Chapa(chapaClient, "").getBanks();

        Assertions.assertNotNull(responseData);
        Assertions.assertTrue(responseData.size() == 1);
    }
    @Test
    public void verifyTransaction_fail() {
        Assertions.assertThrows(ChapaException.class, () -> chapa.verify(""));
    }

    @Test
    public void verifyTransaction_success() throws ChapaException {
        // given
        VerifyResponseData expectedResponseData = new VerifyResponseData()
                .setMessage("Payment not paid yet")
                .setStatus("null")
                .setStatusCode(200)
                .setData(null);

        // when
        when(chapaClient.verify(anyString(), anyString())).thenReturn(expectedResponseData);
        VerifyResponseData actualResponseData = chapa.verify("test-transaction");

        // verify
        verify(chapaClient).verify(anyString(), anyString());
        Assertions.assertEquals(actualResponseData.getMessage(), "Payment not paid yet");
        Assertions.assertEquals(actualResponseData.getStatusCode(), 200);
    }

    @Test
    public void createSubAccount_success() throws Throwable {
        // given
        SubAccountResponseData expectedResponseData = new SubAccountResponseData()
                .setMessage("The Bank Code is incorrect please check if it does exist with our getbanks endpoint.")
                .setStatus("failed")
                .setStatusCode(200)
                .setData(null);

        // when
        when(chapaClient.createSubAccount(anyString(), anyMap())).thenReturn(expectedResponseData);
        SubAccountResponseData actualResponse = chapa.createSubAccount(subAccountDto);

        // then
        verify(chapaClient).createSubAccount(anyString(), anyMap());
        Assertions.assertEquals(actualResponse.getMessage(), "The Bank Code is incorrect please check if it does exist with our getbanks endpoint.");
        Assertions.assertEquals(actualResponse.getStatusCode(), 200);
    }
}