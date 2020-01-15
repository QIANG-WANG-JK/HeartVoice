package com.hv.heartvoice.View.activity;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hv.heartvoice.Adapter.SongAdapter;
import com.hv.heartvoice.Base.BaseTitleActivity;
import com.hv.heartvoice.Domain.Sheet;
import com.hv.heartvoice.Model.Api;
import com.hv.heartvoice.Model.myObserver.HttpObserver;
import com.hv.heartvoice.Model.response.DetailResponse;
import com.hv.heartvoice.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 歌单详情界面
 */
public class SheetDetailActivity extends BaseTitleActivity {

    private static final String TAG = "SheetDetailActivity";

    @BindView(R.id.sheetDetailRecyclerView)
    RecyclerView sheetDetailRecyclerView;

    @BindView(R.id.back)
    ImageView back;

    /**
     * 歌单ID
     */
    private String id;

    /**
     * 歌单数据
     */
    private Sheet data;

    /**
     * 布局管理器
     */
    private LinearLayoutManager layoutManager;

    /**
     * 适配器
     */
    private SongAdapter songAdapter;

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

        //尺寸固定
        sheetDetailRecyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getMainActivity());

        //设置布局管理器
        sheetDetailRecyclerView.setLayoutManager(layoutManager);

    }

    @Override
    public void initData() {
        super.initData();

        //获取传递的id
        id = extraId();

        //创建适配器
        songAdapter = new SongAdapter(R.layout.item_song_detail);

        //设置适配器
        sheetDetailRecyclerView.setAdapter(songAdapter);

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

        if(data.getSongs() != null && data.getSongs().size() > 0){
            //设置数据
            songAdapter.replaceData(data.getSongs());
        }
    }

    @OnClick(R.id.back)
    public void back(){
        onBackPressed();
    }

}
