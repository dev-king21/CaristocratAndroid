package com.ingic.caristocrat.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.WalkthroughActivity;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.interfaces.WalkthroughActionListener;
import com.ingic.caristocrat.models.WalkthroughWrapper;

import java.util.ArrayList;

public class WalkthroughAdapter extends PagerAdapter {
    WalkthroughActivity walkthroughActivityContext;
    LayoutInflater layoutInflater;
    ArrayList<WalkthroughWrapper> arrayList;


    public WalkthroughAdapter(WalkthroughActivity walkthroughActivityContext) {
        this.walkthroughActivityContext = walkthroughActivityContext;
        this.layoutInflater = (LayoutInflater) walkthroughActivityContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.arrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View rootView = layoutInflater.inflate(R.layout.layout_app_walkthrough, container, false);

        VideoView videoPlayer;
        CardView cvMedia;
        TextView tvTitle, tvContent;

        ImageView ivThumbnail, ivPlay;

        cvMedia = rootView.findViewById(R.id.cvMedia);
        tvTitle = rootView.findViewById(R.id.tvTitle);
        tvContent = rootView.findViewById(R.id.tvContent);
        videoPlayer = rootView.findViewById(R.id.videoPlayer);
        ivThumbnail = rootView.findViewById(R.id.ivThumbnail);
        ivPlay = rootView.findViewById(R.id.ivPlay);

        tvTitle.setText(arrayList.get(position).getTitle());
        tvContent.setText(arrayList.get(position).getContent());

        switch (arrayList.get(position).getType()) {
            case AppConstants.WalkthroughTypes.IMAGE_ONLY:
            case AppConstants.WalkthroughTypes.TEXT_IMAGE:
            case AppConstants.WalkthroughTypes.TEXT_IMAGE_URL:
                if (arrayList.get(position).getMedia() != null && arrayList.get(position).getMedia().size() > 0) {
                    UIHelper.setImageWithGlide(walkthroughActivityContext, ivThumbnail, arrayList.get(position).getMedia().get(0).getFileUrl());
                    ivThumbnail.setVisibility(View.VISIBLE);
                    cvMedia.setVisibility(View.VISIBLE);
                }
                break;

            case AppConstants.WalkthroughTypes.VIDEO_ONLY:
            case AppConstants.WalkthroughTypes.TEXT_VIDEO:
            case AppConstants.WalkthroughTypes.TEXT_VIDEO_URL:
                if (arrayList.get(position).getMedia() != null && arrayList.get(position).getMedia().size() > 0) {
                    Uri uri = Uri.parse(arrayList.get(position).getMedia().get(0).getFileUrl());
                    UIHelper.setImageWithGlide(walkthroughActivityContext, ivThumbnail, uri.toString());
                    ivPlay.setVisibility(View.VISIBLE);
                    ivThumbnail.setVisibility(View.VISIBLE);
                    cvMedia.setVisibility(View.VISIBLE);

                    ivPlay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ivThumbnail.setVisibility(View.GONE);
                            ivPlay.setVisibility(View.GONE);
                            videoPlayer.setVisibility(View.VISIBLE);
                            videoPlayer.stopPlayback();
                            videoPlayer.destroyDrawingCache();
                            Uri uri = Uri.parse(arrayList.get(position).getMedia().get(0).getFileUrl());
                            videoPlayer.setVideoURI(uri);
                            videoPlayer.requestFocus();
                            videoPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                                @Override
                                public void onPrepared(MediaPlayer mp) {
                                    videoPlayer.seekTo(0);
                                    videoPlayer.start();

                                    mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                                        @Override
                                        public void onVideoSizeChanged(MediaPlayer mp, int arg1, int arg2) {
                                        }
                                    });
                                }
                            });
                            videoPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    ivPlay.setVisibility(View.VISIBLE);
                                    ivThumbnail.setVisibility(View.VISIBLE);
                                    videoPlayer.stopPlayback();
                                    videoPlayer.setVisibility(View.GONE);
                                }
                            });
                        }
                    });
                }
                break;
        }

        container.addView(rootView);
        return rootView;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        VideoView videoPlayer = container.findViewById(R.id.videoPlayer);

        if (videoPlayer != null) {
            videoPlayer.stopPlayback();
            videoPlayer.destroyDrawingCache();
            videoPlayer = null;
        }

        container.removeView((View) object);
    }

    public void addAll(ArrayList<WalkthroughWrapper> arrayList) {
        this.arrayList.clear();
        this.arrayList.addAll(arrayList);
    }

    public VideoView getViewPlayer(){
        return null;
    }

}
