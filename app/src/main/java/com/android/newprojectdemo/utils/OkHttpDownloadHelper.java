package com.android.newprojectdemo.utils;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import android.text.TextUtils;

import com.android.newprojectdemo.service.ServiceClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpDownloadHelper {

    private final String downloadUrl;
    private final String fileName;
    private final String filePath;
    private String savePath;
    private DownloadListener downloadListener;

    private final Handler mHandler;
    private Call call;

    private OkHttpDownloadHelper(Builder builder) {
        this.downloadUrl = builder.downloadUrl;
        this.fileName = builder.fileName;
        this.savePath = builder.savePath;
        this.downloadListener = builder.downloadListener;

        mHandler = new Handler(Looper.getMainLooper());

        if (TextUtils.isEmpty(savePath)) {
            savePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .getAbsolutePath();
        }
        filePath = savePath + File.separator + fileName;
    }

    public void startDownload() {
        final File file = new File(filePath);

        final Request request = new Request.Builder().url(downloadUrl).build();
        call = ServiceClient.getOkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull final Call call, @NonNull IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!call.isCanceled() && downloadListener != null) {
                            downloadListener.onFailure("下载失败");
                        }
                    }
                });
            }

            @Override
            public void onResponse(@NonNull final Call call, @NonNull Response response) throws IOException {
                InputStream is = null;
                final byte[] buf = new byte[2048];
                int len;
                FileOutputStream fos = null;
                try {
                    final long total = response.body().contentLength();
                    long current = 0;
                    is = response.body().byteStream();
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        current += len;
                        final long finalCurrent = current;
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (!call.isCanceled() && downloadListener != null) {
                                    downloadListener.onProgress(finalCurrent, total);
                                }
                            }
                        });
                        fos.write(buf, 0, len);
                    }
                    fos.flush();

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (!call.isCanceled() && downloadListener != null) {
                                downloadListener.onSuccess(file);
                            }
                        }
                    });
                } catch (IOException e) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (downloadListener != null) {
                                downloadListener.onFailure("下载已取消");
                            }
                        }
                    });
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void cancelDownload() {
        if (call != null && !call.isCanceled()) {
            call.cancel();
        }
    }

    public static class Builder {

        private final String downloadUrl;
        private final String fileName;
        private String savePath;
        private DownloadListener downloadListener;

        public Builder(String downloadUrl, String fileName) {
            this.downloadUrl = downloadUrl;
            this.fileName = fileName;
        }

        public Builder setSavePath(String savePath) {
            this.savePath = savePath;
            return this;
        }

        public Builder setDownloadListener(DownloadListener downloadListener) {
            this.downloadListener = downloadListener;
            return this;
        }

        public OkHttpDownloadHelper bulid() {
            return new OkHttpDownloadHelper(this);
        }
    }

    public interface DownloadListener {
        void onSuccess(File file);

        void onFailure(String errorMessage);

        void onProgress(long currentLength, long totalLength);
    }
}
