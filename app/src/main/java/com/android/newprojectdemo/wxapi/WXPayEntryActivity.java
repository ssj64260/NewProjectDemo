package com.android.newprojectdemo.wxapi;

import com.android.newprojectdemo.app.BaseActivity;
import com.android.newprojectdemo.utils.ToastMaster;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

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
        final int type = baseResp.getType();

        if (ConstantsAPI.COMMAND_PAY_BY_WX == type) {
            switch (baseResp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    //TODO 支付成功
                    break;
                case BaseResp.ErrCode.ERR_COMM:
                    ToastMaster.show("支付发生错误");
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
    }
}
