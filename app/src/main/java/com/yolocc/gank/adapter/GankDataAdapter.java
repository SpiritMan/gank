package com.yolocc.gank.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.yolocc.gank.R;
import com.yolocc.gank.databinding.ItemGankDataBinding;
import com.yolocc.gank.databinding.ItemMeiziBinding;
import com.yolocc.gank.model.DataGank;
import com.yolocc.gank.viewModel.ItemGankViewModel;

import java.util.Collections;
import java.util.List;

import static android.databinding.DataBindingUtil.inflate;

/**
 */

public class GankDataAdapter extends RecyclerView.Adapter {

    private List<DataGank> mDataGanks;
    private String category;

    public GankDataAdapter(String category) {
        this.mDataGanks = Collections.emptyList();
        this.category = category;
    }

    public void addData(List<DataGank> addData) {
        DataGank dataGank = mDataGanks.get(mDataGanks.size() - 1);
        //消除分页导致同一天的信息显示在不同item中
        if (dataGank.getDate().equals(addData.get(0).getDate())) {
            dataGank.getGankInfos().addAll(addData.get(0).getGankInfos());
            addData.remove(0);
        }
        this.mDataGanks.addAll(addData);
        notifyItemRangeChanged(mDataGanks.size() - 1, addData.size());
    }

    public void refreshData(List<DataGank> newData) {
        this.mDataGanks = newData;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (TextUtils.equals(category, "福利")) {
            ItemMeiziBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_meizi, parent, false);
            return new MeiZiViewHolder(mBinding);
        } else {
            ItemGankDataBinding mBinding = inflate(LayoutInflater.from(parent.getContext()), R.layout.item_gank_data, parent, false);
            return new GankDataViewHolder(mBinding);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GankDataViewHolder) {
            GankDataViewHolder gankDataViewHolder = (GankDataViewHolder) holder;
            gankDataViewHolder.bindGankData(mDataGanks.get(position));
        } else if (holder instanceof MeiZiViewHolder) {
            MeiZiViewHolder meiZiViewHolder = (MeiZiViewHolder) holder;
            meiZiViewHolder.bindMeiZi(mDataGanks.get(position));
        }
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
            if (mBinding.getViewModel() == null) {
                mBinding.setViewModel(new ItemGankViewModel(dataGank, itemView.getContext()));
            } else {
                mBinding.getViewModel().setDataGank(dataGank);
            }
            GankTitleAdapter adapter = new GankTitleAdapter(dataGank.getGankInfos());
            mBinding.dateGankRecyclerView.setAdapter(adapter);
            mBinding.dateGankRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        }
    }

    public static class MeiZiViewHolder extends RecyclerView.ViewHolder {

        final ItemMeiziBinding mBinding;

        public MeiZiViewHolder(ItemMeiziBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }

        void bindMeiZi(DataGank dataGank) {
            if (mBinding.getViewModel() == null) {
                mBinding.setViewModel(new ItemGankViewModel(dataGank, itemView.getContext()));
            } else {
                mBinding.getViewModel().setDataGank(dataGank);
            }
        }
    }
}
