package com.ingic.caristocrat.fragments;

import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.databinding.ItemWalkthroughBinding;
import com.ingic.caristocrat.helpers.Titlebar;

import java.io.IOException;

/**
 */
public class WalkthroughItemFragment extends BaseFragment implements View.OnClickListener {
    String videoUrl = "";
    String description = "";
    ItemWalkthroughBinding binding;

    public WalkthroughItemFragment() {
    }

    public static WalkthroughItemFragment Instance() {
        return new WalkthroughItemFragment();
    }

    @Override
    public void setTitlebar(Titlebar titlebar) {
        titlebar.resetTitlebar(registrationActivityContext);
        titlebar.hideTitlebar();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.item_walkthrough, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {


        }
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onPause() {
        super.onPause();

           }












   /* private void initVideo() {
        binding.ivThumbnail.setVisibility(View.GONE);
        binding.ivPlay.setVisibility(View.GONE);
        binding.videoPlayer.setVisibility(View.VISIBLE);
        binding.videoPlayer.setActivity(registrationActivityContext);
        binding.videoPlayer.hideFullscreen(true);
        Uri videoUri = Uri.parse("https://www.rmp-streaming.com/media/bbb-360p.mp4");
        try {
            binding.videoPlayer.setVideoURI(videoUri);

//            videoview.

        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
