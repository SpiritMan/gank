package com.yolocc.gank.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.yolocc.gank.R;
import com.yolocc.gank.databinding.ItemGankDataBinding;
import com.yolocc.gank.model.DataGank;
import com.yolocc.gank.viewModel.ItemGankViewModel;

import java.util.Collections;
import java.util.List;

/**
 */

public class GankDataAdapter extends RecyclerView.Adapter<GankDataAdapter.GankDataViewHolder>{

    private List<DataGank> mDataGanks;

    public GankDataAdapter() {
        this.mDataGanks = Collections.emptyList();
    }

    public void addData(List<DataGank> addData){
        this.mDataGanks.addAll(addData);
        //TODO 优化更新测量
        notifyDataSetChanged();
    }

    public void refreshData(List<DataGank> newData) {
        this.mDataGanks = newData;
        notifyDataSetChanged();
    }


    @Override
    public GankDataAdapter.GankDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemGankDataBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_gank_data,parent,false);
        return new GankDataViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(GankDataAdapter.GankDataViewHolder holder, int position) {
        holder.bindGankData(mDataGanks.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataGanks.size();
    }

    public static class GankDataViewHolder extends RecyclerView.ViewHolder {
        final ItemGankDataBinding mBinding;

        public GankDataViewHolder(ItemGankDataBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }

        void bindGankData(DataGank dataGank) {
            if(mBinding.getViewModel() == null) {
                mBinding.setViewModel(new ItemGankViewModel(dataGank));
            } else {
                mBinding.getViewModel().setDataGank(dataGank);
            }
        }
    }
}
