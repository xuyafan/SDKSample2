package com.okokkid.ui.sign;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.okokkid.R;
import com.okokkid.base.BaseMvpActivity;
import com.okokkid.util.Log;
import com.okokkid.util.ValidationUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseMvpActivity<SignPresenter> implements SignView {
    String token;
    String phone;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.btn_next)
    Button btnNext;


    @Override
    protected SignPresenter createPresenter() {
        return new SignPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.btn_next)
    public void onViewClicked() {
        phone = etPhone.getText().toString();
        if (ValidationUtil.isPhoneValid(phone)) {
            mvpPresenter.checkExist(phone);
        } else {
            toastError("请输入正确的手机号码");
        }
    }

    @Override
    public void checkExistSucc(String response) {
        if (response.equals("false")) {
            mvpPresenter.sendRegisterSms(phone);
        } else {
            toastWarning("该手机号已被注册！");
        }
    }

    @Override
    public void sendSmsCodeSucc(String response) {
        token = response;
        Log.d("sendSmsCodeSucc:[token:" + response + "phone:" + phone + "]");
        Intent intent = new Intent(this, VerifyActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("phone", phone);
        startActivity(intent);
    }

    @Override
    public void registerSucc(String response) {

    }


    @Override
    public void loginInSucc(String response) {

    }

    @Override
    public void getDataFail(String msg) {
        Log.e(msg);
    }
}
