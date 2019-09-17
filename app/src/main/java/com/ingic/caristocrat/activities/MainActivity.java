package com.ingic.caristocrat.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.ingic.caristocrat.R;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.ActivityMainBinding;
import com.ingic.caristocrat.dialogs.CallConsultantDialog;
import com.ingic.caristocrat.dialogs.CountryPickerDialog;
import com.ingic.caristocrat.fragments.HomeFragment;
import com.ingic.caristocrat.fragments.LuxuryMarketDetailsFragment;
import com.ingic.caristocrat.fragments.MainDetailPageFragment;
import com.ingic.caristocrat.fragments.NotificationsFragment;
import com.ingic.caristocrat.fragments.ProfileEditFragment;
import com.ingic.caristocrat.helpers.JsonHelpers;
import com.ingic.caristocrat.helpers.Titlebar;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.helpers.Utils;
import com.ingic.caristocrat.interfaces.MediaTypePicker;
import com.ingic.caristocrat.models.FCMPayload;
import com.ingic.caristocrat.models.MyImageFile;
import com.ingic.caristocrat.models.SlugType;
import com.ingic.caristocrat.models.TradeCar;
import com.ingic.caristocrat.webhelpers.models.ApiResponse;
import com.ingic.caristocrat.webhelpers.models.Category;
import com.ingic.caristocrat.webhelpers.models.InteractionCar;
import com.ingic.caristocrat.webhelpers.services.WebApiRequest;
import com.mukesh.countrypicker.CountryPickerListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.utils.Orientation;
import id.zelory.compressor.Compressor;

import static com.ingic.caristocrat.constants.AppConstants.SELECT_IMAGE_COUNT;

public class MainActivity extends BaseActivity {
    public ActivityMainBinding binding;
    MediaTypePicker mediaPickerListener;
    ArrayList<String> photoPaths;
    ProgressDialog progressDialog;
    ArrayList<Category> mainCategories = new ArrayList<>();
    private String currency;

