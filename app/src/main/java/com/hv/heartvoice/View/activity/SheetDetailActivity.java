package com.hv.heartvoice.View.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hv.heartvoice.Adapter.SongAdapter;
import com.hv.heartvoice.Base.BaseTitleActivity;
import com.hv.heartvoice.Domain.Sheet;
import com.hv.heartvoice.Domain.Song;
import com.hv.heartvoice.Listener.MusicPlayerListener;
import com.hv.heartvoice.Manager.ListManager;
import com.hv.heartvoice.Manager.MusicPlayerManager;
import com.hv.heartvoice.Model.Api;
import com.hv.heartvoice.Model.myObserver.HttpObserver;
import com.hv.heartvoice.Model.response.DetailResponse;
import com.hv.heartvoice.R;
import com.hv.heartvoice.Service.MusicPlayerService;
import com.hv.heartvoice.Util.ImageUtil;
import com.hv.heartvoice.Util.ResourceUtil;
import com.hv.heartvoice.Util.ToastUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Response;

import static com.hv.heartvoice.Util.Constant.MODEL_LOOP_ONE;

/**
 * 歌单详情界面
 */
public class SheetDetailActivity extends BaseTitleActivity implements MusicPlayerListener {

    private static final String TAG = "SheetDetailActivity";

    @BindView(R.id.sheetDetailRecyclerView)
    RecyclerView sheetDetailRecyclerView;

    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.playControll)
    LinearLayout playControll;

    @BindView(R.id.playControllBanner)
    ImageView playControllBanner;

    @BindView(R.id.playControllSong)
    TextView playControllSong;

    @BindView(R.id.playControllPlay)
    ImageView playControllPlay;

    @BindView(R.id.playControllProgress)
    ProgressBar playControllProgress;

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

    /**
     * 评论
     */
    private ImageView comment;

    /**
     * 评论数
     */
    private TextView comment_count;

    /**
     * 列表管理器
     */
    private ListManager listManager;

    /**
     * 音乐播放管理器
     */
    private MusicPlayerManager musicPlayerManager;

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

        comment = view.findViewById(R.id.comment);

        comment_count = view.findViewById(R.id.comment_count);

        return view;
    }

    @Override
    public void initData() {
        super.initData();

        //初始化列表管理器
        listManager = MusicPlayerService.getListManager(getMainActivity());

        musicPlayerManager = MusicPlayerService.getMusicPlayerManager(getMainActivity());
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
     * 按钮点击回调方法
     */
    @Override
    public void initListeners() {
        super.initListeners();

        bt_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processCollectionClick();
            }
        });

        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CommentActivity.start(getMainActivity(),id);
            }
        });

        iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityExtraId(UserDetailActivity.class,data.getUser().getId());
            }
        });

        songAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                play(position);
            }
        });

    }

    /**
     * 播放当前位置的音乐
     * @param position
     */
    private void play(int position) {
        Song data = songAdapter.getItem(position);
        listManager.setDatas(songAdapter.getData());
        listManager.play(data);
        //跳转到播放界面
        SimplePlayerActivity.start(getMainActivity());
    }

    /**
     * 处理歌单收藏与取消
     */
    private void processCollectionClick() {

        if(data.isCollection()){
            Api.getInstance()
                    .cancelCollection(id)
                    .subscribe(new HttpObserver<Response<Void>>(getMainActivity(),false) {
                        @Override
                        public void onSucceeded(Response<Void> d) {
                            ToastUtil.successShort(R.string.cancel_collection_success);
                            fetchData();
                        }
                    });
        }else{
            Api.getInstance()
                    .collection(id)
                    .subscribe(new HttpObserver<Response<Void>>(getMainActivity(),false) {
                        @Override
                        public void onSucceeded(Response<Void> d) {
                            ToastUtil.successShort(R.string.collection_success);
                            fetchData();
                        }
                    });
        }
    }

    /**
     * 请求数据
     */
    private void fetchData() {
        Api.getInstance()
                .sheetDetail(id)
                .subscribe(new HttpObserver<DetailResponse<Sheet>>(getMainActivity(),false) {
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
            ImageUtil.showLocal(getMainActivity(),R.mipmap.user_head,iv_banner);
        }else{
            //有图片
            //ImageUtil.show(getMainActivity(),iv_banner,data.getBanner(),1);
            Glide.with(getMainActivity())
                    .asBitmap()
                    .load(ResourceUtil.resourceUri(data.getBanner()))
                    .into(new CustomTarget<Bitmap>() {

                        /**
                         * 资源加载完成
                         * @param resource
                         * @param transition
                         */
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            //显示封面
                            iv_banner.setImageBitmap(resource);

                            //调色板
                            Palette.from(resource)
                                    .generate(new Palette.PaletteAsyncListener() {

                                        /**
                                         * 颜色计算完成
                                         * @param palette
                                         */
                                        @Override
                                        public void onGenerated(@Nullable Palette palette) {
                                            //获取有活力的颜色
                                            Palette.Swatch swatch = palette.getVibrantSwatch();

                                            if(swatch != null){
                                                //获取颜色RGB
                                                int rgb = swatch.getRgb();

                                                //设置toolbar颜色
                                                toolbar.setBackgroundColor(rgb);

                                                //设置头部容器颜色
                                                //ll_header.setBackgroundColor(rgb);

                                                //状态栏
                                                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                                                    //设置状态栏颜色
                                                    Window window = getWindow();

                                                    //设置状态栏颜色
                                                    window.setStatusBarColor(rgb);

                                                    //设置导航栏颜色
                                                    //window.setNavigationBarColor(rgb);
                                                }
                                            }
                                        }
                                    });
                        }

                        /**
                         * 加载任务取消  释放资源
                         * @param placeholder
                         */
                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });
        }

        //标题
        tv_title.setText(data.getTitle());
        //头像
        //ImageUtil.showImage(getMainActivity(),iv_avatar,data.getUser().getAvatar(),0);
        ImageUtil.showLocalWithCicle(getMainActivity(),R.mipmap.user_head,iv_avatar);

        //昵称
        //tv_nickname.setText(data.getUser().getNickname());

        tv_nickname.setText(getString(R.string.heartVoice));

        if(data.getSongs() == null){
            tv_count.setText(getString(R.string.music_count,0));
        }else{
            tv_count.setText(getString(R.string.music_count,data.getSongs().size()));
        }

        comment_count.setText(String.valueOf(data.getComments_count()));

        //显示收藏状态
        showCollectionStatus();

        //选中当前播放的音乐
        scrollPositionAsync();

    }

    @SuppressLint("ResourceType")
    private void showCollectionStatus() {
        if(data.isCollection()){
            //收藏了
            bt_collection.setText(getString(R.string.cancel_collection,data.getCollections_count()));

            //弱化取消收藏按钮
            bt_collection.setBackground(null);

            //设置文字颜色为灰色
            bt_collection.setTextColor(getColor(R.color.light_grey));
        }else{
            bt_collection.setText(getString(R.string.collection,data.getCollections_count()));
            bt_collection.setBackgroundResource(R.drawable.selector_register_button);
            bt_collection.setTextColor(getResources().getColorStateList(R.drawable.selector_text,null));
        }
    }

    /**
     * 界面显示
     */
    @Override
    protected void onResume() {
        super.onResume();

        //添加播放管理监听器
        musicPlayerManager.addMusicPlayerListener(this);

        //显示迷你控制器数据
        showSmallPlayControlData();

        //选中播放的音乐
        scrollPositionAsync();
    }

    /**
     * 界面隐藏
     */
    @Override
    protected void onPause() {
        super.onPause();

        musicPlayerManager.removeMusicPlayerListener(this);

    }

    /**
     * 异步选中当前播放的音乐
     */
    private void scrollPositionAsync() {
        sheetDetailRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                scrollPosition();
            }
        });
    }

    /**
     * 选中当前音乐
     */
    private void scrollPosition() {
        //判断歌单是否有音乐
        if(data != null && data.getSongs() != null && data.getSongs().size() > 0){
            List<Song> datas = data.getSongs();

            Song data = listManager.getData();

            if(data != null){
                //有正在播放的音乐
                int index = -1;
                Song song;
                for (int i = 0;i < datas.size();i++){
                    song = datas.get(i);
                    if(song.getId().equals(data.getId())){
                        index = i;
                        break;
                    }
                }

                if(index != -1){
                    songAdapter.setSelectedIndex(index+1);
                }else{
                    songAdapter.setSelectedIndex(-1);
                }

            }
        }
    }

    private void showSmallPlayControlData() {
        if(listManager.getDatas() != null && listManager.getDatas().size() > 0){
            playControll.setVisibility(View.VISIBLE);
            Song data = listManager.getData();
            if(data != null){
                //显示初始化数据
                showInitData(data);

                //显示音乐时长
                showDuration(data);

                //显示播放状态
                showMusicPlayStatus();

                showProgress(data);
            }
        }else{
            playControll.setVisibility(View.GONE);
        }
    }

    private void showMusicPlayStatus() {
        if(musicPlayerManager.isPlaying()){
            showPauseStatus();
        }else{
            showPlayStatus();
        }
    }

    private void showPlayStatus() {
        playControllPlay.setSelected(false);
    }

    private void showPauseStatus() {
        playControllPlay.setSelected(true);
    }

    private void showProgress(Song data) {
        playControllProgress.setProgress((int) data.getProgress());
    }

    private void showDuration(Song data) {
        int end = (int)data.getDuration();
        //设置到进度条
        playControllProgress.setMax(end);
    }

    /**
     * 显示初始化数据
     * @param data
     */
    private void showInitData(Song data) {
        //封面
        ImageUtil.show(getMainActivity(),playControllBanner,data.getBanner(),0);
        //标题
        playControllSong.setText(data.getTitle());
    }

    @OnClick(R.id.playControll)
    public void playControlContainer(){
        SimplePlayerActivity.start(getMainActivity());
    }

    @OnClick(R.id.playControllPlay)
    public void playControl(){
        if (musicPlayerManager.isPlaying()){
            listManager.pause();
        }else{
            listManager.resume();
        }
    }

    @OnClick(R.id.playControllMusicList)
    public void musicList(){

    }

    @OnClick(R.id.back)
    public void back(){
        onBackPressed();
    }

    @Override
    public void onPaused(Song data) {
        showPlayStatus();
    }

    @Override
    public void onPlaying(Song data) {
        showPauseStatus();
    }

    @Override
    public void onPrepared(MediaPlayer mp, Song data) {
        showInitData(data);

        scrollPositionAsync();
    }

    @Override
    public void onProgress(Song data) {
        //显示播放进度
        showProgress(data);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(listManager.getLoopModel() != MODEL_LOOP_ONE){
            Song next = listManager.next();
            if(next != null){
                listManager.play(next);
            }
        }else{
            Song data = listManager.getData();
            if(data != null){
                listManager.play(data);
            }
        }
    }
}
