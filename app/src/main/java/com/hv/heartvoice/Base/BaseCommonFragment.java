package com.hv.heartvoice.Base;

import android.content.Intent;
import android.text.TextUtils;

import com.hv.heartvoice.Util.Constant;
import com.hv.heartvoice.Util.PreferenceUtil;

import butterknife.ButterKnife;

public abstract class BaseCommonFragment extends BaseFragment {

    PreferenceUtil sp;

    @Override
    protected void initView() {
        super.initView();
        sp = PreferenceUtil.getInstance(getMainActivity().getApplicationContext());
        if(isBindView()){
            bindView();
        }
    }

    /**
     * 绑定ButterKnife
     */

    protected boolean isBindView() {
        return true;
    }

    protected void bindView() {
        ButterKnife.bind(this,getView());
    }

    protected void startActivity(Class<?> cls){
        Intent intent = new Intent(getMainActivity(),cls);
        startActivity(intent);
    }

    protected void startActivityAfterFinshThis(Class<?> cls){
        Intent intent = new Intent(getMainActivity(),cls);
        startActivity(intent);
        getMainActivity().finish();
    }

    public BaseCommonActivity getMainActivity() {
        return (BaseCommonActivity) getActivity();
    }

    /**
     * 启动界面传递一个字符串参数
     * @param cls
     * @param id
     */
    protected void startActivityExtraId(Class<?> cls,String id){
        Intent intent = new Intent(getMainActivity(), cls);

        if(!TextUtils.isEmpty(id)){
            intent.putExtra(Constant.ID,id);
        }

        startActivity(intent);
    }

}
