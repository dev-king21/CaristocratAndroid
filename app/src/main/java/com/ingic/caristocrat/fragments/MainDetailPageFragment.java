package com.ingic.caristocrat.fragments;

import android.Manifest;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.SourceLinkWebViewActivity;
import com.ingic.caristocrat.adapters.SimilarListingsAdapter;
import com.ingic.caristocrat.adapters.SimilarNewsListingAdapter;
import com.ingic.caristocrat.adapters.SubcategoriesFeatureAdapter;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.FragmentMainDetailPageBinding;
import com.ingic.caristocrat.dialogs.PDFViewDialog;
import com.ingic.caristocrat.dialogs.SubscribeViewDialog;
import com.ingic.caristocrat.helpers.DateFormatHelper;
import com.ingic.caristocrat.helpers.JsonHelpers;
import com.ingic.caristocrat.helpers.RecyclerTouchListener;
import com.ingic.caristocrat.helpers.ShareDetail;
import com.ingic.caristocrat.helpers.Titlebar;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.helpers.Utils;
import com.ingic.caristocrat.interfaces.ClickListenerRecycler;
import com.ingic.caristocrat.models.TradeCar;
import com.ingic.caristocrat.telr.TelrUtils;
import com.ingic.caristocrat.webhelpers.models.ApiResponse;
import com.ingic.caristocrat.webhelpers.models.Media;
import com.ingic.caristocrat.webhelpers.models.News;
import com.ingic.caristocrat.webhelpers.models.User;
import com.ingic.caristocrat.webhelpers.services.WebApiRequest;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerInitListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class MainDetailPageFragment extends BaseFragment implements View.OnClickListener {
    FragmentMainDetailPageBinding binding;
    PopupWindow popup;
    int categoryId;
    String imageUrl;
    User user;
    News news;
    int viewsCount, likesCount, commentsCount;
    boolean isLiked, isVideo;
    YouTubePlayer windowYouTubePlayer;
    SubcategoriesFeatureAdapter featureAdapter;
    ArrayList<News> featuredArticleArrayList;
    ArrayList<News> similarListing = new ArrayList<>();
    ArrayList<TradeCar> tradeCars = new ArrayList<>();
    SimilarListingsAdapter similarListingsAdapter;
    private float singlePrice = 0.f;
    private float allPrice = 0.f;
    private double itemWidth;
    public static boolean paid = false, login = false;


    public static MainDetailPageFragment Instance() {
        return new MainDetailPageFragment();
    }

    public void setSimilarListing(ArrayList<News> similarListing) {
        this.similarListing = similarListing;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_detail_page, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = preferenceHelper.getUser();

        itemWidth = UIHelper.screensize(mainActivityContext, "x") / 2.45;

        featureAdapter = new SubcategoriesFeatureAdapter(mainActivityContext, null, false);
        binding.viewpager.setAdapter(featureAdapter);
        initSimilarListing();
        initRelatedCars();

        if (mainActivityContext.showLoader()) {
            getDetail();
        }
        setListeners();
        binding.btnSubscribe.setOnClickListener(this);
    }

    private void initRelatedCars() {
        similarListingsAdapter = new SimilarListingsAdapter(mainActivityContext);
        similarListingsAdapter.setWidth(itemWidth);
        binding.rvRelatedCar.setLayoutManager(new LinearLayoutManager(mainActivityContext, LinearLayoutManager.HORIZONTAL, false));
        binding.rvRelatedCar.setAdapter(similarListingsAdapter);
        binding.rvRelatedCar.addOnItemTouchListener(new RecyclerTouchListener(mainActivityContext, binding.rvRelatedCar, new ClickListenerRecycler() {
            @Override
            public void onClick(View view, int position) {
                LuxuryMarketDetailsFragment luxuryMarketDetailsFragment = new LuxuryMarketDetailsFragment();
                luxuryMarketDetailsFragment.setCurrentTradeCar(similarListingsAdapter.getItem(position));
                if (similarListingsAdapter.getItem(position).getCategory() != null) {
                    luxuryMarketDetailsFragment.setCategoryKey(similarListingsAdapter.getItem(position).getCategory().getSlug());
                }
                mainActivityContext.replaceFragment(luxuryMarketDetailsFragment, LuxuryMarketDetailsFragment.class.getSimpleName(), true, true);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void getRelatedCars(int id) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);
        WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.LUXURY_MARKET_CATEGORY_DETAIL, binding.getRoot(), null, params, new WebApiRequest.WebServiceObjectResponse() {
            @Override
            public void onSuccess(ApiResponse apiResponse) {
                TradeCar currentTradeCar = (TradeCar) JsonHelpers.convertToModelClass(apiResponse.getData(), TradeCar.class);
                tradeCars.clear();
                tradeCars.add(currentTradeCar);
                similarListingsAdapter.addAll(tradeCars);
                binding.llRelatedCar.setVisibility(View.VISIBLE);
                mainActivityContext.hideLoader();
            }

            @Override
            public void onError() {
                mainActivityContext.hideLoader();
            }
        }, null);
    }

    private void initSimilarListing() {
        if (similarListing != null && similarListing.size() > 0) {
            binding.llSimilarListing.setVisibility(View.VISIBLE);
            SimilarNewsListingAdapter similarNewsListingAdapter = new SimilarNewsListingAdapter(mainActivityContext);
            similarNewsListingAdapter.setWidth(itemWidth);
            binding.rvSimilarListings.setLayoutManager(new LinearLayoutManager(mainActivityContext, LinearLayoutManager.HORIZONTAL, false));
            binding.rvSimilarListings.setAdapter(similarNewsListingAdapter);
            similarNewsListingAdapter.addAll(similarListing);
            binding.rvSimilarListings.addOnItemTouchListener(new RecyclerTouchListener(mainActivityContext, binding.rvSimilarListings, new ClickListenerRecycler() {
                @Override
                public void onClick(View view, int position) {
                    MainDetailPageFragment mainDetailPageFragment = new MainDetailPageFragment();
                    mainDetailPageFragment.setCategoryId(similarListing.get(position).getId());
                    if (similarListing != null && similarListing.size() > 0) {
                        if (similarListing.get(position).getMedia().size() > 0) {
                            mainDetailPageFragment.setImageUrl(similarListing.get(position).getMedia().get(0).getFileUrl());
                        }
                    }
                    ArrayList<News> similarListingList = new ArrayList<>();
                    similarListingList.addAll(similarListing);
                    similarListingList.remove(position);
                    similarListingList.add(news);
                    mainDetailPageFragment.setSimilarListing(similarListingList);
                    mainActivityContext.replaceFragment(mainDetailPageFragment, MainDetailPageFragment.class.getSimpleName(), true, false);
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));
        } else {
            binding.llSimilarListing.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (paid && preferenceHelper.getLoginStatus())//check successful transaction
        {
            binding.btnSubscribe.setText("View Report");
            paid = false;
        }

        if (login && preferenceHelper.getLoginStatus()) {//
            user = preferenceHelper.getUser();
            resumeReportPayment();
            login = false;
        }

        if (windowYouTubePlayer != null) {
            windowYouTubePlayer.play();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (windowYouTubePlayer != null) {
            windowYouTubePlayer.pause();
        }
    }

    @Override
    public void onDestroyView() {
//        mainActivityContext.getRvSubCategoryItem().setVisibility(View.GONE);
        mainActivityContext.getIvSubCategoryItem().setVisibility(View.GONE);
        mainActivityContext.getIvSubCategoryItem().setImageDrawable(null);
        mainActivityContext.getBinding().tvWriteComment.setVisibility(View.GONE);
        mainActivityContext.getBinding().llCommentsLayout.setVisibility(View.GONE);
//        mainActivityContext.getBinding().youtubePlayer.setVisibility(View.GONE);
//        mainActivityContext.getBinding().youtubePlayer.release();

        AppBarLayout.LayoutParams params = new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, AppBarLayout.LayoutParams.WRAP_CONTENT);
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED | AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL);
        mainActivityContext.getCollapsingToolBarLayout().setLayoutParams(params);
        mainActivityContext.getCollapsingToolBarLayout().requestLayout();
        mainActivityContext.getRvSubCategoryItem().setVisibility(View.GONE);
        mainActivityContext.getCollapsingToolBarLayout().setVisibility(View.VISIBLE);

        binding.youtubePlayer.release();
        super.onDestroyView();
    }

    @Override
    public void setTitlebar(final Titlebar titlebar) {
        titlebar.resetTitlebar(mainActivityContext);

        if (news != null) {
            if (news.getMedia().size() > 0) {
                switch (news.getMedia().get(0).getMediaType()) {
                    case AppConstants.MediaType.IMAGE:
                        UIHelper.getContentScrimColor(mainActivityContext, imageUrl);
                        titlebar.resetTitlebar(mainActivityContext);
                        titlebar.showBackButton(mainActivityContext, true);
                        titlebar.showTransparentTitlebar(mainActivityContext);
                        mainActivityContext.getCollapsingToolBarLayout().setVisibility(View.GONE);
                        binding.rlSlider.setVisibility(View.VISIBLE);
                        binding.ibBackbtn.setOnClickListener(this);
                        binding.ibShare.setOnClickListener(this);
                        binding.ibLikeWhite.setOnLikeListener(new OnLikeListener() {
                            @Override
                            public void liked(LikeButton likeButton) {
                                if (!preferenceHelper.getLoginStatus()) {
                                    binding.ibLikeWhite.setLiked(false);
                                    launchSigninRequirement(mainActivityContext, mainActivityContext.getResources().getString(R.string.require_signin_message));
                                } else {
                                    setNewsInteraction(AppConstants.NewsInteractions.LIKE);
                                    likesCount = likesCount + 1;
                                    binding.tvLikes.setText((likesCount == 0 || likesCount == 1) ? likesCount + " " + mainActivityContext.getResources().getString(R.string.like) : likesCount + " " + mainActivityContext.getResources().getString(R.string.likes));
                                }
                            }

                            @Override
                            public void unLiked(LikeButton likeButton) {
                                if (!preferenceHelper.getLoginStatus()) {
                                    launchSigninRequirement(mainActivityContext, mainActivityContext.getResources().getString(R.string.require_signin_message));
                                } else {
                                    setNewsInteraction(AppConstants.NewsInteractions.LIKE);
                                    likesCount = likesCount - 1;
                                    binding.tvLikes.setText((likesCount == 0 || likesCount == 1) ? likesCount + " " + mainActivityContext.getResources().getString(R.string.like) : likesCount + " " + mainActivityContext.getResources().getString(R.string.likes));
                                }
                            }
                        });
                        binding.ibAddFav.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!preferenceHelper.getLoginStatus()) {
                                    launchSigninRequirement(mainActivityContext, mainActivityContext.getResources().getString(R.string.require_signin_message));
                                } else {
                                    showFavPopup(titlebar.showAddToFavButton());
                                }
                            }
                        });
                        break;

                    case AppConstants.MediaType.VIDEO:
                        isVideo = true;
                        binding.youtubePlayer.setVisibility(View.VISIBLE);
                        binding.youtubePlayer.addFullScreenListener(new YouTubePlayerFullScreenListener() {
                            @Override
                            public void onYouTubePlayerEnterFullScreen() {
                                binding.youtubePlayer.exitFullScreen();
                            }

                            @Override
                            public void onYouTubePlayerExitFullScreen() {
                                binding.youtubePlayer.exitFullScreen();
                            }
                        });
                        titlebar.setTitle(mainActivityContext.getResources().getString(R.string.news));

/*
                        if (news.getHeadline().length() < 10) {
                            titlebar.setTitle(news.getHeadline());
                        } else {
                            titlebar.setTitle(news.getHeadline().substring(0, 10) + "...");
                        }
*/
                        titlebar.showBackButton(mainActivityContext, false);

                        titlebar.showLikeButton().setLikeDrawable(mainActivityContext.getResources().getDrawable(R.drawable.heart));
                        titlebar.showLikeButton().setUnlikeDrawable(mainActivityContext.getResources().getDrawable(R.drawable.like));
                        titlebar.showShareButton().setImageDrawable(mainActivityContext.getResources().getDrawable(R.drawable.share_inverted));
                        titlebar.showAddToFavButton().setImageDrawable(mainActivityContext.getResources().getDrawable(R.drawable.add_fav_inverted));
                        binding.rlSlider.setVisibility(View.GONE);
                        break;
                }
            }
        }

        titlebar.showAddToFavButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!preferenceHelper.getLoginStatus()) {
                    launchSigninRequirement(mainActivityContext, mainActivityContext.getResources().getString(R.string.require_signin_message));
                } else {
                    showFavPopup(titlebar.showAddToFavButton());
                }
            }
        });
        titlebar.showLikeButton().setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                if (!preferenceHelper.getLoginStatus()) {
                    titlebar.showLikeButton().setLiked(false);
                    launchSigninRequirement(mainActivityContext, mainActivityContext.getResources().getString(R.string.require_signin_message));
                } else {
                    setNewsInteraction(AppConstants.NewsInteractions.LIKE);
                    likesCount = likesCount + 1;
                    binding.tvLikes.setText((likesCount == 0 || likesCount == 1) ? likesCount + " " + mainActivityContext.getResources().getString(R.string.like) : likesCount + " " + mainActivityContext.getResources().getString(R.string.likes));
                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                if (!preferenceHelper.getLoginStatus()) {
                    launchSigninRequirement(mainActivityContext, mainActivityContext.getResources().getString(R.string.require_signin_message));
                } else {
                    setNewsInteraction(AppConstants.NewsInteractions.LIKE);
                    likesCount = likesCount - 1;
                    binding.tvLikes.setText((likesCount == 0 || likesCount == 1) ? likesCount + " " + mainActivityContext.getResources().getString(R.string.like) : likesCount + " " + mainActivityContext.getResources().getString(R.string.likes));
                }
            }
        });

        titlebar.showShareButton().setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        UIHelper.hideSoftKeyboard(mainActivityContext, view);
        if (windowYouTubePlayer != null) {
            windowYouTubePlayer.pause();
        }
        switch (view.getId()) {
            case R.id.llCommentsLayout:
            case R.id.tvComments:
                openCommentsWindow();
                break;

            case R.id.ibShare:
//                UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.implemented_later), Toast.LENGTH_SHORT);
                if (news != null)
                    shareDetail(news);
                break;

            case R.id.tvLikes:
//                UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.implemented_later), Toast.LENGTH_SHORT);
                break;

            case R.id.tvViews:
//                UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.implemented_later), Toast.LENGTH_SHORT);
                break;

            case R.id.tvNewsArlicleLink:
                openSourceLink();
                break;

            case R.id.ibBackbtn:
                mainActivityContext.onBackPressed();
                break;
            case R.id.btnSubscribe:

                if (binding.btnSubscribe.getText().toString().equalsIgnoreCase("Subscribe to View")) {
                    SubscribeViewDialog subscribeViewDialog = SubscribeViewDialog.newInstance(mainActivityContext);
                    subscribeViewDialog.singlePrice = singlePrice;
                    subscribeViewDialog.allPrice = allPrice;
                    subscribeViewDialog.show(mainActivityContext.getFragmentManager(), null);
                    /*subscribeViewDialog.setPrice();*/
                    subscribeViewDialog.setSubscribeListener((boolean single) -> {
                        mainActivityContext.newsId = "" + news.getId();
                        if (single) {
                            mainActivityContext.oneReport = true;
                            if (preferenceHelper.getLoginStatus())
                                TelrUtils.IntentTelr(String.format("%.2f", singlePrice), getActivity(), user);//payment for report subscription
                            else
                                launchSignin(mainActivityContext);
                        } else {
                            mainActivityContext.oneReport = false;
                            //mainActivityContext.twiceAllReport = true;
                            if (preferenceHelper.getLoginStatus())
                                TelrUtils.IntentTelr(String.format("%.2f", allPrice), getActivity(), user);//payment for report subscription
                            else
                                launchSignin(mainActivityContext);
                        }
                        subscribeViewDialog.onDismiss(subscribeViewDialog.getDialog());
                    });
                } else
                    mainActivityContext.replaceFragment(new PDFViewDialog().newInstance(mainActivityContext, news.getRelated_car()), MainDetailPageFragment.class.getSimpleName(), true, false);

                break;
        }
    }


    private void checkReportPayment() {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", user.getId());
        params.put("report_id", news.getId());
        WebApiRequest.Instance(registrationActivityContext).request(
                AppConstants.WebServicesKeys.CHECK_REPORT_PAYMENT, null,
                null,
                params,
                new WebApiRequest.WebServiceObjectResponse() {
                    @Override
                    public void onSuccess(ApiResponse apiResponse) {
                        if (apiResponse.isSuccess()) {
                            binding.btnSubscribe.setVisibility(View.VISIBLE);
                            if (apiResponse.getData().toString().contains("user_id"))
                            {
                                binding.btnSubscribe.setText("View Report");
                            }
                            else
                                binding.btnSubscribe.setText("Subscribe to View");
                        }
                    }

                    @Override
                    public void onError() {
                    }
                }, null);
    }

    private void resumeReportPayment() {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", user.getId());
        params.put("report_id", news.getId());
        WebApiRequest.Instance(registrationActivityContext).request(
                AppConstants.WebServicesKeys.CHECK_REPORT_PAYMENT, null,
                null,
                params,
                new WebApiRequest.WebServiceObjectResponse() {
                    @Override
                    public void onSuccess(ApiResponse apiResponse) {
                        if (apiResponse.isSuccess()) {
                            binding.btnSubscribe.setVisibility(View.VISIBLE);
                            if (apiResponse.getData().toString().contains("user_id"))
                            {
                                binding.btnSubscribe.setText("View Report");
                            }
                            else
                            {
                                binding.btnSubscribe.setText("Subscribe to View");
                                if (mainActivityContext.oneReport)
                                    TelrUtils.IntentTelr(String.format("%.2f", singlePrice), getActivity(), user);
                                else
                                    TelrUtils.IntentTelr(String.format("%.2f", allPrice), getActivity(), user);
                            }
                        }
                    }

                    @Override
                    public void onError() {
                    }
                }, null);
    }


    private void setFeaturedNews(ArrayList<Media> media) {
        featuredArticleArrayList = new ArrayList<News>();
        for (int i = 0; i < media.size(); i++) {
            News news = new News();
            ArrayList<Media> mediaArrayList = new ArrayList<>();
            mediaArrayList.add(media.get(i));
            news.setMedia(mediaArrayList);
            featuredArticleArrayList.add(news);
        }
        featureAdapter.addAll(featuredArticleArrayList);
        featureAdapter.notifyDataSetChanged();
        binding.circlePageIndicator.setViewPager(binding.viewpager);
    }

    private void openCommentsWindow() {
        CommentsFragment commentsFragment = CommentsFragment.Instance(mainActivityContext, binding);
        commentsFragment.setNewsId(categoryId);
//        commentsFragment.setCancelable(false);
        commentsFragment.show(mainActivityContext.getSupportFragmentManager(), commentsFragment.getTag());
    }

    private void addFav() {
        popup.dismiss();
        setNewsInteraction(AppConstants.NewsInteractions.FAVORITE);
        //  UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.implemented_later), Toast.LENGTH_LONG);
    }

    private void setData(News news) {
        if (news.getMedia().size() > 0) {
            switch (news.getMedia().get(0).getMediaType()) {
                case AppConstants.MediaType.IMAGE:
//                    UIHelper.setImageWithGlide(mainActivityContext, mainActivityContext.getIvSubCategoryItem(), news.getMedia().get(0).getFileUrl());
                    setFeaturedNews(news.getMedia());
                    break;
            }
        }

        Titlebar titlebar = mainActivityContext.getTitlebar();
        titlebar.showLikeButton().setLiked(news.isLiked());
        binding.ibLikeWhite.setLiked(news.isLiked());
        setTitlebar(titlebar);

        binding.tvArticleTitle.setText((news.getHeadline() == null) ? "" : news.getHeadline());
        binding.tvArticle.setText((news.getDescription() == null) ? "" : news.getDescription());
        binding.tvDate.setText((news.getCreatedAt() == null) ? "" : DateFormatHelper.changeServerToOurFormatDate(news.getCreatedAt()));
        binding.tvViews.setText((news.getViewsCount() == 0 || news.getViewsCount() == 1) ? news.getViewsCount() + " " + mainActivityContext.getResources().getString(R.string.view) : news.getViewsCount() + " " + mainActivityContext.getResources().getString(R.string.views));
        binding.tvLikes.setText((news.getLikeCount() == 0 || news.getLikeCount() == 1) ? news.getLikeCount() + " " + mainActivityContext.getResources().getString(R.string.like) : news.getLikeCount() + " " + mainActivityContext.getResources().getString(R.string.likes));
        binding.tvComments.setText((news.getCommentsCount() == 0 || news.getCommentsCount() == 1) ? news.getCommentsCount() + " " + mainActivityContext.getResources().getString(R.string.comment) : news.getCommentsCount() + " " + mainActivityContext.getResources().getString(R.string.comments));
        mainActivityContext.getBinding().llCommentsLayout.setVisibility(View.VISIBLE);
        mainActivityContext.getBinding().tvWriteComment.setVisibility(View.VISIBLE);

//        binding.webviewArticle.getSettings().setLoadWithOverviewMode(true);
        binding.webviewArticle.getSettings().setUseWideViewPort(false);
        binding.webviewArticle.loadData((news.getDescription() == null) ? "" : "<div style='width: 100%;'>" + news.getDescription() + "</div>", "text/html", null);

        viewsCount = news.getViewsCount();
        commentsCount = news.getCommentsCount();
        likesCount = news.getLikeCount();
        isLiked = news.isLiked();
        singlePrice = news.getSubscriptionPrice();
        allPrice = news.getAllReportSubcriptionAmount();

        if (news.getSource() != null) {
            binding.tvNewsArlicleLink.setText((news.getSource() == null) ? "" : news.getSource());
//            binding.tvNewsArlicleLink.setText((news.getSource() == null) ? "" : Html.fromHtml(news.getSource()));
//            binding.tvNewsArlicleLink.setMovementMethod(LinkMovementMethod.getInstance());

            if (news.getSourceImage() != null) {
                UIHelper.setImageWithGlide(mainActivityContext, binding.ivNewsChannelLogo, news.getSourceImage());
            }
            /*
            boolean isValidUrl = URLUtil.isValidUrl(news.getSource());
            if (isValidUrl) {
                int indexOfCom = news.getSource().indexOf(".com", 0);
                UIHelper.setImageWithGlide(mainActivityContext, binding.ivNewsChannelLogo, news.getSource().substring(0, indexOfCom) + ".com/favicon.ico");
            }
            */
        } else {
            binding.llArticleLink.setVisibility(View.GONE);
        }

//            initPlayer(mainActivityContext.getBinding().youtubePlayer, news.getMedia().get(0).getFileUrl().split("=")[1]);
        if (news.getMedia() != null && news.getMedia().size() > 0) {
            if (mainActivityContext.internetConnected()) {
                if (news.getMedia().get(0).getMediaType() == AppConstants.MediaType.VIDEO) {
                    String videoID = Utils.getYouTubeId(news.getMedia().get(0).getFileUrl());
                    if (!videoID.equals("error")) {
                        initPlayer(binding.youtubePlayer, videoID);
                    } else {
                        UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.youtube_video_not_found), Toast.LENGTH_SHORT);
                    }
                }
            } else {
                UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.video_internet_error), Toast.LENGTH_LONG);
            }
        }

        if (news.getRelated_car() != null) {
            if (news.getRelated_car().contains("id=")) {
                String carId = news.getRelated_car().split("id=")[1];
                if (carId != null) {
                    getRelatedCars(Integer.parseInt(carId));
                }
            }
        } else {
            binding.llRelatedCar.setVisibility(View.GONE);
        }

