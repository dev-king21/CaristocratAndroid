package com.ingic.caristocrat.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.ingic.caristocrat.R;
import com.ingic.caristocrat.adapters.DetailedReviewAdapter;
import com.ingic.caristocrat.adapters.RatingAttributeAdapter;
import com.ingic.caristocrat.adapters.SubcategoriesFeatureAdapter;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.FragmentReviewDetailsBinding;
import com.ingic.caristocrat.helpers.JsonHelpers;
import com.ingic.caristocrat.helpers.Titlebar;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.helpers.Utils;
import com.ingic.caristocrat.interfaces.ReviewSubmitListener;
import com.ingic.caristocrat.models.RatingAttribute;
import com.ingic.caristocrat.models.RatingPost;
import com.ingic.caristocrat.models.ReviewDetail;
import com.ingic.caristocrat.models.TradeCar;
import com.ingic.caristocrat.models.UserRating;
import com.ingic.caristocrat.webhelpers.models.ApiArrayResponse;
import com.ingic.caristocrat.webhelpers.models.ApiResponse;
import com.ingic.caristocrat.webhelpers.models.Media;
import com.ingic.caristocrat.webhelpers.models.News;
import com.ingic.caristocrat.webhelpers.services.WebApiRequest;
import com.like.LikeButton;
import com.like.OnLikeListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ReviewDetailsFragment extends BaseFragment implements View.OnClickListener, ReviewSubmitListener {
    FragmentReviewDetailsBinding binding;
    TradeCar tradeCar;
    SubcategoriesFeatureAdapter featureAdapter;
    ArrayList<News> featuredArticleArrayList;
    DetailedReviewAdapter detailedReviewAdapter;
    ArrayList<UserRating> userRatings;
    ArrayList<RatingAttribute> ratingAttributes;
    ArrayList<ReviewDetail> reviewDetails = new ArrayList<>();
    private RatingAttributeAdapter ratingAttributeAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_review_details, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userRatings = new ArrayList<>();

        featureAdapter = new SubcategoriesFeatureAdapter(mainActivityContext, null, false);
        binding.viewpager.setAdapter(featureAdapter);

        detailedReviewAdapter = new DetailedReviewAdapter(mainActivityContext);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mainActivityContext);
        binding.recyclerview.setLayoutManager(linearLayoutManager);
        binding.recyclerview.setNestedScrollingEnabled(false);
        binding.recyclerview.setAdapter(detailedReviewAdapter);

        ratingAttributeAdapter = new RatingAttributeAdapter(mainActivityContext);

        mainActivityContext.getBinding().nestedscroll.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (binding.etAbout.hasFocus()) {
                    binding.etAbout.clearFocus();
                }
                return false;
            }
        });

        setValues(tradeCar);

        setListeners();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (tradeCar != null) {
            getDetail(tradeCar.getId());
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
//        titlebar.resetTitlebar(mainActivityContext);
//        titlebar.showBackButton(mainActivityContext, true);
//        titlebar.showTransparentTitlebar(mainActivityContext);
        mainActivityContext.getCollapsingToolBarLayout().setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibBackbtn:
                mainActivityContext.onBackPressed();
                break;

            case R.id.ibShare:
                share();
                break;

            case R.id.llRate:
//                rate();
                break;

            case R.id.tvReadAllReviews:
//                readAllReviews();
                break;

            case R.id.btnSubmitReview:
                submitReview();
                break;

            case R.id.tvConsumerReviews:
                showHideConsumerReviews();
                break;
        }
    }

    @Override
    public void onReviewSubmitted() {
        if (tradeCar != null) {
            getDetail(tradeCar.getId());
        }
    }

    private void setListeners() {
        binding.ibBackbtn.setOnClickListener(this);
        binding.ibShare.setOnClickListener(this);
        binding.btnSubmitReview.setOnClickListener(this);
        binding.tvConsumerReviews.setOnClickListener(this);
//        binding.llRate.setOnClickListener(this);
//        binding.tvReadAllReviews.setOnClickListener(this);
        binding.ibLikeWhite.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                setCarInteraction(AppConstants.CarInteractions.LIKE);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                setCarInteraction(AppConstants.CarInteractions.LIKE);
            }
        });
    }

    private void setValues(TradeCar result) {
        binding.rlSlider.requestFocus();
        if (result != null) {
            binding.ibLikeWhite.setLiked(result.isIs_liked());

            if (result.getMedia() != null && result.getMedia().size() > 0) {
                setFeaturedNews(result.getMedia());
            }

            binding.tvOutMallTitle.setText(Utils.getCarName(result, false));
            if (result.getYear() != null) {
                binding.tvModelYear.setText(result.getYear() + "");
            }

            binding.ratingbar.setRating(result.getAverage_rating());
            binding.tvRating.setText(result.getAverage_rating() + "");

            if (result.getIs_reviewed() == 1) {
                binding.tvAlreadyReviewed.setVisibility(View.VISIBLE);
                binding.btnSubmitReview.setVisibility(View.GONE);
            }
        }
        getReviewAspects();
    }

    private void showHideConsumerReviews() {
        if (binding.llReviewDetails.getVisibility() == View.VISIBLE) {
            UIHelper.animation(Techniques.FadeOut, 400, 0, binding.llReviewDetails);
            binding.tvConsumerReviews.setCompoundDrawablesWithIntrinsicBounds(null, null, mainActivityContext.getResources().getDrawable(R.drawable.downarrow1), null);
            binding.llReviewDetails.setVisibility(View.GONE);
        } else {
            UIHelper.animation(Techniques.FadeIn, 400, 0, binding.llReviewDetails);
            binding.tvConsumerReviews.setCompoundDrawablesWithIntrinsicBounds(null, null, mainActivityContext.getResources().getDrawable(R.drawable.uparrow), null);
            binding.llReviewDetails.setVisibility(View.VISIBLE);
            binding.llReviewDetails.post(new Runnable() {
                @Override
                public void run() {
                    binding.nestedscroll.fullScroll(View.FOCUS_DOWN);
                }
            });
        }
    }

    private void rate() {
        if (tradeCar.getIs_reviewed() == 0) {
            if (ratingAttributes != null && ratingAttributes.size() > 0) {
                disableViewsForSomeSeconds(binding.llRate);
                RatingDialog ratingDialog = new RatingDialog(mainActivityContext, tradeCar, mainActivityContext.getTitlebar(), this);
                ratingDialog.setRatingAttributes(ratingAttributes);
//        ratingDialog.show(mainActivityContext.getSupportFragmentManager(), RatingDialog.class.getSimpleName());
                mainActivityContext
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(mainActivityContext.getMainFrameLayoutID(), ratingDialog, RatingDialog.class.getSimpleName())
                        .addToBackStack(mainActivityContext.getSupportFragmentManager().getBackStackEntryCount() == 0 ? "firstFrag" : null)
                        .commit();
            }
        } else {
            UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.review_already), Toast.LENGTH_SHORT);
        }
    }

    private void submitReview() {
/*
        if (binding.etAbout.getText().toString().length() == 0) {
            UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.write_review_details), Toast.LENGTH_SHORT);
            return;
        }
*/
        if (!preferenceHelper.getLoginStatus()) {
            launchSigninRequirement(mainActivityContext, mainActivityContext.getResources().getString(R.string.require_signin_review_post));
            return;
        }

//      This variable keeps track if every attribute rated atleast 1 star.
        boolean everyAttributeRated = false;

        RatingPost ratingPost = new RatingPost();
        ratingPost.setCar_id(tradeCar.getId());
        ratingPost.setReview_message(binding.etAbout.getText().toString().trim());
        JSONArray jsonArray = new JSONArray();
        for (int pos = 0; pos < ratingAttributeAdapter.getAll().size(); pos++) {

            if (ratingAttributeAdapter.getAll().get(pos).getRating() >= 1) {
                everyAttributeRated = true;
            } else {
                everyAttributeRated = false;
            }
            if (!everyAttributeRated) {
                UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.select_atleast_1_star), Toast.LENGTH_LONG);
                return;
            }

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(ratingAttributes.get(pos).getId() + "", ratingAttributeAdapter.getAll().get(pos).getRating() + "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
        }
        ratingPost.setRating(jsonArray.toString());

        if (mainActivityContext.showLoader()) {
            WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.POST_REVIEWS, null, ratingPost, null, new WebApiRequest.WebServiceObjectResponse() {
                @Override
                public void onSuccess(ApiResponse apiResponse) {
                    binding.etAbout.setText("");
                    binding.etAbout.clearFocus();
                    UIHelper.showToast(mainActivityContext, apiResponse.getMessage(), Toast.LENGTH_SHORT);
                    /*
                    if(reviewSubmitListener != null){
                        reviewSubmitListener.onReviewSubmitted();
                    }
                    */
                    if (tradeCar != null) {
                        getDetail(tradeCar.getId());
                    }
//                    mainActivityContext.onBackPressed();
                    mainActivityContext.hideLoader();
                }

                @Override
                public void onError() {
                    binding.etAbout.setText("");
                    binding.etAbout.clearFocus();
                    mainActivityContext.hideLoader();
                }
            }, null);
        }
    }

    private void readAllReviews() {
        disableViewsForSomeSeconds(binding.tvReadAllReviews);
        if (userRatings != null && userRatings.size() > 0) {
            detailedReviewAdapter.addAll(userRatings);
            detailedReviewAdapter.notifyDataSetChanged();
            binding.tvReadAllReviews.setVisibility(View.GONE);
        }
        /*
        AllReviewsFragment allReviewsFragment = new AllReviewsFragment();
        allReviewsFragment.setArrayList(userRatings);
        mainActivityContext.replaceFragment(allReviewsFragment, AllReviewsFragment.class.getSimpleName(), true, true);
        */
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

    private void getReviewAspects() {
        if (mainActivityContext.internetConnected()) {
            WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.GET_REVIEW_ASPECTS, null, null, null, null, new WebApiRequest.WebServiceArrayResponse() {
                @Override
                public void onSuccess(ApiArrayResponse apiArrayResponse) {
                    ratingAttributes = JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), RatingAttribute.class);

                    ratingAttributeAdapter.setRatingPost(true);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mainActivityContext);
                    binding.recyclerviewRate.setLayoutManager(linearLayoutManager);

                    int spacingInPixels = mainActivityContext.getResources().getDimensionPixelSize(R.dimen.dp8);
                    UIHelper.SpacesItemDecorationTopBottomEqual itemDecoration = new UIHelper.SpacesItemDecorationTopBottomEqual(spacingInPixels);

                    binding.recyclerviewRate.addItemDecoration(itemDecoration);
                    binding.recyclerviewRate.setNestedScrollingEnabled(false);
                    binding.recyclerviewRate.setAdapter(ratingAttributeAdapter);

                    ratingAttributeAdapter.addAll(ratingAttributes);
                    ratingAttributeAdapter.notifyDataSetChanged();

                    getReviewDetails();
                }

                @Override
                public void onError() {
                }
            });
        }
    }

    private void getReviewDetails() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("car_id", tradeCar.getId());
        if (mainActivityContext.showLoader()) {
            WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.GET_REVIEWS, null, null, params, null, new WebApiRequest.WebServiceArrayResponse() {
                @Override
                public void onSuccess(ApiArrayResponse apiArrayResponse) {
                    userRatings = new ArrayList<>();
                    reviewDetails = JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), ReviewDetail.class);
                    if (reviewDetails.size() > 0) {
//                        Collections.reverse(reviewDetails);
                        for (int pos = 0; pos < reviewDetails.size(); pos++) {
                            UserRating userRating = new UserRating();
                            userRating.setId(reviewDetails.get(pos).getId());
                            userRating.setUser(reviewDetails.get(pos).getUserDetails());
                            userRating.setOverAllRating(reviewDetails.get(pos).getAverageRating());
                            userRating.setReviewDetails(reviewDetails.get(pos).getReviewMessage());
                            ArrayList<RatingAttribute> rAttributes = new ArrayList<>();
                            for (int posi = 0; posi < reviewDetails.get(pos).getDetails().size(); posi++) {
                                RatingAttribute ratingAttribute = new RatingAttribute();
                                ratingAttribute.setId(reviewDetails.get(pos).getDetails().get(posi).getId());
                                ratingAttribute.setRating(reviewDetails.get(pos).getDetails().get(posi).getRating());
                                ratingAttribute.setTitle(reviewDetails.get(pos).getDetails().get(posi).getAspectTitle());
                                rAttributes.add(ratingAttribute);
                            }
                            userRating.setRatingAttributes(rAttributes);
                            userRatings.add(userRating);
                        }
                        if (userRatings != null) {
                            if (userRatings.size() > 0) {
//                                if (userRatings.size() <= 2) {
//                                    detailedReviewAdapter.addAll(userRatings);
//                                    binding.tvReadAllReviews.setVisibility(View.GONE);
//                                } else {
//                                    ArrayList<UserRating> subArray = new ArrayList<>();
//                                    for (int pos = 0; pos < 2; pos++) {
//                                        subArray.add(userRatings.get(pos));
//                                    }
//                                }
                                detailedReviewAdapter.addAll(userRatings);
                                binding.tvReadAllReviews.setVisibility(View.GONE);
                                binding.tvReadAllReviews.setOnClickListener(ReviewDetailsFragment.this);
                                if (tradeCar.getIs_reviewed() == 1) {
                                    binding.tvRate.setText(mainActivityContext.getResources().getString(R.string.your_ratings));
                                    if (preferenceHelper.getLoginStatus() && preferenceHelper.getUser() != null) {
                                        UserRating myRating = null;
                                        for (int pos = 0; pos < userRatings.size(); pos++) {
                                            if (preferenceHelper.getUser().getId() == userRatings.get(pos).getUser().getUser_id()) {
                                                myRating = userRatings.get(pos);
                                                break;
                                            }
                                        }
                                        ratingAttributeAdapter.setRated(true);
                                        ratingAttributeAdapter.setUserRating(myRating);
                                        ratingAttributeAdapter.notifyDataSetChanged();
                                        binding.etAbout.setText(myRating.getReviewDetails() != null ? myRating.getReviewDetails() : mainActivityContext.getResources().getString(R.string.no_review_comment_added));
                                        binding.etAbout.clearFocus();
                                        binding.etAbout.setEnabled(false);
                                    }
                                }
                            } else {
                                binding.llReviewDetails.setVisibility(View.GONE);
                                binding.tvReadAllReviews.setText(mainActivityContext.getResources().getString(R.string.no_review_to_show));
                                binding.tvReadAllReviews.setOnClickListener(null);
                            }
                        } else {
                            binding.llReviewDetails.setVisibility(View.GONE);
                        }
                        detailedReviewAdapter.notifyDataSetChanged();
                    } else {
                        binding.tvReadAllReviews.setText(mainActivityContext.getResources().getString(R.string.no_review_to_show));
                        binding.tvReadAllReviews.setOnClickListener(null);
                    }
                    mainActivityContext.hideLoader();
                }

                @Override
                public void onError() {
                    mainActivityContext.hideLoader();
                }
            });
        }
    }

    private void getDetail(int id) {
        if (mainActivityContext.showLoader()) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("id", id);
            WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.LUXURY_MARKET_CATEGORY_DETAIL, null, null, params, new WebApiRequest.WebServiceObjectResponse() {
                @Override
                public void onSuccess(ApiResponse apiResponse) {
                    tradeCar = (TradeCar) JsonHelpers.convertToModelClass(apiResponse.getData(), TradeCar.class);
                    setValues(tradeCar);
                    mainActivityContext.hideLoader();
                }

                @Override
                public void onError() {
                    mainActivityContext.hideLoader();
                }
            }, null);
        }
    }

    private void share() {
        if (tradeCar != null) {
            TradeCar currentTradeCar = tradeCar;
            if (currentTradeCar.getName() != null) {
                if (currentTradeCar.getMedia() != null) {
                    if (currentTradeCar.getMedia().size() > 0) {
                        if (mainActivityContext.internetConnected()) {
                            if (currentTradeCar.getLink() != null) {
                                Utils.share(mainActivityContext, currentTradeCar.getName(), currentTradeCar.getLink(), currentTradeCar.getMedia().get(0).getFileUrl());
                            } else {
                                Utils.share(mainActivityContext, currentTradeCar.getName(), AppConstants.WEB_URL, currentTradeCar.getMedia().get(0).getFileUrl());
                            }
                        }
                    } else {
                        UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.cannot_share_image), Toast.LENGTH_SHORT);
                    }
                } else {
                    UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.cannot_share_image), Toast.LENGTH_SHORT);
                }
            } else {
                UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.cannot_share_name), Toast.LENGTH_SHORT);
            }
        }
    }

    private void setCarInteraction(final int type) {
        Map<String, Object> params = new HashMap<>();
        params.put("car_id", tradeCar.getId());
        params.put("type", type);


        if (!Utils.isNetworkAvailable(mainActivityContext)) {
            UIHelper.showSnackbar(mainActivityContext.getMainFrameLayout(), getResources().getString(R.string.no_internet_connection), Snackbar.LENGTH_SHORT);
        } else {
            WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.CAR_INTERACTIONS, null, null, params, new WebApiRequest.WebServiceObjectResponse() {
                @Override
                public void onSuccess(ApiResponse apiResponse) {
                    mainActivityContext.hideLoader();
                }

                @Override
                public void onError() {
                    mainActivityContext.hideLoader();
                }
            }, null);
        }
    }

    public void setTradeCar(TradeCar tradeCar) {
        this.tradeCar = tradeCar;
    }
}
