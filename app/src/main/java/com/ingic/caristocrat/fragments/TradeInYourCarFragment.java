package com.ingic.caristocrat.fragments;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.adapters.TradeInMediaAdapter;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.FragmentTradeInYourCarBinding;
import com.ingic.caristocrat.dialogs.CountryPickerDialog;
import com.ingic.caristocrat.dialogs.TradeInRequestApprovalDialog;
import com.ingic.caristocrat.helpers.CustomValidation;
import com.ingic.caristocrat.helpers.DialogFactory;
import com.ingic.caristocrat.helpers.JsonHelpers;
import com.ingic.caristocrat.helpers.RecyclerTouchListener;
import com.ingic.caristocrat.helpers.Titlebar;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.interfaces.ClickListenerRecycler;
import com.ingic.caristocrat.interfaces.MediaTypePicker;
import com.ingic.caristocrat.interfaces.OnCarSelectedForTradeListener;
import com.ingic.caristocrat.interfaces.SimpleDialogActionListener;
import com.ingic.caristocrat.models.AttributesWrapper;
import com.ingic.caristocrat.models.EngineType;
import com.ingic.caristocrat.models.Make;
import com.ingic.caristocrat.models.Model;
import com.ingic.caristocrat.models.MyImageFile;
import com.ingic.caristocrat.models.OptionArray;
import com.ingic.caristocrat.models.Page;
import com.ingic.caristocrat.models.RegionalSpecs;
import com.ingic.caristocrat.models.TradeCar;
import com.ingic.caristocrat.models.Version;
import com.ingic.caristocrat.webhelpers.models.ApiArrayResponse;
import com.ingic.caristocrat.webhelpers.models.ApiResponse;
import com.ingic.caristocrat.webhelpers.models.Media;
import com.ingic.caristocrat.webhelpers.models.TradeInCar;
import com.ingic.caristocrat.webhelpers.models.User;
import com.ingic.caristocrat.webhelpers.services.WebApiRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 */
public class TradeInYourCarFragment extends BaseFragment implements View.OnClickListener, MediaTypePicker {
    FragmentTradeInYourCarBinding binding;
    TradeInMediaAdapter tradeInMediaAdapter;
    ArrayList<Media> media;
    ArrayList<Make> make;
    ArrayList<Model> models;
    ArrayList<RegionalSpecs> regionalSpecs;
    AttributesWrapper attributesWrapper;
    int exteriorColorKey, interiorColorKey, accidentKey, trimKey, model_id, regional_spec_id, make_id, engineTypeId;
    OptionArray exteriorColor, interiorColor, accident;
    ArrayList<OptionArray> exteriorOptions, interiorOptions, accidentOptions;
    ArrayList<Integer> carFeatures;
    ArrayList<EngineType> engineTypes;
    ArrayList<Version> versions;

    User currentUser;

    boolean trading, profile, edit, loaded, evaluate;
    OnCarSelectedForTradeListener onCarSelectedForTradeListener;
    Titlebar titlebar;
    TradeCar tradeCar;

    Media frontImageMedia, backImageMedia, rightImageMedia, leftImageMedia, interiorImageMedia, registrationImageMedia;
    ArrayList<Media> deletedItems;
    Version selectedVersion;
    String selectedVersionApp, title;

    public TradeInYourCarFragment() {
    }

    public static TradeInYourCarFragment Instance() {
        TradeInYourCarFragment fragment = new TradeInYourCarFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_trade_in_your_car, container, false);
        deletedItems = new ArrayList<>();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListeners();
        initMediaAdapter();
        setProfile();
        carFeatures = new ArrayList<>();
        if (mainActivityContext.showLoader()) {
            make = new ArrayList<>();
            getMake();
            regionalSpecs = new ArrayList<>();
            getRegionalSpecs();
            getAttributes();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setProfile();
    }

    @Override
    public void onDestroyView() {
        if (trading) {
            titlebar.resetTitlebar(mainActivityContext);
            titlebar.showTitlebar(mainActivityContext);
            titlebar.setTitle(mainActivityContext.getResources().getString(R.string.select_a_car));
            titlebar.showBackButton(mainActivityContext, false).setOnClickListener(this);
        }

        if (profile) {
            mainActivityContext.getIvSubCategoryItem().setImageResource(R.drawable.car_prof_bg);
            mainActivityContext.getIvSubCategoryItem().setScaleType(ImageView.ScaleType.FIT_XY);
            mainActivityContext.getIvSubCategoryItem().requestLayout();
            mainActivityContext.getCollapsingToolBarLayout().setVisibility(View.GONE);
            mainActivityContext.getRvSubCategoryItem().setVisibility(View.VISIBLE);
        }
        super.onDestroyView();
    }

