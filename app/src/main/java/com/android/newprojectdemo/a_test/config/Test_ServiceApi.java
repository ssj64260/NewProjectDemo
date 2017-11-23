package com.android.newprojectdemo.a_test.config;

import com.android.newprojectdemo.a_test.model.Test_UserInfoDetailBean;
import com.android.newprojectdemo.model.FirImBean;
import com.android.newprojectdemo.model.ServiceResult;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * 测试用：物流接口
 */

public interface Test_ServiceApi {
    String BASE_HOST = "http://test3.msqsoft.net/aboutapp/index.php/";//A+物流

    @POST("?g=WebApi&m=login&a=mobilesSignIn")//A+物流登录接口
    @FormUrlEncoded
    Observable<ServiceResult<Test_UserInfoDetailBean>> doLogin(@Field("mobile") String mobile,
                                                               @Field("password") String password);

    ///////////////////////////////////////////////////////////////////////////
    // Fir更新app
    ///////////////////////////////////////////////////////////////////////////
    @GET("http://api.fir.im/apps/latest/5996f9ab548b7a129a000052?api_token=82ccd3b7d0c62f78581ca306e1f06fd9")
    Observable<FirImBean> checkUpdate();
}
