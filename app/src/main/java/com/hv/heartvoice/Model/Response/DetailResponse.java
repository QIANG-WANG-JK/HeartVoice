package com.hv.heartvoice.Model.response;

import com.hv.heartvoice.Base.BaseResponse;

public class DetailResponse<T> extends BaseResponse {

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
