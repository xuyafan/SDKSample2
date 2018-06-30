package com.okokkid;

import android.app.Application;
import android.content.Context;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import com.rjsz.booksdk.BookSdkConfig;
import com.rjsz.booksdk.EnvConfig;
import com.rjsz.booksdk.RJBookManager;
import com.rjsz.booksdk.net.NetUtils;
import com.rjsz.booksdk.tool.FileUtil;

import java.io.File;

import me.yokeyword.fragmentation.Fragmentation;
import me.yokeyword.fragmentation.helper.ExceptionHandler;


/**
 * Created by xuyafan on 2018/5/23.
 */

public class MyApp extends Application {
    //保存全局context
    private static MyApp instance;

    public static MyApp getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        //点读机sdk配置
        EnvConfig.setEnv(getApplicationContext(), true);
        FileUtil.setStorageDir(this);
        File dir = FileUtil.getFilesDir(this);
        File cacheDir = getCacheDir();
        BookSdkConfig config = new BookSdkConfig.Builder()
                .setBookDir(dir)
                .setCacheDir(cacheDir)
                .setOkHttpClient(NetUtils.getOkHttpClient(this))
                .setHost(NetUtils.getBaseHttpsUrl(this))
                .build();
        RJBookManager.getInstance().init(this, "c1860a61705f36538055c0955c3270aa", config);

        //Logger配置 fragmentation配置
        Fragmentation.builder()
                // 设置 栈视图 模式为 （默认）悬浮球模式   SHAKE: 摇一摇唤出  NONE：隐藏， 仅在Debug环境生效
                .stackViewMode(Fragmentation.BUBBLE)
                .debug(BuildConfig.DEBUG) // 实际场景建议.debug(BuildConfig.DEBUG)
                /**
                 * 可以获取到{@link me.yokeyword.fragmentation.exception.AfterSaveStateTransactionWarning}
                 * 在遇到After onSaveInstanceState时，不会抛出异常，会回调到下面的ExceptionHandler
                 */
                .handleException(new ExceptionHandler() {
                    @Override
                    public void onException(Exception e) {
                        // 以Bugtags为例子: 把捕获到的 Exception 传到 Bugtags 后台。
                        // Bugtags.sendException(e);
                    }
                })
                .install();

        Logger.addLogAdapter(new AndroidLogAdapter());


    }
}