    @Override
    public void setTitlebar(Titlebar titlebar) {
        if (loaded) {
            this.titlebar = titlebar;
            titlebar.resetTitlebar(mainActivityContext);
            AppBarLayout.LayoutParams params = new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, AppBarLayout.LayoutParams.WRAP_CONTENT);
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED | AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL);
            mainActivityContext.getCollapsingToolBarLayout().setLayoutParams(params);
            mainActivityContext.getCollapsingToolBarLayout().requestLayout();
            mainActivityContext.getRvSubCategoryItem().setVisibility(View.GONE);
            mainActivityContext.getCollapsingToolBarLayout().setVisibility(View.VISIBLE);
            if (profile) {
                if (edit) {
                    titlebar.setTitle(mainActivityContext.getResources().getString(R.string.edit_car));
                    binding.btnSubmitRequest.setText(mainActivityContext.getResources().getString(R.string.edit_car));
                } else {
                    titlebar.setTitle(mainActivityContext.getResources().getString(R.string.add_car));
                    binding.btnSubmitRequest.setText(mainActivityContext.getResources().getString(R.string.add_car));
                }
            } else {
                if (evaluate) {
                    if (title != null) {
                        titlebar.setTitle(title);
                    } else {
                        titlebar.setTitle(mainActivityContext.getResources().getString(R.string.evaluate_your_car));
                    }
                } else {
                    titlebar.setTitle(mainActivityContext.getResources().getString(R.string.trade_in_your_car));
                }

            }
            titlebar.showBackButton(mainActivityContext, false);
            loaded = false;
        }
    }

    @Override
    public void onClick(View view) {
        UIHelper.hideKeyboard(view, mainActivityContext);
        switch (view.getId()) {
            case R.id.tvCodePicker:
                openCodePicker();
                break;

            case R.id.btnSubmitRequest:
                if (!preferenceHelper.getLoginStatus()) {
                    String message = "";

                    if (evaluate) {
                        if (title != null) {
                            message = mainActivityContext.getResources().getString(R.string.you_need_to_sign_in) + " " + title.toLowerCase() + ". " + mainActivityContext.getResources().getString(R.string.do_you_want_to_sign_in);
                        } else {
                            message = mainActivityContext.getResources().getString(R.string.require_signin_message);
                        }
                    } else {
                        message = mainActivityContext.getResources().getString(R.string.you_need_to_sign_in) + " " + mainActivityContext.getResources().getString(R.string.trade_in_your_car).toLowerCase() + ". " + mainActivityContext.getResources().getString(R.string.do_you_want_to_sign_in);
                    }

                    launchSigninRequirement(mainActivityContext, message);
                    return;
                }

                if (!CustomValidation.validateLength(binding.etName, binding.ilName, mainActivityContext.getResources().getString(R.string.err_full_name), "3", "50"))
                    return;

                if (!CustomValidation.validateEmail(binding.etEmail, binding.ilEmail, mainActivityContext.getResources().getString(R.string.err_email)))
                    return;

                if (!CustomValidation.validateLength(binding.etNumber, binding.ilPhoneNumber, mainActivityContext.getResources().getString(R.string.err_phone), "7", "20"))
                    return;

                if (UIHelper.isEmptyOrNull(binding.tvCodePicker.getText().toString()) && binding.etNumber.getText().toString().length() >= 5) {
                    UIHelper.showSnackbar(getView(), mainActivityContext.getResources().getString(R.string.err_code), Toast.LENGTH_SHORT);
                    return;
                }

                if (UIHelper.isEmptyOrNull(binding.tvMake.getText().toString())) {
                    UIHelper.showSnackbar(getView(), mainActivityContext.getResources().getString(R.string.err_make), Toast.LENGTH_SHORT);
                    return;
                }
                if (UIHelper.isEmptyOrNull(binding.tvModel.getText().toString())) {
                    UIHelper.showSnackbar(getView(), mainActivityContext.getResources().getString(R.string.err_model), Toast.LENGTH_SHORT);
                    return;
                }
                if (UIHelper.isEmptyOrNull(binding.tvYearTrade.getText().toString())) {
                    UIHelper.showSnackbar(getView(), mainActivityContext.getResources().getString(R.string.err_year), Toast.LENGTH_SHORT);
                    return;
                }

                //                if (binding.etYearTrade.getText().length() > 0) {
//                if (!CustomValidation.validateLength(binding.etYearTrade, binding.ilYearTrade, mainActivityContext.getResources().getString(R.string.err_year), "4", "4"))
//                    return;
                // }
                //  if (binding.etKm.getText().length() > 0) {
                if (!CustomValidation.validateLength(binding.etKm, binding.ilKm, mainActivityContext.getResources().getString(R.string.err_km), "1", "20"))
                    return;
                // }
                if (UIHelper.isEmptyOrNull(binding.tvRegionalSpecs.getText().toString())) {
                    UIHelper.showSnackbar(getView(), mainActivityContext.getResources().getString(R.string.err_specs), Toast.LENGTH_SHORT);
                    return;
                }
                if (UIHelper.isEmptyOrNull(binding.tvAccident.getText().toString())) {
                    UIHelper.showSnackbar(getView(), mainActivityContext.getResources().getString(R.string.err_accident), Toast.LENGTH_SHORT);
                    return;
                }
                if (UIHelper.isEmptyOrNull(binding.tvExteriorColor.getText().toString())) {
                    UIHelper.showSnackbar(getView(), mainActivityContext.getResources().getString(R.string.err_exterior_color), Toast.LENGTH_SHORT);
                    return;
                }
                if (UIHelper.isEmptyOrNull(binding.tvInteriorColor.getText().toString())) {
                    UIHelper.showSnackbar(getView(), mainActivityContext.getResources().getString(R.string.err_interior_color), Toast.LENGTH_SHORT);
                    return;
                }
                // if (binding.etChassis.getText().length() > 0) {
                if (!CustomValidation.validateLength(binding.etChassis, binding.ilChassis, mainActivityContext.getResources().getString(R.string.err_chassis), "1", "20"))
                    return;
                //  }
/*
                if (engineTypeId == 0) {
                    UIHelper.showSnackbar(getView(), mainActivityContext.getResources().getString(R.string.select_engine_type), Toast.LENGTH_SHORT);
                    return;
                }
*/

/*
                if (!CustomValidation.validateLength(binding.etTrim, binding.ilTrim, mainActivityContext.getResources().getString(R.string.enter_trim_value), "1", "20")) {
                    return;
                }
*/
                if (!CustomValidation.validateLength(binding.etWarrantyRemaining, binding.ilWarrantyRemaining, mainActivityContext.getResources().getString(R.string.enter_warranty_remaining), "1", "20")) {
                    return;
                }

                if (!CustomValidation.validateLength(binding.etServiceWarrantyRemaining, binding.ilServiceWarrantyRemaining, mainActivityContext.getResources().getString(R.string.enter_service_warranty_remaining), "1", "20")) {
                    return;
                }
/*
                if (!CustomValidation.validateLength(binding.etRmngWarranty, binding.ilRmngWarranty, mainActivityContext.getResources().getString(R.string.enter_rmng_warranty), "1", "20")) {
                    return;
                }
*/


                /*
                if (tradeInMediaAdapter.getAllList().size() == 1) {
                    UIHelper.showSnackbar(getView(), mainActivityContext.getResources().getString(R.string.images_error), Toast.LENGTH_SHORT);
                    return;
                }
                */

                boolean imageSelected = false;

                if (frontImageMedia != null || backImageMedia != null || rightImageMedia != null || leftImageMedia != null || interiorImageMedia != null || registrationImageMedia != null) {
                    imageSelected = true;
                }

                if (!imageSelected) {
                    UIHelper.showSnackbar(getView(), mainActivityContext.getResources().getString(R.string.images_error), Toast.LENGTH_SHORT);
                    return;
                }

                if (!binding.checkboxTerms.isChecked()) {
                    UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.agree_terms_of_use), Toast.LENGTH_LONG);
                    return;
                }

                if (mainActivityContext.showLoader()) {
                    submitRequest();
                }

                break;

            case R.id.tvAccident:
                openAccidentPicker();
                break;

            case R.id.tvMake:
                models = new ArrayList<>();
                if (make != null && make.size() > 0)
                    openMakePicker();
                else {
                    UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.no_make_found), Toast.LENGTH_SHORT);
                }
                break;

            case R.id.tvModel:
                if (models != null && models.size() > 0) {
                    openModelPicker();
                } else {
                    if (binding.tvMake.getText().toString().isEmpty()) {
                        UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.make_first_error), Toast.LENGTH_SHORT);
                    } else {
                        UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.no_models_found), Toast.LENGTH_SHORT);

                    }
                }
                break;

            case R.id.tvRegionalSpecs:
                openRegionalSpecsPicker();
                break;

            case R.id.tvExteriorColor:
                if (exteriorOptions != null && exteriorOptions.size() > 0)
                    openExteriorColorPicker();
                break;

            case R.id.tvInteriorColor:
                if (interiorOptions != null && interiorOptions.size() > 0)
                    openInteriorColorPicker();
                break;

            case R.id.tvYearTrade:
                openYearPicker();
                break;

            case R.id.tvFormDescription:
//                mainActivityContext.onBackPressed();
//                onCarSelectedForTradeListener.onCarAdded(new TradeCar());
                break;

            case R.id.ibBackbtn:
                mainActivityContext.onBackPressed();
                break;

            case R.id.tvEngineType:
                disableViewsForSomeSeconds(binding.tvEngineType);
