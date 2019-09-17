package com.ingic.caristocrat.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.multidex.MultiDex;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.ingic.caristocrat.R;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.fragments.BaseFragment;
import com.ingic.caristocrat.helpers.BasePreferenceHelper;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.helpers.Utils;
import com.ingic.caristocrat.interfaces.SimpleDialogActionListener;

import java.util.Locale;

/**
 */
public abstract class BaseActivity extends AppCompatActivity {
    BaseFragment baseFragment;
    private int mainFrameLayoutID, coordinatorLayoutID, appBarID, collapsingToolBarID, toolbarID;
    private View mainFrameLayout, coordinatorLayout, appbarLayout, collapsingToolBarLayout, toolbarLayout, ivSubCategoryItem, rvSubCategoryItem;
    private BasePreferenceHelper preferenceHelper;
    public com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer initYoutube = null;
    public static  String newsId = "";
    public static Boolean oneReport = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        MultiDex.install(this);
        this.preferenceHelper = new BasePreferenceHelper(this);
//        Fabric.with(this, new Crashlytics());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        preferenceHelper.setBooleanPrefrence(AppConstants.FILTER_OPEN, false);
    }

    @Override
    public void onBackPressed() {
//        if (!internetConnected()) {
//            closeApp();
//        } else {
        UIHelper.hideSoftKeyboard(this);
        if (preferenceHelper.getBooleanPrefrence(AppConstants.FILTER_OPEN)) {
            switch (preferenceHelper.getIntegerPrefrence(AppConstants.FILTER_OPENED_SCREEN)) {
                case AppConstants.FILTER_OPENED_FILTER:
                    UIHelper.showToast(this, this.getResources().getString(R.string.close_apply), Toast.LENGTH_SHORT);
                    break;
                case AppConstants.FILTER_OPENED_BRANDS:
                    UIHelper.showToast(this, this.getResources().getString(R.string.back_close), Toast.LENGTH_SHORT);
                    break;
                case AppConstants.FILTER_OPENED_MODELS:
                    UIHelper.showToast(this, this.getResources().getString(R.string.back_select), Toast.LENGTH_SHORT);
                    break;
            }
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 1)
                getSupportFragmentManager().popBackStack();
            else {
                closeApp();
            }
        }
