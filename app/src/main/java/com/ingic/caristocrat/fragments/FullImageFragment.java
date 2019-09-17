package com.ingic.caristocrat.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.adapters.FullImageAdapter;
import com.ingic.caristocrat.databinding.LayoutFullImageDialogBinding;
import com.ingic.caristocrat.helpers.Titlebar;
import com.ingic.caristocrat.webhelpers.models.Media;

import java.util.ArrayList;

public class FullImageFragment extends BaseFragment {
    LayoutFullImageDialogBinding binding;
    ArrayList<Media> arrayList;
    FullImageAdapter adapter;
    int currentPosition;

    public FullImageFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_full_image_dialog, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.ibBackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivityContext.onBackPressed();
            }
        });

        adapter = new FullImageAdapter(mainActivityContext);
        binding.viewpager.setAdapter(adapter);

        if (arrayList != null) {
            if (arrayList.size() > 0) {
                adapter.addAll(arrayList);
                adapter.notifyDataSetChanged();
                binding.viewpager.setCurrentItem(currentPosition, true);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AppBarLayout.LayoutParams params = new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, AppBarLayout.LayoutParams.WRAP_CONTENT);
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED | AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL);
        mainActivityContext.getCollapsingToolBarLayout().setLayoutParams(params);
        mainActivityContext.getCollapsingToolBarLayout().requestLayout();
        mainActivityContext.getRvSubCategoryItem().setVisibility(View.GONE);
        mainActivityContext.getCollapsingToolBarLayout().setVisibility(View.VISIBLE);
    }

    @Override
    public void setTitlebar(Titlebar titlebar) {
        titlebar.resetTitlebar(mainActivityContext);
        titlebar.showTransparentTitlebar(mainActivityContext);
        mainActivityContext.getCollapsingToolBarLayout().setVisibility(View.GONE);
    }

    public void setArrayList(ArrayList<Media> arrayList) {
        this.arrayList = arrayList;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

}
