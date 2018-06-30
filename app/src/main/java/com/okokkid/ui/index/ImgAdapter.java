package com.okokkid.ui.index;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import com.okokkid.R;
import com.rjsz.booksdk.bean.BookList;

import java.util.List;

/**
 * author： xuyafan
 * description:
 */
public class ImgAdapter extends BaseQuickAdapter<BookList.Item, BaseViewHolder> {


    public ImgAdapter(@Nullable List<BookList.Item> data) {
        super(R.layout.item_img, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BookList.Item item) {
        helper.setText(R.id.tv_name, item.bookname);
        Glide.with(mContext)
                .load(item.icon)
                .placeholder(R.drawable.book_default_bg)
                .into((ImageView) helper.getView(R.id.iv_img));
    }
}
