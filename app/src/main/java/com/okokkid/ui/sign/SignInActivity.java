package com.okokkid.ui.sign;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ansen.http.net.HTTPCaller;
import com.ansen.http.net.RequestDataCallback;
import com.bumptech.glide.Glide;
import com.okokkid.R;
import com.okokkid.base.BaseMvpActivity;
import com.okokkid.event.WXEvent;
import com.okokkid.util.Log;
import com.okokkid.util.ValidationUtil;
import com.okokkid.util.storage.PreferenceUtil;
import com.okokkid.vo.LoginVO;
import com.okokkid.wxapi.WXAPIWrapper;
import com.okokkid.wxapi.WeiXinInfo;
import com.okokkid.wxapi.WeiXinToken;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SignInActivity extends BaseMvpActivity<SignPresenter> implements SignView {

    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.btn_getSms)
    Button btnGetSms;
    @BindView(R.id.etVerifyCode)
    EditText etVerifyCode;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.btnSignIn)
    Button btnSignIn;
    @BindView(R.id.tvRegister)
    TextView tvRegister;

    String phone;
    String token;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.btn_weixin_login)
    Button btnWeixinLogin;

    @Override
    protected SignPresenter createPresenter() {
        return new SignPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        EventBus.getDefault().register(this);//注册
        ButterKnife.bind(this);
    }

    @Override
    public void checkExistSucc(String response) {

    }

    @Override
    public void sendSmsCodeSucc(String response) {
        token = response;
        Log.d("sendSmsCodeSucc:[response:" + response + "phone:" + phone + "]");
    }

    @Override
    public void registerSucc(String response) {

    }

    @Override
    public void loginInSucc(String response) {
        Log.d("loginInSucc,response:" + response);
        PreferenceUtil.put(PreferenceUtil.TAG_TOKEN, response);
        toastSuccess("登录成功");
    }

    @Override
    public void getDataFail(String msg) {

    }

    @OnClick({R.id.btn_getSms, R.id.btnSignIn, R.id.tvRegister, R.id.btn_weixin_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_getSms:
                phone = etPhone.getText().toString();
                if (ValidationUtil.isPhoneValid(phone)) {
                    mvpPresenter.sendLoginSms(phone);
                } else {
                    toastError("请输入正确的手机号码");
                }
                break;
            case R.id.btnSignIn:
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
                LoginVO vo = new LoginVO(password, phone, token, verifyCode);
                mvpPresenter.login(vo);
                break;
            case R.id.tvRegister:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.btn_weixin_login:
                SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = String.valueOf(System.currentTimeMillis());
                req.state = WXAPIWrapper.STATE_SIGNIN;
                WXAPIWrapper.getWxapi(this).sendReq(req);
                finish();
                break;
        }
    }

    @Subscribe
    public void onEventMainThread(WXEvent weiXin) {
        Log.i("收到微信event请求type:" + weiXin.getType());
        if (weiXin.getType() == 1) {//登录
            getAccessToken(weiXin.getCode());
        } else if (weiXin.getType() == 2) {//分享
            switch (weiXin.getErrCode()) {
                case BaseResp.ErrCode.ERR_OK:
                    Log.i("微信分享成功.....");
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL://分享取消
                    Log.i("微信分享取消.....");
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED://分享被拒绝
                    Log.i("微信分享被拒绝.....");
                    break;
            }
        } else if (weiXin.getType() == 3) {//微信支付
            if (weiXin.getErrCode() == BaseResp.ErrCode.ERR_OK) {//成功
                Log.i("微信支付成功.....");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消注册
    }

    public void getAccessToken(String code) {
        //TODO: 这里应该是传code到后台获得openId
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid=" + WXAPIWrapper.APPID + "&secret=" + WXAPIWrapper.SECRET +
                "&code=" + code + "&grant_type=authorization_code";
        HTTPCaller.getInstance().get(WeiXinToken.class, url, null, new RequestDataCallback<WeiXinToken>() {
            @Override
            public void dataCallback(WeiXinToken obj) {
                if (obj.getErrcode() == 0) {//请求成功
                    getWeiXinUserInfo(obj);
                } else {//请求失败
                    toastError(obj.getErrmsg());
                    Log.e(obj.getErrmsg());
                }
            }
        });
    }

    public void getWeiXinUserInfo(WeiXinToken weiXinToken) {
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" +
                weiXinToken.getAccess_token() + "&openid=" + weiXinToken.getOpenid();
        HTTPCaller.getInstance().get(WeiXinInfo.class, url, null, new RequestDataCallback<WeiXinInfo>() {
            @Override
            public void dataCallback(WeiXinInfo obj) {
                tvNickname.setText("昵称:" + obj.getNickname());
                tvAge.setText("年龄:" + obj.getAge());
                Glide.with(SignInActivity.this)
                        .load(obj.getHeadimgurl())
                        .placeholder(R.drawable.book_default_bg)
                        .into(ivAvatar);

            }
        });
    }


}
