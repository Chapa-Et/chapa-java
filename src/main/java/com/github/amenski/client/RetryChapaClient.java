package com.github.amenski.client;

import com.github.amenski.client.provider.RetrierRetrofitClientProvider;
import com.github.amenski.exception.ChapaException;
import com.github.amenski.model.InitializeResponse;
import com.github.amenski.model.ResponseBanks;
import com.github.amenski.model.SubAccountResponse;
import com.github.amenski.model.VerifyResponse;

import java.util.Map;

/**
 *Retry capable Chapa client
 * since 1.0.1
 */
import static com.github.amenski.utility.StringUtils.isBlank;

public class RetryChapaClient implements IChapaClient {

    private final ChapaClient defaultChapaClient;

    public RetryChapaClient() {
        this("https://api.chapa.co/v1/");
    }

    public RetryChapaClient(final String url) {
        this(url, 3);
    }

    public RetryChapaClient(final String url, int maxRetries) {
        this(url, maxRetries, 10000);
    }

    public RetryChapaClient(final String url, int maxRetries, long timeOutMillis) {
        if (isBlank(url)) {
            throw new ChapaException("Unable to create a client. Api baseUrl can't be empty");
        }
        this.defaultChapaClient = new ChapaClient(url);
        this.defaultChapaClient.setChapaClientApi(new RetrierRetrofitClientProvider(timeOutMillis,maxRetries).buildClient(ChapaClientApi.class, url));
    }

    @Override
    public InitializeResponse initialize(String secretKey, Map<String, Object> fields) throws ChapaException {
        return defaultChapaClient.initialize(secretKey, fields);
    }

    @Override
    public InitializeResponse initialize(String secretKey, String body) throws ChapaException {
        return defaultChapaClient.initialize(secretKey, body);
    }

    @Override
    public VerifyResponse verify(String secretKey, String transactionReference) throws ChapaException {
        return defaultChapaClient.verify(secretKey, transactionReference);
    }

    @Override
    public ResponseBanks getBanks(String secretKey) throws ChapaException {
        return defaultChapaClient.getBanks(secretKey);
    }

    @Override
    public SubAccountResponse createSubAccount(String secretKey, Map<String, Object> fields) throws ChapaException {
        return defaultChapaClient.createSubAccount(secretKey, fields);
    }

    @Override
    public SubAccountResponse createSubAccount(String secretKey, String body) throws ChapaException {
        return defaultChapaClient.createSubAccount(secretKey, body);
    }
}
