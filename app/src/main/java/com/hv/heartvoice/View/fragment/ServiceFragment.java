package com.hv.heartvoice.View.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hv.heartvoice.Base.BaseCommonFragment;
import com.hv.heartvoice.R;


public class ServiceFragment extends BaseCommonFragment {

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_service,container,false);
    }

    //单例方法会内存泄漏
    //private static ServiceFragment serviceFragment;
//    public static ServiceFragment newInstance(){
//        if(serviceFragment == null){
//            serviceFragment = new ServiceFragment();
//        }
//        return serviceFragment;
//    }

}
