package com.hv.heartvoice.Domain;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.hv.heartvoice.R;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.lang.ref.PhantomReference;

public class User extends BaseModel{

    /**
     * 昵称
     */
    private String nickname;
    /**
     * 手机号
     */
    private String phone;

    /**
     * email
     */
    private String email;

    /**
     * 密码
     */
    private String password;

    /**
     * 验证码
     * 只有找回密码的时候才会用到
     */
    private String code;

    /**
     * 头像
     */
    private String description;

    /**
     * 省
     */
    private String province;

    /**
     * 省编码
     */
    @SerializedName("province_code")
    private String provinceCode;

    /**
     * 市
     */
    private String city;

    /**
     * 市编码
     */
    @SerializedName("city_code")
    private String cityCode;

    /**
     * 区
     */
    private String area;

    /**
     * 区编码
     */
    @SerializedName("area_code")
    private String areaCode;

    private String avatar;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionFormat(){
        if(TextUtils.isEmpty(description)){
            return "~~~~~~";
        }
        return description;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("nickname", nickname)
                .append("phone", phone)
                .append("email", email)
                .append("password", password)
                .toString();
    }

}