//                getEnginetypes();
                openEngineTypePicker();
                break;

            case R.id.ivFrontImage:
                if (frontImageMedia == null) {
                    mainActivityContext.openImagePicker(TradeInYourCarFragment.this, AppConstants.SELECT_IMAGE_COUNT, AppConstants.MyCarThumnailsIds.FRONT);
                }
                break;

            case R.id.ivBackImage:
                if (backImageMedia == null) {
                    mainActivityContext.openImagePicker(TradeInYourCarFragment.this, AppConstants.SELECT_IMAGE_COUNT, AppConstants.MyCarThumnailsIds.BACK);
                }
                break;

            case R.id.ivRightImage:
                if (rightImageMedia == null) {
                    mainActivityContext.openImagePicker(TradeInYourCarFragment.this, AppConstants.SELECT_IMAGE_COUNT, AppConstants.MyCarThumnailsIds.RIGHT);
                }
                break;

            case R.id.ivLeftImage:
                if (leftImageMedia == null) {
                    mainActivityContext.openImagePicker(TradeInYourCarFragment.this, AppConstants.SELECT_IMAGE_COUNT, AppConstants.MyCarThumnailsIds.LEFT);
                }
                break;

            case R.id.ivInteriorImage:
                if (interiorImageMedia == null) {
                    mainActivityContext.openImagePicker(TradeInYourCarFragment.this, AppConstants.SELECT_IMAGE_COUNT, AppConstants.MyCarThumnailsIds.INTERIOR);
                }
                break;

            case R.id.ivRegistrationImage:
                if (registrationImageMedia == null) {
                    mainActivityContext.openImagePicker(TradeInYourCarFragment.this, AppConstants.SELECT_IMAGE_COUNT, AppConstants.MyCarThumnailsIds.REGISTRATION_BOOK);
                }
                break;

            case R.id.ivRemoveFrontImage:
            case R.id.ivRemoveBackImage:
            case R.id.ivRemoveRightImage:
            case R.id.ivRemoveLeftImage:
            case R.id.ivRemoveRegistrationImage:
            case R.id.ivRemoveInteriorImage:
                UIHelper.showSimpleDialog(
                        mainActivityContext,
                        0,
                        mainActivityContext.getResources().getString(R.string.delete_image),
                        mainActivityContext.getResources().getString(R.string.delete_image_confirmation),
                        mainActivityContext.getResources().getString(R.string.delete),
                        mainActivityContext.getResources().getString(R.string.cancel),
                        false,
                        false,
                        new SimpleDialogActionListener() {
                            @Override
                            public void onDialogActionListener(DialogInterface dialog, int which, boolean positive, boolean logout) {
                                if (positive) {
                                    switch (view.getId()) {
                                        case R.id.ivRemoveFrontImage:
                                            deletedItems.add(frontImageMedia);
                                            frontImageMedia = null;
                                            binding.ivFrontImage.setImageResource(R.drawable.camerabox);
                                            binding.ivRemoveFrontImage.setVisibility(View.GONE);
                                            break;
                                        case R.id.ivRemoveBackImage:
                                            deletedItems.add(backImageMedia);
                                            backImageMedia = null;
                                            binding.ivBackImage.setImageResource(R.drawable.camerabox);
                                            binding.ivRemoveBackImage.setVisibility(View.GONE);
                                            break;
                                        case R.id.ivRemoveRightImage:
                                            deletedItems.add(rightImageMedia);
                                            rightImageMedia = null;
                                            binding.ivRightImage.setImageResource(R.drawable.camerabox);
                                            binding.ivRemoveRightImage.setVisibility(View.GONE);
                                            break;
                                        case R.id.ivRemoveLeftImage:
                                            deletedItems.add(leftImageMedia);
                                            leftImageMedia = null;
                                            binding.ivLeftImage.setImageResource(R.drawable.camerabox);
                                            binding.ivRemoveLeftImage.setVisibility(View.GONE);
                                            break;
                                        case R.id.ivRemoveInteriorImage:
                                            deletedItems.add(interiorImageMedia);
                                            interiorImageMedia = null;
                                            binding.ivInteriorImage.setImageResource(R.drawable.camerabox);
                                            binding.ivRemoveInteriorImage.setVisibility(View.GONE);
                                            break;

                                        case R.id.ivRemoveRegistrationImage:
                                            deletedItems.add(registrationImageMedia);
                                            registrationImageMedia = null;
                                            binding.ivRegistrationImage.setImageResource(R.drawable.camerabox);
                                            binding.ivRemoveRegistrationImage.setVisibility(View.GONE);
                                            break;
                                    }
                                }
                            }
                        }
                );
                break;

            case R.id.etVersion:
                if (model_id == 0) {
                    UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.select_model), Toast.LENGTH_SHORT);
                } else {
                    if (mainActivityContext.showLoader()) {
                        getVersions(model_id);
                    }
                }
                break;

            case R.id.tvTerms:
                terms();
                break;
        }
    }

    @Override
    public void onPhotoClicked(ArrayList<MyImageFile> file) {
        /*
        media = new ArrayList<>();
        for (int i = 0; i < file.size(); i++) {
            Media newMedia = new Media();
            newMedia.setNew(true);
            newMedia.setFileUrl(file.get(i).getAbsolutePath());
            media.add(newMedia);
        }
        tradeInMediaAdapter.addAll(media);
        */

        switch (file.get(0).getImageType()) {
            case AppConstants.MyCarThumnailsIds.FRONT:
                frontImageMedia = new Media();
                frontImageMedia.setNew(true);
                frontImageMedia.setFileUrl(file.get(0).getAbsolutePath());
                UIHelper.setImageWithGlide(mainActivityContext, binding.ivFrontImage, frontImageMedia.getFileUrl());
                binding.ivRemoveFrontImage.setVisibility(View.VISIBLE);
                break;

            case AppConstants.MyCarThumnailsIds.BACK:
                backImageMedia = new Media();
                backImageMedia.setNew(true);
                backImageMedia.setFileUrl(file.get(0).getAbsolutePath());
                UIHelper.setImageWithGlide(mainActivityContext, binding.ivBackImage, backImageMedia.getFileUrl());
                binding.ivRemoveBackImage.setVisibility(View.VISIBLE);
                break;

            case AppConstants.MyCarThumnailsIds.RIGHT:
                rightImageMedia = new Media();
                rightImageMedia.setNew(true);
                rightImageMedia.setFileUrl(file.get(0).getAbsolutePath());
                UIHelper.setImageWithGlide(mainActivityContext, binding.ivRightImage, rightImageMedia.getFileUrl());
                binding.ivRemoveRightImage.setVisibility(View.VISIBLE);
                break;

            case AppConstants.MyCarThumnailsIds.LEFT:
                leftImageMedia = new Media();
                leftImageMedia.setNew(true);
                leftImageMedia.setFileUrl(file.get(0).getAbsolutePath());
                UIHelper.setImageWithGlide(mainActivityContext, binding.ivLeftImage, leftImageMedia.getFileUrl());
                binding.ivRemoveLeftImage.setVisibility(View.VISIBLE);
                break;

            case AppConstants.MyCarThumnailsIds.INTERIOR:
                interiorImageMedia = new Media();
                interiorImageMedia.setNew(true);
                interiorImageMedia.setFileUrl(file.get(0).getAbsolutePath());
                UIHelper.setImageWithGlide(mainActivityContext, binding.ivInteriorImage, interiorImageMedia.getFileUrl());
                binding.ivRemoveInteriorImage.setVisibility(View.VISIBLE);
                break;

            case AppConstants.MyCarThumnailsIds.REGISTRATION_BOOK:
                registrationImageMedia = new Media();
                registrationImageMedia.setNew(true);
                registrationImageMedia.setFileUrl(file.get(0).getAbsolutePath());
                UIHelper.setImageWithGlide(mainActivityContext, binding.ivRegistrationImage, registrationImageMedia.getFileUrl());
                binding.ivRemoveRegistrationImage.setVisibility(View.VISIBLE);
                break;

        }
    }

    private void terms() {
        if (mainActivityContext.showLoader()) {
            WebApiRequest.Instance(mainActivityContext)
                    .request(AppConstants.WebServicesKeys.PAGES, null, null, null, null, new WebApiRequest.WebServiceArrayResponse() {
                        @Override
                        public void onSuccess(ApiArrayResponse apiArrayResponse) {
                            ArrayList<Page> pages = JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), Page.class);
                            if (pages != null && pages.size() > 0) {
                                for (int pos = 0; pos < pages.size(); pos++) {
                                    if (pages.get(pos).getSlug().equals(AppConstants.PagesSlug.TERMS)) {
                                        PagesFragment pagesFragment = new PagesFragment();
                                        pagesFragment.setTitle(pages.get(pos).getTitle());
                                        pagesFragment.setContent(pages.get(pos).getContent());
                                        mainActivityContext.replaceFragment(pagesFragment, PagesFragment.class.getSimpleName(), true, true);
                                        break;
                                    }
                                }
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

    private void setProfile() {
        if (tradeCar != null) {
            binding.llTradingCarDetail.setVisibility(View.VISIBLE);
            binding.viewDivider.setVisibility(View.VISIBLE);

            if (tradeCar.getMedia() != null && tradeCar.getMedia().size() > 0) {
                UIHelper.setImageWithGlide(mainActivityContext, binding.ivCarImage, tradeCar.getMedia().get(0).getFileUrl());
            }

            if (tradeCar.getModel() != null) {
                binding.tvCarBrand.setText(tradeCar.getModel().getName());
                if (tradeCar.getModel().getBrand() != null) {
                    binding.tvCarName.setText(tradeCar.getModel().getBrand().getName());
                }
            }
            binding.tvCarYear.setText(tradeCar.getYear() + "");
            if (tradeCar.getAmount() != null) {
                binding.tvCarPrice.setText((tradeCar.getCurrency() == null ? mainActivityContext.getCurrency() : tradeCar.getCurrency()) + "\t" + NumberFormat.getNumberInstance(Locale.US).format(tradeCar.getAmount()));
            } else {
                binding.tvCarPrice.setVisibility(View.GONE);
            }

            if (edit) {
                selectedYear = tradeCar.getYear() + "";
                binding.tvYearTrade.setText(mainActivityContext.getResources().getString(R.string.colon_year) + " " + tradeCar.getYear());
                binding.etName.setText(tradeCar.getName());
                if (tradeCar.getUser() != null) {
                    binding.etEmail.setText((tradeCar.getUser().getEmail() == null) ? "" : tradeCar.getUser().getEmail());
                    binding.etEmail.setEnabled(false);
                    if (tradeCar.getUser().getDetails() != null) {
//                    binding.etName.setText((tradeCar.getUser().getDetails().getFirstName() == null) ? "" : tradeCar.getUser().getDetails().getFirstName());
                        if (tradeCar.getUser().getDetails().getCountryCode() != null) {
                            binding.tvCodePicker.setText(tradeCar.getUser().getDetails().getCountryCode().equals("0") ? AppConstants.defaultCountrycode : tradeCar.getUser().getDetails().getCountryCode());
                        } else {
                            binding.tvCodePicker.setHint(AppConstants.defaultCountrycode);
                        }
                        if (tradeCar.getPhone() != null) {
                            binding.etNumber.setText(tradeCar.getPhone() + "");
                        }
                    }
                }

                if (tradeCar.getModel() != null) {
                    binding.tvModel.setText(mainActivityContext.getResources().getString(R.string.colon_model) + " " + tradeCar.getModel().getName());
                    if (tradeCar.getModel().getBrand() != null) {
                        binding.tvMake.setText(mainActivityContext.getResources().getString(R.string.colon_make) + " " + tradeCar.getModel().getBrand().getName());
                        getModels(tradeCar.getModel().getBrand().getId());
                    }
                }

                if (tradeCar.getRegional_specs() != null) {
                    binding.tvRegionalSpecs.setText(mainActivityContext.getResources().getString(R.string.colon_regional_specs) + " " + tradeCar.getRegional_specs().getName());
                }

                if (tradeCar.getNotes() != null) {
                    binding.etTradeNotes.setText(tradeCar.getNotes());
                }

                binding.etKm.setText(tradeCar.getKilometre() == null ? "" : tradeCar.getKilometre() + "");
                binding.etChassis.setText(tradeCar.getChassis() == null ? "" : tradeCar.getChassis());

                make_id = tradeCar.getModel() == null ? 0 : (tradeCar.getModel().getBrand() == null ? 0 : (tradeCar.getModel().getBrand().getId()));
                model_id = tradeCar.getModel() == null ? 0 : tradeCar.getModel().getId();
                regional_spec_id = tradeCar.getRegional_specs() == null ? 0 : tradeCar.getRegional_specs().getId();

                if (tradeCar.getVersionApp() != null) {
//                    selectedVersionApp = tradeCar.getVersionApp();
                    binding.etVersion.setText(tradeCar.getVersionApp());
                }

                if (tradeCar.getEngine_type() != null) {
                    engineTypeId = tradeCar.getEngine_type().getId();
                    binding.tvEngineType.setText(mainActivityContext.getResources().getString(R.string.colon_engine) + " " + tradeCar.getEngine_type().getName());
                }

                for (int i = 0; i < tradeCar.getMy_car_attributes().size(); i++) {
                    switch (tradeCar.getMy_car_attributes().get(i).getAttr_id()) {
                        case AppConstants.CarAttributes.INTERIOR_COLOR:
//                            interiorColorKey = tradeCar.getMy_car_attributes().get(i).getValue() == null ? 0 : Integer.parseInt(tradeCar.getMy_car_attributes().get(i).getValue());
                            interiorColorKey = AppConstants.CarAttributes.INTERIOR_COLOR;
                            OptionArray savedInteriorColor = new OptionArray();
                            savedInteriorColor.setId(tradeCar.getMy_car_attributes().get(i).getValue() != null ? Integer.valueOf(tradeCar.getMy_car_attributes().get(i).getValue()) : 0);
                            interiorColor = savedInteriorColor;
                            binding.tvInteriorColor.setText(mainActivityContext.getResources().getString(R.string.colon_interior_color) + " " + tradeCar.getMy_car_attributes().get(i).getAttr_option());
                            break;
                        case AppConstants.CarAttributes.EXTERIOR_COLOR:
//                            exteriorColorKey = tradeCar.getMy_car_attributes().get(i).getValue() == null ? 0 : Integer.parseInt(tradeCar.getMy_car_attributes().get(i).getValue());
                            exteriorColorKey = AppConstants.CarAttributes.EXTERIOR_COLOR;
                            OptionArray savedExteriorColor = new OptionArray();
                            savedExteriorColor.setId(tradeCar.getMy_car_attributes().get(i).getValue() != null ? Integer.valueOf(tradeCar.getMy_car_attributes().get(i).getValue()) : 0);
                            exteriorColor = savedExteriorColor;
                            binding.tvExteriorColor.setText(mainActivityContext.getResources().getString(R.string.colon_exterior_color) + " " + tradeCar.getMy_car_attributes().get(i).getAttr_option());
                            break;
                        case AppConstants.CarAttributes.ACCIDENT:
//                            exteriorColorKey = tradeCar.getMy_car_attributes().get(i).getValue() == null ? 0 : Integer.parseInt(tradeCar.getMy_car_attributes().get(i).getValue());
                            accidentKey = AppConstants.CarAttributes.ACCIDENT;
                            OptionArray savedAccident = new OptionArray();
                            savedAccident.setId(tradeCar.getMy_car_attributes().get(i).getValue() != null ? Integer.valueOf(tradeCar.getMy_car_attributes().get(i).getValue()) : 0);
                            accident = savedAccident;
                            binding.tvAccident.setText(mainActivityContext.getResources().getString(R.string.colon_accident) + " " + tradeCar.getMy_car_attributes().get(i).getAttr_option());
                            break;

                        case AppConstants.CarAttributes.TRIM:
                            binding.etTrim.setText(tradeCar.getMy_car_attributes().get(i).getValue());
                            break;

                        case AppConstants.CarAttributes.WARRANTY_REMAINING:
                            binding.etWarrantyRemaining.setText(tradeCar.getMy_car_attributes().get(i).getValue());
                            break;

                        case AppConstants.CarAttributes.SERVICE_CONTRACT:
                            binding.etServiceWarrantyRemaining.setText(tradeCar.getMy_car_attributes().get(i).getValue());
                            break;
/*
                        case AppConstants.CarAttributes.RMNG_WARRANTY:
                            binding.etRmngWarranty.setText(tradeCar.getMy_car_attributes().get(i).getValue());
                            break;
*/
                    }
                }
/*
                if (tradeCar.getMyCarFeatures() != null && tradeCar.getMyCarFeatures().size() > 0) {
                    for (int i = 0; i < tradeCar.getMyCarFeatures().size(); i++) {
                        if (tradeCar.getMyCarFeatures().get(i).getId() == 1) {
                            binding.tvAccident.setText(AppConstants.YES);
                            break;
                        }
                    }
                } else {
                    binding.tvAccident.setText(AppConstants.NO);
                }
*/
                media = new ArrayList<>();
                /*
                for (int position = 0; position < tradeCar.getMedia().size(); position++) {
                    media.add(tradeCar.getMedia().get(position).getFileUrl());
                }
                */

//                tradeInMediaAdapter.addAll(tradeCar.getMedia());

                for (int position = 0; position < tradeCar.getMedia().size(); position++) {
                    switch (tradeCar.getMedia().get(position).getTitle()) {
                        case AppConstants.MyCarThumbnailsKeys.FRONT:
                            frontImageMedia = tradeCar.getMedia().get(position);
                            frontImageMedia.setNew(false);
                            UIHelper.setImageWithGlide(mainActivityContext, binding.ivFrontImage, frontImageMedia.getFileUrl());
                            binding.ivRemoveFrontImage.setVisibility(View.VISIBLE);
                            break;

                        case AppConstants.MyCarThumbnailsKeys.BACK:
                            backImageMedia = tradeCar.getMedia().get(position);
                            backImageMedia.setNew(false);
                            UIHelper.setImageWithGlide(mainActivityContext, binding.ivBackImage, backImageMedia.getFileUrl());
                            binding.ivRemoveBackImage.setVisibility(View.VISIBLE);
                            break;

                        case AppConstants.MyCarThumbnailsKeys.RIGHT:
                            rightImageMedia = tradeCar.getMedia().get(position);
                            rightImageMedia.setNew(false);
                            UIHelper.setImageWithGlide(mainActivityContext, binding.ivRightImage, rightImageMedia.getFileUrl());
                            binding.ivRemoveRightImage.setVisibility(View.VISIBLE);
                            break;

                        case AppConstants.MyCarThumbnailsKeys.LEFT:
                            leftImageMedia = tradeCar.getMedia().get(position);
                            leftImageMedia.setNew(false);
                            UIHelper.setImageWithGlide(mainActivityContext, binding.ivLeftImage, leftImageMedia.getFileUrl());
                            binding.ivRemoveLeftImage.setVisibility(View.VISIBLE);
                            break;

                        case AppConstants.MyCarThumbnailsKeys.INTERIOR:
                            interiorImageMedia = tradeCar.getMedia().get(position);
                            interiorImageMedia.setNew(false);
                            UIHelper.setImageWithGlide(mainActivityContext, binding.ivInteriorImage, interiorImageMedia.getFileUrl());
                            binding.ivRemoveInteriorImage.setVisibility(View.VISIBLE);
                            break;

                        case AppConstants.MyCarThumbnailsKeys.REGISTRATION_CARD:
                            registrationImageMedia = tradeCar.getMedia().get(position);
                            registrationImageMedia.setNew(false);
                            UIHelper.setImageWithGlide(mainActivityContext, binding.ivRegistrationImage, registrationImageMedia.getFileUrl());
                            binding.ivRemoveRegistrationImage.setVisibility(View.VISIBLE);
                            break;
                    }
                }

/*
                binding.tvInteriorColor.setText(interiorColorKey);
                binding.tvExteriorColor.setText(exteriorColorKey);
*/

            }
        }

        currentUser = preferenceHelper.getUser();
        if (!edit) {
            if (currentUser != null) {
                binding.etEmail.setText((currentUser.getEmail() == null) ? "" : currentUser.getEmail());
                binding.etEmail.setEnabled(false);
                if (currentUser.getDetails() != null) {
                    binding.etName.setText((currentUser.getDetails().getFirstName() == null) ? "" : currentUser.getDetails().getFirstName());
                    if (currentUser.getDetails().getCountryCode() != null) {
                        binding.tvCodePicker.setText(currentUser.getDetails().getCountryCode().equals("0") ? AppConstants.defaultCountrycode : currentUser.getDetails().getCountryCode());
                    } else {
                        binding.tvCodePicker.setHint(AppConstants.defaultCountrycode);
                    }
                    if (currentUser.getDetails().getPhone() != null) {
                        binding.etNumber.setText(currentUser.getDetails().getPhone());
                    }
                }
            }
        }
    }

    private void initMediaAdapter() {
        tradeInMediaAdapter = new TradeInMediaAdapter(mainActivityContext, new ArrayList<Media>());
        binding.rvMedia.setLayoutManager(new LinearLayoutManager(mainActivityContext, LinearLayoutManager.HORIZONTAL, false));
        binding.rvMedia.setNestedScrollingEnabled(false);
        binding.rvMedia.setAdapter(tradeInMediaAdapter);
        tradeInMediaAdapter.addFirstItem();
        binding.rvMedia.addOnItemTouchListener(new RecyclerTouchListener(mainActivityContext, binding.rvMedia, new ClickListenerRecycler() {
            @Override
            public void onClick(View view, int position) {
                if (position == 0) {
                    if (tradeInMediaAdapter.getItemCount() < AppConstants.SELECT_MEDIA_COUNT)
                        mainActivityContext.openImagePicker(TradeInYourCarFragment.this, AppConstants.SELECT_IMAGE_COUNT, AppConstants.MyCarThumnailsIds.FRONT);
                    else
                        UIHelper.showSnackbar(getView(), mainActivityContext.getResources().getString(R.string.images_limit_error), Toast.LENGTH_SHORT);
                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void setListeners() {
        binding.ilName.setErrorEnabled();
        binding.ilEmail.setErrorEnabled();
        binding.ilPhoneNumber.setErrorEnabled();
        binding.tradeNotesInputLayout.setErrorEnabled();
        binding.ilKm.setErrorEnabled();
        binding.ilChassis.setErrorEnabled();
        binding.ilTrim.setErrorEnabled();
        binding.ilWarrantyRemaining.setErrorEnabled();
        binding.ilServiceWarrantyRemaining.setErrorEnabled();
        binding.ilRmngWarranty.setErrorEnabled();
        binding.tvYearTrade.setOnClickListener(this);
        binding.tvCodePicker.setOnClickListener(this);
        binding.btnSubmitRequest.setOnClickListener(this);
        binding.tvAccident.setOnClickListener(this);
        binding.tvMake.setOnClickListener(this);
        binding.tvModel.setOnClickListener(this);
        binding.tvRegionalSpecs.setOnClickListener(this);
        binding.tvExteriorColor.setOnClickListener(this);
        binding.tvInteriorColor.setOnClickListener(this);
        binding.tvFormDescription.setOnClickListener(this);
        binding.tvEngineType.setOnClickListener(this);
//        binding.etVersion.setOnClickListener(this);

        binding.ivFrontImage.setOnClickListener(this);
        binding.ivBackImage.setOnClickListener(this);
        binding.ivRightImage.setOnClickListener(this);
        binding.ivLeftImage.setOnClickListener(this);
        binding.ivInteriorImage.setOnClickListener(this);
        binding.ivRegistrationImage.setOnClickListener(this);

        binding.ivRemoveFrontImage.setOnClickListener(this);
        binding.ivRemoveBackImage.setOnClickListener(this);
        binding.ivRemoveRightImage.setOnClickListener(this);
        binding.ivRemoveLeftImage.setOnClickListener(this);
        binding.ivRemoveInteriorImage.setOnClickListener(this);
        binding.ivRemoveRegistrationImage.setOnClickListener(this);

        binding.tvTerms.setOnClickListener(this);
    }

    private void openExteriorColorPicker() {
        final ArrayList<String> exteriorColorList = new ArrayList<>();
        for (int i = 0; i < exteriorOptions.size(); i++) {
            exteriorColorList.add(exteriorOptions.get(i).getName());
        }
        final ArrayList<OptionArray> finalExteriorOptions = exteriorOptions;
        DialogFactory.listDialog(mainActivityContext, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                exteriorColor = finalExteriorOptions.get(i);
                binding.tvExteriorColor.setText(mainActivityContext.getResources().getString(R.string.colon_exterior_color) + " " + exteriorColorList.get(i));

            }
        }, mainActivityContext.getResources().getString(R.string.exterior_color), exteriorColorList);

    }

    private void openInteriorColorPicker() {
        final ArrayList<String> interiorColorList = new ArrayList<>();

        for (int i = 0; i < interiorOptions.size(); i++) {
            interiorColorList.add(interiorOptions.get(i).getName());
        }
        final ArrayList<OptionArray> finalInteriorOptions = interiorOptions;
        DialogFactory.listDialog(mainActivityContext, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                interiorColor = finalInteriorOptions.get(i);
                binding.tvInteriorColor.setText(mainActivityContext.getResources().getString(R.string.colon_interior_color) + " " + interiorColorList.get(i));

            }
        }, mainActivityContext.getResources().getString(R.string.interior_color), interiorColorList);
    }

    private void openAccidentPicker() {
        final ArrayList<String> accidentList = new ArrayList<>();

        for (int i = 0; i < accidentOptions.size(); i++) {
            accidentList.add(accidentOptions.get(i).getName());
        }
        final ArrayList<OptionArray> finalAccidentOptions = accidentOptions;
        DialogFactory.listDialog(mainActivityContext, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                accident = finalAccidentOptions.get(i);
                binding.tvAccident.setText(mainActivityContext.getResources().getString(R.string.colon_accident) + " " + accidentList.get(i));
            }
        }, mainActivityContext.getResources().getString(R.string.accident), accidentList);
    }

    private void openRegionalSpecsPicker() {
        final ArrayList<String> regionalSpecsList = new ArrayList<>();
        for (int i = 0; i < regionalSpecs.size(); i++) {
            if (regionalSpecs.get(i).getName() != null) {
                regionalSpecsList.add(regionalSpecs.get(i).getName());
            }
        }
        if (regionalSpecsList.size() > 0) {
            DialogFactory.listDialog(mainActivityContext, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    binding.tvRegionalSpecs.setText(mainActivityContext.getResources().getString(R.string.colon_regional_specs) + " " + regionalSpecsList.get(i));
                    regional_spec_id = regionalSpecs.get(i).getId();


                }
            }, mainActivityContext.getResources().getString(R.string.regional_specification), regionalSpecsList);
        }
    }

    String selectedYear = "";

    private void openYearPicker() {
        final ArrayList<String> yearList = new ArrayList<>();
        for (int i = 2019; i > (2019 - 15); i--) {
            yearList.add(i + "");
        }
        DialogFactory.listDialog(mainActivityContext, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectedYear = yearList.get(i);
                binding.tvYearTrade.setText(mainActivityContext.getResources().getString(R.string.colon_year) + " " + yearList.get(i));
            }
        }, mainActivityContext.getResources().getString(R.string.year), yearList);
    }

    private void openMakePicker() {
        final ArrayList<String> makeList = new ArrayList<>();
        for (int i = 0; i < make.size(); i++) {
            makeList.add(make.get(i).getName());
        }
        DialogFactory.listDialog(mainActivityContext, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                binding.tvMake.setText(mainActivityContext.getResources().getString(R.string.colon_make) + " " + makeList.get(i));
                make_id = make.get(i).getId();
                binding.tvModel.setText("");
                model_id = 0;
                getModels(make.get(i).getId());
                selectedVersion = null;
                binding.etVersion.setText("");
                binding.tvYearTrade.setText("");
            }
        }, mainActivityContext.getResources().getString(R.string.make), makeList);
    }

    private void openModelPicker() {
        final ArrayList<String> modelList = new ArrayList<>();
        for (int i = 0; i < models.size(); i++) {
            modelList.add(models.get(i).getName());
        }
        DialogFactory.listDialog(mainActivityContext, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                binding.tvModel.setText(mainActivityContext.getResources().getString(R.string.colon_model) + " " + modelList.get(i));
                model_id = models.get(i).getId();
                selectedVersion = null;
                binding.etVersion.setText("");
                binding.tvYearTrade.setText("");
            }
        }, mainActivityContext.getResources().getString(R.string.model), modelList);
    }

    private void openCodePicker() {
        mainActivityContext.pickCountry(new CountryPickerDialog.OnCountrySelectedListener() {
            @Override
            public void onCountrySelected(String name, String code, String dialCode, int flagDrawableResID) {
                binding.tvCodePicker.setText(dialCode);
            }
        });
    }

    private void getMake() {

        WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.MAKE, binding.getRoot(), null, null, null, new WebApiRequest.WebServiceArrayResponse() {
            @Override
            public void onSuccess(ApiArrayResponse apiArrayResponse) {
                make = JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), Make.class);
                loaded = true;
                setTitlebar(mainActivityContext.getTitlebar());
                mainActivityContext.hideLoader();

            }

            @Override
            public void onError() {
                mainActivityContext.hideLoader();
            }
        });
    }

    private void getModels(int brandId) {
        Map<String, Object> params = new HashMap<>();
        params.put("brand_id", brandId);
        WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.MODEL, null, null, params, null, new WebApiRequest.WebServiceArrayResponse() {
            @Override
            public void onSuccess(ApiArrayResponse apiArrayResponse) {
                models = JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), Model.class);
                mainActivityContext.hideLoader();

            }

            @Override
            public void onError() {
                mainActivityContext.hideLoader();
            }
        });
    }

    private void getRegionalSpecs() {

        WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.REGIONAL_SPECS, binding.getRoot(), null, null, null, new WebApiRequest.WebServiceArrayResponse() {
            @Override
            public void onSuccess(ApiArrayResponse apiArrayResponse) {
                ArrayList<RegionalSpecs> responseRegionalSpecs = JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), RegionalSpecs.class);
                for (int i = 0; i < responseRegionalSpecs.size(); i++) {
                    if (responseRegionalSpecs.get(i).getName() != null) {
                        regionalSpecs.add(responseRegionalSpecs.get(i));
                    }
                }
                mainActivityContext.hideLoader();

            }

            @Override
            public void onError() {
                mainActivityContext.hideLoader();
            }
        });
    }

    private void getAttributes() {
        HashMap<String, Object> interiorParams = new HashMap<>();
        interiorParams.put("id", AppConstants.CarAttributes.INTERIOR_COLOR);
        WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.ATTRIBUTES, binding.getRoot(), null, interiorParams, new WebApiRequest.WebServiceObjectResponse() {
            @Override
            public void onSuccess(ApiResponse apiResponse) {
                AttributesWrapper attributesWrapper = (AttributesWrapper) JsonHelpers.convertToModelClass(apiResponse.getData(), AttributesWrapper.class);
                interiorOptions = new ArrayList<>();

                interiorColorKey = AppConstants.CarAttributes.INTERIOR_COLOR;
                interiorOptions = attributesWrapper.getOptionArray();

                binding.tvInteriorColor.setEnabled(true);

                HashMap<String, Object> exteriorParams = new HashMap<>();
                exteriorParams.put("id", AppConstants.CarAttributes.EXTERIOR_COLOR);
                WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.ATTRIBUTES, null, null, exteriorParams, new WebApiRequest.WebServiceObjectResponse() {
                    @Override
                    public void onSuccess(ApiResponse apiResponse) {
                        AttributesWrapper attributesWrapper = (AttributesWrapper) JsonHelpers.convertToModelClass(apiResponse.getData(), AttributesWrapper.class);
                        exteriorOptions = new ArrayList<>();

                        exteriorColorKey = AppConstants.CarAttributes.EXTERIOR_COLOR;
                        exteriorOptions = attributesWrapper.getOptionArray();

                        binding.tvExteriorColor.setEnabled(true);

                        HashMap<String, Object> accidentParams = new HashMap<>();
                        accidentParams.put("id", AppConstants.CarAttributes.ACCIDENT);
                        WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.ATTRIBUTES, null, null, accidentParams, new WebApiRequest.WebServiceObjectResponse() {
                            @Override
                            public void onSuccess(ApiResponse apiResponse) {
                                AttributesWrapper attributesWrapper = (AttributesWrapper) JsonHelpers.convertToModelClass(apiResponse.getData(), AttributesWrapper.class);
                                accidentOptions = new ArrayList<>();

                                accidentKey = AppConstants.CarAttributes.ACCIDENT;
                                accidentOptions = attributesWrapper.getOptionArray();

                                binding.tvAccident.setEnabled(true);

                                WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.ENGINE_TYPES, null, null, null, null, new WebApiRequest.WebServiceArrayResponse() {
                                    @Override
                                    public void onSuccess(ApiArrayResponse apiArrayResponse) {
                                        engineTypes = JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), EngineType.class);
                                        binding.tvEngineType.setEnabled(true);
                                    }

                                    @Override
                                    public void onError() {
                                    }
                                });
                            }

                            @Override
                            public void onError() {
                            }
                        }, null);

                    }

                    @Override
                    public void onError() {
                        mainActivityContext.hideLoader();
                    }
                }, null);
            }

            @Override
            public void onError() {
            }
        }, null);
    }

    EngineType engineType;

    private void openEngineTypePicker() {
        if (engineTypes != null & engineTypes.size() > 0) {
            final ArrayList<String> engineList = new ArrayList<>();
            for (int i = 0; i < engineTypes.size(); i++) {
                engineList.add(engineTypes.get(i).getName());
            }
            DialogFactory.listDialog(mainActivityContext, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    engineType = engineTypes.get(i);
                    engineTypeId = engineTypes.get(i).getId();
                    binding.tvEngineType.setText(mainActivityContext.getResources().getString(R.string.colon_engine) + " " + engineList.get(i));
                }
            }, mainActivityContext.getResources().getString(R.string.engine), engineList);
        }
    }

    private void getEnginetypes() {

    }

    private void submitRequest() {
        binding.btnSubmitRequest.setEnabled(false);
        binding.btnSubmitRequest.setAlpha((float) 0.5);
        HashMap<String, Object> params = new HashMap<>();
        JSONObject jsonObjectExteriorColor = new JSONObject();
        try {
            jsonObjectExteriorColor.put(exteriorColorKey + "", exteriorColor.getId() + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject jsonObjectInteriorColor = new JSONObject();
        try {
            jsonObjectInteriorColor.put(interiorColorKey + "", interiorColor.getId() + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject jsonObjectAccident = new JSONObject();
        try {
            jsonObjectAccident.put(accidentKey + "", accident.getId() + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
/*
        JSONObject jsonObjectTrim = new JSONObject();
        try {
            jsonObjectTrim.put(AppConstants.CarAttributes.TRIM + "", binding.etTrim.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
*/
        JSONObject jsonObjectWarrantyRemaining = new JSONObject();
        try {
            jsonObjectWarrantyRemaining.put(AppConstants.CarAttributes.WARRANTY_REMAINING + "", binding.etWarrantyRemaining.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject jsonObjectServiceContract = new JSONObject();
        try {
            jsonObjectServiceContract.put(AppConstants.CarAttributes.SERVICE_CONTRACT + "", binding.etServiceWarrantyRemaining.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject jsonObjectRmngwarranty = new JSONObject();
        try {
            jsonObjectRmngwarranty.put(AppConstants.CarAttributes.RMNG_WARRANTY + "", binding.etRmngWarranty.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(jsonObjectExteriorColor);
        jsonArray.put(jsonObjectInteriorColor);
        jsonArray.put(jsonObjectAccident);
//        jsonArray.put(jsonObjectTrim);
        jsonArray.put(jsonObjectWarrantyRemaining);
        jsonArray.put(jsonObjectServiceContract);
//        jsonArray.put(jsonObjectRmngwarranty);
        ArrayList<MultipartBody.Part> mediaFiles = new ArrayList<>();

        //REMOVING OLD IMAGE UPLOADING
        /*
        for (int i = 1; i < tradeInMediaAdapter.getAllList().size(); i++) {
            if (tradeInMediaAdapter.getAllList().get(i).isNew()) {
                mediaFiles.add(MultipartBody.Part.createFormData("media[]", tradeInMediaAdapter.getAllList().get(i).getFileUrl(), convertFile(tradeInMediaAdapter.getAllList().get(i).getFileUrl())));
            }
        }
        */

        if (frontImageMedia != null && frontImageMedia.isNew()) {
            mediaFiles.add(MultipartBody.Part.createFormData("media[" + AppConstants.MyCarThumbnailsKeys.FRONT + "]", frontImageMedia.getFileUrl(), convertFile(frontImageMedia.getFileUrl())));
        }

        if (backImageMedia != null && backImageMedia.isNew()) {
            mediaFiles.add(MultipartBody.Part.createFormData("media[" + AppConstants.MyCarThumbnailsKeys.BACK + "]", backImageMedia.getFileUrl(), convertFile(backImageMedia.getFileUrl())));
        }

        if (rightImageMedia != null && rightImageMedia.isNew()) {
            mediaFiles.add(MultipartBody.Part.createFormData("media[" + AppConstants.MyCarThumbnailsKeys.RIGHT + "]", rightImageMedia.getFileUrl(), convertFile(rightImageMedia.getFileUrl())));
        }

        if (leftImageMedia != null && leftImageMedia.isNew()) {
            mediaFiles.add(MultipartBody.Part.createFormData("media[" + AppConstants.MyCarThumbnailsKeys.LEFT + "]", leftImageMedia.getFileUrl(), convertFile(leftImageMedia.getFileUrl())));
        }

        if (interiorImageMedia != null && interiorImageMedia.isNew()) {
            mediaFiles.add(MultipartBody.Part.createFormData("media[" + AppConstants.MyCarThumbnailsKeys.INTERIOR + "]", interiorImageMedia.getFileUrl(), convertFile(interiorImageMedia.getFileUrl())));
        }

        if (registrationImageMedia != null && registrationImageMedia.isNew()) {
            mediaFiles.add(MultipartBody.Part.createFormData("media[" + AppConstants.MyCarThumbnailsKeys.REGISTRATION_CARD + "]", registrationImageMedia.getFileUrl(), convertFile(registrationImageMedia.getFileUrl())));
        }

/*
        if (binding.tvAccident.getText().toString().toLowerCase().equals(AppConstants.YES.toLowerCase())) {
            ArrayList<Integer> arrayList = new ArrayList<>();
            arrayList.add(1);

            JSONArray jsonArray1 = new JSONArray(arrayList);
            params.put("car_features", jsonArray1.toString());
        } else {
            ArrayList<Integer> arrayList = new ArrayList<>();
            JSONArray jsonArray1 = new JSONArray(arrayList);
            params.put("car_features", jsonArray1.toString());
        }
*/
        params.put("name", binding.etName.getText().toString());
        params.put("email", binding.etEmail.getText().toString());
        params.put("country_code", binding.tvCodePicker.getText().toString());
        params.put("phone", binding.etNumber.getText().toString());
        params.put("chassis", binding.etChassis.getText().toString());
        params.put("kilometer", binding.etKm.getText().toString());
        params.put("model_id", model_id + "");
        params.put("year", selectedYear);
        params.put("regional_specification_id", regional_spec_id + "");
        params.put("engine_type_id", engineTypeId + "");
        params.put("car_attributes", jsonArray.toString());
        params.put("media[]", mediaFiles);
        params.put("notes", UIHelper.isEmptyOrNull(binding.etTradeNotes.getText().toString()) ? "" : binding.etTradeNotes.getText().toString());

        if (binding.etVersion.getText().toString().trim().length() > 0) {
            params.put("version_app", binding.etVersion.getText().toString());
        } else {
            params.put("version_app", "");
        }

/*
        if (selectedVersion != null) {
            params.put("version_id", selectedVersion.getId() + "");
        } else {
            params.put("version_id", "");
        }
*/
        if (edit) {
            String deletedImagesIds = null, postedIDs = "";
            if (deletedItems != null && deletedItems.size() > 0) {
                deletedImagesIds = "";
                for (int i = 0; i < deletedItems.size(); i++) {
                    if (deletedItems.get(i).getId() > 0) {
                        deletedImagesIds += deletedItems.get(i).getId() + ",";
                    }
                }
                if (deletedImagesIds.length() > 0) {
                    if (deletedImagesIds.charAt(deletedImagesIds.length() - 1) == ',') {
                        deletedImagesIds = deletedImagesIds.substring(0, deletedImagesIds.length() - 1);
                    }
                }
            }

            params.put("id", tradeCar.getId());
            if (deletedImagesIds != null && deletedImagesIds.length() > 0) {
                params.put("deleted_images", deletedImagesIds);
            } else {
                params.put("deleted_images", "0");
            }

            params.put("_method", "PUT");
            WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.PUT_MY_CARS, null, null, params, new WebApiRequest.WebServiceObjectResponse() {
                @Override
                public void onSuccess(ApiResponse apiResponse) {
                    UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.car_updated_successful), Toast.LENGTH_LONG);
                    mainActivityContext.onBackPressed();
                    mainActivityContext.hideLoader();
                }

                @Override
                public void onError() {
                    UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.server_error), Toast.LENGTH_SHORT);
                    mainActivityContext.hideLoader();
                    binding.btnSubmitRequest.setEnabled(true);
                    binding.btnSubmitRequest.setAlpha((float) 1);
                }
            }, null);
        } else {
            WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.POST_TRADE_CAR, null, null, params, new WebApiRequest.WebServiceObjectResponse() {
                @Override
                public void onSuccess(ApiResponse apiResponse) {
//                if (!UIHelper.isEmptyOrNull(apiResponse.getMessage()))
//                    UIHelper.showToast(mainActivityContext, apiResponse.getMessage(), Toast.LENGTH_SHORT);

                    if (!trading) {
                        addedSuccessful((TradeCar) JsonHelpers.convertToModelClass(apiResponse.getData(), TradeCar.class));
                    } else {
                        UIHelper.showToast(mainActivityContext, apiResponse.getMessage(), Toast.LENGTH_LONG);
                        if (onCarSelectedForTradeListener != null) {
                            onCarSelectedForTradeListener.onCarAdded((TradeCar) JsonHelpers.convertToModelClass(apiResponse.getData(), TradeCar.class));
                        }
                        mainActivityContext.onBackPressed();
                    }
                    mainActivityContext.hideLoader();
                    binding.btnSubmitRequest.setEnabled(true);
                    binding.btnSubmitRequest.setAlpha((float) 1);
                }

                @Override
                public void onError() {
                    mainActivityContext.hideLoader();
                    binding.btnSubmitRequest.setEnabled(true);
                    binding.btnSubmitRequest.setAlpha((float) 1);
                }
            }, null);
        }
    }

    private void addedSuccessful(TradeCar myNewCar) {
        UIHelper.showSimpleDialog(
                mainActivityContext,
                0,
                mainActivityContext.getResources().getString(R.string.success_ex),
                mainActivityContext.getResources().getString(R.string.added_car_successfully),
                mainActivityContext.getResources().getString(R.string.ok),
                null,
                false,
                false,
                new SimpleDialogActionListener() {
                    @Override
                    public void onDialogActionListener(DialogInterface dialog, int which, boolean positive, boolean logout) {
                        if (positive) {
                            dialog.dismiss();
                            launchEvaluateRequest(myNewCar);
                        }
                    }
                }
        );
    }

    private void launchEvaluateRequest(TradeCar myNewCar) {
        if (myNewCar != null) {
            UIHelper.showSimpleDialog(
                    mainActivityContext,
                    0,
                    mainActivityContext.getResources().getString(R.string.evaluate_your_car),
                    mainActivityContext.getResources().getString(R.string.submit_your_car_evaluation),
                    mainActivityContext.getResources().getString(R.string.yes),
                    mainActivityContext.getResources().getString(R.string.later),
                    false,
                    false,
                    new SimpleDialogActionListener() {
                        @Override
                        public void onDialogActionListener(DialogInterface dialog, int which, boolean positive, boolean logout) {
                            if (positive) {
                                evaluateCar(myNewCar.getId(), AppConstants.MyCarActions.EVALUATE);
                            } else {
                                mainActivityContext.onBackPressed();
                            }
                        }
                    }
            );
        }
    }

    private void evaluateCar(int myCarId, int type) {
        TradeInCar tradeInCar = new TradeInCar();
        tradeInCar.setCustomerCarId(myCarId);
        tradeInCar.setType(type);

        WebApiRequest.Instance(mainActivityContext).request(
                AppConstants.WebServicesKeys.POST_TRADE_IN_CAR,
                null,
                tradeInCar,
                null,
                new WebApiRequest.WebServiceObjectResponse() {
                    @Override
                    public void onSuccess(ApiResponse apiResponse) {
                        UIHelper.showToast(mainActivityContext, apiResponse.getMessage(), Toast.LENGTH_LONG);
                        mainActivityContext.onBackPressed();
                        mainActivityContext.hideLoader();
                    }

                    @Override
                    public void onError() {
                        mainActivityContext.hideLoader();
                    }
                },
                null
        );
    }

    private void showTradeCarRequestDialog() {
//        LayoutInflater inflater = mainActivityContext.getLayoutInflater();
//        View view=inflater.inflate(R.layout.layout_trade_request_approval,null);
//            UIHelper.showSimpleDialog(
//                    mainActivityContext,
//                    0,
//                    mainActivityContext.getResources().getString(R.string.we_will_be_in_touch),
//                    null,
//                    view,
//                    mainActivityContext.getResources().getString(R.string.close),
//                    null,
//                    null,
//                    false,
//                    false,
//                    new SimpleDialogActionListener() {
//                        @Override
//                        public void onDialogActionListener(DialogInterface dialog, int which, boolean positive, boolean logout) {
//                           if(positive)
//                               mainActivityContext.onBackPressed();
//                        }
//                    }
//            );
        TradeInRequestApprovalDialog tradeInRequestApprovalDialog = TradeInRequestApprovalDialog.newInstance(mainActivityContext);
        if (edit) {
            tradeInRequestApprovalDialog.setEdit(edit);
        }
        tradeInRequestApprovalDialog.show(mainActivityContext.getFragmentManager(), null);

    }

    public RequestBody convertFile(String filePath) {
        return RequestBody.create(MediaType.parse("image/*"), new File(filePath));
    }

    private void getVersions(int modelId) {
        Map<String, Object> params = new HashMap<>();
        params.put("model_id", modelId);
        WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.CAR_VERSIONS, null, null, params, null, new WebApiRequest.WebServiceArrayResponse() {
            @Override
            public void onSuccess(ApiArrayResponse apiArrayResponse) {
                versions = JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), Version.class);
                if (versions.size() == 0) {
                    UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.no_versions_to_show), Toast.LENGTH_SHORT);
                } else {
                    final ArrayList<String> versionList = new ArrayList<>();
                    for (int i = 0; i < versions.size(); i++) {
                        versionList.add(versions.get(i).getName());
                    }
                    DialogFactory.listDialog(mainActivityContext, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            binding.etVersion.setText(versions.get(i).getName());
                            selectedVersion = versions.get(i);
                        }
                    }, mainActivityContext.getResources().getString(R.string.version), versionList);
                }
                mainActivityContext.hideLoader();
            }

            @Override
            public void onError() {
                mainActivityContext.hideLoader();
            }
        });
    }

    public boolean isTrading() {
        return trading;
    }

    public void setTrading(boolean trading) {
        this.trading = trading;
    }

    public boolean isProfile() {
        return profile;
    }

    public void setProfile(boolean profile) {
        this.profile = profile;
    }

    public OnCarSelectedForTradeListener getOnCarSelectedForTradeListener() {
        return onCarSelectedForTradeListener;
    }

    public void setTradeCar(TradeCar tradeCar) {
        this.tradeCar = tradeCar;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    public void setOnCarSelectedForTradeListener(OnCarSelectedForTradeListener onCarSelectedForTradeListener) {
        this.onCarSelectedForTradeListener = onCarSelectedForTradeListener;
    }

    public void setEvaluate(boolean evaluate) {
        this.evaluate = evaluate;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
