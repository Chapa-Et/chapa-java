package com.github.amenski;

import com.github.amenski.client.IChapaClient;
import com.github.amenski.exception.ChapaException;
import com.github.amenski.model.Bank;
import com.github.amenski.model.Customization;
import com.github.amenski.model.InitializeResponse;
import com.github.amenski.model.PostData;
import com.github.amenski.model.ResponseBanks;
import com.github.amenski.model.SplitTypeEnum;
import com.github.amenski.model.SubAccountDto;
import com.github.amenski.model.SubAccountResponse;
import com.github.amenski.model.VerifyResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.anyMap;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


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
    public void initializeTransaction_Fail() {
        // verify
        Assertions.assertThrows(ChapaException.class, () -> chapa.initialize(new PostData()));
    }

    @Test
    public void initializeTransaction_success() throws ChapaException {
        // when
        InitializeResponse.Data data = new InitializeResponse.Data();
        data.setCheckOutUrl("");
        InitializeResponse response = new InitializeResponse("","","", 200, data);
        when(chapaClient.initialize(anyString(), anyMap())).thenReturn(response);

        // verify
        InitializeResponse responseData = chapa.initialize(postData);

        assertNotNull(responseData);
        assertNotNull(responseData.getData().getCheckOutUrl());
    }

    @Test
    public void initializeTransaction_success2() throws ChapaException {
        // when
        InitializeResponse.Data data = new InitializeResponse.Data();
        data.setCheckOutUrl("");
        InitializeResponse response = new InitializeResponse("","","", 200, data);
        when(chapaClient.initialize(anyString(), anyString())).thenReturn(response);

        // verify
        InitializeResponse responseData = chapa.initialize(postDataString);

        assertNotNull(responseData);
        assertNotNull(responseData.getData().getCheckOutUrl());
    }


    @Test
    public void getBank_success() throws ChapaException {
        // when
        when(chapaClient.getBanks(anyString())).thenReturn(new ResponseBanks().setData(Collections.singletonList(new Bank())));

        // verify
        ResponseBanks responseData = chapa.getBanks();

        assertNotNull(responseData);
        assertFalse(responseData.getData().isEmpty());
    }

    @Test
    public void verifyTransaction_fail() {
        Assertions.assertThrows(ChapaException.class, () -> chapa.verify(""));
    }

    @Test
    public void verifyTransaction_success() throws ChapaException {
        // given
        VerifyResponse expectedResponseData = new VerifyResponse()
                .setMessage("Payment not paid yet")
                .setStatus("null")
                .setStatusCode(200)
                .setData(null);

        // when
        when(chapaClient.verify(anyString(), anyString())).thenReturn(expectedResponseData);
        VerifyResponse actualResponseData = chapa.verify("test-transaction");

        // verify
        verify(chapaClient).verify(anyString(), anyString());
        assertEquals(actualResponseData.getMessage(), "Payment not paid yet");
        assertEquals(actualResponseData.getStatusCode(), 200);
    }

    @Test
    public void createSubAccount_success() throws Throwable {
        // given
        SubAccountResponse expectedResponseData = new SubAccountResponse()
                .setMessage("The Bank Code is incorrect please check if it does exist with our getbanks endpoint.")
                .setStatus("failed")
                .setStatusCode(200);

        // when
        when(chapaClient.createSubAccount(anyString(), anyMap())).thenReturn(expectedResponseData);
        SubAccountResponse actualResponse = chapa.createSubAccount(subAccountDto);

        // then
        verify(chapaClient).createSubAccount(anyString(), anyMap());
        assertEquals(actualResponse.getMessage(), "The Bank Code is incorrect please check if it does exist with our getbanks endpoint.");
        assertEquals(actualResponse.getStatusCode(), 200);
    }
}