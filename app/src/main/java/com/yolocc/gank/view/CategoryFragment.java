package com.yolocc.gank.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yolocc.gank.R;
import com.yolocc.gank.adapter.GankDataAdapter;
import com.yolocc.gank.databinding.FragmentCategoryBinding;
import com.yolocc.gank.model.DataGank;
import com.yolocc.gank.viewModel.CategoryFragmentViewModel;

import java.util.List;

/**
 */

public class CategoryFragment extends Fragment implements CategoryFragmentViewModel.DataListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String CATEGORY_NAME = "categoryName";
    private int page = 1;
    private boolean isLoadingData = true, isLoadComplete = false;
    private LinearLayoutManager mLinearLayoutManager;
    private FragmentCategoryBinding fragmentCategoryBinding;

    public static CategoryFragment newInstance(String categoryName) {
        Bundle args = new Bundle();
        args.putString(CATEGORY_NAME, categoryName);
        CategoryFragment categoryFragment = new CategoryFragment();
        categoryFragment.setArguments(args);
        return categoryFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentCategoryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_category, container, false);
        fragmentCategoryBinding.refreshSrl.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorAccent));
        fragmentCategoryBinding.refreshSrl.setOnRefreshListener(this);
        fragmentCategoryBinding.refreshSrl.setRefreshing(true);
        fragmentCategoryBinding.gankRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
                int totalItemCount = mLinearLayoutManager.getItemCount();
                if ((lastVisibleItem > totalItemCount - 5) && !isLoadingData && !isLoadComplete) {
                    page = page + 1;
                    isLoadingData = true;
                    fragmentCategoryBinding.getViewModel().getCategoryData(page);
                } else if((lastVisibleItem > totalItemCount - 2) && !isLoadingData && isLoadComplete){
                    Snackbar.make(recyclerView,"没有更多数据啦╮(╯_╰)╭",Snackbar.LENGTH_SHORT).show();
                }
            }

        });
        CategoryFragmentViewModel categoryFragmentViewModel = new CategoryFragmentViewModel(getArguments().getString(CATEGORY_NAME), this);
        fragmentCategoryBinding.setViewModel(categoryFragmentViewModel);
        initRecyclerView(fragmentCategoryBinding.gankRecyclerView, categoryFragmentViewModel.mCategory);
        return fragmentCategoryBinding.getRoot();
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView(RecyclerView recyclerView, String category) {
        GankDataAdapter gankDataAdapter = new GankDataAdapter(category);
        recyclerView.setAdapter(gankDataAdapter);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLinearLayoutManager);
    }

    @Override
    public void onGankDataChanged(List<DataGank> dataGanks, boolean isRefresh) {
        isLoadingData = false;
        fragmentCategoryBinding.refreshSrl.setRefreshing(false);
        if (dataGanks.size() == 0) {
            isLoadComplete = true;
            return;
        }
        GankDataAdapter gankDataAdapter = (GankDataAdapter) fragmentCategoryBinding.gankRecyclerView.getAdapter();
        if (isRefresh) {
            gankDataAdapter.refreshData(dataGanks);
        } else {
            gankDataAdapter.addData(dataGanks);
        }

    }

    @Override
    public void onGankDateError() {
        isLoadingData = false;
        fragmentCategoryBinding.refreshSrl.setRefreshing(false);
        if (page == 1) {
            // TODO: 16/12/29 show empty view 
        }
    }

    @Override
    public void onRefresh() {
        if (!isLoadingData) {
            page = 1;
            fragmentCategoryBinding.getViewModel().getCategoryData(page);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentCategoryBinding.getViewModel().destroy();
    }
}
