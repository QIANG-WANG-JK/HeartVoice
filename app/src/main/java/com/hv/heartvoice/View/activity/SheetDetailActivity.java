package com.hv.heartvoice.View.activity;

import android.os.Bundle;

import com.hv.heartvoice.Base.BaseTitleActivity;
import com.hv.heartvoice.Domain.Sheet;
import com.hv.heartvoice.Model.Api;
import com.hv.heartvoice.Model.myObserver.HttpObserver;
import com.hv.heartvoice.Model.response.DetailResponse;
import com.hv.heartvoice.R;
import com.hv.heartvoice.Util.LogUtil;
import com.hv.heartvoice.Util.ToastUtil;

/**
 * 歌单详情界面
 */
public class SheetDetailActivity extends BaseTitleActivity {

    private static final String TAG = "SheetDetailActivity";
    /**
     * 歌单ID
     */
    private String id;

    /**
     * 歌单数据
     */
    private Sheet data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet_detail);
    }

    @Override
    protected void initViews() {
        super.initViews();

        //禁用ToolBar按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

    }

    @Override
    public void initData() {
        super.initData();

        //获取传递的id
        id = extraId();

        fetchData();
    }

    /**
     * 请求数据
     */
    private void fetchData() {
        Api.getInstance()
                .sheetDetail(id)
                .subscribe(new HttpObserver<DetailResponse<Sheet>>(getMainActivity(),true) {
                    @Override
                    public void onSucceeded(DetailResponse<Sheet> data) {
                        next(data.getData());
                    }
                });

    }

    private void next(Sheet data) {
        this.data = data;
    }
}
