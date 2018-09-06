package com.android.newprojectdemo.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.android.newprojectdemo.R;
import com.android.newprojectdemo.model.FirImBean;
import com.android.newprojectdemo.service.ServiceClient;
import com.android.newprojectdemo.ui.dialog.TipsAlertDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;


/**
 * 更新app工具类
 */

public class UpdateAppUtils {

    private File apkFile;//apk完整uri
    private Call call;

    private Context mContext;
    private FirImBean mUpdateInfo;
    private String mVersionName;

    private TipsAlertDialog alertDialog;

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    private String mCancelTips;
    private boolean mIsFinishDownload = false;

    public UpdateAppUtils(Context context, FirImBean updateInfo) {
        mContext = context;
        mUpdateInfo = updateInfo;

        mVersionName = mUpdateInfo.getVersionShort();
        final File apkDirectory = new File(SDCardUtils.getExternalFilesDir(mContext));
        final String apkName = String.format("BaoyingMall_%s.apk", mVersionName.replace(".", "_"));
        apkFile = new File(apkDirectory, apkName);

        mCancelTips = "取消";
//        if ("1".equals(mUpdateInfo.getIs_update())) {
//            mCancelTips = "退出程序";
//        } else {
//            mCancelTips = "取消";
//        }
    }

    public void showUpdateDialog() {
        alertDialog = new TipsAlertDialog(mContext);
        alertDialog.show();
        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    alertDialog.getTvCancel().callOnClick();
                }
                return false;
            }
        });
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mHandler.removeCallbacksAndMessages(null);
            }
        });

        setDialogStyle(false);
    }

    private void setDialogStyle(boolean isDownloading) {
        final TextView tvConfirm = alertDialog.getTvConfirm();
        final TextView tvCancel = alertDialog.getTvCancel();
        if (!tvConfirm.isEnabled()) {
            tvConfirm.setOnClickListener(dialogClick);
            tvConfirm.setEnabled(true);
        }
        if (!tvCancel.isEnabled()) {
            tvCancel.setOnClickListener(dialogClick);
            tvCancel.setEnabled(true);
        }
        if (isDownloading) {
            alertDialog.setTitleText("下载中...");
            tvConfirm.setVisibility(View.GONE);
            tvCancel.setText("取消下载");
        } else if (mIsFinishDownload) {
            alertDialog.setTitleText("下载完成");
            tvConfirm.setVisibility(View.VISIBLE);
            tvConfirm.setText("立即安装");
            tvCancel.setText(mCancelTips);
        } else {
            if (apkFile.exists()) {
                apkFile.delete();
            }

            final String title = "发现新版本：" + mVersionName;
            alertDialog.setTitleText(title);

            tvConfirm.setVisibility(View.VISIBLE);
            tvConfirm.setText("立即更新");
            tvCancel.setText(mCancelTips);
        }
    }

    private View.OnClickListener dialogClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_confirm:
                    if (mIsFinishDownload) {
                        installApk();
                    } else {
                        startDownload();
                    }
                    break;
                case R.id.tv_cancel:
                    if (call != null && !call.isCanceled()) {
                        call.cancel();
                    }
                    if (alertDialog != null && alertDialog.isShowing()) {
                        alertDialog.dismiss();
                    }
                    break;
            }
        }
    };

    private void startDownload() {
        setDialogStyle(true);

        final String downloadUrl = mUpdateInfo.getInstall_url();
        final Request request = new Request.Builder().url(downloadUrl).build();
        call = ServiceClient.getOkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull final Call call, @NonNull IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!call.isCanceled()) {
                            ToastMaster.show("下载失败，请重试");
                            setDialogStyle(false);
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
                    fos = new FileOutputStream(apkFile);
                    while ((len = is.read(buf)) != -1) {
                        current += len;
                        final long finalCurrent = current;
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (alertDialog != null && !call.isCanceled()) {
                                    String currentFileSize = FileUtils.formatFileSize(mContext, finalCurrent);
                                    String totalFileSize = FileUtils.formatFileSize(mContext, total);
                                    alertDialog.setTitleText("下载中：" + currentFileSize + "/" + totalFileSize);
                                }
                            }
                        });
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (!call.isCanceled()) {
                                mIsFinishDownload = true;
                                setDialogStyle(false);
                            }
                        }
                    });
                } catch (IOException e) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            ToastMaster.show("下载已取消");
                            setDialogStyle(false);
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

    private void installApk() {
        if (!apkFile.exists()) {
            setDialogStyle(false);
            ToastMaster.show("安装文件不存在，请重新下载");
        } else {
            final Intent intent = new Intent(Intent.ACTION_VIEW);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                final String authority = mContext.getPackageName() + ".file_provider";
                Uri fileUri = FileProvider.getUriForFile(mContext, authority, apkFile);

                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            }

            mContext.startActivity(intent);
        }
    }
}
