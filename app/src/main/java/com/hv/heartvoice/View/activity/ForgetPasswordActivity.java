package com.hv.heartvoice.View.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.hv.heartvoice.Base.BaseLoginActivity;
import com.hv.heartvoice.Base.BaseResponse;
import com.hv.heartvoice.Domain.BaseModel;
import com.hv.heartvoice.Domain.User;
import com.hv.heartvoice.Model.Api;
import com.hv.heartvoice.Model.MyObserver.HttpObserver;
import com.hv.heartvoice.Model.Response.DetailResponse;
import com.hv.heartvoice.R;
import com.hv.heartvoice.Util.Constant;
import com.hv.heartvoice.Util.StringUtil;
import com.hv.heartvoice.Util.ToastUtil;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class ForgetPasswordActivity extends BaseLoginActivity {

    @BindView(R.id.forgetBackLogin)
    ImageView forgetBackLogin;
    @BindView(R.id.forgetOutNumber)
    EditText forgetOutNumber;
    @BindView(R.id.sendCheck)
    Button sendCheck;
    @BindView(R.id.outCheckData)
    EditText outCheckData;
    @BindView(R.id.forOutPassword)
    EditText forOutPassword;
    @BindView(R.id.confirmPassword)
    EditText confirmPassword;
    @BindView(R.id.confirmChange)
    Button confirmChange;

    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
    }

    @Override
    protected void initViews() {
        super.initViews();
        lightStatusBarAndBAR(Constant.Transparent);
    }

    @OnClick(R.id.sendCheck)
    public void send(){
        String phone = String.valueOf(forgetOutNumber.getText()).trim();
        if(StringUtils.isBlank(phone)){
            ToastUtil.errorShort(R.string.outAccountNumber);
            return;
        }
        if(!StringUtil.isPhone(phone)){
            ToastUtil.errorShort(R.string.numberFormatError);
            return;
        }
        sendSMSCode(phone);
    }

    @OnClick(R.id.confirmChange)
    public void submit(){
        String phone = String.valueOf(forgetOutNumber.getText()).trim();
        if(StringUtils.isBlank(phone)){
            ToastUtil.errorShort(R.string.outAccountNumber);
            return;
        }
        if(!StringUtil.isPhone(phone)){
            ToastUtil.errorShort(R.string.numberFormatError);
            return;
        }
        String code = String.valueOf(outCheckData.getText()).trim();
        if(StringUtils.isBlank(code)){
            ToastUtil.errorShort(R.string.outCode);
            return;
        }
        if(!StringUtil.isCode(code)){
            ToastUtil.errorShort(R.string.codeFormatError);
            return;
        }
        String password = String.valueOf(forOutPassword.getText()).trim();
        if(StringUtils.isBlank(password)){
            ToastUtil.errorShort(R.string.outPassword);
            return;
        }
        if(!StringUtil.isPassword(password)){
            ToastUtil.errorShort(R.string.passwordFormatError);
            return;
        }
        String passwordConfirm = String.valueOf(confirmPassword.getText()).trim();
        if(StringUtils.isBlank(passwordConfirm)){
            ToastUtil.errorShort(R.string.outNewPassword);
            return;
        }
        if(!StringUtil.isPassword(passwordConfirm)){
            ToastUtil.errorShort(R.string.passwordFormatError);
            return;
        }
        if(!password.equals(passwordConfirm)){
            ToastUtil.errorShort(R.string.confirmPasswordError);
            return;
        }
        User data = new User();
        data.setPhone(phone);
        data.setPassword(password);
        data.setCode(code);
        Api.getInstance().resetPassword(data)
                .subscribe(new HttpObserver<BaseResponse>(getMainActivity(),true) {
                    @Override
                    public void onSucceeded(BaseResponse data) {
                        //重置成功
                        login(phone,password,false);
                    }
                });
    }

    @OnClick(R.id.forgetBackLogin)
    public void backLogin(){
        onBackPressed();
    }

    private void sendSMSCode(String phone) {
        User user = new User();
        user.setPhone(phone);
        Api.getInstance().sendSMSCode(user)
                .subscribe(new HttpObserver<DetailResponse<BaseModel>>(getMainActivity(),false) {
                    @Override
                    public void onSucceeded(DetailResponse<BaseModel> data) {
                        ToastUtil.successShort(R.string.sendSuccess);
                        startCountDown();
                    }
                });
    }

    /**
     * 开始倒计时
     */
    private void startCountDown() {
        //倒计时的总时间,间隔
        //单位是毫秒
        countDownTimer = new CountDownTimer(60 * 1000, 1000) {
            /**
             * 每间隔时间调用
             * @param millisUntilFinished
             */
            @Override
            public void onTick(long millisUntilFinished) {
                sendCheck.setText(getString(R.string.countSecond,millisUntilFinished/1000));
            }

            /**
             * 倒计时完成
             */
            @Override
            public void onFinish() {
                sendCheck.setText(R.string.sendCode);
            }
        };
        //启动
        countDownTimer.start();
        //禁用按钮
        sendCheck.setEnabled(false);
    }

    /**
     * 界面销毁时调用
     */
    @Override
    protected void onDestroy() {
        if(countDownTimer != null){
            countDownTimer.cancel();
            countDownTimer = null;
        }
        super.onDestroy();
    }

}
