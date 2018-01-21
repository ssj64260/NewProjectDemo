package com.android.newprojectdemo.a_test.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.luck.picture.lib.photoview.PhotoView;

/**
 * TODO 尽量添加描述，例如：登录页面
 */
public class LoginActivity extends AppCompatActivity {

    //TODO（1）静态常量（单词语单词用下划线分开）
    private static final String KEY_PHONE_NUMBER = "key_phone_number";
    private static final int REQUEST_CODE_LOGIN = 100;

    //TODO（2）控件变量（原生控件用控件每个单词首字母开头，加功能描述，驼峰式命名，遵循简洁易懂）
    private EditText etPhoneNumber;
    private EditText etPassword;
    //（非原生控件，以"m"开头，加控件名称，或用控件每个单词首字母开头，加功能描述，驼峰式命名，遵循简洁易懂）
    private PhotoView mPhotoView;
    private PhotoView pvUserAvatar;

    //TODO（3）其他变量（以"m"开头，加功能描述，驼峰式命名）
    private String mPhoneNumber;
    private String mPassword;

    //TODO（4）Activity 生命周期相关方法
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //TODO（5）类方法（驼峰式命名，描述功能，遵循简洁易懂）
    private void getPhoneNumber() {

    }

    //方法参数尽量加约束条件
    private void setPhoneNumber(@NonNull String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    //TODO（6）监听或一些接口回调（以"m"开头，加功能描述，驼峰式命名）
    private View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
