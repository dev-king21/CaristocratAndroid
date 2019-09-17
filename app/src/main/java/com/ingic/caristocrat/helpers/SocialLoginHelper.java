package com.ingic.caristocrat.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.BaseActivity;
import com.ingic.caristocrat.activities.RegistrationActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 */
public class SocialLoginHelper {
    public CallbackManager callbackManager;
    private AccessToken accessToken;
    Context context;
    private static FbLoginInfoFetcher mlistener;
    private static GoogleLoginInfoFetcher mGooglelistener;
    private static SocialLoginHelper socialLoginHelper;
    private GoogleApiClient googleApiClient;
    public static final int FB_REQ_CODE = 64206;
    public static final int G_REQ_CODE = 103;
    private String WEB_CLIENT_ID = "715532030018-ll4qkpnovkkk3hba8vvr0vi3tjqj5mbk.apps.googleusercontent.com";


    public static SocialLoginHelper Instance() {
        if (socialLoginHelper == null)
            socialLoginHelper = new SocialLoginHelper();
        return socialLoginHelper;
    }

    //************************************************************FACEBOOK***************************************************************************//
    private void configureFacebook(Context context) {
        FacebookSdk.sdkInitialize(context);
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                fetchUserInfo(accessToken);
                Log.e("fb", accessToken.getUserId());
                disconnectSocialNetworks();

            }

            @Override
            public void onCancel() {
                Log.e("onCancel", "onCancel");

            }

            @Override
            public void onError(FacebookException e) {
                Log.e("error", e.toString());
            }

        });

        // If the access token is available already assign it.
        accessToken = AccessToken.getCurrentAccessToken();
    }

    public void loginWithFacebook(Activity activity) {
        context = activity;
        configureFacebook(activity);
        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile", "email"));
    }

    public void setSocialInfoFetcher(FbLoginInfoFetcher fbLoginInfoFetcher) {
        mlistener = fbLoginInfoFetcher;
    }

    private void fetchUserInfo(AccessToken accessToken) {
        if (accessToken != null) {
            GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {

                    // interface to pass json object

                    Log.e("JSON OBJECT:", jsonObject.toString());
                    try {
                        String id = jsonObject.getString("id");
                        String name = jsonObject.getString("first_name") + " " + jsonObject.getString("last_name");
                        String email = name.replaceAll(" ", "") + context.getResources().getString(R.string.caristocrat_email);
                        String picture = "https://graph.facebook.com/" + id + "/picture?type=large";
                        if (jsonObject.has("email")) {
                            email = jsonObject.getString("email");
                        }
                        mlistener.onFbInfoFetched(id, name, email, picture);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            //GraphRequest.executeBatchAsync(request);

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,first_name,last_name,email,hometown");
            request.setParameters(parameters);
            request.executeAsync();
        }
    }

    private void logoutFromFacebook() {
        LoginManager.getInstance().logOut();
    }

    private void disconnectSocialNetworks() {
        logoutFromFacebook();
    }

    public void getFaceBookonActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public interface FbLoginInfoFetcher {
        void onFbInfoFetched(String id, String name, String email, String picture);
    }

    //*************************************************Google*********************************************************************************//
    private void configureGoogle(RegistrationActivity context) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();

        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    public void loginWithGoogle(RegistrationActivity activity) {
        context = activity;
        configureGoogle(activity);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        activity.startActivityForResult(signInIntent, G_REQ_CODE);
    }

    public void getGoogleonActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == G_REQ_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d(TAG, "handleGoogleSignInResult:" + result.isSuccess());
            if (result.isSuccess()) {
                // Signed in successfully, show authenticated UI.
                GoogleSignInAccount acct = result.getSignInAccount();
                handleResult(acct);
            }
        }
    }

    public void setGoogleInfoFetcher(GoogleLoginInfoFetcher googleLoginInfoFetcher) {
        mGooglelistener = googleLoginInfoFetcher;
    }

    public interface GoogleLoginInfoFetcher {
        void onGoogleInfoFetched(String id, String name, String email, String pictureUrl, String accessToken, String profileUrl);
    }

    public void handleResult(GoogleSignInAccount account) {
        if (account != null) {
            String id = account.getId();
            String accesstoken = "";
            String pictureUrl = "";
            String email = "";
            String name = account.getDisplayName();
            if (account.getPhotoUrl() != null) {
                pictureUrl = account.getPhotoUrl().toString();
            }
            String profileUrl = "https://plus.google.com/profile_" + id;
            if (account.getServerAuthCode() != null) {
                accesstoken = account.getServerAuthCode();
            }
            if (account.getEmail() != null) {
                email = account.getEmail();
            }
            mGooglelistener.onGoogleInfoFetched(id, name, email, pictureUrl, accesstoken, profileUrl);

        }


    }
}
