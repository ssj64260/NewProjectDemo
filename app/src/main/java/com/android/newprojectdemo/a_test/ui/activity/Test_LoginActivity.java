package com.android.newprojectdemo.a_test.ui.activity;

import android.content.Intent;
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
import com.android.newprojectdemo.model.ServiceResult;
import com.android.newprojectdemo.service.MyObserver;
import com.android.newprojectdemo.utils.ToastMaster;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
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

    private Test_UserInfoDetailBean mUserInfo;

    @Override
    protected int getContentView() {
        return R.layout.test_activity_login;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        initToolbar();
        etPhoneNumber = (EditText) findViewById(R.id.et_phone_number);
        ivClearPhone = (ImageView) findViewById(R.id.iv_clear_phone);
        etPassword = (EditText) findViewById(R.id.et_password);
        ivClearPassword = (ImageView) findViewById(R.id.iv_clear_password);
        tvDoLogin = (TextView) findViewById(R.id.tv_do_login);

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        etPhoneNumber.removeTextChangedListener(textWatcher);
        etPassword.removeTextChangedListener(textWatcher);
    }

    private void initToolbar() {
        final ImageView ivBack = (ImageView) findViewById(R.id.iv_toolbar_back);
        final TextView tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);

        ivBack.setVisibility(View.GONE);
        tvTitle.setText("登录");
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

                        ToastMaster.toast("登录成功");
                        hideProgress();

                        startActivity(new Intent(Test_LoginActivity.this, Test_MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onError(String errorMsg) {
                        super.onError(errorMsg);
                        hideProgress();
                        ToastMaster.toast(errorMsg);
                    }
                });
    }

    private View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
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
}
