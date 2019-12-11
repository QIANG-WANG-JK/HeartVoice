package com.hv.heartvoice.Base;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class BaseResponse {

    private int status;//状态码

    private String message;//错误信息

    public int getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("status", status)
                .append("message", message)
                .toString();
    }

}
