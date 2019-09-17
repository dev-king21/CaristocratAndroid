package com.ingic.caristocrat.helpers;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.models.Age;
import com.ingic.caristocrat.models.TradeCar;
import com.ingic.caristocrat.services.TokenExpiryReceiver;
import com.ingic.caristocrat.webhelpers.models.ApiResponse;
import com.ingic.caristocrat.webhelpers.models.ErrorModelWrapper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.utils.Orientation;


/**
 */
public class Utils {
    public static void hideShowPassword(ImageView imageView, EditText editText, int showdrawable, int hidedrawable) {
        if (editText.getTransformationMethod() instanceof PasswordTransformationMethod) {
            editText.setTransformationMethod(new HideReturnsTransformationMethod());
            imageView.setImageResource(showdrawable);

        } else {
            editText.setTransformationMethod(new PasswordTransformationMethod());
            imageView.setImageResource(hidedrawable);

        }
    }

    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (manager == null)
            return false;

        // 3g-4g available
        boolean is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        // wifi available
        boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();

        System.out.println(is3g + " net " + isWifi);

        if (!is3g && !isWifi) {
            return false;
        } else
            return true;
    }

    public static void setAlarm(Context context, long trigger, long interval) {
        Calendar cal = Calendar.getInstance();
        Intent alertIntent = new Intent(context, TokenExpiryReceiver.class);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, trigger, interval, PendingIntent.getBroadcast(context, 0, alertIntent, PendingIntent.FLAG_CANCEL_CURRENT));
    }

    public static void openEmailComposer(String emailId, Context context) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
