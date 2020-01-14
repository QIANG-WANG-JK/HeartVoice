package com.hv.heartvoice.View.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.IntegerArrayAdapter;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hv.heartvoice.Adapter.MusicHallAdapter;
import com.hv.heartvoice.Base.BaseCommonFragment;
import com.hv.heartvoice.Domain.BaseMultiItemEntity;
import com.hv.heartvoice.Domain.Sheet;
import com.hv.heartvoice.Domain.Song;
import com.hv.heartvoice.Domain.Title;
import com.hv.heartvoice.Model.Api;
import com.hv.heartvoice.Model.myObserver.HttpObserver;
import com.hv.heartvoice.Model.response.ListResponse;
import com.hv.heartvoice.R;
import com.hv.heartvoice.Util.DateUtil;
import com.hv.heartvoice.Util.ImageUtil;
import com.hv.heartvoice.Util.ToastUtil;
import com.hv.heartvoice.View.activity.WebViewActivity;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;

public class MusicHallFragment extends BaseCommonFragment implements OnBannerListener {

    /**
     * 列表控件
     */
    @BindView(R.id.recycleView)
    RecyclerView recyclerView;

    /**
     * 网格式布局控制器
     */
    private GridLayoutManager manager;

    /**
     * 音乐馆适配器
     */
    private MusicHallAdapter adapter;

    /**
     * 日期
     */
    private TextView date;

    /**
     * 轮播图
      */
    private Banner banner;

    /**
     * 轮播图数据
     */
    private List<Integer> bannerData;

    /**
     * 返回实例
     * @return
     */
    public static MusicHallFragment newInstance() {
        Bundle args = new Bundle();
        MusicHallFragment fragment = new MusicHallFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 布局
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_music_hall,null);
    }

    @Override
    protected void initView() {
        super.initView();
        /**
         * 高度固定
         * 可以提高性能
         */
        recyclerView.setHasFixedSize(true);

        //设置显示3列
        manager = new GridLayoutManager(getMainActivity(), 3);
        //需要有layoutManager
        recyclerView.setLayoutManager(manager);
    }

    @Override
    protected void initData() {
        super.initData();

        //创建适配器
        adapter = new MusicHallAdapter();

        //设置列宽度
        adapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int i) {
                //在模型上面的宽度
                return adapter.getItem(i).getSpanSize();
            }
        });

        //添加头部
        adapter.addHeaderView(createHeaderView());

        //设置适配器
        recyclerView.setAdapter(adapter);

        //请求数据
        fetchData();
    }

    //添加头部控件逻辑
    private View createHeaderView() {
        //从XML中创建View
        View view = getLayoutInflater().inflate(R.layout.header_music_hall, (ViewGroup) recyclerView.getParent(), false);

        banner = view.findViewById(R.id.banner);

        banner.setImageLoader(new GlideImageLoad());

        showBanner();

        date = view.findViewById(R.id.musicHallHeaderDate);

        date.setText(String.valueOf(DateUtil.getDayOfMonth()));

        return view;
    }

    private void showBanner() {
        bannerData = new ArrayList<>();
        bannerData.add(R.mipmap.banner1);
        bannerData.add(R.mipmap.banner2);
        banner.setImages(bannerData);
        banner.setOnBannerListener(this);
        //显示数据
        banner.start();

    }

    /**
     * 第一次也要滚动
     */
    private void startBannerScroll() {
        banner.startAutoPlay();
    }

    /**
     *
     */
    @Override
    public void onResume() {
        super.onResume();
        if(bannerData != null){
            //有数据才开始滚动
            startBannerScroll();
        }
    }

    /**
     *当界面不见执行
     */
    @Override
    public void onPause() {
        super.onPause();
        banner.stopAutoPlay();
    }

    /**
     * 数据请求
     */
    private void fetchData() {
        List<BaseMultiItemEntity> datas = new ArrayList<>();

        //歌单请求API
        Observable<ListResponse<Sheet>> sheets = Api.getInstance().sheets();

        //单曲API
        Observable<ListResponse<Song>> songs = Api.getInstance().songs();

        //请求数据  分开请求  也可用RXJAVA合并请求
        sheets.subscribe(new HttpObserver<ListResponse<Sheet>>(getMainActivity(),false) {
            @Override
            public void onSucceeded(ListResponse<Sheet> data) {
                //歌单数据
                datas.add(new Title("推荐歌单"));

                List<Sheet> list = data.getData().subList(0,5);

                datas.addAll(list);

                //单曲数据
                songs.subscribe(new HttpObserver<ListResponse<Song>>(getMainActivity(),false) {
                    @Override
                    public void onSucceeded(ListResponse<Song> data) {
                        //添加单曲
                        datas.add(new Title("推荐单曲"));
                        datas.addAll(data.getData());

                        //将数据设置到适配器
                        adapter.replaceData(datas);
                    }
                });
            }
        });

    }

    /**
     * 轮播图点击回调
     * @param position
     */
    @Override
    public void OnBannerClick(int position) {
        WebViewActivity.start(getMainActivity(),"周杰伦","https://baike.baidu.com/item/%E5%91%A8%E6%9D%B0%E4%BC%A6");
    }

    class GlideImageLoad extends ImageLoader{

        /**
         * 加载图片
         * @param context
         * @param path
         * @param imageView
         */
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Integer id = (Integer) path;
            ImageUtil.showLocal(context,id,imageView);
        }

    }

}

