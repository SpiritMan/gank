package com.yolocc.gank.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.yolocc.gank.R;
import com.yolocc.gank.databinding.ItemGankTitleBinding;
import com.yolocc.gank.model.GankInfo;
import com.yolocc.gank.viewModel.ItemGankTitleViewModel;

import java.util.List;

/**
 */

public class GankTitleAdapter extends RecyclerView.Adapter<GankTitleAdapter.GankTitleViewHolder>{

    private List<GankInfo> mGankInfos;

    public GankTitleAdapter(List<GankInfo> gankInfos) {
        mGankInfos = gankInfos;
    }

    @Override
    public GankTitleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemGankTitleBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_gank_title,parent,false);
        return new GankTitleViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(GankTitleViewHolder holder, int position) {
        holder.bindMeiZi(mGankInfos.get(position));
    }

    @Override
    public int getItemCount() {
        return mGankInfos.size();
    }

    public static class GankTitleViewHolder extends RecyclerView.ViewHolder {

        final ItemGankTitleBinding mBinding;

        public GankTitleViewHolder(ItemGankTitleBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }

        void bindMeiZi(GankInfo gankInfo) {
            if(mBinding.getViewModel() == null) {
                mBinding.setViewModel(new ItemGankTitleViewModel(itemView.getContext(),gankInfo));
            } else {
                mBinding.getViewModel().setGankInfo(gankInfo);
            }
        }

    }
}
