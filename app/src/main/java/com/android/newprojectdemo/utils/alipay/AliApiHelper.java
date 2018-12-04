package com.android.newprojectdemo.utils.alipay;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.PayTask;
import com.android.newprojectdemo.model.ServiceResult;
import com.android.newprojectdemo.service.MyObserver;
import com.android.newprojectdemo.service.ServiceClient;
import com.android.newprojectdemo.utils.ThreadPoolUtils;
import com.android.newprojectdemo.utils.ToastMaster;
import com.android.newprojectdemo.wxapi.PayInfo;
import com.orhanobut.logger.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AliApiHelper {

    private static final String APP_ID = "";//TODO 支付宝AppId
    private static final String PID = "";
    private static final String RSA2_PRIVATE = "";
    private static final String API_NAME = "com.alipay.account.auth";
    private static final String APP_NAME = "mc";
    private static final String BIZ_TYPE = "openservice";
    private static final String PRODUCT_ID = "APP_FAST_LOGIN";
    private static final String SCOPE = "kuaijie";
    private static final String AUTH_TYPE = "AUTHACCOUNT";//AUTHACCOUNT代表授权；LOGIN代表登录
    private static final String SIGN_TYPE = "RSA2";
    private static final String METHOD = "alipay.open.auth.sdk.code.get";

    private static final String STATUS_SUCCESS = "9000";//请求处理成功 & 订单支付成功
    private static final String STATUS_DOING = "8000";//正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
    private static final String STATUS_ERROR = "4000";//系统异常 & 订单支付失败
    private static final String STATUS_CANCEL = "6001";//用户中途取消
    private static final String STATUS_NETWORK_ERROR = "6002";//网络连接出错
    private static final String STATUS_UNKNOW = "6004";//支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
    private static final String STATUS_REPEAT_REQUEST = "5000";//重复请求
    private static final String CODE_SUCCESS = "200";//业务处理成功，会返回authCode
    private static final String CODE_FREEZE = "1005";//账户已冻结，如有疑问，请联系支付宝技术支持
    private static final String CODE_ERROR = "202";//系统异常，请稍后再试或联系支付宝技术支持


    private static AliApiHelper mHelper;
    private final Handler mHandler;
    private OnResultListener mOnResult;

    private AliApiHelper() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static AliApiHelper get() {
        if (mHelper == null) {
            mHelper = new AliApiHelper();
        }
        return mHelper;
    }

    public void doAliPay(final Activity activity, PayInfo payInfo) {
        ThreadPoolUtils.getInstache().singleExecute(new Runnable() {
            @Override
            public void run() {
                final String orderInfo = "";

                final PayTask alipay = new PayTask(activity);
                final Map<String, String> result = alipay.payV2(orderInfo, true);
                final AliResultInfo resultInfo = new AliResultInfo(result);
                final String resultStatus = resultInfo.getResultStatus();

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (STATUS_SUCCESS.equals(resultStatus)) {
                            ToastMaster.show("支付成功");
                        } else {
                            if (STATUS_DOING.equals(resultStatus)) {
                                ToastMaster.show("正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态");
                            } else if (STATUS_ERROR.equals(resultStatus)) {
                                ToastMaster.show("系统异常");
                            } else if (STATUS_REPEAT_REQUEST.equals(resultStatus)) {
                                ToastMaster.show("重复请求");
                            } else if (STATUS_CANCEL.equals(resultStatus)) {
                                ToastMaster.show("已取消");
                            } else if (STATUS_NETWORK_ERROR.equals(resultStatus)) {
                                ToastMaster.show("网络连接出错");
                            } else if (STATUS_UNKNOW.equals(resultStatus)) {
                                ToastMaster.show("支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态");
                            }

                            doReturn(false);
                        }
                    }
                });
            }
        });
    }

    public void doAliLogin(final Activity activity) {
        ThreadPoolUtils.getInstache().singleExecute(new Runnable() {
            @Override
            public void run() {
                final String info = getAuthInfo();
                final AuthTask aliAuth = new AuthTask(activity);
                final Map<String, String> result = aliAuth.authV2(info, true);
                final AliResultInfo resultInfo = new AliResultInfo(result);
                final String resultStatus = resultInfo.getResultStatus();
                final String resultCode = resultInfo.getResultCode();
                final String authCode = resultInfo.getAuthCode();
                final String userId = resultInfo.getUserId();

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (STATUS_SUCCESS.equals(resultStatus) && CODE_SUCCESS.equals(resultCode)) {
                            Logger.d(authCode + "  " + userId);
                            doAliLogin(authCode, userId);
                        } else {
                            if (CODE_FREEZE.equals(resultCode)) {
                                ToastMaster.show("账户已冻结，如有疑问，请联系支付宝技术支持");
                            } else if (CODE_ERROR.equals(resultCode)) {
                                ToastMaster.show("系统异常，请稍后再试或联系支付宝技术支持");
                            } else if (STATUS_ERROR.equals(resultStatus)) {
                                ToastMaster.show("系统异常");
                            } else if (STATUS_CANCEL.equals(resultStatus)) {
                                ToastMaster.show("已取消");
                            } else if (STATUS_NETWORK_ERROR.equals(resultStatus)) {
                                ToastMaster.show("网络连接出错");
                            }

                            doReturn(false);
                        }
                    }
                });
            }
        });
    }

    public AliApiHelper setOnResult(OnResultListener onResult) {
        this.mOnResult = onResult;
        return this;
    }

    private void doReturn(boolean isSuccess) {
        if (mOnResult != null) {
            if (isSuccess) {
                mOnResult.onSuccess();
            } else {
                mOnResult.onError();
            }
        }
    }

    private void doAliLogin(String code, String userId) {
        ServiceClient.getService().doAliLogin(code, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<ServiceResult<UserInfo>>() {
                    @Override
                    public void onSuccess(ServiceResult<UserInfo> result) {
                        final UserInfo data = result.getResultData();
                        if (data != null) {
                            final String token = data.getAccessToken();
                            if (!TextUtils.isEmpty(token)) {
                                doReturn(true);
                                return;
                            }
                        }

                        onError("data is null or accessToken is empty");
                    }

                    @Override
                    public void onError(String errorMsg) {
                        super.onError(errorMsg);
                        ToastMaster.show(errorMsg);
                        doReturn(false);
                    }
                });
    }

    private String getAuthInfo() {
        final String targetId = String.valueOf(System.currentTimeMillis());
        final Map<String, String> keyValue = new HashMap<>();
        keyValue.put("app_id", APP_ID);
        keyValue.put("pid", PID);
        keyValue.put("target_id", targetId);
        keyValue.put("apiname", API_NAME);
        keyValue.put("app_name", APP_NAME);
        keyValue.put("biz_type", BIZ_TYPE);
        keyValue.put("product_id", PRODUCT_ID);
        keyValue.put("scope", SCOPE);
        keyValue.put("auth_type", AUTH_TYPE);
        keyValue.put("sign_type", SIGN_TYPE);

        final String info = buildOrderParam(keyValue);
        final String sign = getSign(keyValue);

        return info + "&" + sign;
    }

    private String buildOrderParam(Map<String, String> map) {
        final List<String> keys = new ArrayList<>(map.keySet());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            sb.append(buildKeyValue(key, value, true));
            sb.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        sb.append(buildKeyValue(tailKey, tailValue, true));

        return sb.toString();
    }

    private String getSign(Map<String, String> map) {

        final List<String> keys = new ArrayList<>(map.keySet());
        // key排序
        Collections.sort(keys);

        StringBuilder authInfo = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            authInfo.append(buildKeyValue(key, value, false));
            authInfo.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        authInfo.append(buildKeyValue(tailKey, tailValue, false));

        String oriSign = SignUtils.sign(authInfo.toString(), RSA2_PRIVATE, true);
        String encodedSign = "";

        try {
            encodedSign = URLEncoder.encode(oriSign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "sign=" + encodedSign;
    }

    private String buildKeyValue(String key, String value, boolean isEncode) {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append("=");
        if (isEncode) {
            try {
                sb.append(URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                sb.append(value);
            }
        } else {
            sb.append(value);
        }
        return sb.toString();
    }

    public interface OnResultListener {
        void onSuccess();

        void onError();
    }
}
