package com.android.newprojectdemo.config;

import com.android.newprojectdemo.a_test.model.Test_UserInfoDetailBean;
import com.android.newprojectdemo.model.ServiceResult;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * 网络请求接口 api
 */

public interface ServiceApi {

    String BASE_HOST = "http://test3.msqsoft.net/aboutapp/index.php/";//TODO 更改服务器地址

    @POST("?g=WebApi&m=login&a=mobilesSignIn")
    @FormUrlEncoded
    Observable<ServiceResult<Test_UserInfoDetailBean>> doLogin(
            @Field("mobile") String mobile,
            @Field("password") String password);

    @GET()
    Observable<ResponseBody> download(@Url String url);
}