//        }
    }

    public <T> void startActivity(Class<T> cls, boolean isActivityFinish) {
        Intent intent = new Intent(this, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        startActivity(intent);
        if (isActivityFinish) {
            finish();
        }
    }

    public <T> void startActivityFinishAffinity(Class<T> cls) {
        Intent intent = new Intent(this, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finishAffinity();
    }

    public void addFragment(BaseFragment frag, String tag, boolean isAddToBackStack, boolean animate) {
        baseFragment = frag;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (animate) {
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        } else {
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        }
        transaction.add(mainFrameLayoutID, frag, tag);

        if (isAddToBackStack) {
            transaction.addToBackStack(null).commit();
        } else {
            transaction.commit();
        }
    }

    public void replaceFragment(BaseFragment frag, String tag, boolean isAddToBackStack, boolean animate) {
        baseFragment = frag;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (animate) {
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        }
        transaction.replace(mainFrameLayoutID, frag, tag);

        if (isAddToBackStack) {
            transaction.addToBackStack(null).commit();
        } else {
            transaction.commit();
        }
    }

    public void replaceFragment(BaseFragment frag, String tag, boolean isAddToBackStack, boolean animate, int frame_ID) {
        baseFragment = frag;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (animate) {
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        }
        transaction.replace(frame_ID, frag, tag);

        if (isAddToBackStack) {
            transaction.addToBackStack(null).commit();
        } else {
            transaction.commit();
        }
    }

    public void replaceFragmentWithClearBackStack(BaseFragment frag, String tag, boolean isAddToBackStack, boolean animate) {
        clearBackStack();
        baseFragment = frag;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (animate) {
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        }
        transaction.replace(mainFrameLayoutID, frag, tag);

        if (isAddToBackStack) {
            transaction.addToBackStack(null).commit();
        } else {
            transaction.commit();
        }
    }

    public void setLanguage(String lang_key) {
        Locale locale = new Locale(lang_key);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(locale);
        conf.locale = locale;
        res.updateConfiguration(conf, dm);
    }

    public void clearBackStack() {
        popBackStackTillEntry(0);
    }

    public void popBackStackTillEntry(int entryIndex) {
        if (getSupportFragmentManager() == null)
            return;
        if (getSupportFragmentManager().getBackStackEntryCount() <= entryIndex)
            return;
        FragmentManager.BackStackEntry entry = getSupportFragmentManager().getBackStackEntryAt(
                entryIndex);
        if (entry != null) {
            getSupportFragmentManager().popBackStack(entry.getId(),
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    protected String getCurrentFragmentName() {
        if (getSupportFragmentManager() != null && getSupportFragmentManager().getBackStackEntryCount() != 0) {
            int index = getSupportFragmentManager().getBackStackEntryCount() - 1;
            FragmentManager.BackStackEntry backEntry = getSupportFragmentManager().getBackStackEntryAt(index);
            return backEntry.getName();
        }
        return null;
    }

    public int getMainFrameLayoutID() {
        return mainFrameLayoutID;
    }

    public void setMainFrameLayoutID(int mainFrameLayoutID) {
        this.mainFrameLayoutID = mainFrameLayoutID;
    }

    public View getMainFrameLayout() {
        return mainFrameLayout;
    }

    public void setMainFrameLayout(View mainFrameLayout) {
        this.mainFrameLayout = mainFrameLayout;
    }

    public int getCoordinatorLayoutID() {
        return coordinatorLayoutID;
    }

    public void setCoordinatorLayoutID(int coordinatorLayoutID) {
        this.coordinatorLayoutID = coordinatorLayoutID;
    }

    public int getAppBarID() {
        return appBarID;
    }

    public void setAppBarID(int appBarID) {
        this.appBarID = appBarID;
    }

    public int getCollapsingToolBarID() {
        return collapsingToolBarID;
    }

    public void setCollapsingToolBarID(int collapsingToolBarID) {
        this.collapsingToolBarID = collapsingToolBarID;
    }

    public int getToolbarID() {
        return toolbarID;
    }

    public void setToolbarID(int toolbarID) {
        this.toolbarID = toolbarID;
    }

    public CoordinatorLayout getCoordinatorLayout() {
        return (CoordinatorLayout) coordinatorLayout;
    }

    public void setCoordinatorLayout(View coordinatorLayout) {
        this.coordinatorLayout = coordinatorLayout;
    }

    public View getAppbarLayout() {
        return appbarLayout;
    }

    public void setAppbarLayout(View appbarLayout) {
        this.appbarLayout = appbarLayout;
    }

    public View getCollapsingToolBarLayout() {
        return collapsingToolBarLayout;
    }

    public void setCollapsingToolBarLayout(View collapsingToolBarLayout) {
        this.collapsingToolBarLayout = collapsingToolBarLayout;
    }

    public View getToolbarLayout() {
        return toolbarLayout;
    }

    public void setToolbarLayout(View toolbarLayout) {
        this.toolbarLayout = toolbarLayout;
    }

    public ImageView getIvSubCategoryItem() {
        return (ImageView) ivSubCategoryItem;
    }

    public void setIvSubCategoryItem(View ivSubCategoryItem) {
        this.ivSubCategoryItem = ivSubCategoryItem;
    }

    public View getRvSubCategoryItem() {
        return rvSubCategoryItem;
    }

    public void setRvSubCategoryItem(View rvSubCategoryItem) {
        this.rvSubCategoryItem = rvSubCategoryItem;
    }

    public BasePreferenceHelper getPreferenceHelper() {
        return preferenceHelper;
    }

    private void exitApp() {
        UIHelper.showSimpleDialog(
                this, 0, getResources().getString(R.string.close_app), getResources().getString(R.string.do_you_want_close_app), getResources().getString(R.string.yes), getResources().getString(R.string.no), false, false,
                new SimpleDialogActionListener() {
                    @Override
                    public void onDialogActionListener(DialogInterface dialog, int which, boolean positive, boolean logout) {
                        if (positive) {
                            dialog.dismiss();
                            finish();
                        } else {
                            dialog.dismiss();
                        }
                    }
                }
        );
    }

    public void closeApp() {
        UIHelper.showSimpleDialog(
                this, 0, getResources().getString(R.string.close_app), getResources().getString(R.string.do_you_want_close_app), getResources().getString(R.string.no), getResources().getString(R.string.yes), false, false,
                new SimpleDialogActionListener() {
                    @Override
                    public void onDialogActionListener(DialogInterface dialog, int which, boolean positive, boolean logout) {
                        if (positive) {
                            dialog.dismiss();
                        } else {
                            dialog.dismiss();
                            finish();
                        }
                    }
                }
        );
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

}
