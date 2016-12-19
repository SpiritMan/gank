package com.yolocc.gank.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yolocc.gank.R;
import com.yolocc.gank.adapter.GankDataAdapter;
import com.yolocc.gank.databinding.FragmentCategoryBinding;
import com.yolocc.gank.viewModel.CategoryFragmentViewModel;

/**
 */

public class CategoryFragment extends Fragment{

    public static final String CATEGORY_NAME = "categoryName";

    public static CategoryFragment newInstance(String categoryName) {
        Bundle args = new Bundle();
        args.putString(CATEGORY_NAME,categoryName);
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
        FragmentCategoryBinding fragmentCategoryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_category,container,false);
        CategoryFragmentViewModel categoryFragmentViewModel = new CategoryFragmentViewModel(getArguments().getString(CATEGORY_NAME),getActivity());
        fragmentCategoryBinding.setViewModel(categoryFragmentViewModel);
        initRecyclerView(fragmentCategoryBinding.gankRecyclerView);
        return fragmentCategoryBinding.getRoot();
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView(RecyclerView recyclerView) {
        GankDataAdapter gankDataAdapter = new GankDataAdapter();
        recyclerView.setAdapter(gankDataAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

}
