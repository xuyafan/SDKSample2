package com.okokkid.ui.sign;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;

import com.okokkid.R;
import com.okokkid.base.BaseMvpActivity;
import com.okokkid.util.Log;
import com.okokkid.util.ValidationUtil;
import com.okokkid.util.storage.PreferenceUtil;
import com.okokkid.vo.RegisterVO;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class VerifyActivity extends BaseMvpActivity<SignPresenter> implements SignView {


    String token;
    String phone;
    @BindView(R.id.etVerifyCode)
    EditText etVerifyCode;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.btnRegister)
    Button btnRegister;

    @Override
    protected SignPresenter createPresenter() {
        return new SignPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        ButterKnife.bind(this);
        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra("token")) {
            token = intent.getStringExtra("token");
        }
        if (intent.hasExtra("phone")) {
            phone = intent.getStringExtra("phone");
        }
    }


    @OnClick(R.id.btnRegister)
    public void onViewClicked() {
        String password = etPassword.getText().toString();
        String verifyCode = etVerifyCode.getText().toString();
        if (token == null) {
            toastWarning("请先获取短信验证码");
            return;
        }
        if (!ValidationUtil.isValidationCodeValid(verifyCode)) {
            toastWarning("请输入6位数字验证码");
            return;
        }
        if (!ValidationUtil.isPasswordValid(password)) {
            toastWarning("请输入6位以上的密码");
            return;
        }
        RegisterVO vo = new RegisterVO(password, phone, token, verifyCode);
        mvpPresenter.register(vo);
    }

    @Override
    public void sendSmsCodeSucc(String response) {

    }

    @Override
    public void registerSucc(String response) {
        Log.d("registerSucc,response:" + response);
        PreferenceUtil.put(PreferenceUtil.TAG_TOKEN, response);
        toastSuccess("注册成功");
    }

    @Override
    public void loginInSucc(String response) {

    }

    @Override
    public void getDataFail(String msg) {

    }

    @Override
    public void checkExistSucc(String response) {

    }
}
