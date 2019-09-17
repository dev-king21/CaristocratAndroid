package com.ingic.caristocrat.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.webhelpers.models.Category;
import com.ingic.caristocrat.webhelpers.models.User;

import java.util.ArrayList;

/**
 */
public class BasePreferenceHelper extends PreferenceHelper{
    private Context context;

    private static final String FILENAME = "preferences";
    public static final String AUTHENTICATE_USER_TOKEN = "user_token";
    public static final String KEY_USER_TYPE = "user_type";
    public static final String KEY_LOGIN_STATUS = "islogin";
    public static final String KEY_WALKTHROUGH = "isWalkthrough";
    protected static final String KEY_USER = "user";
    public static final String KEY_DEVICE_TOKEN = "device_token";
    public static final String KEY_CATEGORIES_LIST = "categories_list";
    public static final String KEY_SUB_CATEGORIES_LIST = "sub_categories_list";
    public static final String KEY_VIDEO = "video";
    public static final String KEY_REGION_SELECTED = "region_selected";
    public static final String KEY_ENABLE_NOTIFICATIONS= "enable_notifications";

    public BasePreferenceHelper(Context c) {
        this.context = c;
    }

    public SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(FILENAME, Activity.MODE_PRIVATE);
    }

    public void putUser(User user) {
        putStringPreference(context, FILENAME, KEY_USER, new GsonBuilder()
                .create().toJson(user));
    }

    public User getUser() {
        return new GsonBuilder().create().fromJson(
                getStringPreference(context, FILENAME, KEY_USER), User.class);
    }

    public void putUserToken(String token) {
        putStringPreference(context, FILENAME, AUTHENTICATE_USER_TOKEN, token);
    }

    public String getUserToken() {
        return getStringPreference(context, FILENAME, AUTHENTICATE_USER_TOKEN);
    }

    public void setLoginStatus(boolean isLogin) {
        putBooleanPreference(context, FILENAME, KEY_LOGIN_STATUS, isLogin);
    }

    public boolean getLoginStatus() {
        return getBooleanPreference(context, FILENAME, KEY_LOGIN_STATUS);
    }

    public void setWalkthrough(boolean isWalkthrough) {
        putBooleanPreference(context, FILENAME, KEY_WALKTHROUGH, isWalkthrough);
    }

    public boolean getWalkthrough() {
        return getBooleanPreference(context, FILENAME, KEY_WALKTHROUGH);
    }

    public void putDeviceToken(String token) {
        putStringPreference(context, FILENAME, KEY_DEVICE_TOKEN, token);
    }

    public String getDeviceToken() {
        return getStringPreference(context, FILENAME, KEY_DEVICE_TOKEN);
    }

    public void putLoginPreference(User user) {
        setLoginStatus(true);
        putUser(user);
    }

    public void removeLoginPreference() {

        setLoginStatus(false);
        removePreference(context, FILENAME, KEY_USER);
        removePreference(context, FILENAME, AUTHENTICATE_USER_TOKEN);
        removePreference(context, FILENAME, KEY_LOGIN_STATUS);
        removePreference(context,FILENAME,KEY_CATEGORIES_LIST);
        removePreference(context,FILENAME,KEY_SUB_CATEGORIES_LIST);
        removePreference(context,FILENAME,KEY_ENABLE_NOTIFICATIONS);
        removePreference(context,FILENAME,KEY_REGION_SELECTED);
        removePreference(context, FILENAME, AppConstants.FILTER_OPEN);

//        removePreference(context, FILENAME, AppConstants.UserType.USER_TYPE_KEY);
    }

    public void setStringPrefrence(String key, String value) {
        putStringPreference(context, FILENAME, key, value);
    }

    public String getStringPrefrence(String key) {
        return getStringPreference(context, FILENAME, key);
    }

    public void setIntegerPrefrence(String key, int value) {
        putIntegerPreference(context, FILENAME, key, value);
    }

    public int getIntegerPrefrence(String key) {
        return getIntegerPreference(context, FILENAME, key);
    }

    public void setBooleanPrefrence(String Key, boolean value) {
        putBooleanPreference(context, FILENAME, Key, value);
    }

    public boolean getBooleanPrefrence(String Key) {
        return getBooleanPreference(context, FILENAME, Key);
    }
    public void putCategoriesList(ArrayList<Category> value) {

        putStringPreference(context, FILENAME, KEY_CATEGORIES_LIST,
                new Gson().toJson(value, new TypeToken<ArrayList<Category>>() {
                }.getType()));
    }

    public ArrayList<Category> getCategoriesList() {
        ArrayList<Category> list = new GsonBuilder().create().fromJson(
                getStringPreference(context, FILENAME, KEY_CATEGORIES_LIST),
                new TypeToken<ArrayList<Category>>() {
                }.getType());

        return list == null ? new ArrayList<Category>() : list;
    }

    public void putSubCategoriesList(ArrayList<Category> value) {

        putStringPreference(context, FILENAME, KEY_SUB_CATEGORIES_LIST,
                new Gson().toJson(value, new TypeToken<ArrayList<Category>>() {
                }.getType()));
    }

    public ArrayList<Category> getSubCategoriesList() {
        ArrayList<Category> list = new GsonBuilder().create().fromJson(
                getStringPreference(context, FILENAME, KEY_SUB_CATEGORIES_LIST),
                new TypeToken<ArrayList<Category>>() {
                }.getType());

        return list == null ? new ArrayList<Category>() : list;
    }
    public void setAppLanguage(String language) {
//        putStringPreference(context, FILENAME, AppConstants.AppLanguage.KEY_APP_LANGUAGE, language);
    }

    public String getAppLanguage() {
//        return getStringPrefrence(AppConstants.AppLanguage.KEY_APP_LANGUAGE);
        return "";
    }
}
