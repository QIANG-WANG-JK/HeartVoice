package com.hv.heartvoice.View.activity;

import android.os.Bundle;

import com.hv.heartvoice.Base.BaseTitleActivity;
import com.hv.heartvoice.R;

/**
 * 歌单详情界面
 */
public class SheetDetailActivity extends BaseTitleActivity {

    private String id;

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
    }
}
