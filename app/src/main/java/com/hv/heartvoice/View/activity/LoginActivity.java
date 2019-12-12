package com.hv.heartvoice.View.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.hv.heartvoice.Base.BaseLoginActivity;
import com.hv.heartvoice.R;
import com.hv.heartvoice.Util.StringUtil;
import com.hv.heartvoice.Util.ToastUtil;
import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseLoginActivity {

    private static final String TAG = "LoginActivity";

    @BindView(R.id.accountPhone)
    EditText accountPhone;
    @BindView(R.id.outPassword)
    EditText outPassword;
    @BindView(R.id.goLogin)
    ImageButton goLogin;
    @BindView(R.id.forgetPassword)
    TextView forgetPassword;
    @BindView(R.id.userRegister)
    TextView userRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void initViews() {
        super.initViews();
        lightStatusBar();
    }

    @OnClick(R.id.userRegister)
    public void register(){
        startActivity(RegisterActivity.class);
    }

    @OnClick(R.id.goLogin)
    public void goLogin(){
        String phone = String.valueOf(accountPhone.getText()).trim();
        if(StringUtils.isBlank(phone)){
            ToastUtil.errorShort(R.string.outAccountNumber);
            return;
        }
        if(!StringUtil.isPhone(phone)){
            ToastUtil.errorShort(R.string.numberFormatError);
            return;
        }
        String password = String.valueOf(outPassword.getText()).trim();
        if(StringUtils.isBlank(password)){
            ToastUtil.errorShort(R.string.outPassword);
            return;
        }
        if(!StringUtil.isPassword(password)){
            ToastUtil.errorShort(R.string.passwordFormatError);
            return;
        }
        login(phone,password,true);
    }

    @OnClick(R.id.forgetPassword)
    public void forget(){
        startActivity(ForgetPasswordActivity.class);
    }

}
