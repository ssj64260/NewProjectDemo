package com.android.newprojectdemo.a_test.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.android.newprojectdemo.R;
import com.android.newprojectdemo.a_test.ui.ButtonListAdapter;
import com.android.newprojectdemo.app.BaseActivity;
import com.android.newprojectdemo.service.DownloadResponseBody;
import com.android.newprojectdemo.service.ServiceClient;
import com.android.newprojectdemo.ui.adapter.IOnListClickListener;
import com.android.newprojectdemo.ui.adapter.OnListClickListener;
import com.android.newprojectdemo.utils.FileUtils;
import com.android.newprojectdemo.utils.OkHttpDownloadHelper;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * retrofit 和 rxjava 请求demo
 */
public class Test_RetrofitAndRxjavaActivity extends BaseActivity {

    private TextView tvTips;
    private RecyclerView rvButtonList;

    private List<String> mButtonList;
    private ButtonListAdapter mButtonAdapter;

    private Disposable mDisposable;
    private OkHttpDownloadHelper mHelper;

    public static void show(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, Test_RetrofitAndRxjavaActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.test_activity_retrofit_and_rxjava;
    }

    @Override
    protected void initData() {
        super.initData();

        mButtonList = new ArrayList<>();
        mButtonList.add("Retrofit + Rxjava下载");
        mButtonList.add("OkHttp下载");
        mButtonList.add("取消");

        mButtonAdapter = new ButtonListAdapter(this, mButtonList);
        mButtonAdapter.setListClick(mListClick);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        tvTips = findViewById(R.id.tv_tips);
        rvButtonList = findViewById(R.id.rv_button_list);
        rvButtonList.setLayoutManager(new LinearLayoutManager(this));
        rvButtonList.setAdapter(mButtonAdapter);
    }

    private void doDownload() {
        doCancel();

        tvTips.setText("开始下载");

        final String path = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                .getAbsolutePath()
                + File.separator + "纪念碑谷2.apk";

        final String url = "http://dlied5.myapp.com/myapp/6337/1106097521/jnbg2/10006654_com.tencent.tmgp.jnbg2_u102_1.12.3_0289c2.apk";

        mDisposable = ServiceClient.getDownloadService(mDownload).download(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        final InputStream inputStream = responseBody.byteStream();
                        if (inputStream != null) {
                            if (FileUtils.write(inputStream, path)) {
                                final File file = new File(path);
                                final Uri uri = Uri.fromFile(file);
                                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));

                                tvTips.setText("下载成功，文件路径：" + path);
                            } else {
                                tvTips.setText("下载文件保存失败!");
                            }
                        } else {
                            tvTips.setText("下载失败，Url：" + url);
                        }
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        tvTips.setText(throwable.getMessage());
                    }
                })
                .subscribe();
    }

    private void doOkHttpDownload() {
        doCancel();

        final String downloadUrl = "http://dlied5.myapp.com/myapp/6337/1106097521/jnbg2/10006654_com.tencent.tmgp.jnbg2_u102_1.12.3_0289c2.apk";
        final String fileName = "纪念碑谷2.apk";

        mHelper = new OkHttpDownloadHelper.Builder(downloadUrl, fileName)
                .setDownloadListener(new OkHttpDownloadHelper.DownloadListener() {
                    @Override
                    public void onSuccess(File file) {
                        tvTips.setText("下载成功，保存路径：" + file.getAbsolutePath());
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        tvTips.setText(errorMessage);
                    }

                    @Override
                    public void onProgress(long currentLength, long totalLength) {
                        final String currentFileSize = FileUtils.formatFileSize(Test_RetrofitAndRxjavaActivity.this, currentLength);
                        final String totalFileSize = FileUtils.formatFileSize(Test_RetrofitAndRxjavaActivity.this, totalLength);
                        tvTips.setText("通过OkHttp已下载：" + currentFileSize + "/" + totalFileSize);
                    }
                })
                .bulid();

        mHelper.startDownload();
    }

    private void doCancel() {
        if (mDisposable != null && !mDisposable.isDisposed()) mDisposable.dispose();

        if (mHelper != null) mHelper.cancelDownload();
    }

    private IOnListClickListener mListClick = new OnListClickListener() {
        @Override
        public void onItemClick(int position) {
            switch (position) {
                case 0:
                    doDownload();
                    break;
                case 1:
                    doOkHttpDownload();
                    break;
                default:
                    doCancel();
                    break;
            }
        }
    };

    private DownloadResponseBody.OnDownloadListener mDownload = new DownloadResponseBody.OnDownloadListener() {
        @Override
        public void onProgress(final long progress, final long total) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final String currentFileSize = FileUtils.formatFileSize(Test_RetrofitAndRxjavaActivity.this, progress);
                    final String totalFileSize = FileUtils.formatFileSize(Test_RetrofitAndRxjavaActivity.this, total);
                    tvTips.setText("通过Retrofit已下载：" + currentFileSize + "/" + totalFileSize);
                }
            });
        }
    };
}
