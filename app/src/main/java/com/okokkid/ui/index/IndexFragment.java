package com.okokkid.ui.index;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import com.okokkid.R;
import com.okokkid.base.BaseMainFragment;
import com.okokkid.util.Log;
import com.rjsz.booksdk.PreferenceUtil;
import com.rjsz.booksdk.RJBookManager;
import com.rjsz.booksdk.bean.BookList;
import com.rjsz.booksdk.bean.ClickReadInfo;
import com.rjsz.booksdk.bean.CloseInfo;
import com.rjsz.booksdk.bean.StatisticsEvaluateInfo;
import com.rjsz.booksdk.callback.ReqCallBack;
import com.rjsz.booksdk.downloader.DownloadInfo;
import com.rjsz.booksdk.event.DownloadEvent;
import com.rjsz.booksdk.event.SdkEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

/**
 * author： xuyafan
 * description:
 */
public class IndexFragment extends BaseMainFragment {

    @BindView(R.id.recy_grade1)
    RecyclerView recyGrade1;
    @BindView(R.id.recy_grade2)
    RecyclerView recyGrade2;
    @BindView(R.id.recy_grade3)
    RecyclerView recyGrade3;
    @BindView(R.id.recy_grade4)
    RecyclerView recyGrade4;
    @BindView(R.id.recy_grade5)
    RecyclerView recyGrade5;
    @BindView(R.id.recy_grade6)
    RecyclerView recyGrade6;

    ImgAdapter mImgAdapter1;
    ImgAdapter mImgAdapter2;
    ImgAdapter mImgAdapter3;
    ImgAdapter mImgAdapter4;
    ImgAdapter mImgAdapter5;
    ImgAdapter mImgAdapter6;


    public static IndexFragment newInstance() {
        Bundle args = new Bundle();
        IndexFragment fragment = new IndexFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_index;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
//        EventBusActivityScope.getDefault(_mActivity).register(this);
        EventBus.getDefault().register(this);
        int index = PreferenceUtil.getSharePref(_mActivity, "PAGE_INDEX", 0);


        reqBookList();
        RJBookManager.getInstance().setThemeColor(_mActivity, "#88a5e38c");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        EventBusActivityScope.getDefault(_mActivity).unregister(this);
        EventBus.getDefault().unregister(this);
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


                //一年级上下
                List<BookList.Item> list1 = bookList.booklist.get(0).grade.get(0).section;
                list1.addAll(bookList.booklist.get(0).grade.get(1).section);

                List<BookList.Item> list2 = bookList.booklist.get(1).grade.get(0).section;
                list2.addAll(bookList.booklist.get(1).grade.get(1).section);

                List<BookList.Item> list3 = bookList.booklist.get(2).grade.get(0).section;
                list3.addAll(bookList.booklist.get(2).grade.get(1).section);

                List<BookList.Item> list4 = bookList.booklist.get(3).grade.get(0).section;
                list4.addAll(bookList.booklist.get(3).grade.get(1).section);

                List<BookList.Item> list5 = bookList.booklist.get(4).grade.get(0).section;
                list5.addAll(bookList.booklist.get(4).grade.get(1).section);

                List<BookList.Item> list6 = bookList.booklist.get(5).grade.get(0).section;
                list6.addAll(bookList.booklist.get(5).grade.get(1).section);
                mImgAdapter1 = new ImgAdapter(list1);
                mImgAdapter2 = new ImgAdapter(list2);
                mImgAdapter3 = new ImgAdapter(list3);
                mImgAdapter4 = new ImgAdapter(list4);
                mImgAdapter5 = new ImgAdapter(list5);
                mImgAdapter6 = new ImgAdapter(list6);

                recyGrade1.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.HORIZONTAL, false));
                recyGrade2.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.HORIZONTAL, false));
                recyGrade3.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.HORIZONTAL, false));
                recyGrade4.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.HORIZONTAL, false));
                recyGrade5.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.HORIZONTAL, false));
                recyGrade6.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.HORIZONTAL, false));

                recyGrade1.setAdapter(mImgAdapter1);
                recyGrade2.setAdapter(mImgAdapter2);
                recyGrade3.setAdapter(mImgAdapter3);
                recyGrade4.setAdapter(mImgAdapter4);
                recyGrade5.setAdapter(mImgAdapter5);
                recyGrade6.setAdapter(mImgAdapter6);

                mImgAdapter1.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        //打开书
                        RJBookManager.getInstance().openBook(_mActivity,
                                (BookList.Item) adapter.getItem(position), false, true, 0);
                    }
                });
                mImgAdapter2.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        //打开书
                        RJBookManager.getInstance().openBook(_mActivity,
                                (BookList.Item) adapter.getItem(position), false, true, 0);
                    }
                });
                mImgAdapter3.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        //打开书
                        RJBookManager.getInstance().openBook(_mActivity,
                                (BookList.Item) adapter.getItem(position), false, true, 0);
                    }
                });
                mImgAdapter4.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        //打开书
                        RJBookManager.getInstance().openBook(_mActivity,
                                (BookList.Item) adapter.getItem(position), false, true, 0);
                    }
                });
                mImgAdapter5.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        //打开书
                        RJBookManager.getInstance().openBook(_mActivity,
                                (BookList.Item) adapter.getItem(position), false, true, 0);
                    }
                });
                mImgAdapter6.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        //打开书
                        RJBookManager.getInstance().openBook(_mActivity,
                                (BookList.Item) adapter.getItem(position), false, true, 0);
                    }
                });


            }

            @Override
            public void onReqFailed(int errCode, String errMsg) {
                Log.e("reqBookList errCode:" + errCode + " errMsg:" + errMsg);
            }
        });
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
        Log.d("buy subscribe");
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
                break;
            case DownloadInfo.STATE_NONE:
                PreferenceUtil.setSharePref(_mActivity, "BOOK_STATE", 2);
                toastInfo("已暂停");
                break;
            case DownloadInfo.STATE_UNZIP:
                toastInfo("解压中");
                break;
        }

    }


}
