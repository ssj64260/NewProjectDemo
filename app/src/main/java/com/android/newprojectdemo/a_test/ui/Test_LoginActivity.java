package com.android.newprojectdemo.a_test.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.newprojectdemo.R;
import com.android.newprojectdemo.a_test.model.Test_UserInfoDetailBean;
import com.android.newprojectdemo.a_test.service.Test_ServiceClient;
import com.android.newprojectdemo.app.BaseActivity;
import com.android.newprojectdemo.db.LiteOrmHelper;
import com.android.newprojectdemo.model.FirImBean;
import com.android.newprojectdemo.model.ServiceResult;
import com.android.newprojectdemo.service.MyObserver;
import com.android.newprojectdemo.utils.ToastMaster;
import com.android.newprojectdemo.utils.UpdateAppUtils;
import com.android.newprojectdemo.utils.VersionUtils;
import com.android.newprojectdemo.widget.imageloader.GlideCircleTransform;
import com.android.newprojectdemo.widget.imageloader.ImageLoaderFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 测试用：登录页面
 */

public class Test_LoginActivity extends BaseActivity {

    private EditText etPhoneNumber;
    private ImageView ivClearPhone;
    private EditText etPassword;
    private ImageView ivClearPassword;
    private TextView tvDoLogin;
    private TextView tvJsonText;
    private ImageView ivAvatar;

    private Test_UserInfoDetailBean mUserInfo;

    private View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_toolbar_back:
                    hideKeyboard();
                    onBackPressed();
                    break;
                case R.id.tv_toolbar_action:
                    hideKeyboard();
                    doUpdateCheck();
                    break;
                case R.id.iv_clear_phone:
                    etPhoneNumber.setText("");
                    break;
                case R.id.iv_clear_password:
                    etPassword.setText("");
                    break;
                case R.id.tv_do_login:
                    hideKeyboard();
                    doLogin();
                    break;
            }
        }
    };

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            setEditStatus();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private View.OnFocusChangeListener focusChange = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            setEditStatus();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_login);

        initView();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        etPhoneNumber.removeTextChangedListener(textWatcher);
        etPassword.removeTextChangedListener(textWatcher);
    }

    private void initToolbar() {
        final ImageView ivBack = (ImageView) findViewById(R.id.iv_toolbar_back);
        final TextView tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        final TextView tvAction = (TextView) findViewById(R.id.tv_toolbar_action);

        ivBack.setOnClickListener(mClick);
        tvTitle.setText("登录");
        tvAction.setText("更新App");
        tvAction.setVisibility(View.VISIBLE);
        tvAction.setOnClickListener(mClick);
    }

    private void initView() {
        initToolbar();
        etPhoneNumber = (EditText) findViewById(R.id.et_phone_number);
        ivClearPhone = (ImageView) findViewById(R.id.iv_clear_phone);
        etPassword = (EditText) findViewById(R.id.et_password);
        ivClearPassword = (ImageView) findViewById(R.id.iv_clear_password);
        tvDoLogin = (TextView) findViewById(R.id.tv_do_login);
        tvJsonText = (TextView) findViewById(R.id.tv_json_text);
        ivAvatar = (ImageView) findViewById(R.id.iv_avatar);

        etPhoneNumber.addTextChangedListener(textWatcher);
        etPhoneNumber.setOnFocusChangeListener(focusChange);
        etPassword.addTextChangedListener(textWatcher);
        etPassword.setOnFocusChangeListener(focusChange);
        ivClearPhone.setOnClickListener(mClick);
        ivClearPassword.setOnClickListener(mClick);
        tvDoLogin.setOnClickListener(mClick);

        tvDoLogin.setEnabled(false);

        etPhoneNumber.setText("13800000002");
        etPassword.setText("123456");
    }

    private void setEditStatus() {
        final String phoneNumber = etPhoneNumber.getText().toString();
        final String password = etPassword.getText().toString();
        final boolean phoneIsEmpty = TextUtils.isEmpty(phoneNumber);
        final boolean passwordIsEmpty = TextUtils.isEmpty(password);
        final boolean enabled = tvDoLogin.isEnabled();
        if (!phoneIsEmpty && !passwordIsEmpty && !enabled) {
            tvDoLogin.setEnabled(true);
        } else if ((phoneIsEmpty || passwordIsEmpty) && enabled) {
            tvDoLogin.setEnabled(false);
        }
        ivClearPhone.setVisibility((etPhoneNumber.hasFocus() && !phoneIsEmpty) ? View.VISIBLE : View.GONE);
        ivClearPassword.setVisibility((etPassword.hasFocus() && !passwordIsEmpty) ? View.VISIBLE : View.GONE);
    }

    private void setUserInfo() {
        final LiteOrmHelper dbHelper = new LiteOrmHelper(Test_LoginActivity.this);
        final Test_UserInfoDetailBean userInfo = dbHelper.queryFirst(Test_UserInfoDetailBean.class);
        dbHelper.closeDB();
        if (userInfo != null) {
            final Gson gson = new GsonBuilder().create();
            final String json = toJson(gson.toJson(userInfo));

            tvJsonText.setText(json);
        }
    }

    public String toJson(String jsonString) {
        if (TextUtils.isEmpty(jsonString)) {
            return "";
        }
        try {
            jsonString = jsonString.trim();
            if (jsonString.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(jsonString);
                return jsonObject.toString(8);
            }
            if (jsonString.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(jsonString);
                return jsonArray.toString(8);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void doLogin() {
        final String phoneNumber = etPhoneNumber.getText().toString();
        final String password = etPassword.getText().toString();

        showProgress(getString(R.string.text_progress_logining));
        Test_ServiceClient.getService().doLogin(phoneNumber, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<ServiceResult<Test_UserInfoDetailBean>>() {
                    @Override
                    public void onSuccess(@NonNull ServiceResult<Test_UserInfoDetailBean> result) {
                        mUserInfo = result.getResultData();

                        final LiteOrmHelper dbHelper = new LiteOrmHelper(Test_LoginActivity.this);
                        dbHelper.save(mUserInfo);
                        dbHelper.closeDB();

                        final String avatarUrl = mUserInfo.getAvatar();
                        ImageLoaderFactory.getLoader()
                                .loadImageFitCenter(Test_LoginActivity.this, ivAvatar, avatarUrl, new GlideCircleTransform());
                        setUserInfo();

                        ToastMaster.toast("登录成功");
                        hideProgress();
                    }

                    @Override
                    public void onError(String errorMsg) {
                        super.onError(errorMsg);
                        hideProgress();
                        ToastMaster.toast(errorMsg);
                    }
                });
    }

    private void doUpdateCheck() {
        showProgress(getString(R.string.text_progress_checking_update));
        Test_ServiceClient.getService().checkUpdate()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<FirImBean>() {
                            @Override
                            public void accept(@NonNull FirImBean firImBean) throws Exception {
                                final Gson gson = new GsonBuilder().create();
                                final String json = toJson(gson.toJson(firImBean));
                                tvJsonText.setText(json);

                                final int lastVersionCode = Integer.parseInt(firImBean.getVersion());
                                final int currentVersionCode = VersionUtils.getVersionCode(Test_LoginActivity.this);
                                if (lastVersionCode > currentVersionCode) {
                                    final UpdateAppUtils updateApp = new UpdateAppUtils(Test_LoginActivity.this, firImBean);
                                    updateApp.showUpdateDialog();
                                } else {
                                    ToastMaster.toast("已是最新版");
                                }
                                hideProgress();
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                ToastMaster.toast("获取版本失败");
                                hideProgress();
                            }
                        });
    }
}
