package com.hv.heartvoice.Model.response;

import com.hv.heartvoice.Base.BaseResponse;

import java.util.List;

public class ListResponse<T> extends BaseResponse {

    private List<T> data;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

}
