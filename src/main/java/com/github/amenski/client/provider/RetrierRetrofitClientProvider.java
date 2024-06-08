package com.github.amenski.client.provider;

import com.github.amenski.client.ChapaClientApi;
import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.retrofit.RetryCallAdapter;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/**
 *
 * Provides retrofit client to make calls to chapa api. <br>
 *
 * This implementation will retry calls to chapa api upto 3 (default) times. You can set the number of retries too.<br>
 *
 * Retry will be done with an <a href="https://en.wikipedia.org/wiki/Exponential_backoff">ExponentialBackoff</a> strategy.<br>
 *
 * Timeouts are all set to 10_000 millis <br>
 *
 *
 * <pre>
 * Example usage:
 * public class CustomChapaClient implements IChapaClient {
 *
 *     private String baseUrl = "https://api.chapa.co/v1/";
 *     private ChapaClientApi chapaClientApi;
 *     .
 *     .
 *     private void buildApiClient() {
 *          if (isBlank(baseUrl)) throw new ChapaException("Unable to create a client. Api baseUrl can't be empty");
 *          chapaClientApi = new RetrierRetrofitClientProvider().buildClient(ChapaClientApi.class, baseUrl);
 *     }
 * }
 * </pre>
 */
public class RetrierRetrofitClientProvider implements RetrofitClientProvider {

    private int retryCount = 3;
    private long timeOutMillis = 10000;

    public RetrierRetrofitClientProvider() {
    }

    public RetrierRetrofitClientProvider(long timeOutMillis) {
        this(timeOutMillis, 3);
    }

    public RetrierRetrofitClientProvider(int retryCount) {
        this(10000, retryCount);
    }

    public RetrierRetrofitClientProvider(long timeOutMillis, int retryCount) {
        this.timeOutMillis = timeOutMillis;
        this.retryCount = retryCount;
    }

    @Override
    public Retrofit.Builder provideRetrofitBuilder(String baseUrl) {
        OkHttpClient client = new OkHttpClient.Builder()
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .connectTimeout(timeOutMillis, TimeUnit.MILLISECONDS)
//                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .readTimeout(timeOutMillis, TimeUnit.MILLISECONDS)
                .writeTimeout(timeOutMillis, TimeUnit.MILLISECONDS)
                .build();

        return new Retrofit.Builder()
                .addCallAdapterFactory(RetryCallAdapter.of(retry(timeOutMillis)))
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client);
    }

    private Retry retry(long initialIntervalMillis) {
        RetryConfig retryCOnfig = RetryConfig.custom()
                .maxAttempts(retryCount)
                .intervalFunction(IntervalFunction.ofExponentialBackoff(initialIntervalMillis, 2))
                .retryOnResult(new RetryPredicate())
                .failAfterMaxAttempts(true)
                .build();

        return Retry.of("chapa-retry", retryCOnfig);
    }

    private static class RetryPredicate implements Predicate<Object> {

        @Override
        public boolean test(Object value) {
            try {
                Response<Object> response = (Response<Object>) value;
                return !response.isSuccessful();
            } catch (ClassCastException e) {
                return true;
            }
        }
    }
}
