package com.hv.heartvoice.View.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hv.heartvoice.Adapter.PlayListAdapter;
import com.hv.heartvoice.Base.BaseBottomSheetDialogFragment;
import com.hv.heartvoice.Domain.event.PlayListChangedEvent;
import com.hv.heartvoice.Manager.ListManager;
import com.hv.heartvoice.R;
import com.hv.heartvoice.Service.MusicPlayerService;
import com.hv.heartvoice.Util.Constant;
import com.hv.heartvoice.Util.PreferenceUtil;
import com.hv.heartvoice.Util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

import static com.hv.heartvoice.Util.Constant.MODEL_LOOP_LIST;
import static com.hv.heartvoice.Util.Constant.MODEL_LOOP_ONE;
import static com.hv.heartvoice.Util.Constant.PLAY_MODEL;

/**
 * 迷你控制器播放列表
 */
public class PlayListDialogFragment extends BaseBottomSheetDialogFragment {

    /**
     * 循环模式
     */
    @BindView(R.id.loop_model)
    TextView loop_model;

    @BindView(R.id.song_count)
    TextView song_count;

    @BindView(R.id.collect_all)
    Button collect_all;

    @BindView(R.id.close)
    ImageView close;

    @BindView(R.id.recycleView)
    RecyclerView recyclerView;

    private PlayListAdapter adapter;

    private ListManager listManager;

    private PreferenceUtil sp;

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_play_list,container);
    }

    public static void show(FragmentManager fragmentManager){
        
        //创建Fragment
        PlayListDialogFragment fragment = newInstance();

        fragment.show(fragmentManager,"dialog");
        
    }

    public static PlayListDialogFragment newInstance() {
        
        Bundle args = new Bundle();

        PlayListDialogFragment fragment = new PlayListDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initViews() {
        super.initViews();
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getMainActivity());
        recyclerView.setLayoutManager(layoutManager);

        //分割线
        DividerItemDecoration decoration = new DividerItemDecoration(getMainActivity(), RecyclerView.VERTICAL);
        recyclerView.addItemDecoration(decoration);

    }

    @Override
    protected void initData() {
        super.initData();

        sp = PreferenceUtil.getInstance(getMainActivity());

        //创建列表管理器
        listManager = MusicPlayerService.getListManager(getMainActivity());

        adapter = new PlayListAdapter(R.layout.item_play_list,listManager);
        recyclerView.setAdapter(adapter);

        //设置数据
        adapter.replaceData(listManager.getDatas());

        showLoopModel(sp.getPlayModel(PLAY_MODEL));
    }

    @Override
    protected void initListeners() {
        super.initListeners();

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //关闭dialog
                //dismiss();
                int index = listManager.getDatas().indexOf(listManager.getData());

                if(position != index){
                    //播放点击的音乐
                    listManager.play(listManager.getDatas().get(position));

                    //选中当前音乐
                    scrollPosition();
                }
            }
        });

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(R.id.delete_song == view.getId()){
                    //删除按钮点击
                    listManager.delete(position);
                    notifyData();
                    scrollPosition();
                }
            }
        });

    }

    private void showLoopModel(String playModel) {

        int model = Integer.parseInt(playModel);

        int size = listManager.getDatas().size();

        switch (model){
            case MODEL_LOOP_LIST:
                loop_model.setText(String.format("%s(%d)",getString(R.string.list_loop),size));
                break;
            case MODEL_LOOP_ONE:
                loop_model.setText(String.format("%s(%d)",getString(R.string.one_loop),size));
                break;
            default:
                loop_model.setText(String.format("%s(%d)",getString(R.string.random_loop),size));
                break;
        }

    }

    private void notifyData(){

        if(listManager.getDatas().size() == 0){
            //发送通知 关闭迷你音乐播放器
            EventBus.getDefault().post(new PlayListChangedEvent());
            dismiss();
            return;
        }

        showLoopModel(sp.getPlayModel(PLAY_MODEL));
        adapter.replaceData(listManager.getDatas());
        adapter.notifyDataSetChanged();
    }

    private void scrollPosition() {
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                //获取当前音乐位置
                int index = listManager.getDatas().indexOf(listManager.getData());
                if(index != -1){
                    //滚动到该位置
                    recyclerView.smoothScrollToPosition(index);

                    //选中
                    adapter.setSelectedIndex(index);
                }
            }
        });
    }

    @OnClick(R.id.close)
    public void close(){
        dismiss();
    }

}
