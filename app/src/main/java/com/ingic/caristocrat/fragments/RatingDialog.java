package com.ingic.caristocrat.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.adapters.RatingAttributeAdapter;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.RatingDialogBinding;
import com.ingic.caristocrat.helpers.Titlebar;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.interfaces.ReviewSubmitListener;
import com.ingic.caristocrat.models.RatingAttribute;
import com.ingic.caristocrat.models.RatingPost;
import com.ingic.caristocrat.models.TradeCar;
import com.ingic.caristocrat.webhelpers.models.ApiResponse;
import com.ingic.caristocrat.webhelpers.services.WebApiRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class RatingDialog extends DialogFragment implements View.OnClickListener {
    private RatingDialogBinding binding;
    private MainActivity mainActivityContext;
    private RatingAttributeAdapter ratingAttributeAdapter;
    private Titlebar titlebar;
    ArrayList<RatingAttribute> ratingAttributes = new ArrayList<>();
    TradeCar tradeCar;
    ReviewSubmitListener reviewSubmitListener;

    public RatingDialog(MainActivity mainActivityContext, TradeCar tradeCar, Titlebar titlebar, ReviewSubmitListener reviewSubmitListener) {
        this.mainActivityContext = mainActivityContext;
        this.tradeCar = tradeCar;
        this.titlebar = titlebar;
        this.reviewSubmitListener = reviewSubmitListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.rating_dialog, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setTitlebar(titlebar);
/*
        //Dummy Rating Details
        ArrayList<RatingAttribute> ratingAttributes = new ArrayList<>();
        RatingAttribute ratingAttribute = new RatingAttribute();
        ratingAttribute.setId(1);
        ratingAttribute.setTitle("Design & Comfort");
        ratingAttributes.add(ratingAttribute);

        ratingAttribute = new RatingAttribute();
        ratingAttribute.setId(2);
        ratingAttribute.setTitle("Technology");
        ratingAttributes.add(ratingAttribute);

        ratingAttribute = new RatingAttribute();
        ratingAttribute.setId(3);
        ratingAttribute.setTitle("Performance");
        ratingAttributes.add(ratingAttribute);

        ratingAttribute = new RatingAttribute();
        ratingAttribute.setId(4);
        ratingAttribute.setTitle("Image & Luxury");
        ratingAttributes.add(ratingAttribute);

        ratingAttribute = new RatingAttribute();
        ratingAttribute.setId(5);
        ratingAttribute.setTitle("Quality & Reliability");
        ratingAttributes.add(ratingAttribute);

        ratingAttribute = new RatingAttribute();
        ratingAttribute.setId(6);
        ratingAttribute.setTitle("Price Value");
        ratingAttributes.add(ratingAttribute);
*/
        ratingAttributeAdapter = new RatingAttributeAdapter(mainActivityContext);
        ratingAttributeAdapter.setRatingPost(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mainActivityContext);
        binding.recyclerview.setLayoutManager(linearLayoutManager);

        int spacingInPixels = mainActivityContext.getResources().getDimensionPixelSize(R.dimen.dp8);
        UIHelper.SpacesItemDecorationTopBottomEqual itemDecoration = new UIHelper.SpacesItemDecorationTopBottomEqual(spacingInPixels);

        binding.recyclerview.addItemDecoration(itemDecoration);
        binding.recyclerview.setNestedScrollingEnabled(false);
        binding.recyclerview.setAdapter(ratingAttributeAdapter);

        ratingAttributeAdapter.addAll(ratingAttributes);
        ratingAttributeAdapter.notifyDataSetChanged();

        setListeners();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivityContext);
        builder.setView(binding.getRoot());
        builder.setPositiveButton(mainActivityContext.getResources().getString(R.string.submit_review), null);
        builder.setNegativeButton(mainActivityContext.getResources().getString(R.string.cancel), null);

        return builder.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancel:
                cancel();
                break;

            case R.id.btnSubmitReview:
                submitReview();
                break;
        }
    }

    private void setListeners() {
        binding.btnCancel.setOnClickListener(this);
        binding.btnSubmitReview.setOnClickListener(this);
    }

    private void cancel() {
        mainActivityContext.onBackPressed();
    }

    private void submitReview() {
        if (binding.etAbout.getText().toString().length() == 0) {
            UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.write_review_details), Toast.LENGTH_SHORT);
            return;
        }

        RatingPost ratingPost = new RatingPost();
        ratingPost.setCar_id(tradeCar.getId());
        ratingPost.setReview_message(binding.etAbout.getText().toString().trim());
        JSONArray jsonArray = new JSONArray();
        for (int pos = 0; pos < ratingAttributeAdapter.getAll().size(); pos++) {
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
                    UIHelper.showToast(mainActivityContext, apiResponse.getMessage(), Toast.LENGTH_SHORT);
                    if(reviewSubmitListener != null){
                        reviewSubmitListener.onReviewSubmitted();
                    }
                    mainActivityContext.onBackPressed();
                    mainActivityContext.hideLoader();
                }

                @Override
                public void onError() {
                    mainActivityContext.hideLoader();
                }
            }, null);
        }
    }

    private void setTitlebar(Titlebar titlebar) {
        titlebar.resetTitlebar(mainActivityContext);
        titlebar.setTitle(mainActivityContext.getResources().getString(R.string.submit_review));
        titlebar.showBackButton(mainActivityContext, false);
    }

    public void setRatingAttributes(ArrayList<RatingAttribute> ratingAttributes) {
        this.ratingAttributes.addAll(ratingAttributes);
    }
}
