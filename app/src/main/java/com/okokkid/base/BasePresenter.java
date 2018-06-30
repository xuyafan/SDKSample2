package com.okokkid.base;


import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class BasePresenter<V> {
    public V mvpView;

    private CompositeDisposable mCompositeDisposable;

    public BasePresenter(V mvpView) {
        attachView(mvpView);
    }

    public void attachView(V mvpView) {
        this.mvpView = mvpView;
    }

    //在detachView时取消RxJava注册，以避免内存泄露
    public void detachView() {
        this.mvpView = null;
        onUnSubscribe();
    }

    //RxJava取消注册
    private void onUnSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
    }

    /**
     * 添加observer到mCompositeDisposable以便管理，
     * 被观察者一般是从网络、数据库中获取数据操作
     * 指定被观察者运行在IO线程，观察者运行在主线程
     *
     * @param observable 被观察者
     * @param observer   观察者
     * @param <T>        被观察者数据泛型
     */
    public <T> void addSubscription(Observable<T> observable, DisposableObserver<T> observer) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(observer);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
