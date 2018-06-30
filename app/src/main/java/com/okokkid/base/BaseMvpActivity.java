package com.okokkid.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.okokkid.util.loader.LatteLoader;

import es.dmoral.toasty.Toasty;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * author： xuyafan
 * description:
 */
public abstract class BaseMvpActivity<P extends BasePresenter> extends SupportActivity implements BaseView {
    protected P mvpPresenter;

    protected abstract P createPresenter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mvpPresenter = createPresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mvpPresenter != null) {
            mvpPresenter.detachView();
        }
    }

    @Override
    public void showLoading() {
        LatteLoader.showLoading(this);
    }

    @Override
    public void hideLoading() {
        LatteLoader.stopLoading();
    }

    public void toastInfo(String msg) {
        Toasty.info(this, msg, Toast.LENGTH_SHORT, true).show();
//        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void toastError(String msg) {
        Toasty.error(this, msg, Toast.LENGTH_SHORT, true).show();
    }

    public void toastSuccess(String msg) {
        Toasty.success(this, msg, Toast.LENGTH_SHORT, true).show();
    }

    public void toastWarning(String msg) {
        Toasty.warning(this, msg, Toast.LENGTH_SHORT, true).show();
    }
}
