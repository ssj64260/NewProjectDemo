package com.android.newprojectdemo.wxapi;

import android.content.Intent;

import com.android.newprojectdemo.app.APP;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 微信SDK帮助类
 */
public class WXApiHelper {

    public static final String WX_APP_ID = "wx2b8bb764a1ca7eb5";//TODO 微信相关
    public static final String WX_APP_SECRET = "37be64a3e2075de56272feb932f14eb1";
    public static final String WX_SCOPE = "snsapi_userinfo";
    public static final String WX_STATE = "sport_dict_wx_login";
    public static final String WX_GRANT_TYPE = "authorization_code";
    public static final String WX_PACKAGE_VALUE = "Sign=WXPay";

    private static WXApiHelper mHelper;
    private final IWXAPI mWxApi;

    private WXApiHelper() {
        mWxApi = WXAPIFactory.createWXAPI(APP.getInstance(), WX_APP_ID, false);
        mWxApi.registerApp(WX_APP_ID);
    }

    public static WXApiHelper get() {
        if (mHelper == null) {
            mHelper = new WXApiHelper();
        }
        return mHelper;
    }

    public void doWxLogin() {
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = WX_SCOPE;
        req.state = WX_STATE;

        mWxApi.sendReq(req);
    }

    public void doWxPay(PayInfo payInfo) {
        PayReq req = new PayReq();
        req.appId = WX_APP_ID;
        req.partnerId = payInfo.getPartnerid();
        req.prepayId = payInfo.getPrepayid();
        req.nonceStr = payInfo.getNoncestr();
        req.timeStamp = payInfo.getTimestamp();
        req.packageValue = WX_PACKAGE_VALUE;
        req.sign = payInfo.getSign();

        mWxApi.sendReq(req);
    }

    public void handleIntent(Intent intent, IWXAPIEventHandler handler) {
        mWxApi.handleIntent(intent, handler);
    }
}
