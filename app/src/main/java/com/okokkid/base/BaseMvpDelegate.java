package com.okokkid.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.okokkid.util.loader.LatteLoader;


public abstract class BaseMvpDelegate<P extends BasePresenter> extends BaseDelegate implements BaseView{
    protected P mvpPresenter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mvpPresenter = createPresenter();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected abstract P createPresenter();


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mvpPresenter != null) {
            mvpPresenter.detachView();
        }
    }

    @Override
    public void showLoading() {
        LatteLoader.showLoading(getContext());
    }

    @Override
    public void hideLoading() {
        LatteLoader.stopLoading();
    }
}
