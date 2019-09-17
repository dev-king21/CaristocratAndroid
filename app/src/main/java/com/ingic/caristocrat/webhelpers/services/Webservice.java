package com.ingic.caristocrat.webhelpers.services;

import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.models.RatingPost;
import com.ingic.caristocrat.models.ReportListing;
import com.ingic.caristocrat.webhelpers.models.ApiArrayResponse;
import com.ingic.caristocrat.webhelpers.models.ApiResponse;
import com.ingic.caristocrat.webhelpers.models.InteractionCar;
import com.ingic.caristocrat.webhelpers.models.PostRegion;
import com.ingic.caristocrat.webhelpers.models.TradeInCar;
import com.ingic.caristocrat.webhelpers.models.UserRegister;
import com.ingic.caristocrat.webhelpers.models.UserSignin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 */
public interface Webservice {

    @POST(AppConstants.WebServices.REGISTER)
    Call<ApiResponse> UserRegister(
            @Body UserRegister post
    );

    @POST(AppConstants.WebServices.LOGIN)
    Call<ApiResponse> UserSignin(
            @Body UserSignin post
    );

    @POST(AppConstants.WebServices.REFRESH)
    Call<ApiResponse> RefreshToken();

    @GET(AppConstants.WebServices.FORGET_PASSWORD)
    Call<ApiResponse> ForgetPassword(
            @QueryMap Map<String, Object> params
    );

    @GET(AppConstants.WebServices.CATEGORIES)
    Call<ApiArrayResponse> Categories(
            @QueryMap Map<String, Object> params
    );

    @POST(AppConstants.WebServices.VERIFY_RESET_CODE)
    Call<ApiResponse> VerifyResetCode(
            @QueryMap Map<String, Object> params
    );

    @POST(AppConstants.WebServices.RESET_PASSWORD)
    Call<ApiResponse> ResetPassword(
            @QueryMap Map<String, Object> params
    );

    @GET(AppConstants.WebServices.NEWS)
    Call<ApiArrayResponse> News(
            @QueryMap Map<String, Object> params
    );

    @GET(AppConstants.WebServices.NEWS_V2)
    Call<ApiArrayResponse> NewsV2(
            @QueryMap Map<String, Object> params
    );

    @GET(AppConstants.WebServices.NEWS_DETAIL)
    Call<ApiResponse> NewsDetail(
            @Path("id") Object id
    );

    @PUT(AppConstants.WebServices.EDIT_COMMENT)
    Call<ApiResponse> EditComment(
            @Path("id") Object id,
            @QueryMap Map<String, Object> params
    );

    @DELETE(AppConstants.WebServices.EDIT_COMMENT)
    Call<ApiResponse> DeleteComment(
            @Path("id") Object id
    );

    @POST(AppConstants.WebServices.COMMENT_EDIT)
    Call<ApiResponse> CommentEdit(
            @Path("id") Object id,
            @QueryMap Map<String, Object> params
    );

    @POST(AppConstants.WebServices.COMMENT_DELETE)
    Call<ApiResponse> CommentDelete(
            @Path("id") Object id
    );

    @POST(AppConstants.WebServices.LOGOUT)
    Call<ApiResponse> Logout();

    @POST(AppConstants.WebServices.NEWS_INTERACTIONS)
    Call<ApiResponse> NewsInteractions(
            @QueryMap Map<String, Object> params
    );

    @GET(AppConstants.WebServices.GET_COMMENTS)
    Call<ApiArrayResponse> GetComments(
            @QueryMap Map<String, Object> params
    );

    @POST(AppConstants.WebServices.POST_COMMENTS)
    Call<ApiResponse> PostComments(
            @QueryMap Map<String, Object> params
    );

    @POST(AppConstants.WebServices.PROFILE)
    Call<ApiResponse> Profile();

    @POST(AppConstants.WebServices.CHANGE_PASSWORD)
    Call<ApiResponse> ChangePassword(
            @QueryMap Map<String, Object> params
    );

    @Multipart
    @POST(AppConstants.WebServices.UPDATE_PROFILE)
    Call<ApiResponse> UpdateProfile(
            @QueryMap Map<String, Object> params,
            @Part MultipartBody.Part image,
            @Part("dummy") String dummy
    );

    @Multipart
    @POST(AppConstants.WebServices.SOCIAL_LOGIN)
    Call<ApiResponse> SocialLogin(
            @QueryMap Map<String, Object> params,
            @Part MultipartBody.Part image,
            @Part("dummy") String dummy
    );

    @GET(AppConstants.WebServices.FAVORITE_NEWS)
    Call<ApiArrayResponse> GetFavoriteNews(
            @QueryMap Map<String, Object> params
    );

    @GET(AppConstants.WebServices.MAKE)
    Call<ApiArrayResponse> Make(
    );

    @GET(AppConstants.WebServices.MODEL)
    Call<ApiArrayResponse> Model(
            @QueryMap Map<String, Object> params
    );

    @GET(AppConstants.WebServices.REGIONAL_SPECS)
    Call<ApiArrayResponse> RegionSpecs(
    );

    @GET(AppConstants.WebServices.ATTRIBUTES)
    Call<ApiResponse> Attributes(
            @Path("id") Object id
    );

    @Multipart
    @POST(AppConstants.WebServices.POST_TRADE_CAR)
    Call<ApiResponse> PostTradeCar(
            @PartMap HashMap<String, RequestBody> data,
            @Part ArrayList<MultipartBody.Part> media
    );

    @Multipart
    @POST(AppConstants.WebServices.PUT_MY_CARS)
    Call<ApiResponse> PutTradeCar(
            @Path("id") Object id,
            @PartMap HashMap<String, RequestBody> data,
            @Part ArrayList<MultipartBody.Part> media
    );

