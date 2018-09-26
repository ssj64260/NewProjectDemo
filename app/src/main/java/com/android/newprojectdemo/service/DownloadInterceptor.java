package com.android.newprojectdemo.service;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class DownloadInterceptor implements Interceptor {
    private DownloadResponseBody.OnDownloadListener downloadListener;

    public DownloadInterceptor(DownloadResponseBody.OnDownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        return response.newBuilder()
                .body(new DownloadResponseBody(response.body(), downloadListener))
                .build();
    }

}
