package com.ingic.caristocrat.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.adapters.LuxuryMarketCategoriesAdapter;
import com.ingic.caristocrat.databinding.FragmentLuxuryMarketBinding;
import com.ingic.caristocrat.helpers.Titlebar;
import com.ingic.caristocrat.webhelpers.models.Category;

import java.util.ArrayList;

/**
 */
public class LuxuryMarketFragment extends BaseFragment {
    FragmentLuxuryMarketBinding binding;
    LuxuryMarketCategoriesAdapter luxuryMarketCategoriesAdapter;
    private ArrayList<Category> child_category = new ArrayList<>();

    public static LuxuryMarketFragment Instance() {
        return new LuxuryMarketFragment();
    }

    public LuxuryMarketFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_luxury_market, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAdapter();
        setValue();
//        binding.swipeRefresh.setColorSchemeColors(ContextCompat.getColor(mainActivityContext,R.color.colorBlack));
//        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                binding.swipeRefresh.setRefreshing(false);
//            }
//        });
    }

    @Override
    public void setTitlebar(Titlebar titlebar) {
        titlebar.resetTitlebar(mainActivityContext);
        titlebar.showTitlebar(mainActivityContext);
        titlebar.setTitle(mainActivityContext.getResources().getString(R.string.luxury_market));
        titlebar.showBackButton(mainActivityContext, false);
    }

    private void setValue() {
        binding.tvHeader.setText(R.string.luxury_market_body1);
        binding.tvsubHeader.setText(Html.fromHtml(mainActivityContext.getResources().getString(R.string.luxury_market_body2)));
        binding.rvLuxuryMarketCategory.scrollToPosition(0);
        luxuryMarketCategoriesAdapter.addAll(child_category);
        if (luxuryMarketCategoriesAdapter.getItemCount() > 0)
            visibleView();
        else
            hideView();
    }

    private void initAdapter() {
        luxuryMarketCategoriesAdapter = new LuxuryMarketCategoriesAdapter(mainActivityContext, new ArrayList<Category>());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mainActivityContext);
        binding.rvLuxuryMarketCategory.setLayoutManager(linearLayoutManager);
        binding.rvLuxuryMarketCategory.setNestedScrollingEnabled(false);
        binding.rvLuxuryMarketCategory.setAdapter(luxuryMarketCategoriesAdapter);


    }

    public void setChild_category(ArrayList<Category> child_category) {
        this.child_category = child_category;
    }

    public void visibleView() {
        binding.rvLuxuryMarketCategory.setVisibility(View.VISIBLE);
        binding.noDataLayout.setVisibility(View.GONE);
    }

    public void hideView() {
        binding.rvLuxuryMarketCategory.setVisibility(View.GONE);
        binding.noDataLayout.setVisibility(View.VISIBLE);
    }

}
