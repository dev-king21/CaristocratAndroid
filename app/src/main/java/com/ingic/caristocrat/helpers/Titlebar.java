package com.ingic.caristocrat.helpers;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.BaseActivity;
import com.ingic.caristocrat.databinding.TitlebarBinding;
import com.like.LikeButton;
import com.skyfishjy.library.RippleBackground;

/**
 */
public class Titlebar extends RelativeLayout {
    private TitlebarBinding binding;

    public Titlebar(Context context) {
        super(context);
        initLayout(context);
    }

    public Titlebar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context);
    }

    public Titlebar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout(context);
    }

    private void initLayout(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        binding = DataBindingUtil.inflate(inflater, R.layout.titlebar, this, true);
    }

    public void hideTitlebar() {
        binding.rlTitlebarMainLayout.setVisibility(View.GONE);
    }

    public void inVisibleTitlebar() {
        binding.rlTitlebarMainLayout.setVisibility(View.INVISIBLE);
    }

    public void showTitlebar(BaseActivity activityContext) {
        binding.rlTitlebarMainLayout.setVisibility(View.VISIBLE);
        binding.rlTitlebarMainLayout.setBackgroundColor(activityContext.getResources().getColor(R.color.colorPrimary));
    }

    public void resetTitlebar(final BaseActivity activityContext) {
        binding.ibBackbtn.setEnabled(true);
        binding.ibBackbtn.setImageResource(R.drawable.backbtn);
        binding.ibBackbtn.setVisibility(View.GONE);
        binding.ibLike.setVisibility(View.GONE);
        binding.ibHome.setVisibility(View.GONE);
        binding.ibSearch.setVisibility(View.GONE);
        binding.tvTitle.setVisibility(View.GONE);
        binding.llProfileButton.setVisibility(View.GONE);
        binding.ibLikeWhite.setVisibility(View.GONE);
        binding.ibShare.setVisibility(View.GONE);
        binding.ibAddFav.setVisibility(View.GONE);
        binding.ibSettings.setVisibility(View.GONE);
        binding.rlNotification.setVisibility(View.GONE);
        binding.tvClose.setVisibility(View.GONE);
        binding.tvCancel.setVisibility(View.GONE);
        binding.ibFilter.setVisibility(View.GONE);
        binding.ibAddCar.setVisibility(View.GONE);
        binding.etTitlebarSearch.setVisibility(View.GONE);
        binding.tvLink.setVisibility(View.GONE);
        binding.ibSort.setVisibility(View.GONE);
    }

    public void setTitle(String title) {
        binding.tvTitle.setVisibility(VISIBLE);
        binding.tvTitle.setText(title.trim());
        binding.tvTitle.setSelected(true);
    }

    public void showLink(String link) {
        binding.tvLink.setVisibility(VISIBLE);
        binding.tvLink.setText(link);
        binding.tvLink.setSelected(true);
    }

    public ImageButton showBackButton(final BaseActivity activityContext, boolean isWhite) {
        if (isWhite) {
            binding.ibBackbtn.setImageResource(R.drawable.backbtn_white);
        }
        binding.ibBackbtn.setVisibility(View.VISIBLE);
        binding.ibBackbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.ibBackbtn.setEnabled(false);
                activityContext.onBackPressed();
            }
        });

        return binding.ibBackbtn;
    }

    public ImageButton showSearchButton(final BaseActivity activityContext) {
        binding.ibSearch.setVisibility(View.VISIBLE);
        binding.ibSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return binding.ibSearch;
    }

    public ImageButton showHomeButton(final BaseActivity activityContext) {
        binding.ibHome.setVisibility(View.VISIBLE);
        binding.ibHome.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                activityContext.recreate();
            }
        });

        return binding.ibHome;
    }

    public RippleBackground showProfileButton() {
        binding.llProfileButton.setVisibility(View.VISIBLE);
        binding.llProfileButton.startRippleAnimation();
        return binding.llProfileButton;
    }

    public ImageButton showShareButton() {
        binding.ibShare.setVisibility(View.VISIBLE);
        return binding.ibShare;
    }

    public LikeButton showLikeButton() {
        binding.ibLikeWhite.setVisibility(View.VISIBLE);
        return binding.ibLikeWhite;
    }

    public ImageButton showAddToFavButton() {
        binding.ibAddFav.setVisibility(View.VISIBLE);
        return binding.ibAddFav;
    }

    public ImageButton showSettingsButton() {
        binding.ibSettings.setVisibility(View.VISIBLE);
        return binding.ibSettings;
    }

    public RelativeLayout showNotifications() {
        binding.rlNotification.setVisibility(View.VISIBLE);
        return binding.rlNotification;
    }

    public TextView showCloseText() {
        binding.tvClose.setVisibility(View.VISIBLE);
        return binding.tvClose;
    }

    public ImageButton showFilter() {
        binding.ibFilter.setVisibility(View.VISIBLE);
        return binding.ibFilter;
    }

    public TextView showCancelText() {
        binding.tvCancel.setVisibility(View.VISIBLE);
        return binding.tvCancel;
    }

    public ImageButton showAddCarButton() {
        binding.ibAddCar.setVisibility(View.VISIBLE);
        return binding.ibAddCar;
    }

    public EditText showSearch() {
        binding.etTitlebarSearch.setVisibility(View.VISIBLE);
        return binding.etTitlebarSearch;
    }

    public void showTransparentTitlebar(BaseActivity activityContext) {
        showTitlebar(activityContext);
        binding.rlTitlebarMainLayout.setBackgroundColor(activityContext.getResources().getColor(android.R.color.transparent));
    }

    public void setProfilepic(BaseActivity activity, String path) {
        UIHelper.setUserImageWithGlide(activity, binding.rivProfilePic, path);
    }

    public ImageButton showSort() {
        binding.ibSort.setVisibility(View.VISIBLE);
        return binding.ibSort;
    }
}