//        if (preferenceHelper.getLoginStatus()) {
//            if (!news.isIs_viewed()) {
//            }
//        }
        setNewsInteraction(AppConstants.NewsInteractions.VIEW);
    }

    private void showFavPopup(View parentView) {
        popup = new PopupWindow(mainActivityContext);
        View layout = getLayoutInflater().inflate(R.layout.layout_add_to_fav_popup, null);
        final TextView addFav = (TextView) layout.findViewById(R.id.tvAddFav);
        if (news != null) {
            if (news.isFavorite())
                addFav.setText(mainActivityContext.getResources().getString(R.string.remove_fav));
            else
                addFav.setText(mainActivityContext.getResources().getString(R.string.add_to_fav));
        }
        popup.setContentView(layout);
        // Set content width and height
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        // Closes the popup window when touch outside of it - when looses focus
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        // Show anchored to button
        popup.setBackgroundDrawable(/*mainActivityContext.getResources().getDrawable(R.drawable.shape)*/new BitmapDrawable());
        popup.showAsDropDown(parentView);
        UIHelper.applyDim(mainActivityContext);

        addFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFav();
            }
        });
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                UIHelper.clearDim(mainActivityContext);
            }
        });
    }

    private void setListeners() {
        binding.llCommentsLayout.setOnClickListener(this);
        binding.tvViews.setOnClickListener(this);
        binding.tvLikes.setOnClickListener(this);
        binding.tvComments.setOnClickListener(this);
        binding.tvWriteComment.setOnClickListener(this);
        mainActivityContext.getBinding().llCommentsLayout.setOnClickListener(this);
        binding.tvNewsArlicleLink.setOnClickListener(this);
    }

    private void getDetail() {
        Map<String, Object> params = new HashMap<>();
        params.put("id", categoryId);
        WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.NEWS_DETAIL, binding.getRoot(), null, params, new WebApiRequest.WebServiceObjectResponse() {
            @Override
            public void onSuccess(ApiResponse apiResponse) {
                news = (News) JsonHelpers.convertToModelClass(apiResponse.getData(), News.class);
                setData(news);
                mainActivityContext.hideLoader();
                if (preferenceHelper.getLoginStatus() && news.getRelated_car() != null)
                    checkReportPayment();
                else if (news.getRelated_car() != null)
                    binding.btnSubscribe.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError() {

            }
        }, null);
    }

    private void setNewsInteraction(final int type) {
        Map<String, Object> params = new HashMap<>();
        params.put("news_id", categoryId);
        params.put("type", type);

        if (mainActivityContext.showLoader()) {
            WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.NEWS_INTERACTIONS, null, null, params, new WebApiRequest.WebServiceObjectResponse() {
                @Override
                public void onSuccess(ApiResponse apiResponse) {
                    if (type == AppConstants.NewsInteractions.FAVORITE) {
                        if (!news.isFavorite()) {
                            UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.success_add_fav), Toast.LENGTH_SHORT);
                            news.setFavorite(true);
                        } else {
                            UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.err_favorite), Toast.LENGTH_SHORT);
                            news.setFavorite(false);
                        }
                    }
                    mainActivityContext.hideLoader();
                }

                @Override
                public void onError() {
                    mainActivityContext.hideLoader();
                }
            }, null);
        }
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;


    }

    public void shareDetail(final News news) {
        TedPermission.with(mainActivityContext).setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE).setPermissionListener(new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                if (news != null) {
                    if (news.getHeadline() != null) {
                        if (news.getMedia() != null) {
                            if (news.getMedia().size() > 0) {
                                if (mainActivityContext.internetConnected()) {
                                    if (news.getMedia().get(0).getMediaType() == AppConstants.MediaType.IMAGE) {
                                        if (news.getDeepLink() != null) {
                                            new ShareDetail(mainActivityContext, news.getHeadline(), news.getDeepLink()).execute(news.getMedia().get(0).getFileUrl());
                                        } else {
                                            new ShareDetail(mainActivityContext, news.getHeadline(), AppConstants.WEB_URL).execute(news.getMedia().get(0).getFileUrl());
                                        }
                                    } else {
                                        Utils.shareWithVideoLink(mainActivityContext, news.getHeadline(), news.getDeepLink());
                                    }
                                }
                            } else {
                                UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.cannot_share_image), Toast.LENGTH_SHORT);
                            }
                        } else {
                            UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.cannot_share_image), Toast.LENGTH_SHORT);
                        }
                    }
                }
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.permission_denied), Toast.LENGTH_SHORT);

            }
        }).check();

    }

    private void openSourceLink() {
        Intent intent = new Intent(mainActivityContext, SourceLinkWebViewActivity.class);
        if (news.getSource() != null) {
            intent.putExtra("link", news.getSource());
            mainActivityContext.startActivity(intent);
        } else {
            UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.no_link_found), Toast.LENGTH_SHORT);
        }
//        mainActivityContext.startActivity(SourceLinkWebViewActivity.class, false);
    }

    private void initPlayer(YouTubePlayerView youTubePlayerView, String videoID) {
        youTubePlayerView.initialize(new YouTubePlayerInitListener() {
            @Override
            public void onInitSuccess(final YouTubePlayer initializedYouTubePlayer) {
                windowYouTubePlayer = initializedYouTubePlayer;
                initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady() {
                        mainActivityContext.initYoutube = initializedYouTubePlayer;
                        initializedYouTubePlayer.loadVideo(videoID, 0);
                    }
                });
            }
        }, true);

    }
}
