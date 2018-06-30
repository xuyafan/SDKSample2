package com.okokkid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.rjsz.booksdk.PreferenceUtil;
import com.rjsz.booksdk.RJBookManager;
import com.rjsz.booksdk.bean.Book;
import com.rjsz.booksdk.bean.BookList;
import com.rjsz.booksdk.bean.ClickReadInfo;
import com.rjsz.booksdk.bean.CloseInfo;
import com.rjsz.booksdk.bean.StatisticsEvaluateInfo;
import com.rjsz.booksdk.callback.ReqCallBack;
import com.rjsz.booksdk.downloader.DownloadInfo;
import com.rjsz.booksdk.event.DownloadEvent;
import com.rjsz.booksdk.event.SdkEvent;
import com.rjsz.booksdk.tool.BookState;
import com.rjsz.booksdk.tool.FileUtil;
import com.rjsz.booksdk.tool.RJUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.page_index)
    EditText pageIndex;
    @BindView(R.id.download)
    Button download;
    @BindView(R.id.cache_size)
    Button cacheSize;
    private BookList.Item mItem;
    private int num;
    private String TAG = "MainActivity";


    //获取点击信息(点击时长，点击次数)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getTrackInfo(ClickReadInfo clickReadInfo) {

    }

    //获取评测信息（句子得分）
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEvaluateInfo(StatisticsEvaluateInfo statisticsEvaluateInfo) {

    }

    /**
     * 关闭课本时回调 获取页码，所属单元，标题
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getBookUnitAndTitle(CloseInfo info) {
        System.out.println("unit：" + info.unit + "    title：" + info.title + "    pageIndex：" + info.pageIndex);
    }

    /**
     * 购买回调
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleSdkEvent(SdkEvent event) {
        com.okokkid.util.Log.d("buy event");
        if (event.eventType == SdkEvent.EVENT_EXPERIENCE_END) {
            event.activity.finish();//关闭阅读器
            Toast.makeText(this, "点击了前往购买", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 下载书的回调
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void downloadEvent(DownloadEvent event) {
        final DownloadInfo downloadInfo = event.downloadInfo;

        switch (downloadInfo.state) {
            case DownloadInfo.STATE_SUCCESS:
                PreferenceUtil.setSharePref(this, "BOOK_STATE", 0);
//                Toast.makeText(this, "下载成功", Toast.LENGTH_SHORT).show();
                download.setText("下载成功");
                break;
            case DownloadInfo.STATE_ERROR:
                PreferenceUtil.setSharePref(this, "BOOK_STATE", 1);
                Toast.makeText(this, "下载失败", Toast.LENGTH_SHORT).show();
                break;
            case DownloadInfo.STATE_WAIT:
                Toast.makeText(this, "等待中", Toast.LENGTH_SHORT).show();
                break;
            case DownloadInfo.STATE_DOWNLOAD:
                download.setText(downloadInfo.progress + "%");
                break;
            case DownloadInfo.STATE_NONE:
                PreferenceUtil.setSharePref(this, "BOOK_STATE", 2);
//                Toast.makeText(this, "已暂停", Toast.LENGTH_SHORT).show();
                download.setText("已暂停");
                break;
            case DownloadInfo.STATE_UNZIP:
                Toast.makeText(this, "解压中", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        int index = PreferenceUtil.getSharePref(this, "PAGE_INDEX", 0);
        pageIndex.setText(index + "");
        bookState();
        reqBookList();
        RJBookManager.getInstance().setThemeColor(this, "#88a5e38c");
        //获取书的购买状态
//        RJBookManager.getInstance().getBookState(this, bookid);
        EventBus.getDefault().register(this);
    }


    private void bookState() {
        int BookState = PreferenceUtil.getSharePref(this, "BOOK_STATE", -1);
        switch (BookState) {
            case 0:
                download.setText("下载成功");
                break;
            case 1:
                download.setText("下载课本");
                break;
            case 2:
                download.setText("已暂停");
                break;
        }
    }


    /**
     * 同步订单
     */
    @OnClick(R.id.sync_order)
    public void syncOrder() {
//        reqBookList();
        RJBookManager.getInstance().syncOrder(this, "13187580", new ReqCallBack() {
            @Override
            public void onReqSuccess(Object result) {
                String json = (String) result;
                Log.i(TAG, "同步订单接口返回的json" + json);
            }

            @Override
            public void onReqFailed(int errCode, String errMsg) {
                Log.i(TAG, "同步订单接口返回的错误码" + errCode + "错误信息" + errMsg);
            }
        });

    }

    /**
     * 请求书籍列表
     */
    private void reqBookList() {

        RJBookManager.getInstance().getAllBookList(this, new ReqCallBack() {
            @Override
            public void onReqSuccess(Object result) {
                BookList bookList = (BookList) result;
                if (bookList.booklist.size() == 0) {
                    return;
                }
                mItem = bookList.booklist.get(3).grade.get(1).section.get(1);
                showIcon();

            }

            @Override
            public void onReqFailed(int errCode, String errMsg) {
                Log.i(TAG, "send all booklist errcode:" + errCode + "errMsg:" + errMsg);
            }
        });
    }


    /**
     * 获取书本目录
     */
    @OnClick(R.id.catalog)
    public void getBookCatalog() {
        RJBookManager.getInstance().getMyBookCatalog(this, mItem, new ReqCallBack() {
            @Override
            public void onReqSuccess(Object result) {
                ArrayList<Book.BookItem> items = (ArrayList<Book.BookItem>) result;
                for (Book.BookItem item : items) {
                    Log.i("TAG", item.title + item.page + item.unit);
                }
            }

            @Override
            public void onReqFailed(int errCode, String errMsg) {
                Log.i("TAG", "errcode" + errCode + "errMsg" + errMsg);
            }
        });

    }

    /**
     * 打开书
     */
    @OnClick(R.id.icon)
    public void open() {
        //获取页码并且持久化
        savePageIndex();
//        int index = PreferenceUtil.getSharePref(this, "PAGE_INDEX", 0);
        RJBookManager.getInstance().openBook(this, mItem, false, true, num);
    }

    /**
     * 下载书
     */
    @OnClick(R.id.download)
    public void downloadBook() {
        int state = RJBookManager.getInstance().getBookState(mItem);
        if (state == BookState.DOWNLOADED) {
            RJBookManager.getInstance().openBook(this, mItem, false, false);
        } else if (state == BookState.DOWNLOADING) {
            RJBookManager.getInstance().stopDownload(mItem);
        } else if (state != BookState.UNZIPPING) {
            RJBookManager.getInstance().downloadBook(mItem);
        }

    }

    /**
     * 获取缓存大小
     */
    @OnClick(R.id.cache_size)
    public void getCacheSize() {
        long cacheDirSize = RJBookManager.getInstance().getCacheDirSize(MainActivity.this);
        cacheSize.setText(FileUtil.formatLengthString(cacheDirSize));
    }

    /**
     * 清除缓存
     */
    @OnClick(R.id.clean_cache)
    public void cleanCache() {
        boolean isClean = RJBookManager.getInstance().deleteCacheDir(MainActivity.this);
        if (isClean) {
            Toast.makeText(MainActivity.this, "清除成功", Toast.LENGTH_SHORT).show();
            cacheSize.setText("缓存大小");
        } else {
            Toast.makeText(MainActivity.this, "清除失败", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 删除书
     */
    @OnClick(R.id.delete)
    public void deleteBook() {
        boolean isDelete = RJBookManager.getInstance().deleteBook(mItem);
        if (isDelete) {
            Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
            download.setText("下载课本");
            PreferenceUtil.setSharePref(this, "BOOK_STATE", 1);
        } else {
            Toast.makeText(this, "删除失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 删除未下载完成的书
     */
    public void deleteBookTmp() {
        boolean isDelete = RJBookManager.getInstance().deleteBookTmp(mItem);
        if (isDelete) {
            Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "删除失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 判断是否书本下载
     */
    @OnClick(R.id.is_download)
    public void stopDownload() {
        boolean isDownloaded = RJUtils.isDownloaded(mItem);
        if (isDownloaded) {
            Toast.makeText(this, "已经下载", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "没有下载", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 判断书本是否有更新
     */
    @OnClick(R.id.has_update)
    public void hasUpdate() {
        boolean hasUpdate = RJUtils.hasUpdate(mItem);
        if (hasUpdate) {
            Toast.makeText(this, "有更新", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "没有更新", Toast.LENGTH_SHORT).show();
        }
    }

    private void savePageIndex() {
        String index = pageIndex.getText().toString();
        if (TextUtils.isEmpty(index)) {
            num = 0;
        } else {
            num = Integer.parseInt(index);
        }

        PreferenceUtil.setSharePref(this, "PAGE_INDEX", num);
    }

    private void showIcon() {
//        Glide
//                .with(this)
//                .load(mItem.icon)
//                .placeholder(R.drawable.book_default_bg)
//                .into(icon);
    }

    /**
     * 根据book_id返回教材信息的接口
     * 返回结果code有
     * 2000：参数错误
     * 2099：其他系统错误
     */
    @OnClick(R.id.getItem)
    public void getItem() {
        RJBookManager.getInstance().getBookItemById(this, "tape6a_002004", new ReqCallBack() {
            @Override
            public void onReqSuccess(Object result) {
                BookList.Item bookItem = (BookList.Item) result;
                Log.i("TAG", bookItem.toString());
            }

            @Override
            public void onReqFailed(int errCode, String errMsg) {
                Log.i(TAG, "获取BookItem返回的错误码" + errCode + "错误信息" + errMsg);
            }
        });
    }

    /**
     * 解绑用户设备
     */
    @OnClick(R.id.removeDevices)
    public void removeDevices() {
        List<String> list = new ArrayList<>();
        list.add("B34037EC-C1E4-4890-AB34-D80BF77D27E5");
        list.add("29CA4AA4-FDF4-4E3B-94AC-7A51B7B1B53D");
//        list.add("45DCC617-E077-4BD6-9464-DD5992B784DA");
        RJBookManager.getInstance().removeDevice(getApplicationContext(), "666666", list, new ReqCallBack() {
            @Override
            public void onReqSuccess(Object result) {
                String json = (String) result;
                Log.i(TAG, json);
            }

            @Override
            public void onReqFailed(int errCode, String errMsg) {
                Log.i(TAG, "removeDevices, errCode: " + errCode + "errMesg" + errMsg);
            }
        });
    }
}
