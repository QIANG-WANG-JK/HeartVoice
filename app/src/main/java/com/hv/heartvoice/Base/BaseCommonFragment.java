package com.hv.heartvoice.Base;

import android.content.Intent;

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

}
