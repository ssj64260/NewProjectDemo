package com.android.newprojectdemo.service;


import com.android.newprojectdemo.config.ServiceApi;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求配置
 */

public class ServiceClient {

    private static ServiceApi mService;

    private static OkHttpClient.Builder mClientBuilder;

    public static ServiceApi getService() {
        if (mService == null) {
            synchronized (ServiceClient.class) {
                if (mService == null) {
                    createService();
                }
            }
        }
        return mService;
    }

    public static ServiceApi getDownloadService(DownloadResponseBody.OnDownloadListener downloadListener) {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor(true))
                .addInterceptor(new DownloadInterceptor(downloadListener))
                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                .readTimeout(10000, TimeUnit.MILLISECONDS)
                .writeTimeout(10000, TimeUnit.MILLISECONDS)
                .build();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServiceApi.BASE_HOST)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit.create(ServiceApi.class);
    }

    public static OkHttpClient getOkHttpClient() {
        if (mClientBuilder == null) {
            mClientBuilder = new OkHttpClient.Builder()
                    .addInterceptor(new LoggerInterceptor(true))
                    .connectTimeout(10000, TimeUnit.MILLISECONDS)
                    .readTimeout(10000, TimeUnit.MILLISECONDS)
                    .writeTimeout(10000, TimeUnit.MILLISECONDS);
//            initIgonreVerification();
        }
        return mClientBuilder.build();
    }

    private static void createService() {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServiceApi.BASE_HOST)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpClient())
                .build();

        mService = retrofit.create(ServiceApi.class);
    }

    //忽略验证的初始化
    private static void initIgonreVerification() {
        try {
            final X509TrustManager trustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }
            };

            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());

            final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            mClientBuilder.sslSocketFactory(sslContext.getSocketFactory(), trustManager)
                    .hostnameVerifier(DO_NOT_VERIFY);

        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new NullPointerException("Failed to initialize the ignored authentication client");
        }
    }

}
