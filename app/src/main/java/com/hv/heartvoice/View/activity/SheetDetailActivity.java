package com.hv.heartvoice.View.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hv.heartvoice.Adapter.SongAdapter;
import com.hv.heartvoice.Base.BaseTitleActivity;
import com.hv.heartvoice.Domain.Sheet;
import com.hv.heartvoice.Model.Api;
import com.hv.heartvoice.Model.myObserver.HttpObserver;
import com.hv.heartvoice.Model.response.DetailResponse;
import com.hv.heartvoice.R;
import com.hv.heartvoice.Util.ImageUtil;
import com.hv.heartvoice.Util.LogUtil;

import org.apache.commons.lang3.StringUtils;

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

    /**
     * 头部布局
     */
    private LinearLayout ll_header;

    /**
     * 封面图
     */
    private ImageView iv_banner;

    /**
     * 标题
     */
    private TextView tv_title;

    /**
     * 头像
     */
    private ImageView iv_avatar;

    /**
     * 歌手名称
     */
    private TextView tv_nickname;

    /**
     * 收藏
     */
    private Button bt_collection;

    /**
     * 播放全部
     */
    private LinearLayout ll_play_all_container;

    /**
     * 歌曲数
     */
    private TextView tv_count;

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

        //添加RcycleView分割线
        DividerItemDecoration decoration = new DividerItemDecoration(getMainActivity(), RecyclerView.VERTICAL);

        sheetDetailRecyclerView.addItemDecoration(decoration);

        layoutManager = new LinearLayoutManager(getMainActivity());

        //设置布局管理器
        sheetDetailRecyclerView.setLayoutManager(layoutManager);

    }

    private View createHeaderView(){
        //创建头部
        View view = getLayoutInflater().inflate(R.layout.header_sheet_detail, (ViewGroup) sheetDetailRecyclerView.getParent(),false);

        ll_header = view.findViewById(R.id.ll_header);

        iv_banner = view.findViewById(R.id.iv_banner);

        tv_title = view.findViewById(R.id.tv_title);

        iv_avatar = view.findViewById(R.id.iv_avatar);

        tv_nickname = view.findViewById(R.id.tv_nickname);

        bt_collection = view.findViewById(R.id.bt_collection);

        ll_play_all_container = view.findViewById(R.id.ll_play_all_container);

        tv_count = view.findViewById(R.id.tv_count);

        return view;
    }

    @Override
    public void initData() {
        super.initData();

        //获取传递的id
        id = extraId();

        //创建适配器
        songAdapter = new SongAdapter(R.layout.item_song_detail);

        //创建头部
        songAdapter.addHeaderView(createHeaderView());

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

        //显示封面
        if(StringUtils.isBlank(data.getBanner())){
            //如果图片为空 用默认图片
            iv_banner.setImageResource(R.mipmap.place_holder);
        }else{
            //有图片
            ImageUtil.show(getMainActivity(),iv_banner,data.getBanner(),1);
        }

        //标题
        tv_title.setText(data.getTitle());

        //头像
        ImageUtil.showImage(getMainActivity(),iv_avatar,data.getUser().getAvatar(),0);
        //昵称
        tv_nickname.setText(data.getUser().getNickname());

        tv_count.setText(getResources().getString(R.string.music_count,data.getSongs_count()));


    }

    @OnClick(R.id.back)
    public void back(){
        onBackPressed();
    }

}
