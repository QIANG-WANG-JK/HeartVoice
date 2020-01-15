package com.hv.heartvoice.Model;

import com.hv.heartvoice.Base.BaseResponse;
import com.hv.heartvoice.Domain.BaseModel;
import com.hv.heartvoice.Domain.Session;
import com.hv.heartvoice.Domain.Sheet;
import com.hv.heartvoice.Domain.Song;
import com.hv.heartvoice.Domain.User;
import com.hv.heartvoice.Model.response.DetailResponse;
import com.hv.heartvoice.Model.response.ListResponse;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

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

    /**
     * 发送短信验证码
     * @param data
     * @return
     */
    @POST("v1/codes/request_sms_code")
    Observable<DetailResponse<BaseModel>> sendSMSCode(@Body User data);

    /**
     * 用户详情
     * @param id
     * @param data
     * @return
     */
    @GET("v1/users/{id}")
    Observable<DetailResponse<User>> userDetail(@Path("id") String id, @QueryMap Map<String,String> data);

    /**
     * 歌单列表
     * @return
     */
    @GET("v1/sheets")
    Observable<ListResponse<Sheet>> sheets();

    /**
     * 单曲
     * @return
     */
    @GET("v1/songs")
    Observable<ListResponse<Song>> songs();

    /**
     * 歌单详情
     *
     * @param id
     * @return
     */
    @GET("v1/sheets/{id}")
    Observable<DetailResponse<Sheet>> sheetDetail(@Path("id") String id);

}
