package com.hv.heartvoice.Domain;

import android.text.TextUtils;

import org.apache.commons.lang3.builder.ToStringBuilder;

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

    private String description;

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
