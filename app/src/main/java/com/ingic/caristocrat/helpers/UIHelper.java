package com.ingic.caristocrat.helpers;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewOverlay;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.activities.RegistrationActivity;
import com.ingic.caristocrat.interfaces.ImageDownloadListener;
import com.ingic.caristocrat.interfaces.SimpleDialogActionListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;


/**
 */
public class UIHelper {
    static ImageDownloadListener imageDownload;

    public static void showSnackbar(View view, String message, int length) {
        Snackbar.make(view, message.trim(), length).show();
    }

    public static void showToast(Context context, String message, int length) {
        Toast.makeText(context, message.trim(), length).show();
    }

    public static boolean isEmptyOrNull(String string) {
        if (string == null)
            return true;
        return (string.trim().length() <= 0);
    }

    public static void setUserImageWithGlide(Context context, ImageView view, String url) {
        Glide.with(context).clear(view);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate().placeholder(R.drawable.avatar_place_holder);
//        requestOptions.centerCrop();
//        requestOptions.onlyRetrieveFromCache(false);
        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .into(view);
    }

    public static void setImageWithGlide(Context context, ImageView view, String url) {
        Glide.with(context).clear(view);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate().placeholder(R.drawable.image_placeholder);
//        requestOptions.timeout(10000);
//                        .apply(new RequestOptions().override(400, 250))
//        requestOptions.centerCrop();
//        requestOptions.onlyRetrieveFromCache(false);
        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .into(view);
    }

    public static void setImageWithGlideNoPlaceHolder(Context context, ImageView view, String url) {
        Glide.with(context).clear(view);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate().placeholder(R.drawable.new_car_placeholder);
//        requestOptions.timeout(10000);
//                        .apply(new RequestOptions().override(400, 250))
//        requestOptions.centerCrop();
//        requestOptions.onlyRetrieveFromCache(false);
        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .into(view);
    }

    public static void setImageWithGlide(Context context, ImageView view, String url, boolean thumbnail) {
        Glide.with(context).clear(view);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate().placeholder(R.drawable.image_placeholder);
//        requestOptions.centerCrop();
//        requestOptions.onlyRetrieveFromCache(false);
        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .thumbnail(0.5f)
                .into(view);
    }

