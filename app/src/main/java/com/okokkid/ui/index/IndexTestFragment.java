package com.okokkid.ui.index;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import com.okokkid.MyApp;

import com.okokkid.R;
import com.okokkid.base.BaseMainFragment;
import com.okokkid.util.Log;
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

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.yokeyword.eventbusactivityscope.EventBusActivityScope;

/**
 * author： xuyafan
 * description:
 */
public class IndexTestFragment extends BaseMainFragment {


    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.page_index)
    EditText pageIndex;
    @BindView(R.id.sync_order)
    Button syncOrder;
    @BindView(R.id.download)
    Button download;
    @BindView(R.id.delete)
    Button delete;
    @BindView(R.id.cache_size)
    Button cacheSize;
    @BindView(R.id.clean_cache)
    Button cleanCache;
    @BindView(R.id.is_download)
    Button isDownload;
    @BindView(R.id.has_update)
    Button hasUpdate;
    @BindView(R.id.catalog)
    Button catalog;
    @BindView(R.id.getItem)
    Button getItem;
    @BindView(R.id.removeDevices)
    Button removeDevices;

    private BookList.Item mItem;
    private int num;

    public static IndexTestFragment newInstance() {
        Bundle args = new Bundle();
        IndexTestFragment fragment = new IndexTestFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public Object setLayout() {
        return R.layout.fragment_index_test;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        EventBusActivityScope.getDefault(_mActivity).register(this);

        int index = PreferenceUtil.getSharePref(_mActivity, "PAGE_INDEX", 0);
        pageIndex.setText(index + "");
        bookState();
        reqBookList();
        RJBookManager.getInstance().setThemeColor(_mActivity, "#88a5e38c");
        //获取书的购买状态
//        RJBookManager.getInstance().getBookState(_mActivity, bookid);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBusActivityScope.getDefault(_mActivity).unregister(this);
    }


    //获取点击信息(点击时长，点击次数)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getTrackInfo(ClickReadInfo clickReadInfo) {
        Log.i("获取点击信息");
    }

    //获取评测信息（句子得分）
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEvaluateInfo(StatisticsEvaluateInfo statisticsEvaluateInfo) {
        Log.i("获取评测信息");
    }

    /**
     * 关闭课本时回调 获取页码，所属单元，标题
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getBookUnitAndTitle(CloseInfo info) {
        Log.i("unit：" + info.unit + "    title：" + info.title + "    pageIndex：" + info.pageIndex);
    }

    /**
     * 购买回调
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleSdkEvent(SdkEvent event) {
        if (event.eventType == SdkEvent.EVENT_EXPERIENCE_END) {
            event.activity.finish();//关闭阅读器
            toastInfo("点击了前往购买");
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
                PreferenceUtil.setSharePref(_mActivity, "BOOK_STATE", 0);
                download.setText("下载成功");
                toastSuccess("下载成功");
                break;
            case DownloadInfo.STATE_ERROR:
                PreferenceUtil.setSharePref(_mActivity, "BOOK_STATE", 1);
                toastError("下载失败");
                break;
            case DownloadInfo.STATE_WAIT:
                toastInfo("等待中");
                break;
            case DownloadInfo.STATE_DOWNLOAD:
                download.setText(downloadInfo.progress + "%");
                break;
            case DownloadInfo.STATE_NONE:
                PreferenceUtil.setSharePref(_mActivity, "BOOK_STATE", 2);
                toastInfo("已暂停");
                download.setText("已暂停");
                break;
            case DownloadInfo.STATE_UNZIP:
                toastInfo("解压中");
                break;
        }

    }

    private void bookState() {
        int BookState = PreferenceUtil.getSharePref(_mActivity, "BOOK_STATE", -1);
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
     * 请求书籍列表
     */
    private void reqBookList() {

        RJBookManager.getInstance().getAllBookList(_mActivity, new ReqCallBack() {
            @Override
            public void onReqSuccess(Object result) {
                BookList bookList = (BookList) result;
                if (bookList.booklist.size() == 0) {
                    toastInfo("书籍列表size=0");
                    return;
                }
                //获得3,1,1,显示封面
//                mItem = bookList.booklist.get(4).grade.get(1).section.get(1);
//                Glide.with(_mActivity)
//                        .load(mItem.icon)
//                        .placeholder(R.drawable.book_default_bg)
//                        .into(icon);
//
//                private static final RequestOptions RECYCLER_OPTIONS =
//                        new RequestOptions()
//                                .fitCenter()
//                                .placeholder(R.drawable.book_default_bg)
//                                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                .dontAnimate();
            }

            @Override
            public void onReqFailed(int errCode, String errMsg) {
                Log.e("reqBookList errCode:" + errCode + " errMsg:" + errMsg);
            }
        });
    }

    /**
     * 同步订单
     */
    @OnClick(R.id.sync_order)
    public void syncOrder() {
        RJBookManager.getInstance().syncOrder(_mActivity, "13187580", new ReqCallBack() {
            @Override
            public void onReqSuccess(Object result) {
                String json = (String) result;
                Log.i("同步订单接口返回的json" + json);
            }

            @Override
            public void onReqFailed(int errCode, String errMsg) {
                Log.i("同步订单接口返回的错误码" + errCode + "错误信息" + errMsg);
            }
        });

    }

    /**
     * 获取书本目录
     */
    @OnClick(R.id.catalog)
    public void getBookCatalog() {
        RJBookManager.getInstance().getMyBookCatalog(_mActivity, mItem, new ReqCallBack() {
            @Override
            public void onReqSuccess(Object result) {
                ArrayList<Book.BookItem> items = (ArrayList<Book.BookItem>) result;
                for (Book.BookItem item : items) {
                    Log.i(item.title + item.page + item.unit);
                }
            }

            @Override
            public void onReqFailed(int errCode, String errMsg) {
                Log.i("errCode" + errCode + "errMsg" + errMsg);
            }
        });

    }

    /**
     * 打开书
     */
    @OnClick(R.id.icon)
    public void open() {
        //获取页码并且持久化
        String index = pageIndex.getText().toString();
        if (TextUtils.isEmpty(index)) {
            num = 0;
        } else {
            num = Integer.parseInt(index);
        }
        PreferenceUtil.setSharePref(_mActivity, "PAGE_INDEX", num);
//        int index = PreferenceUtil.getSharePref(this, "PAGE_INDEX", 0);

        //打开书
        RJBookManager.getInstance().openBook(_mActivity, mItem, false, true, num);
    }

    /**
     * 下载书
     */
    @OnClick(R.id.download)
    public void downloadBook() {
        int state = RJBookManager.getInstance().getBookState(mItem);
        if (state == BookState.DOWNLOADED) {
            RJBookManager.getInstance().openBook(_mActivity, mItem, false, false);
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
        long cacheDirSize = RJBookManager.getInstance().getCacheDirSize(_mActivity);
        cacheSize.setText(FileUtil.formatLengthString(cacheDirSize));
    }

    /**
     * 清除缓存
     */
    @OnClick(R.id.clean_cache)
    public void cleanCache() {
        boolean isClean = RJBookManager.getInstance().deleteCacheDir(_mActivity);
        if (isClean) {
            toastSuccess("清除成功");
            cacheSize.setText("缓存大小");
        } else {
            toastError("清除失败");
        }
    }


    /**
     * 删除书
     */
    @OnClick(R.id.delete)
    public void deleteBook() {
        boolean isDelete = RJBookManager.getInstance().deleteBook(mItem);
        if (isDelete) {
            toastSuccess("删除成功");
            download.setText("下载课本");
            PreferenceUtil.setSharePref(_mActivity, "BOOK_STATE", 1);
        } else {
            toastError("删除失败");
        }
    }

    /**
     * 删除未下载完成的书
     */
    public void deleteBookTmp() {
        boolean isDelete = RJBookManager.getInstance().deleteBookTmp(mItem);
        if (isDelete) {
            toastSuccess("删除成功");
        } else {
            toastError("删除失败");
        }
    }

    /**
     * 判断是否书本下载
     */
    @OnClick(R.id.is_download)
    public void stopDownload() {
        boolean isDownloaded = RJUtils.isDownloaded(mItem);
        if (isDownloaded) {
            toastInfo("已经下载");
        } else {
            toastInfo("没有下载");
        }
    }

    /**
     * 判断书本是否有更新
     */
    @OnClick(R.id.has_update)
    public void hasUpdate() {
        boolean hasUpdate = RJUtils.hasUpdate(mItem);
        if (hasUpdate) {
            toastInfo("有更新");
        } else {
            toastInfo("没有更新");
        }
    }

    /**
     * 根据book_id返回教材信息的接口
     * 返回结果code有
     * 2000：参数错误
     * 2099：其他系统错误
     */
    @OnClick(R.id.getItem)
    public void getItem() {
        RJBookManager.getInstance().getBookItemById(_mActivity, "tape6a_002004", new ReqCallBack() {
            @Override
            public void onReqSuccess(Object result) {
                BookList.Item bookItem = (BookList.Item) result;
                Log.i(bookItem.toString());
            }

            @Override
            public void onReqFailed(int errCode, String errMsg) {
                Log.i("获取BookItem返回的错误码" + errCode + "错误信息" + errMsg);
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
        RJBookManager.getInstance().removeDevice(MyApp.getContext(), "666666", list, new ReqCallBack() {
            @Override
            public void onReqSuccess(Object result) {
                String json = (String) result;
                Log.i(json);
            }

            @Override
            public void onReqFailed(int errCode, String errMsg) {
                Log.i("removeDevices, errCode: " + errCode + "errMsg" + errMsg);
            }
        });
    }
}
