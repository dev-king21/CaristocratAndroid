package com.ingic.caristocrat.fragments;

import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.eftimoff.viewpagertransformers.DrawFromBackTransformer;
import com.ingic.caristocrat.R;
import com.ingic.caristocrat.adapters.WalkthroughPagerAdapter;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.FragmentWalkthroughBinding;
import com.ingic.caristocrat.helpers.JsonHelpers;
import com.ingic.caristocrat.helpers.Titlebar;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.helpers.Utils;
import com.ingic.caristocrat.models.WalkthroughWrapper;
import com.ingic.caristocrat.webhelpers.models.ApiArrayResponse;
import com.ingic.caristocrat.webhelpers.services.WebApiRequest;

import java.util.ArrayList;

/**
 */
public class WalkthroughFragment extends BaseFragment implements View.OnClickListener, ViewPager.OnPageChangeListener {
    FragmentWalkthroughBinding binding;
    int selectedPosition = 1;
    WalkthroughPagerAdapter adapter;
    ArrayList<WalkthroughWrapper> walkthroughWrappers = new ArrayList<>();
    MediaController vidControl;
    Uri uri;

    public WalkthroughFragment() {
    }

    public static WalkthroughFragment Instance() {
        return new WalkthroughFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_walkthrough, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewPager();
        setListeners();

    }

    @Override
    public void setTitlebar(Titlebar titlebar) {
        titlebar.resetTitlebar(registrationActivityContext);
        titlebar.hideTitlebar();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNext:
                next();
                break;
            case R.id.tvSkip:
                skip();
                break;
            case R.id.ivThumbnail:
            case R.id.ivPlay:
                playVideo(binding.videoPlayer);
                break;

        }

    }

    public void initViewPager() {

        adapter = new WalkthroughPagerAdapter(registrationActivityContext, new ArrayList<>());
        uri = Uri.parse(AppConstants.VIDEO_URL);
        binding.vpWalkthrough.setAdapter(adapter);
        binding.indicator.setViewPager(binding.vpWalkthrough);
        binding.vpWalkthrough.setPageTransformer(true, new DrawFromBackTransformer());
        if (Utils.isNetworkAvailable(registrationActivityContext))
            getWalkthroughContent();
        binding.vpWalkthrough.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectedPosition = position + 1;
                setLayout(binding.videoPlayer);


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void getWalkthroughContent() {

        WebApiRequest.Instance(registrationActivityContext).request(AppConstants.WebServicesKeys.WALKTHROUGH_DETAIIL, binding.getRoot(), null, null, null, new WebApiRequest.WebServiceArrayResponse() {
            @Override
            public void onSuccess(ApiArrayResponse apiArrayResponse) {
                walkthroughWrappers = (ArrayList<WalkthroughWrapper>) JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), WalkthroughWrapper.class);
                adapter.addAll(walkthroughWrappers);
                registrationActivityContext.hideLoader();
            }

            @Override
            public void onError() {
                registrationActivityContext.hideLoader();
            }
        });
    }


    public void setListeners() {
        binding.btnNext.setOnClickListener(this);
        binding.tvSkip.setOnClickListener(this);
        binding.ivPlay.setOnClickListener(this);
        binding.ivThumbnail.setOnClickListener(this);
        binding.vpWalkthrough.setOnPageChangeListener(this);
    }

    public void next() {
        if (selectedPosition < adapter.getCount())
            binding.vpWalkthrough.setCurrentItem(selectedPosition);
        else if (selectedPosition >= adapter.getCount()) {
            WalkthroughEndFragment walkthroughEndFragment = WalkthroughEndFragment.Instance();
            if (walkthroughWrappers.size() > 0) {
                if (walkthroughWrappers.get(selectedPosition).getType() == AppConstants.MediaType.WALKTHROUGH_VIDEO) {
                    if (walkthroughWrappers.get(selectedPosition).getMedia() != null && walkthroughWrappers.get(selectedPosition).getMedia().size() > 0) {
                        if (walkthroughWrappers.get(selectedPosition).getMedia().get(0).getFileUrl() != null) {
                            walkthroughEndFragment.setVideoURL(walkthroughWrappers.get(selectedPosition).getMedia().get(0).getFileUrl());
                        }
                    }
                }
            }
            if (walkthroughWrappers.size() > 0) {
                walkthroughEndFragment.setWalkthroughWrapper(walkthroughWrappers.get(walkthroughWrappers.size() - 1));
            }
            registrationActivityContext.replaceFragment(walkthroughEndFragment, WalkthroughEndFragment.class.getSimpleName(), false, true);
        }
    }

    public void skip() {
        registrationActivityContext.replaceFragment(SigninFragment.Instance(), SigninFragment.class.getName(), true, true);
    }

    public void playVideo(final VideoView videoView) {
        binding.ivThumbnail.setVisibility(View.GONE);
        binding.ivPlay.setVisibility(View.GONE);
        videoView.setVisibility(View.VISIBLE);
        videoView.destroyDrawingCache();
//        vidControl.setAnchorView(videoView);
//        videoView.setMediaController(vidControl);
//        videoView.setVideoURI(Uri.parse("https://www.rmp-streaming.com/media/bbb-360p.mp4"));
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        binding.progressbar.setVisibility(View.VISIBLE);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                // TODO Auto-generated method stub

                videoView.seekTo(0);
                videoView.start();
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

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                setLayout(videoView);
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        stopVideo();
    }

    @Override
    public void onResume() {
        super.onResume();
        setLayout(binding.videoPlayer);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopVideo();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopVideo();

    }

    private void stopVideo() {
        if (binding.videoPlayer.isPlaying()) {
            binding.videoPlayer.stopPlayback();
            binding.videoPlayer.destroyDrawingCache();
        }
    }

    private void setLayout(VideoView videoView) {
        videoView.setVisibility(View.GONE);
        binding.ivThumbnail.setVisibility(View.VISIBLE);
        UIHelper.setImageWithGlide(registrationActivityContext, binding.ivThumbnail, uri.toString());
        binding.ivPlay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (walkthroughWrappers.size() > 0) {
            if (walkthroughWrappers.get(position).getType() == AppConstants.MediaType.WALKTHROUGH_VIDEO) {
                if (walkthroughWrappers.get(position).getMedia() != null && walkthroughWrappers.get(position).getMedia().size() > 0) {
                    if (walkthroughWrappers.get(position).getMedia().get(0).getFileUrl() != null) {
                        uri = Uri.parse(walkthroughWrappers.get(position).getMedia().get(0).getFileUrl());
                        UIHelper.setImageWithGlide(registrationActivityContext, binding.ivThumbnail, uri.toString());
                    }
                }
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
