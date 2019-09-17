package com.ingic.caristocrat.fragments;

import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.FragmentWalkthroughEndBinding;
import com.ingic.caristocrat.helpers.Titlebar;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.models.WalkthroughWrapper;

/**
 */
public class WalkthroughEndFragment extends BaseFragment implements View.OnClickListener {
    String videoUrl = "";
    String description = "";
    FragmentWalkthroughEndBinding binding;
    Uri uri;
    WalkthroughWrapper walkthroughWrapper;
    String videoURL;

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public WalkthroughEndFragment() {
    }

    public void setWalkthroughWrapper(WalkthroughWrapper walkthroughWrapper) {
        this.walkthroughWrapper = walkthroughWrapper;
    }

    public static WalkthroughEndFragment Instance() {
        return new WalkthroughEndFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_walkthrough_end, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListeners();
        setData();
//        uri = Uri.parse("android.resource://" + registrationActivityContext.getPackageName() + "/" + R.raw.welcome_video);
//        if (videoURL != null) {
//            uri = Uri.parse(videoURL);
//        }
        if (walkthroughWrapper != null) {
            uri = Uri.parse(walkthroughWrapper.getMedia().get(0).getFileUrl());
        }

    }

    private void setData() {
        if (walkthroughWrapper != null) {
            binding.tvWalkthroughEndDescription.setText(walkthroughWrapper.getContent() != null ? walkthroughWrapper.getContent() : "");
            binding.tvWalkthroughEndTitle.setText(walkthroughWrapper.getTitle() != null ? walkthroughWrapper.getTitle() : "");
        }
    }

    @Override
    public void setTitlebar(Titlebar titlebar) {
        titlebar.resetTitlebar(registrationActivityContext);
        titlebar.hideTitlebar();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivThumbnail:
            case R.id.ivPlay:
                playVideo();
                break;
            case R.id.btnSignUp:
                signup();
                break;
            case R.id.tvSkip:
                signin();
                break;
            case R.id.tvClickHere:
                signin();
                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.videoPlayer.stopPlayback();
        setLayout();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.videoPlayer.stopPlayback();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.videoPlayer.stopPlayback();
        binding.videoPlayer.destroyDrawingCache();
    }

    @Override
    public void onStop() {
        super.onStop();
        binding.videoPlayer.stopPlayback();
    }

    public void setListeners() {
        binding.tvSkip.setOnClickListener(this);
        binding.btnSignUp.setOnClickListener(this);
        binding.tvClickHere.setOnClickListener(this);
        binding.ivThumbnail.setOnClickListener(this);
        binding.ivPlay.setOnClickListener(this);
    }

    public void setLayout() {
        binding.ivThumbnail.setVisibility(View.VISIBLE);
        binding.ivPlay.setVisibility(View.VISIBLE);
        if (uri!=null) {
            UIHelper.setImageWithGlide(registrationActivityContext, binding.ivThumbnail, uri.toString());
        }
        binding.videoPlayer.setVisibility(View.GONE);
    }


    public void playVideo() {
        binding.ivThumbnail.setVisibility(View.GONE);
        binding.ivPlay.setVisibility(View.GONE);
        binding.videoPlayer.setVisibility(View.VISIBLE);
        binding.videoPlayer.stopPlayback();
        binding.videoPlayer.destroyDrawingCache();
//        final MediaController vidControl = new MediaController(registrationActivityContext);
//        vidControl.setAnchorView(binding.videoPlayer);
//        binding.videoPlayer.setMediaController(vidControl);
        binding.videoPlayer.setVideoURI(uri);
        binding.videoPlayer.requestFocus();
        binding.progressbar.setVisibility(View.VISIBLE);

        binding.videoPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                // TODO Auto-generated method stub
                binding.videoPlayer.seekTo(0);
                binding.videoPlayer.start();

                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int arg1,
                                                   int arg2) {
                        // TODO Auto-generated method stub
                        binding.progressbar.setVisibility(View.GONE);


                    }
                });


            }
        });

        binding.videoPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                setLayout();
            }
        });

    }

    public void signup() {
//        registrationActivityContext.startActivity(MainActivity.class, true);
        registrationActivityContext.replaceFragment(SignupFragment.Instance(false), SignupFragment.class.getName(), false, false);
    }

    public void skip() {
        registrationActivityContext.replaceFragment(SigninFragment.Instance(), SigninFragment.class.getName(), true, true);

    }

    public void signin() {
        registrationActivityContext.replaceFragment(SigninFragment.Instance(), SigninFragment.class.getName(), true, true);

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
