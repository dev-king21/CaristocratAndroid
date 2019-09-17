package com.ingic.caristocrat.helpers;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;


import com.ingic.caristocrat.R;

import java.io.InputStream;


public class ShareDetail extends AsyncTask<String, Void, Bitmap> {

    Context _context;
    String _url;
    String _title;
    ProgressDialog mProgressDialog;

    public ShareDetail(Context context,String title, String url) {
        this._context = context;
        this._url = url;
        this._title = title;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
            mProgressDialog = new ProgressDialog(_context);
            mProgressDialog.setMessage(_context.getResources().getString(R.string.loading));
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
    }

    @Override
    protected Bitmap doInBackground(String... URL) {

        String imageURL = URL[0];

        Bitmap bitmap = null;
        try {
            // Download Image from URL
            InputStream input = new java.net.URL(imageURL).openStream();
            // Decode Bitmap
            bitmap = BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        // Set the bitmap into ImageView
        mProgressDialog.dismiss();

        Utils.shareWithImage(_context,_title,_url, result);
    }
}