    int minWindowWidth, minWindowHeight, aspectRatioX, aspectRatioY, imageType;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setMainFrameLayoutID(binding.mainFrame.getId());
        setMainFrameLayout(binding.mainFrame);
        setCoordinatorLayoutID(binding.mainLayout.getId());
        setAppBarID(binding.appBar.getId());
        setCollapsingToolBarID(binding.collapsingToolBarLayout.getId());
        setToolbarID(binding.toolbar.getId());
        setCoordinatorLayout(binding.mainLayout);
        setAppbarLayout(binding.appBar);
        setCollapsingToolBarLayout(binding.collapsingToolBarLayout);
        setToolbarLayout(binding.toolbar);
        setIvSubCategoryItem(binding.ivSubCategoryItem);
        setRvSubCategoryItem(binding.rvSubCategoryItem);
        replaceFragmentWithClearBackStack(HomeFragment.Instance(), HomeFragment.class.getName(), true, false);
        redirect();
        redirectDeepLinkIntent(getIntent());
        /*
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(1);
        arrayList.add(2);

        JSONArray jsonArray = new JSONArray(arrayList);
        Log.i("testing", jsonArray.toString());
        */

//        binding.webView.loadUrl("file:///android_asset/caristo_loader.gif");
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (getCurrentFragmentName() != null) {
            if (getCurrentFragmentName().equals(ProfileEditFragment.class.getSimpleName())) {
                if (!TedPermission.isGranted(this, AppConstants.AppPermissions())) {
                    UIHelper.showToast(this, getResources().getString(R.string.required_permission_denied), Toast.LENGTH_LONG);
                    onBackPressed();
                }
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FilePickerConst.REQUEST_CODE_PHOTO:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    pickImageResult(data);

                }
                break;
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                cropImageResult(resultCode, data);

                break;
        }

    }

    @Override
    public void onBackPressed() {
        if (WebApiRequest.Instance(this).getCallObject() != null) {
            WebApiRequest.Instance(this).getCallObject().cancel();
        } else if (WebApiRequest.Instance(this).getCallArray() != null) {
            WebApiRequest.Instance(this).getCallArray().cancel();
        }
        hideLoader();
        super.onBackPressed();
    }

    public Titlebar getTitlebar() {
        return binding.titlebar;
    }

    public boolean showLoader() {
        UIHelper.hideSoftKeyboard(this);
        if (!Utils.isNetworkAvailable(this)) {
            UIHelper.showSnackbar(getMainFrameLayout(), getResources().getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG);
            return false;
        } else {
//            Glide.with(this).clear(binding.ivLoader);
//            Glide.with(this).asGif().load(R.drawable.caristo_loader).into(binding.ivLoader);

            showLoaderInWeb();
            binding.progressBarContainer.setVisibility(View.VISIBLE);
            return true;
        }
    }

    private void showLoaderInWeb() {
        binding.webView.getSettings().setLoadsImagesAutomatically(true);
        binding.webView.getSettings().setJavaScriptEnabled(true);

        String yourData = "<html style=\"margin: 0;\">\n" +
                "    <body style=\"margin: 0;\">\n" +
                "    <img src=caristo_loader_new.gif style=\"width: 100%; height: 100%\" />\n" +
                "    </body>\n" +
                "    </html>";
        binding.webView.loadDataWithBaseURL("file:///android_asset/",yourData,"text/html","UTF-8",null);
    }

    public boolean internetConnected() {
        UIHelper.hideSoftKeyboard(this);
        if (!Utils.isNetworkAvailable(this)) {
            UIHelper.showSnackbar(getMainFrameLayout(), getResources().getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG);
            return false;
        } else {
            return true;
        }
    }

    public void hideLoader() {
        binding.progressBarContainer.setVisibility(View.GONE);
    }

    public void setContentscrimColor(int color) {
        if (color == Color.parseColor("#ffffff"))
            binding.collapsingToolBarLayout.setContentScrimColor(ContextCompat.getColor(this, R.color.colorBlack));
        else {
            binding.collapsingToolBarLayout.setContentScrimColor(color);
            binding.collapsingToolBarLayout.setStatusBarScrimColor(color);
        }
    }

    public void pickCountry(final CountryPickerDialog.OnCountrySelectedListener listener) {
        final CountryPickerDialog dialog = new CountryPickerDialog(getResources().getString(R.string.select_country), new CountryPickerDialog.OnDestroyListener() {
            @Override
            public void onDestroy() {
                UIHelper.hideKeyboard(binding.getRoot(), MainActivity.this);
            }
        });
        dialog.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                dialog.dismiss();
                listener.onCountrySelected(name, code, dialCode, flagDrawableResID);
            }
        });
        dialog.show(getSupportFragmentManager(), CountryPickerDialog.class.getSimpleName());
    }

    public void openImagePicker(final MediaTypePicker listener, final int count, int imageType) {
        this.imageType = imageType;
        TedPermission.with(this)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        mediaPickerListener = listener;
                        FilePickerBuilder.getInstance()
                                .setMaxCount(count)
                                //.setSelectedFiles(photoPaths)
                                .setActivityTheme(R.style.FilePickeTheme)
                                .enableVideoPicker(false)
                                .enableCameraSupport(true)
                                .showGifs(false)
                                .enableSelectAll(false)
                                .showFolderView(false)
                                .enableImagePicker(true)
                                .withOrientation(Orientation.UNSPECIFIED)
                                .pickPhoto(MainActivity.this);
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> arrayList) {
                        UIHelper.showToast(MainActivity.this, MainActivity.this.getString(R.string.permission_denied), Toast.LENGTH_SHORT);
//                        Toasty.warning(MainActivity.this, MainActivity.this.getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();

                    }
                }).check();

    }

    public void openImagePicker(final MediaTypePicker listener, final int count,
                                final Boolean videoenabling, int minWindowWidth, int minWindowHeight, int aspectRatioX,
                                int aspectRatioY) {
        this.minWindowWidth = minWindowWidth;
        this.minWindowHeight = minWindowHeight;
        this.aspectRatioX = aspectRatioX;
        this.aspectRatioY = aspectRatioY;

        TedPermission.with(this)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        mediaPickerListener = listener;
                        FilePickerBuilder.getInstance()
                                .setMaxCount(count)
                                //.setSelectedFiles(photoPaths)
                                .setActivityTheme(R.style.FilePickeTheme)
                                .enableVideoPicker(videoenabling)
                                .enableCameraSupport(true)
                                .showGifs(true)
                                .enableSelectAll(false)
                                .enableImagePicker(true)
                                .showFolderView(false)
                                .withOrientation(Orientation.UNSPECIFIED)
                                .pickPhoto(MainActivity.this);
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> arrayList) {
                        //Utils.showToast(this, getString(R.string.permission_denied));
                    }
                }).check();
    }

    private void cropImageResult(int resultCode, Intent data) {
        CropImage.ActivityResult result = CropImage.getActivityResult(data);
        if (resultCode == RESULT_OK) {
            Uri resultUri = result.getUri();
            ArrayList<String> cameraPic = new ArrayList<>();
            cameraPic.add(resultUri.getPath());
            new AsyncTaskRunner().execute(cameraPic);
        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            Exception error = result.getError();
            Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

        }
    }

    private void pickImageResult(Intent data) {
        photoPaths = new ArrayList<>();
        photoPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
        if (photoPaths.size() == SELECT_IMAGE_COUNT && (photoPaths.get(0).endsWith(".jpg") ||
                photoPaths.get(0).toString().endsWith(".jpeg") ||
                photoPaths.get(0).toString().endsWith(".png"))) {

            doCrop(Uri.fromFile(new File(photoPaths.get(0))));
        }
    }

    public void doCrop(Uri uri) {
        Intent intent = CropImage.activity(uri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setMinCropWindowSize(500, 350)
                .getIntent(this);

        startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    public void doCrop(Uri uri, int minWindowWidth, int minWindowHeight, int aspectRatioX,
                       int aspectRatioY) {
        Intent intent = CropImage.activity(uri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setMinCropWindowSize(minWindowWidth, minWindowHeight)
                .setAspectRatio(aspectRatioX, aspectRatioY)
                .getIntent(this);

        startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    public void disableViewsForSomeSeconds(View view) {
        view.setEnabled(false);
        view.setAlpha((float) 0.75);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
                view.setAlpha((float) 1.0);
            }
        }, AppConstants.SPLASH_DURATION);
    }

    public void IntrectionCall(int id, int type) {
        if (getPreferenceHelper().getLoginStatus()) {
            InteractionCar interactionCar = new InteractionCar();
            interactionCar.setCar_id(id);
            interactionCar.setType(type);
            if (showLoader()) {
                WebApiRequest.Instance(this).request(
                        AppConstants.WebServicesKeys.CAR_INTRECTION, null,
                        interactionCar,
                        null,
                        new WebApiRequest.WebServiceObjectResponse() {
                            @Override
                            public void onSuccess(ApiResponse apiResponse) {
                                hideLoader();
                            }

                            @Override
                            public void onError() {
                                hideLoader();
                            }
                        },
                        null);
            }
        }
    }

    public void callConsultant(String name, final String number) {
        CallConsultantDialog callConsultantDialog = CallConsultantDialog.newInstance(this, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                IntrectionCall(currentTradeCar.getId(), AppConstants.Interaction.PHONE);
            }
        });
        callConsultantDialog.setConsultantNameAndPhone(getResources().getString(R.string.would_you_like) + " " + name, number);
        callConsultantDialog.show(getFragmentManager(), null);
    }

    public ActivityMainBinding getBinding() {
        return binding;
    }

    public ArrayList<Category> getMainCategories() {
        return mainCategories;
    }

    public void setMainCategories(ArrayList<Category> mainCategories) {
        this.mainCategories = mainCategories;
    }

    public String getCurrency() {
        currency = getResources().getString(R.string.AED);
        if (getPreferenceHelper().getLoginStatus()) {
            if (getPreferenceHelper().getUser() != null) {
                if (getPreferenceHelper().getUser().getDetails() != null) {
                    if (getPreferenceHelper().getUser().getDetails().getUserRegionDetail() != null) {
                        if (getPreferenceHelper().getUser().getDetails().getUserRegionDetail().getCurrency() != null) {
                            currency = getPreferenceHelper().getUser().getDetails().getUserRegionDetail().getCurrency();
                        }
                    }
                }
            }
        }
        return currency;
    }

    private void redirect() {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            if (bundle.getSerializable(AppConstants.FcmHelper.FCM_DATA_PAYLOAD) != null) {
                FCMPayload fcmPayload = (FCMPayload) bundle.getSerializable(AppConstants.FcmHelper.FCM_DATA_PAYLOAD);
                if (fcmPayload != null) {
                    NotificationsFragment notificationsFragment = NotificationsFragment.Instance();
                    notificationsFragment.setFromNotification(true);
                    notificationsFragment.setActionID(fcmPayload.getAction_id());
                    notificationsFragment.setActionType(fcmPayload.getAction_type());
                    replaceFragment(notificationsFragment, NotificationsFragment.class.getSimpleName(), true, true);
                }
                getIntent().removeExtra(AppConstants.FcmHelper.FCM_DATA_PAYLOAD);
            }
        }
    }

    private void redirectDeepLinkIntent(Intent intent) {
        String appLinkAction = intent.getAction();
        Uri appLinkData = intent.getData();
        if (Intent.ACTION_VIEW.equals(appLinkAction) && appLinkData != null) {
//            appLinkData.toString().split("/")[appLinkData.toString().split("/").length - 1]

            if (appLinkData.toString().split("/").length > 0) {
                String slug = appLinkData.toString().split("/")[appLinkData.toString().split("/").length - 1];
                if (showLoader()) {
                    HashMap<String, Object> params = new HashMap<>();
                    params.put("slug", slug);
                    WebApiRequest.Instance(this).request(AppConstants.WebServicesKeys.GET_TYPE, null, null, params, new WebApiRequest.WebServiceObjectResponse() {
                        @Override
                        public void onSuccess(ApiResponse apiResponse) {
                            SlugType slugType = (SlugType) JsonHelpers.convertToModelClass(apiResponse.getData(), SlugType.class);
                            if (slugType != null) {
                                if (slugType.getType() == 20) {
                                    MainDetailPageFragment mainDetailPageFragment = new MainDetailPageFragment();
                                    mainDetailPageFragment.setCategoryId(slugType.getId());
                                    replaceFragment(mainDetailPageFragment, MainDetailPageFragment.class.getSimpleName(), true, true);
                                } else if (slugType.getType() == 10) {
                                    getCategoryDetail(slugType.getId());
                                }
                            }
                            hideLoader();
                        }

                        @Override
                        public void onError() {
                            hideLoader();
                        }
                    }, null);
                }
            }

            /*
            String type = appLinkData.getQueryParameter("type");
            String id = appLinkData.getQueryParameter("id");
            if (type != null) {
                if (type.equals("20")) {
                    MainDetailPageFragment mainDetailPageFragment = new MainDetailPageFragment();
                    mainDetailPageFragment.setCategoryId(Integer.parseInt(id));
                    replaceFragment(mainDetailPageFragment, MainDetailPageFragment.class.getSimpleName(), true, true);
                } else if (type.equals("10")) {
                    getCategoryDetail(Integer.parseInt(id));
                }
            }
            */
        }
    }

    private void getCategoryDetail(int id) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);
        WebApiRequest.Instance(this).request(AppConstants.WebServicesKeys.LUXURY_MARKET_CATEGORY_DETAIL, null, null, params, new WebApiRequest.WebServiceObjectResponse() {
            @Override
            public void onSuccess(ApiResponse apiResponse) {
                TradeCar currentTradeCar = (TradeCar) JsonHelpers.convertToModelClass(apiResponse.getData(), TradeCar.class);
                LuxuryMarketDetailsFragment luxuryMarketDetailsFragment = new LuxuryMarketDetailsFragment();
                luxuryMarketDetailsFragment.setCategoryKey(currentTradeCar.getCategory().getSlug());
                luxuryMarketDetailsFragment.setCurrentTradeCar(currentTradeCar);
                replaceFragment(luxuryMarketDetailsFragment, LuxuryMarketDetailsFragment.class.getSimpleName(), true, true);
                hideLoader();
            }

            @Override
            public void onError() {
                hideLoader();
            }
        }, null);
    }

    class AsyncTaskRunner extends AsyncTask<ArrayList<String>, ArrayList<File>, ArrayList<MyImageFile>> {
        @Override
        protected ArrayList<MyImageFile> doInBackground(ArrayList<String>... params) {
            ArrayList<MyImageFile> compressedAndVideoImageFileList = new ArrayList<>();
            for (int index = 0; index < params[0].size(); index++) {
                File file = new File(params[0].get(index));
                if (file.toString().endsWith(".jpg") ||
                        file.toString().endsWith(".jpeg") ||
                        file.toString().endsWith(".png") ||
                        file.toString().endsWith(".gif")) {
                    try {
                        File compressedImageFile = new Compressor(MainActivity.this).setQuality(90).compressToFile(file, "compressed_" + file.getName());
                        MyImageFile compressedMyImageFile = new MyImageFile(compressedImageFile, "");
                        compressedAndVideoImageFileList.add(compressedMyImageFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return compressedAndVideoImageFileList;
        }

        @Override
        protected void onPostExecute(ArrayList<MyImageFile> result) {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();
            if (result != null) {
                if (result.size() > 0) {
                    result.get(0).setImageType(imageType);
                    mediaPickerListener.onPhotoClicked(result);
                }
            }
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MainActivity.this,
                    MainActivity.this.getString(R.string.app_name),
                    MainActivity.this.getString(R.string.compressing_please_wait));
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
        }
    }
}
