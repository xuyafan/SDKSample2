package com.okokkid.net;


import com.okokkid.base.BaseView;

import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;

public abstract class ApiCallback<M> extends DisposableObserver<M> {

    BaseView mView;

    public ApiCallback(BaseView view) {
        mView = view;
    }

    public abstract void onSuccess(M response);

    public void onFailure(String msg) {
        mView.getDataFail(msg);
    }

    public void onFinish() {
        mView.hideLoading();
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            //httpException.response().errorBody().string()
            int code = httpException.code();
            String msg = httpException.getMessage();
            if (code == 504) {
                msg = "网络不给力";
            }
            if (code == 502 || code == 404) {
                msg = "服务器异常，请稍后再试";
            }
            onFailure(msg);
        } else {
            onFailure(e.getMessage());
        }
        onFinish();
    }

    @Override
    public void onNext(M model) {
        onSuccess(model);
    }

    @Override
    public void onComplete() {
        onFinish();
    }
}
