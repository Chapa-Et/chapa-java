package com.github.amenski.client;

import com.github.amenski.model.InitializeResponseData;
import com.github.amenski.model.ResponseBanks;
import com.github.amenski.model.SubAccountResponseData;
import com.github.amenski.model.VerifyResponseData;
import com.github.amenski.exception.ChapaException;

import java.util.Map;

public interface IChapaClient {

    /**
     * Initializes transaction
     *
     * @see <a href="https://developer.chapa.co/docs/accept-payments/">Chapa Dcoumentation</a>
     *
     * @param secretKey
     * @param fields
     * @return InitializeResponseData
     * @throws ChapaException
     */
    InitializeResponseData initialize(final String secretKey, Map<String, Object> fields) throws ChapaException;

    /**
     * An alias to {@link #initialize(String, Map)}
     *
     * @see <a href="https://developer.chapa.co/docs/accept-payments/">Chapa Dcoumentation</a>
     *
     * @param secretKey
     * @param body
     * @return InitializeResponseData
     * @throws ChapaException
     */
    InitializeResponseData initialize(final String secretKey, final String body) throws ChapaException;

    /**
     * Verify a transaction
     *
     * @see <a href="https://developer.chapa.co/docs/verify-payments/">Chapa Dcoumentation</a>
     *
     * @param secretKey
     * @param transactionReference
     * @return VerifyResponseData
     * @throws ChapaException
     */
    VerifyResponseData verify(String secretKey, String transactionReference) throws ChapaException;

    /**
     * Get all supported banks
     *
     * @see <a href="https://developer.chapa.co/docs/transfers/">Chapa Dcoumentation</a>
     *
     * @param secretKey
     * @return ResponseBanks
     * @throws ChapaException
     */
    ResponseBanks getBanks(String secretKey) throws ChapaException;

    /**
     *
     * Create sub account for split payment
     *
     * @see <a href="https://developer.chapa.co/docs/split-payment/">Chapa Dcoumentation</a>
     *
     * @param secretKey
     * @param fields
     * @return SubAccountResponseData
     * @throws ChapaException
     */
    SubAccountResponseData createSubAccount(String secretKey, Map<String, Object> fields) throws ChapaException;

    /**
     * Create sub account for split payment, an alternative method to {@link #createSubAccount(String, Map)}
     *
     * @see <a href="https://developer.chapa.co/docs/split-payment/">Chapa Dcoumentation</a>
     *
     * @param body
     * @param secretKey
     * @return SubAccountResponseData
     * @throws ChapaException
     */
    SubAccountResponseData createSubAccount(String secretKey, String body) throws ChapaException;
}
