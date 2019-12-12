package com.hv.heartvoice.Model;

import com.hv.heartvoice.Base.BaseResponse;
import com.hv.heartvoice.Domain.BaseModel;
import com.hv.heartvoice.Domain.Session;
import com.hv.heartvoice.Domain.User;
import com.hv.heartvoice.Model.Response.DetailResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Service {

    /**
     * 注册
     */
    @POST("v1/users")
    Observable<DetailResponse<BaseModel>> register(@Body User data);

    /**
     * 登录
     * @param data
     * @return
     */
    @POST("v1/sessions")
    Observable<DetailResponse<Session>> login(@Body User data);

    /**
     * 重置密码
     * @param data
     * @return
     */
    @POST("v1/users/reset_password")
    Observable<BaseResponse>  resetPassword(@Body User data);

}
