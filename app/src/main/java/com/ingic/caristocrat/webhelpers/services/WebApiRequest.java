package com.ingic.caristocrat.webhelpers.services;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.ingic.caristocrat.activities.BaseActivity;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.activities.RegistrationActivity;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.helpers.BasePreferenceHelper;
import com.ingic.caristocrat.helpers.JsonHelpers;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.models.RatingPost;
import com.ingic.caristocrat.models.ReportListing;
import com.ingic.caristocrat.webhelpers.models.ApiArrayResponse;
import com.ingic.caristocrat.webhelpers.models.ApiResponse;
import com.ingic.caristocrat.webhelpers.models.ErrorModelWrapper;
import com.ingic.caristocrat.webhelpers.models.InteractionCar;
import com.ingic.caristocrat.webhelpers.models.PostRegion;
import com.ingic.caristocrat.webhelpers.models.TradeInCar;
import com.ingic.caristocrat.webhelpers.models.UserRegister;
import com.ingic.caristocrat.webhelpers.models.UserSignin;
import com.ingic.caristocrat.webhelpers.models.WebRequestData;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 */
public class WebApiRequest {
    private Webservice webservice;
    private BaseActivity activityContext;
    public static WebApiRequest Instance;
    private BasePreferenceHelper preferenceHelper;
    private View mainFrameLayout;
    private Call<ApiResponse> callObject = null;
    private Call<ApiArrayResponse> callArray = null;

    private WebApiRequest(BaseActivity activityContext) {
        this.activityContext = activityContext;
        this.webservice = WebServiceFactory.Instance(activityContext);
        this.preferenceHelper = new BasePreferenceHelper(activityContext);
        if (activityContext instanceof MainActivity || activityContext instanceof RegistrationActivity) {
            mainFrameLayout = activityContext.getMainFrameLayout();
        }
    }

    public static WebApiRequest Instance(BaseActivity activityContext) {
        if (Instance == null)
            Instance = new WebApiRequest(activityContext);
        return Instance;
    }

