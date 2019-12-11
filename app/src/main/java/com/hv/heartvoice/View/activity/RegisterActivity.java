package com.hv.heartvoice.View.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.hv.heartvoice.Base.BaseLoginActivity;
import com.hv.heartvoice.Domain.BaseModel;
import com.hv.heartvoice.Domain.User;
import com.hv.heartvoice.Model.Api;
import com.hv.heartvoice.Model.MyObserver.HttpObserver;
import com.hv.heartvoice.Model.Response.DetailResponse;
import com.hv.heartvoice.R;
import com.hv.heartvoice.Util.LogUtil;
import com.hv.heartvoice.Util.StringUtil;
import com.hv.heartvoice.Util.ToastUtil;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterActivity extends BaseLoginActivity {

    public static final String TAG="RegisterActivity";

    @BindView(R.id.backLogin)
    ImageView backLogin;
    @BindView(R.id.registerNickName)
    EditText registerNickName;
    @BindView(R.id.registerAccountNumber)
    EditText registerAccountNumber;
    @BindView(R.id.registerEmail)
    EditText registerEmail;
    @BindView(R.id.registerPassowrd)
    EditText registerPassowrd;
    @BindView(R.id.register)
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    protected void initViews() {
        super.initViews();
        lightStatusBar();
    }

    @OnClick(R.id.register)
    public void register(){
        String nickname = String.valueOf(registerNickName.getText()).trim();
        if(StringUtils.isBlank(nickname)){
            ToastUtil.errorShort(R.string.outNickName);
        }
        String phone = String.valueOf(registerAccountNumber.getText()).trim();
        if(StringUtils.isBlank(phone)){
            ToastUtil.errorShort(R.string.outAccountNumber);
            return;
        }
        if(!StringUtil.isPhone(phone)){
            ToastUtil.errorShort(R.string.numberFormatError);
        }
        String email = String.valueOf(registerEmail.getText()).trim();
        if(StringUtils.isBlank(email)){
            ToastUtil.errorShort(R.string.outEmail);
            return;
        }
        String password = String.valueOf(registerPassowrd.getText()).trim();
        if(StringUtils.isBlank(password)){
            ToastUtil.errorShort(R.string.outPassword);
            return;
        }
        if(!StringUtil.isPassword(password)){
            ToastUtil.errorShort(R.string.passwordFormatError);
            return;
        }
        User user = new User();
        user.setNickname(nickname);
        user.setPhone(phone);
        user.setEmail(email);
        user.setPassword(password);
        LogUtil.e(TAG,user.toString());
        Api.getInstance().register(user)
                .subscribe(new HttpObserver<DetailResponse<BaseModel>>(getMainActivity(),true) {
                    @Override
                    public void onSucceeded(DetailResponse<BaseModel> data) {
                        ToastUtil.successShort(R.string.registerSuccess);
                        login(phone,password,false);
                    }
                });
    }

    @OnClick(R.id.backLogin)
    public void back(){
        onBackPressed();
    }


}