    public static void setImageWithGlide(Context context, final CheckBox view, String url) {
        Glide.with(context).clear(view);
        RequestOptions requestOptions = new RequestOptions();
        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                   view.setButtonDrawable(resource);
                    }
                });

    }

    public static void setImageWithGlide(Context context, final LinearLayout view, String url) {
        Glide.with(context).clear(view);
        RequestOptions requestOptions = new RequestOptions();
        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        view.setBackground(resource);
                    }
                });
    }

    public static void setImageWithGlideWithoutThumbnail(Context context, final ImageView view, String url) {
        Glide.with(context).clear(view);
        RequestOptions requestOptions = new RequestOptions();
        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        view.setBackground(resource);
                    }
                });
    }

    public static void loadVideo(Context context, String url) {

        RequestOptions requestOptions = new RequestOptions();
        Glide.with(context)
                .load(url)
                .apply(requestOptions);
    }

    public static int getContentScrimColor(final MainActivity context, String url) {
        final int[] color = {-1};
        RequestOptions requestOptions = new RequestOptions();
        Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(requestOptions)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        // view.setImageBitmap(resource);
                        Palette.from(resource)
                                .generate(
                                        new Palette.PaletteAsyncListener() {
                                            @Override
                                            public void onGenerated(@NonNull Palette palette) {
                                                Palette.Swatch vibrantSwatch = palette.getMutedSwatch();
                                                if (vibrantSwatch == null) {
                                                    return;
                                                }
                                                color[0] = vibrantSwatch.getRgb();
                                                context.setContentscrimColor(color[0]);

                                            }
                                        }
                                );

                    }
                });
        return color[0];
    }

    public static String saveImage(final RegistrationActivity context, final String url, ImageDownloadListener imageDownloadListener) {
        imageDownload = imageDownloadListener;
        RequestOptions requestOptions = new RequestOptions();

        final String[] path = {""};
        Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(requestOptions)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        // view.setImageBitmap(resource);
                        path[0] = saveFile(resource);


                    }
                });
        return path[0];
    }

    public static String saveFile(Bitmap image) {
        String savedImagePath = null;

        String imageFileName = "JPEG_" + "FILE_NAME" + ".jpg";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "/Iika");
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();

            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
                imageDownload.isSaved(savedImagePath);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        return savedImagePath;
    }

    public static void hideSoftKeyboard(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideSoftKeyboard(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }

    }

    public static void animateResponse(View contentView, final View loadingView,
                                       int animationDuration) {
        contentView.setAlpha(0f);
        contentView.setVisibility(View.VISIBLE);
        contentView.animate()
                .alpha(1f)
                .setDuration(animationDuration)
                .setListener(null);

        loadingView.animate()
                .alpha(0f)
                .setDuration(animationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        loadingView.setVisibility(View.GONE);
                        loadingView.setAlpha(1f);
                    }
                });
    }

    public static void showSimpleDialog(final Context context, int icon, String title, String
            message, String positiveButton, String negativeButton, boolean cancelable,
                                        final boolean logout, final SimpleDialogActionListener simpleDialogActionListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(icon)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(cancelable)
                .setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (logout) {
                            simpleDialogActionListener.onDialogActionListener(dialog, which, true, true);
                        } else {
                            simpleDialogActionListener.onDialogActionListener(dialog, which, true, false);
                        }
                    }
                })
                .setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        simpleDialogActionListener.onDialogActionListener(dialog, which, false, false);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void showSimpleDialog(final Context context, int icon, String title, String
            message, String positiveButton, String negativeButton, String neutralButton,
                                        boolean cancelable, final boolean neutral,
                                        final SimpleDialogActionListener simpleDialogActionListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(icon)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(cancelable)
                .setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        simpleDialogActionListener.onDialogActionListener(dialog, which, true, false);
                    }
                })
                .setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        simpleDialogActionListener.onDialogActionListener(dialog, which, false, false);
                    }
                })
                .setNeutralButton(neutralButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        simpleDialogActionListener.onDialogActionListener(dialog, which, false, neutral);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void showSimpleDialog(final MainActivity context, int icon, String title, String
            message, View view, String positiveButton, String negativeButton, String neutralButton,
                                        boolean cancelable, final boolean neutral,
                                        final SimpleDialogActionListener simpleDialogActionListener) {
        LayoutInflater inflater = context.getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(icon)
                .setTitle(title)
                .setView(/*inflater.inflate(view, null)*/view)
                .setCancelable(cancelable);
        if (positiveButton != null) {
            builder.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    simpleDialogActionListener.onDialogActionListener(dialog, which, true, false);
                }
            });
        }
        if (negativeButton != null) {
            builder.setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    simpleDialogActionListener.onDialogActionListener(dialog, which, false, false);
                }
            });
        }
        if (neutralButton != null) {
            builder.setNeutralButton(neutralButton, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    simpleDialogActionListener.onDialogActionListener(dialog, which, false, neutral);
                }
            });
        }
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void hideKeyboard(View view, Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void applyDim(MainActivity mainActivityContext) {
        Drawable dim = new ColorDrawable(Color.BLACK);
        dim.setBounds(0, 0, mainActivityContext.getBinding().getRoot().getWidth(), mainActivityContext.getBinding().getRoot().getHeight());
        dim.setAlpha((int) (255 * 0.7));
        ViewOverlay overlay = mainActivityContext.getBinding().getRoot().getOverlay();
        overlay.add(dim);
    }

    public static void clearDim(MainActivity mainActivityContext) {
        ViewOverlay overlay = mainActivityContext.getBinding().getRoot().getOverlay();
        overlay.clear();
    }

    public static void animation(Techniques techniques, int duration, int repeat, View view) {
        //view.clearAnimation();
        // view.clearFocus();
        YoYo.with(techniques)
                .duration(duration)
                .repeat(repeat)
                .playOn(view);
        view.clearFocus();
        view.clearAnimation();
    }

    public static class SpacesItemDecorationAllSideEqual extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecorationAllSideEqual(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.top = space;
            outRect.bottom = space;
            outRect.right = space;
        }
    }

    public static class SpacesItemDecorationEnd extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecorationEnd(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = 0;
            outRect.top = 0;
            outRect.bottom = 0;
            outRect.right = space;
        }
    }

    public static class SpacesItemDecorationTopBottomEqual extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecorationTopBottomEqual(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.top = space;
            outRect.bottom = space;
        }
    }

    public static void callHelper(String number,Activity activity){

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        activity.startActivity(intent);

    }

    public static int screensize(Activity acitivty, String axies) {
        int width;
        Display display = acitivty.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        if (axies.equals("x"))
            width = size.x;
        else
            width = size.y;
        return width;
    }
}
