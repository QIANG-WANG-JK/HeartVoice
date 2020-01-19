package com.hv.heartvoice.Base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import butterknife.ButterKnife;

/**
 * BaseBottomSheetDialogFragment父类
 */
public abstract class BaseBottomSheetDialogFragment extends BottomSheetDialogFragment {

    protected void initViews(){

    }

    protected void initData(){

    }

    protected void initListeners(){

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        initData();
        initListeners();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutView(inflater,container,savedInstanceState);

        ButterKnife.bind(this,view);
        return view;
    }

    protected abstract View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * 获取activity
     * @return
     */
    protected BaseCommonActivity getMainActivity(){
        return (BaseCommonActivity) getActivity();
    }

}
