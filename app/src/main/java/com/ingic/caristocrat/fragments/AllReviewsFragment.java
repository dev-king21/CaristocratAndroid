package com.ingic.caristocrat.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.adapters.DetailedReviewAdapter;
import com.ingic.caristocrat.databinding.FragmentAllReviewsBinding;
import com.ingic.caristocrat.helpers.Titlebar;
import com.ingic.caristocrat.models.UserRating;

import java.util.ArrayList;

public class AllReviewsFragment extends BaseFragment {
    private FragmentAllReviewsBinding binding;
    private DetailedReviewAdapter detailedReviewAdapter;
    private ArrayList<UserRating> arrayList;
    private Titlebar titlebar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_all_reviews, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        detailedReviewAdapter = new DetailedReviewAdapter(mainActivityContext);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mainActivityContext);
        binding.recyclerview.setLayoutManager(linearLayoutManager);
        binding.recyclerview.setNestedScrollingEnabled(false);
        binding.recyclerview.setAdapter(detailedReviewAdapter);

        detailedReviewAdapter.addAll(arrayList);
        detailedReviewAdapter.notifyDataSetChanged();
    }

    @Override
    public void setTitlebar(Titlebar titlebar) {
        AppBarLayout.LayoutParams params = new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, AppBarLayout.LayoutParams.WRAP_CONTENT);
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED | AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL);
        mainActivityContext.getCollapsingToolBarLayout().setLayoutParams(params);
        mainActivityContext.getCollapsingToolBarLayout().requestLayout();
        mainActivityContext.getRvSubCategoryItem().setVisibility(View.GONE);
        mainActivityContext.getCollapsingToolBarLayout().setVisibility(View.VISIBLE);

        this.titlebar = titlebar;
        titlebar.resetTitlebar(mainActivityContext);
        titlebar.setTitle(mainActivityContext.getResources().getString(R.string.all_reviews));
        titlebar.showBackButton(mainActivityContext, false);
    }

    @Override
    public void onDestroyView() {
        mainActivityContext.getIvSubCategoryItem().setImageResource(R.drawable.car_prof_bg);
        mainActivityContext.getIvSubCategoryItem().setScaleType(ImageView.ScaleType.FIT_XY);
        mainActivityContext.getIvSubCategoryItem().requestLayout();
        mainActivityContext.getCollapsingToolBarLayout().setVisibility(View.GONE);
        mainActivityContext.getRvSubCategoryItem().setVisibility(View.VISIBLE);
        super.onDestroyView();
    }

    public void setArrayList(ArrayList<UserRating> arrayList) {
        this.arrayList = arrayList;
    }
}
