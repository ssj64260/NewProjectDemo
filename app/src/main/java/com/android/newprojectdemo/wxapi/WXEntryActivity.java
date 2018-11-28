package com.android.newprojectdemo.wxapi;

import com.android.newprojectdemo.app.BaseActivity;
import com.android.newprojectdemo.utils.ToastMaster;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    @Override
    protected int getContentView() {
        return 0;
    }

    @Override
    protected void initData() {
        super.initData();

        WXApiHelper.get().handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                final int type = baseResp.getType();
                final String code = ((SendAuth.Resp) baseResp).code;

                if (ConstantsAPI.COMMAND_SENDAUTH == type) {
                    getWxLoginInfo(code);
                }
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                ToastMaster.show("拒绝授权微信登录");
                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                ToastMaster.show("已取消");
                finish();
                break;
            default:
                ToastMaster.show("错误码：" + baseResp.errCode);
                finish();
                break;
        }
    }

    private void getWxLoginInfo(String code) {

    }

    private void doWechatLogin(String unionId) {

    }
}
