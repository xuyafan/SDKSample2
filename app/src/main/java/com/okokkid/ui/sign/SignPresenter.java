package com.okokkid.ui.sign;

import com.okokkid.base.BasePresenter;
import com.okokkid.net.ApiCallback;
import com.okokkid.net.RetrofitClient;
import com.okokkid.vo.LoginVO;
import com.okokkid.vo.RegisterVO;

/**
 * author： xuyafan
 * description:
 */
public class SignPresenter extends BasePresenter<SignView> {
    public SignPresenter(SignView mvpView) {
        super(mvpView);
    }

    public void checkExist(String phone) {
        mvpView.showLoading();
        addSubscription(RetrofitClient.getApi().exist(phone),
                new ApiCallback<String>(mvpView) {
                    @Override
                    public void onSuccess(String response) {
                        mvpView.checkExistSucc(response);
                    }

                }
        );
    }

    public void sendRegisterSms(String phone) {
        mvpView.showLoading();
        addSubscription(RetrofitClient.getApi().sendRegisterSms(phone),
                new ApiCallback<String>(mvpView) {
                    @Override
                    public void onSuccess(String response) {
                        mvpView.sendSmsCodeSucc(response);
                    }

                }
        );
    }

    public void sendLoginSms(String phone) {
        mvpView.showLoading();
        addSubscription(RetrofitClient.getApi().sendLoginSms(phone),
                new ApiCallback<String>(mvpView) {
                    @Override
                    public void onSuccess(String response) {
                        mvpView.sendSmsCodeSucc(response);
                    }

                }
        );
    }

    public void register(RegisterVO body) {
        mvpView.showLoading();
        addSubscription(RetrofitClient.getApi().register(body),
                new ApiCallback<String>(mvpView) {
                    @Override
                    public void onSuccess(String response) {
                        mvpView.registerSucc(response);
                    }
                }
        );
    }

    public void login(LoginVO body) {
        mvpView.showLoading();
        addSubscription(RetrofitClient.getApi().login(body),
                new ApiCallback<String>(mvpView) {
                    @Override
                    public void onSuccess(String response) {
                        mvpView.loginInSucc(response);
                    }
                }
        );
    }
}
