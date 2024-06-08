package com.github.amenski.client;

import com.github.amenski.client.provider.DefaultRetrofitClientProvider;
import com.github.amenski.model.InitializeResponse;
import com.github.amenski.model.ResponseBanks;
import com.github.amenski.model.SubAccountResponse;
import com.github.amenski.model.VerifyResponse;
import com.github.amenski.exception.ChapaException;
import okhttp3.ResponseBody;
import retrofit2.Response;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static com.github.amenski.utility.StringUtils.*;
import static com.github.amenski.utility.Util.jsonToMap;


public class ChapaClient implements IChapaClient {

    public static final String BEARER = "Bearer ";
    private String baseUrl = "https://api.chapa.co/v1/";
    private ChapaClientApi chapaClientApi;

    public ChapaClient() {
        this("https://api.chapa.co/v1/");
    }

    public ChapaClient(final String url) {
        if(isNotBlank(url)) {
            this.baseUrl = url;
        }
        this.buildApiClient();
    }

    @Override
    public InitializeResponse initialize(final String secretKey, Map<String, Object> fields) {
        try {
            Response<InitializeResponse> response = chapaClientApi.initialize(BEARER + secretKey, fields).execute();
            if (!response.isSuccessful()) {
                throw new ChapaException(extractErrorMessageOrDefault(response.errorBody(), "Unable to Initialize transaction."));
            }
            return response.body();
        } catch (IOException e) {
            throw new ChapaException("Unable to Initialize transaction.");
        }
    }

    @Override
    public InitializeResponse initialize(final String secretKey, final String body) {
        return this.initialize(secretKey, jsonToMap(body));
    }

    @Override
    public VerifyResponse verify(final String secretKey, final String transactionReference) {
        try {
            Response<VerifyResponse> response = chapaClientApi.verify(BEARER + secretKey, transactionReference).execute();
            if (!response.isSuccessful()) {
                throw new ChapaException(extractErrorMessageOrDefault(response.errorBody(), "Unable to verify transaction."));
            }
            return response.body();
        } catch (IOException e) {
            throw new ChapaException("Unable to verify transaction.");
        }
    }

    @Override
    public ResponseBanks getBanks(final String secretKey) {
        try {
            Response<ResponseBanks> response = chapaClientApi.banks(BEARER + secretKey).execute();
            if (!response.isSuccessful()) {
                throw new ChapaException(extractErrorMessageOrDefault(response.errorBody(), "Unable to get bank details."));
            }
            return response.body() != null? response.body() : null;
        } catch (ChapaException | IOException e) {
            throw new ChapaException("Unable to get bank details.");
        }
    }

    @Override
    public SubAccountResponse createSubAccount(final String secretKey, Map<String, Object> fields) {
        try {
            Response<SubAccountResponse> response = chapaClientApi.createSubAccount(BEARER + secretKey, fields).execute();
            if (!response.isSuccessful()) {
                throw new ChapaException(extractErrorMessageOrDefault(response.errorBody(), "Unable to create sub account."));
            }
            return response.body();
        } catch (IOException e) {
            throw new ChapaException("Unable to create sub account.");
        }
    }

    @Override
    public SubAccountResponse createSubAccount(final String secretKey, final String body) throws ChapaException {
        return this.createSubAccount(secretKey, jsonToMap(body));
    }
    
    private void buildApiClient() {
        if (isBlank(baseUrl)) throw new ChapaException("Unable to create a client. Api baseUrl can't be empty");
        chapaClientApi = new DefaultRetrofitClientProvider().buildClient(ChapaClientApi.class, baseUrl);
    }

    private String extractErrorMessageOrDefault(ResponseBody errorMessage, String defaultMessage) {
        return Optional.ofNullable(errorMessage).map(t -> {
            try {
                return t.string();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).orElse(defaultMessage);
    }

}
