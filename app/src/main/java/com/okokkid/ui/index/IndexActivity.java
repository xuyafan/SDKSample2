package com.okokkid.ui.index;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.gyf.barlibrary.ImmersionBar;

import com.okokkid.R;
import com.okokkid.base.BaseMainFragment;
import com.okokkid.util.Log;
import com.rjsz.booksdk.RJBookManager;
import com.rjsz.booksdk.callback.ReqCallBack;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;


public class IndexActivity extends SupportActivity implements BaseMainFragment.OnBackToFirstListener {

    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;

    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    @BindView(R.id.bottomBar)
    AHBottomNavigation mBottomBar;
    @BindView(R.id.fl_container)
    FrameLayout flContainer;

    private long TOUCH_TIME = 0;
    private SupportFragment[] mFragments = new SupportFragment[3];
    private ImmersionBar mImmersionBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        ButterKnife.bind(this);
        //沉浸式状态栏
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.keyboardEnable(true).navigationBarWithKitkatEnable(false).init();

        //加载3个子fragment
        SupportFragment firstFragment = findFragment(IndexFragment.class);
        if (firstFragment == null) {
            mFragments[FIRST] = IndexFragment.newInstance();
            mFragments[SECOND] = SecondFragment.newInstance();
            mFragments[THIRD] = ThirdFragment.newInstance();
            loadMultipleRootFragment(R.id.fl_container, FIRST,
                    mFragments[FIRST],
                    mFragments[SECOND],
                    mFragments[THIRD]
            );
        } else {
            mFragments[FIRST] = firstFragment;
            mFragments[SECOND] = findFragment(SecondFragment.class);
            mFragments[THIRD] = findFragment(ThirdFragment.class);
        }


        // Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("点读", R.drawable.icon1, R.color.colorAccent);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("单词", R.drawable.icon2, R.color.colorAccent);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("我的", R.drawable.icon3, R.color.colorAccent);
        // Add items
        mBottomBar.addItem(item1);
        mBottomBar.addItem(item2);
        mBottomBar.addItem(item3);

        mBottomBar.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                //如果重复点击，不是BaseMainFragment的，则跳转到最外面的子fragment
                if (wasSelected) {
                    final SupportFragment currentFragment = mFragments[position];
                    int count = currentFragment.getChildFragmentManager().getBackStackEntryCount();
                    if (count > 1) {
                        if (currentFragment instanceof IndexFragment) {
                            currentFragment.popToChild(IndexFragment.class, false);
                        } else if (currentFragment instanceof SecondFragment) {
                            currentFragment.popToChild(SecondFragment.class, false);
                        } else if (currentFragment instanceof ThirdFragment) {
                            currentFragment.popToChild(ThirdFragment.class, false);
                        }
                        return true;
                    }
                }
                //显示点击的fragment,隐藏其他fragment
                showHideFragment(mFragments[position]);
                return true;
            }
        });
        mBottomBar.setForegroundTintList(null);
        mBottomBar.setBackgroundTintList(null);


    }


    /**
     * 回退事件处理
     */
    @Override
    public void onBackPressedSupport() {

        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            pop();
        } else {
            if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
                finish();
            } else {
                TOUCH_TIME = System.currentTimeMillis();
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onBackToFirstFragment() {
        mBottomBar.setCurrentItem(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
    }



}