//        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{emailId});
//        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
//        emailIntent.setType("plain/text");
        emailIntent.setData(Uri.parse("mailto:" + emailId));
        context.startActivity(emailIntent);
    }

    public static void openCallMaker(final String phoneNumber, final Context context) {
        TedPermission.with(context)
                .setPermissions(Manifest.permission.CALL_PHONE)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + phoneNumber));
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        context.startActivity(intent);

                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> arrayList) {
                        //Utils.showToast(context, context.getString(R.string.permission_denied));
                        UIHelper.showToast(context, context.getString(R.string.permission_denied), Toast.LENGTH_SHORT);

                    }
                }).check();
    }

    public static void showDetailedErrors(View mainFrameLayout, ApiResponse apiResponse, int mode) {
        if (apiResponse != null) {
            ErrorModelWrapper errorModelWrapper = (ErrorModelWrapper) JsonHelpers.convertToModelClass(apiResponse.getData(), ErrorModelWrapper.class);
            switch (mode) {
                case AppConstants.ErrorsKeys.EMAIL:
                    if (errorModelWrapper.getErrorModel().getEmail().size() > 0) {
                        UIHelper.showSnackbar(mainFrameLayout, errorModelWrapper.getErrorModel().getEmail().get(0), Snackbar.LENGTH_LONG);
                        break;
                    }

                case AppConstants.ErrorsKeys.PASSWORDS:
                    if (errorModelWrapper.getErrorModel().getPassword().size() > 0) {
                        UIHelper.showSnackbar(mainFrameLayout, errorModelWrapper.getErrorModel().getPassword().get(0), Snackbar.LENGTH_LONG);
                        break;
                    }

                case AppConstants.ErrorsKeys.CONFIRM_PASSWORDS:
                    if (errorModelWrapper.getErrorModel().getPasswordConfirmation().size() > 0) {
                        UIHelper.showSnackbar(mainFrameLayout, errorModelWrapper.getErrorModel().getPasswordConfirmation().get(0), Snackbar.LENGTH_LONG);
                        break;
                    }

                default:
                    if (errorModelWrapper.getErrorModel().getError().size() > 0) {
                        UIHelper.showSnackbar(mainFrameLayout, errorModelWrapper.getErrorModel().getError().get(0), Snackbar.LENGTH_LONG);
                        break;
                    }
            }
        }
    }

    public static String compressText(String text, int limit) {
        String compressedText = "";
        if (text != null) {
            if (text.length() > limit) {
                compressedText = text.substring(0, limit - 5) + " ...";
            } else {
                compressedText = text;
            }
        }
        return compressedText;
    }

    public static void shareWithImage(Context context, String title, String url, Bitmap bitmap) {
        if (!Utils.isNetworkAvailable(context)) {
            UIHelper.showToast(context, context.getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT);
            return;
        }

        TedPermission.with(context)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_TEXT, title + "\n" + url);
                        String path = null;
                        path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, title, null);
                        Uri screenshotUri = Uri.parse(path);
                        intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.setType("image/*");
                        context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_image_via)));
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> arrayList) {
                        UIHelper.showToast(context, context.getString(R.string.permission_denied), Toast.LENGTH_SHORT);
//                        Toasty.warning(MainActivity.this, MainActivity.this.getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();

                    }
                }).check();





    }

    public static void shareWithVideoLink(Context context, String title, String url) {
        if (!Utils.isNetworkAvailable(context)) {
            UIHelper.showToast(context, context.getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT);
            return;
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, title + "\n" + url);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("text/plain");
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_video_link_via)));
    }

    public static void setDeviceToken(Context context, String token) {
        BasePreferenceHelper preferenceHelper = new BasePreferenceHelper(context);
        preferenceHelper.putDeviceToken(token);
    }

    public static boolean loginStatus(Context context) {
        BasePreferenceHelper preferenceHelper = new BasePreferenceHelper(context);
        return preferenceHelper.getLoginStatus();
    }

    public static void share(final Context context, final String title, String url, final String fileUrl) {
        if (!Utils.isNetworkAvailable(context)) {
            UIHelper.showToast(context, context.getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT);
            return;
        }
        TedPermission.with(context).setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE).setPermissionListener(new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                new ShareDetail(context, title, url)
                        .execute(fileUrl);
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                UIHelper.showToast(context, context.getResources().getString(R.string.permission_denied), Toast.LENGTH_SHORT);

            }
        }).check();
    }

    public static String getYouTubeId(String youTubeUrl) {
        String pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(youTubeUrl);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return "error";
        }
    }

    public static String getCarName(TradeCar tradeCar, boolean getOnlyModelName) {
        String carName = "";
        if (tradeCar.getModel() != null) {
            carName = tradeCar.getModel().getName();

            if (!getOnlyModelName) {
                if (tradeCar.getModel().getBrand() != null) {
                    carName = tradeCar.getModel().getBrand().getName() + " " + carName;
                }
                /*
                if(tradeCar.getYear() != null){
                    carName = carName + " " + tradeCar.getYear();
                }
                */
            }
        }
        if (tradeCar.getName() != null) {
            return tradeCar.getName();
        } else {
            return carName;
        }
    }

    public static String getCarNameByBrand(TradeCar tradeCar, boolean getOnlyModelName) {
        String carName = "";
        if (tradeCar.getModel() != null) {
            carName = tradeCar.getModel().getName();

            if (!getOnlyModelName) {
                if (tradeCar.getModel().getBrand() != null) {
                    carName = tradeCar.getModel().getBrand().getName() + " " + carName;
                }
                /*
                if(tradeCar.getYear() != null){
                    carName = carName + " " + tradeCar.getYear();
                }
                */
            }
        }
        return carName;
    }

    public static String getCarModelName(TradeCar tradeCar) {
        String carName = "";
        if (tradeCar.getModel() != null) {
            carName = tradeCar.getModel().getName();
        }
        return carName;
    }

    public static Age calculateAge(Date birthDate) {
        int years = 0;
        int months = 0;
        int days = 0;

        //create calendar object for birth day
        Calendar birthDay = Calendar.getInstance();
        birthDay.setTimeInMillis(birthDate.getTime());

        //create calendar object for current day
        long currentTime = System.currentTimeMillis();
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(currentTime);

        //Get difference between years
        years = now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
        int currMonth = now.get(Calendar.MONTH) + 1;
        int birthMonth = birthDay.get(Calendar.MONTH) + 1;

        //Get difference between months
        months = currMonth - birthMonth;

        //if month difference is in negative then reduce years by one
        //and calculate the number of months.
        if (months < 0) {
            years--;
            months = 12 - birthMonth + currMonth;
            if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
                months--;
        } else if (months == 0 && now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
            years--;
            months = 11;
        }

        //Calculate the days
        if (now.get(Calendar.DATE) > birthDay.get(Calendar.DATE))
            days = now.get(Calendar.DATE) - birthDay.get(Calendar.DATE);
        else if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
            int today = now.get(Calendar.DAY_OF_MONTH);
            now.add(Calendar.MONTH, -1);
            days = now.getActualMaximum(Calendar.DAY_OF_MONTH) - birthDay.get(Calendar.DAY_OF_MONTH) + today;
        } else {
            days = 0;
            if (months == 12) {
                years++;
                months = 0;
            }
        }
        //Create new Age object
        return new Age(days, months, years);
    }

    public static int binarySearch(ArrayList<Integer> arr, int l, int r, int x) {
        if (r >= l) {
            int mid = l + (r - l) / 2;

            // If the element is present at the
            // middle itself
            if (arr.get(mid) == x)
                return mid;

            // If element is smaller than mid, then
            // it can only be present in left subarray
            if (arr.get(mid) > x)
                return binarySearch(arr, l, mid - 1, x);

            // Else the element can only be present
            // in right subarray
            return binarySearch(arr, mid + 1, r, x);
        }

        // We reach here when element is not present
        // in array
        return -1;
    }

    public static void printHashKey(String TAG, Context context) {

        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.e(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }
}