    @GET(AppConstants.WebServices.GET_CAR_BODY_TYPES)
    Call<ApiArrayResponse> GetCarBodyTypes(@QueryMap Map<String, Object> params);

    @GET(AppConstants.WebServices.MY_TRADE_INS)
    Call<ApiArrayResponse> MyTradeIns(
            @QueryMap Map<String, Object> params
    );

    @GET(AppConstants.WebServices.MY_TRADE_INS_DETAIL)
    Call<ApiResponse> MyTradeInsDetail(
            @Path("id") Object id
    );

    @GET(AppConstants.WebServices.GET_TRADED_IN_CAR)
    Call<ApiResponse> GetTradedInCar(
            @Path("id") Object id
    );

    @GET(AppConstants.WebServices.LUXURY_MARKET_CATEGORIES)
    Call<ApiArrayResponse> LuxuryMarketCategories(
            @QueryMap Map<String, Object> params
    );

    @GET(AppConstants.WebServices.GET_CARS)
    Call<ApiArrayResponse> GetCars(
            @QueryMap Map<String, Object> params
    );

    @GET(AppConstants.WebServices.LUXURY_MARKET_CATEGORY_DETAIL)
    Call<ApiResponse> LuxuryMarketCategoryDetail(
            @Path("id") Object id
    );

    @GET(AppConstants.WebServices.REGIONS)
    Call<ApiArrayResponse> Regions();

    @POST(AppConstants.WebServices.POST_REGIONS)
    Call<ApiResponse> PostRegions(
            @Body PostRegion postRegion);

    @GET(AppConstants.WebServices.GET_CAR_BRANDS)
    Call<ApiArrayResponse> GetCarBrands();

    @POST(AppConstants.WebServices.CAR_INTERACTIONS)
    Call<ApiResponse> CarInteractions(
            @QueryMap Map<String, Object> params
    );

    @POST(AppConstants.WebServices.REPORT_REQUESTS)
    Call<ApiResponse> ReportRequests(
            @Body ReportListing reportListing
    );

    @POST(AppConstants.WebServices.REQUEST_CONSULTANCY)
    Call<ApiResponse> RequestConsultancy(
            @QueryMap Map<String, Object> params
    );

    @GET(AppConstants.WebServices.FAVORITE_CARS)
    Call<ApiArrayResponse> FavoritesCars(
            @QueryMap Map<String, Object> params
    );

    @GET(AppConstants.WebServices.GET_NOTIFICATIONS)
    Call<ApiArrayResponse> GetNotifications();

    @POST(AppConstants.WebServices.UPDATE_PUSH_NOTIFICATIION)
    Call<ApiResponse> UpdatePushNotification(
            @QueryMap Map<String, Object> params
    );

    @POST(AppConstants.WebServices.CAR_INTRECTION)
    Call<ApiResponse> carIntrection(
            @Body InteractionCar params
    );

    @POST(AppConstants.WebServices.POST_TRADE_IN_CAR)
    Call<ApiResponse> TradeInCar(
            @Body TradeInCar params
    );

    @GET(AppConstants.WebServices.GET_TRADE_IN_CAR)
    Call<ApiArrayResponse> GetTradedInCars(
            @QueryMap Map<String, Object> params
    );

    @GET(AppConstants.WebServices.BANKS_RATES)
    Call<ApiArrayResponse> GetBankRates();

    @GET(AppConstants.WebServices.ENGINE_TYPES)
    Call<ApiArrayResponse> GetEngineTypes();

    @GET(AppConstants.WebServices.GET_REVIEW_ASPECTS)
    Call<ApiArrayResponse> GetReviewAspects();

    @POST(AppConstants.WebServices.REVIEWS)
    Call<ApiResponse> PostReview(
            @Body RatingPost params
    );

    @GET(AppConstants.WebServices.REVIEWS)
    Call<ApiArrayResponse> GetReviews(
            @QueryMap Map<String, Object> params
    );

    @GET(AppConstants.WebServices.REQUEST_CONSULTANCY_DETAIL)
    Call<ApiArrayResponse> GetRequestConsultancy();

    @GET(AppConstants.WebServices.WALKTHROUGH_DETAIIL)
    Call<ApiArrayResponse> GetWalkthroughDetail();

    @DELETE(AppConstants.WebServices.NOTIFICATION_DELETE)
    Call<ApiResponse> DeleteNotification(
            @Path("id") Object id
    );

    @GET(AppConstants.WebServices.CAR_VERSIONS)
    Call<ApiArrayResponse> CarVersions(
            @QueryMap Map<String, Object> params
    );

    @GET(AppConstants.WebServices.PAGES)
    Call<ApiArrayResponse> GetPages();

    @GET(AppConstants.WebServices.GET_TYPE)
    Call<ApiResponse> GetType(
            @Path("slug") Object slug
    );
    @GET(AppConstants.WebServices.CHECK_REPORT_PAYMENT)
    Call<ApiResponse> CheckReportPayment(
            @QueryMap Map<String, Object> params
    );
    @GET(AppConstants.WebServices.ONE_REPORT_PAYMENT)
    Call<ApiResponse> OneReportPayment(
            @QueryMap Map<String, Object> params
    );
    @GET(AppConstants.WebServices.ALL_REPORT_PAYMENT)
    Call<ApiResponse> AllReportPayment(
            @QueryMap Map<String, Object> params
    );
    @GET(AppConstants.WebServices.PRO_COMPARISION_SUBS)
    Call<ApiResponse> ProComparisionSubs(
            @QueryMap Map<String, Object> params
    );
    @GET(AppConstants.WebServices.GET_ALL_SUBSCRIPTION)
    Call<ApiArrayResponse> GetAllSubscription(
            @Path("id") Object id
    );

}