    public void request(int WebApiMethodKey, final View view, WebRequestData webRequest, Map<String, Object> params, final WebServiceObjectResponse webServiceObjectResponse, final WebServiceArrayResponse webServiceArrayResponse) {
        switch (WebApiMethodKey) {
            case AppConstants.WebServicesKeys.REGISTER:
                callObject = webservice.UserRegister((UserRegister) webRequest);
                break;

            case AppConstants.WebServicesKeys.LOGIN:
                callObject = webservice.UserSignin((UserSignin) webRequest);
                break;

            case AppConstants.WebServicesKeys.REFRESH:
                callObject = webservice.RefreshToken();
                break;

            case AppConstants.WebServicesKeys.FORGET_PASSWORD:
                callObject = webservice.ForgetPassword(params);
                break;

            case AppConstants.WebServicesKeys.CATEGORIES:
                callArray = webservice.Categories(params);
                break;

            case AppConstants.WebServicesKeys.VERIFY_RESET_CODE:
                callObject = webservice.VerifyResetCode(params);
                break;

            case AppConstants.WebServicesKeys.RESET_PASSWORD:
                callObject = webservice.ResetPassword(params);
                break;

            case AppConstants.WebServicesKeys.NEWS:
                callArray = webservice.News(params);
                break;

            case AppConstants.WebServicesKeys.NEWS_V2:
                callArray = webservice.NewsV2(params);
                break;

            case AppConstants.WebServicesKeys.NEWS_DETAIL:
                callObject = webservice.NewsDetail(params.get("id"));
                break;

            case AppConstants.WebServicesKeys.LOGOUT:
                callObject = webservice.Logout();
                break;

            case AppConstants.WebServicesKeys.NEWS_INTERACTIONS:
                callObject = webservice.NewsInteractions(params);
                break;

            case AppConstants.WebServicesKeys.GET_COMMENTS:
                callArray = webservice.GetComments(params);
                break;

            case AppConstants.WebServicesKeys.POST_COMMENTS:
                callObject = webservice.PostComments(params);
                break;

            case AppConstants.WebServicesKeys.PROFILE:
                callObject = webservice.Profile();
                break;

            case AppConstants.WebServicesKeys.CHANGE_PASSWORD:
                callObject = webservice.ChangePassword(params);
                break;

            case AppConstants.WebServicesKeys.UPDATE_PROFILE:
                MultipartBody.Part partimage = (MultipartBody.Part) params.get("image");
                params.remove("image");
                callObject = webservice.UpdateProfile(params, partimage, "");
                break;

            case AppConstants.WebServicesKeys.SOCIAL_LOGIN:
//               SocialLogin socialLogin = (SocialLogin) webRequest;
                MultipartBody.Part socialImage = (MultipartBody.Part) params.get("image");
                params.remove("image");
                callObject = webservice.SocialLogin(params, socialImage, "");
                break;

            case AppConstants.WebServicesKeys.FAVORITE_NEWS:
                callArray = webservice.GetFavoriteNews(params);
                break;

            case AppConstants.WebServicesKeys.MAKE:
                callArray = webservice.Make();
                break;

            case AppConstants.WebServicesKeys.MODEL:
                callArray = webservice.Model(params);
                break;

            case AppConstants.WebServicesKeys.REGIONAL_SPECS:
                callArray = webservice.RegionSpecs();
                break;

            case AppConstants.WebServicesKeys.ATTRIBUTES:
                callObject = webservice.Attributes(params.get("id"));
                break;

            case AppConstants.WebServicesKeys.REQUEST_CONSULTANCY_DETAIL:
                callArray = webservice.GetRequestConsultancy();
                break;

            case AppConstants.WebServicesKeys.POST_TRADE_CAR:
                ArrayList<MultipartBody.Part> mediaFiles = (ArrayList<MultipartBody.Part>) params.get("media[]");
                HashMap<String, RequestBody> data = new HashMap<>();
                data.put("name", getStringRequestBody((String) params.get("name")));
                data.put("email", getStringRequestBody((String) params.get("email")));
                data.put("country_code", getStringRequestBody((String) params.get("country_code")));
                data.put("phone", getStringRequestBody((String) params.get("phone")));
                data.put("chassis", getStringRequestBody((String) params.get("chassis")));
                data.put("kilometer", getStringRequestBody((String) params.get("kilometer")));
                data.put("model_id", getStringRequestBody((String) params.get("model_id")));
                data.put("year", getStringRequestBody((String) params.get("year")));
                data.put("regional_specification_id", getStringRequestBody((String) params.get("regional_specification_id")));
                data.put("engine_type_id", getStringRequestBody((String) params.get("engine_type_id")));
                data.put("car_attributes", getStringRequestBody((String) params.get("car_attributes")));
                data.put("notes", getStringRequestBody((String) params.get("notes")));
                data.put("version_app", getStringRequestBody((String) params.get("version_app")));
//                data.put("car_features", getStringRequestBody((String) params.get("car_features")));
                callObject = webservice.PostTradeCar(data, mediaFiles);
                break;

            case AppConstants.WebServicesKeys.PUT_MY_CARS:
                ArrayList<MultipartBody.Part> newmediaFiles = (ArrayList<MultipartBody.Part>) params.get("media[]");
                HashMap<String, RequestBody> newdata = new HashMap<>();
                newdata.put("name", getStringRequestBody((String) params.get("name")));
                newdata.put("email", getStringRequestBody((String) params.get("email")));
                newdata.put("country_code", getStringRequestBody((String) params.get("country_code")));
                newdata.put("phone", getStringRequestBody((String) params.get("phone")));
                newdata.put("chassis", getStringRequestBody((String) params.get("chassis")));
                newdata.put("kilometer", getStringRequestBody((String) params.get("kilometer")));
                newdata.put("model_id", getStringRequestBody((String) params.get("model_id")));
                newdata.put("year", getStringRequestBody((String) params.get("year")));
                newdata.put("regional_specification_id", getStringRequestBody((String) params.get("regional_specification_id")));
                newdata.put("engine_type_id", getStringRequestBody((String) params.get("engine_type_id")));
                newdata.put("car_attributes", getStringRequestBody((String) params.get("car_attributes")));
                newdata.put("notes", getStringRequestBody((String) params.get("notes")));
//                newdata.put("car_features", getStringRequestBody((String) params.get("car_features")));
                newdata.put("deleted_images", getStringRequestBody((String) params.get("deleted_images")));
                newdata.put("_method", getStringRequestBody((String) params.get("_method")));
                newdata.put("version_app", getStringRequestBody((String) params.get("version_app")));
                callObject = webservice.PutTradeCar(params.get("id"), newdata, newmediaFiles);
                break;

            case AppConstants.WebServicesKeys.MY_TRADE_INS:
                callArray = webservice.MyTradeIns(params);
                break;

            case AppConstants.WebServicesKeys.GET_TRADE_IN_CAR:
                callArray = webservice.GetTradedInCars(params);
                break;

            case AppConstants.WebServicesKeys.GET_CAR_BODY_TYPES:
                callArray = webservice.GetCarBodyTypes(params);
                break;

            case AppConstants.WebServicesKeys.MY_TRADE_INS_DETAIL:
                callObject = webservice.MyTradeInsDetail(params.get("id"));
                break;

            case AppConstants.WebServicesKeys.LUXURY_MARKET_CATEGORIES:
                callArray = webservice.LuxuryMarketCategories(params);
                break;

            case AppConstants.WebServicesKeys.GET_CARS:
                callArray = webservice.GetCars(params);
                break;

            case AppConstants.WebServicesKeys.LUXURY_MARKET_CATEGORY_DETAIL:
                callObject = webservice.LuxuryMarketCategoryDetail(params.get("id"));
                break;

            case AppConstants.WebServicesKeys.REGIONS:
                callArray = webservice.Regions();
                break;

            case AppConstants.WebServicesKeys.POST_REGIONS:
                callObject = webservice.PostRegions((PostRegion) webRequest);
                break;

            case AppConstants.WebServicesKeys.GET_CAR_BRANDS:
                callArray = webservice.GetCarBrands();
                break;

            case AppConstants.WebServicesKeys.CAR_INTERACTIONS:
                callObject = webservice.CarInteractions(params);
                break;

            case AppConstants.WebServicesKeys.REPORT_REQUESTS:
                callObject = webservice.ReportRequests((ReportListing) webRequest);
                break;

            case AppConstants.WebServicesKeys.REQUEST_CONSULTANCY:
                callObject = webservice.RequestConsultancy(params);
                break;

            case AppConstants.WebServicesKeys.FAVORITE_CARS:
                callArray = webservice.FavoritesCars(params);
                break;

            case AppConstants.WebServicesKeys.GET_NOTIFICATIONS:
                callArray = webservice.GetNotifications();
                break;

            case AppConstants.WebServicesKeys.UPDATE_PUSH_NOTIFICATIION:
                callObject = webservice.UpdatePushNotification(params);
                break;

            case AppConstants.WebServicesKeys.POST_TRADE_IN_CAR:
                callObject = webservice.TradeInCar((TradeInCar) webRequest);
                break;

            case AppConstants.WebServicesKeys.CAR_INTRECTION:
                callObject = webservice.carIntrection((InteractionCar) webRequest);
                break;

            case AppConstants.WebServicesKeys.BANKS_RATES:
                callArray = webservice.GetBankRates();
                break;

            case AppConstants.WebServicesKeys.ENGINE_TYPES:
                callArray = webservice.GetEngineTypes();
                break;

            case AppConstants.WebServicesKeys.GET_TRADED_IN_CAR:
                callObject = webservice.GetTradedInCar(params.get("id"));
                break;

            case AppConstants.WebServicesKeys.EDIT_COMMENT:
                callObject = webservice.EditComment(params.get("id"), params);
                break;

            case AppConstants.WebServicesKeys.DELETE_COMMENT:
                callObject = webservice.DeleteComment(params.get("id"));
                break;

            case AppConstants.WebServicesKeys.GET_REVIEW_ASPECTS:
                callArray = webservice.GetReviewAspects();
                break;

            case AppConstants.WebServicesKeys.POST_REVIEWS:
                callObject = webservice.PostReview((RatingPost) webRequest);
                break;

            case AppConstants.WebServicesKeys.GET_REVIEWS:
                callArray = webservice.GetReviews(params);
                break;

            case AppConstants.WebServicesKeys.WALKTHROUGH_DETAIIL:
                callArray = webservice.GetWalkthroughDetail();
                break;

            case AppConstants.WebServicesKeys.NOTIFICATION_DELETE:
                callObject = webservice.DeleteNotification(params.get("id"));
                break;

            case AppConstants.WebServicesKeys.CAR_VERSIONS:
                callArray = webservice.CarVersions(params);
                break;

            case AppConstants.WebServicesKeys.PAGES:
                callArray = webservice.GetPages();
                break;

            case AppConstants.WebServicesKeys.COMMENT_EDIT:
                callObject = webservice.CommentEdit(params.get("id"), params);
                break;

            case AppConstants.WebServicesKeys.COMMENT_DELETE:
                callObject = webservice.CommentDelete(params.get("id"));
                break;

            case AppConstants.WebServicesKeys.GET_TYPE:
                callObject = webservice.GetType(params.get("slug"));
                break;

            case AppConstants.WebServicesKeys.CHECK_REPORT_PAYMENT:
                callObject = webservice.CheckReportPayment(params);
                break;

            case AppConstants.WebServicesKeys.ONE_REPORT_PAYMENT:
                callObject = webservice.OneReportPayment(params);
                break;

            case AppConstants.WebServicesKeys.ALL_REPORT_PAYMENT:
                callObject = webservice.AllReportPayment(params);
                break;
            case AppConstants.WebServicesKeys.PRO_COMPARISION_SUBS:
                callObject = webservice.ProComparisionSubs(params);
                break;

            case AppConstants.WebServicesKeys.GET_ALL_SUBSCRIPTION:
                callArray = webservice.GetAllSubscription(params.get("id"));
                break;

        }
        if (view != null) {
            view.setVisibility(View.GONE);
        }
        if (webServiceObjectResponse != null) {
            callObject.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (view != null) {
                        view.setVisibility(View.VISIBLE);
                    }
                    if (!onResponseHandler(response, null)) {
                        if (response.errorBody() != null) {
                            //SPECIAL HANDLING FOR LOGIN KEY
                            if (response.code() == AppConstants.HttpStatusCodes.UNVERIFIED_SIGNIN && WebApiMethodKey == AppConstants.WebServicesKeys.LOGIN) {
                                ApiResponse apiResponse = new ApiResponse();
                                apiResponse.setSuccess(false);
                                webServiceObjectResponse.onSuccess(apiResponse);
                            } else {
                                showErrorOnToast(response.errorBody());
                                webServiceObjectResponse.onError();
                            }
                        }
                    } else {
                        webServiceObjectResponse.onSuccess(response.body());
                        //SPECIAL HANDLING FOR LOGIN KEY
                        /*
                        if(WebApiMethodKey == AppConstants.WebServicesKeys.LOGIN || WebApiMethodKey == AppConstants.WebServicesKeys.REGISTER){
                            ApiResponse apiResponse = new ApiResponse();
                            apiResponse.setSuccess(false);
                            webServiceObjectResponse.onSuccess(apiResponse);
                        }
                        */
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    if (view != null) {
                        view.setVisibility(View.VISIBLE);
                    }
                    t.printStackTrace();
                    webServiceObjectResponse.onError();
                }
            });
        } else if (webServiceArrayResponse != null) {
            callArray.enqueue(new Callback<ApiArrayResponse>() {
                @Override
                public void onResponse(Call<ApiArrayResponse> call, Response<ApiArrayResponse> response) {
                    if (view != null) {
                        view.setVisibility(View.VISIBLE);
                    }
                    if (!onResponseHandler(null, response)) {
                        webServiceArrayResponse.onError();
                    } else {
                        webServiceArrayResponse.onSuccess(response.body());
                    }
                }

                @Override
                public void onFailure(Call<ApiArrayResponse> call, Throwable t) {
                    if (view != null) {
                        view.setVisibility(View.VISIBLE);
                    }
                    t.printStackTrace();
                    webServiceArrayResponse.onError();
                }
            });
        }
    }

    private boolean onResponseHandler(Response<ApiResponse> apiResponse, Response<ApiArrayResponse> apiArrayResponse) {
        boolean status = false;
        if (apiResponse != null) {
            status = false;
            switch (apiResponse.code()) {
                case AppConstants.HttpStatusCodes.UNAUTHORIZED:
//                    showErrorOnToast(apiArrayResponse.errorBody());
                    preferenceHelper.removeLoginPreference();
                    activityContext.startActivityFinishAffinity(RegistrationActivity.class);
                    break;

                case AppConstants.HttpStatusCodes.FORBIDDEN:
                    status = false;
                    break;

                case AppConstants.HttpStatusCodes.UNVERIFIED_SIGNIN:
                    status = false;
                    break;

                case AppConstants.HttpStatusCodes.OK:
                    if (apiResponse.body() != null) {
                        status = true;
                    }
                    break;

                default:
                    if (apiResponse.errorBody() != null) {
                        status = false;
                        showErrorOnSnackbar(apiResponse.errorBody());
                    }
                    break;
            }
        } else if (apiArrayResponse != null) {
            status = false;
            switch (apiArrayResponse.code()) {

                case AppConstants.HttpStatusCodes.UNAUTHORIZED:
//                    showErrorOnToast(apiArrayResponse.errorBody());
                    preferenceHelper.removeLoginPreference();
                    activityContext.startActivity(RegistrationActivity.class, true);
                    break;

                case AppConstants.HttpStatusCodes.OK:
                    if (apiArrayResponse.body() != null) {
                        status = true;
                    }
                    break;

                default:
                    if (apiArrayResponse.errorBody() != null) {
                        status = false;
                        showErrorOnSnackbar(apiArrayResponse.errorBody());
                    }
                    break;
            }
        }
        return status;
    }

    private void showErrorOnSnackbar(ResponseBody errorBody) {
        try {
            JSONObject error = new JSONObject(errorBody.string());
            UIHelper.showSnackbar(mainFrameLayout, error.getString("message"), Snackbar.LENGTH_LONG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showErrorOnToast(ResponseBody errorBody) {
        try {
            JSONObject error = new JSONObject(errorBody.string());
            UIHelper.showToast(activityContext, error.getString("message"), Toast.LENGTH_LONG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public RequestBody getStringRequestBody(String value) {
        return RequestBody.create(MediaType.parse("text/plain"), value);
    }

    public interface WebServiceObjectResponse {
        void onSuccess(ApiResponse apiResponse);

        void onError();
    }

    public interface WebServiceArrayResponse {
        void onSuccess(ApiArrayResponse apiArrayResponse);

        void onError();
    }

    public Call<ApiResponse> getCallObject() {
        return callObject;
    }

    public void setCallObject(Call<ApiResponse> callObject) {
        this.callObject = callObject;
    }

    public Call<ApiArrayResponse> getCallArray() {
        return callArray;
    }

    public void setCallArray(Call<ApiArrayResponse> callArray) {
        this.callArray = callArray;
    }
}
