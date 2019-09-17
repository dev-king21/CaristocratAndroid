package com.ingic.caristocrat.jsonListener;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;


public class JSONClient extends AsyncTask<String, Void, String> {
    GetJSONListener getJSONListener;
    Context curContext;
    HashMap<String, String> map;
    String method;
    public JSONClient(Context context, GetJSONListener listener, String method, HashMap<String, String> hashmap) {
        this.getJSONListener = listener;
        curContext = context;
        map = hashmap;
        this.method = method;
    }
    public String connect(String url) throws JSONException {
        StringBuffer response = new StringBuffer();
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            if (method.equals("Get"))
            {
                con.setDoInput(true);
                con.setDoOutput(false);
                con.setUseCaches(false);
				 /* optional default is GET */
                con.setRequestMethod("GET");
            }else
            {
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setUseCaches(false);
                con.setRequestProperty("Content-Type","form-data");
				 /* optional default is post */
                con.setRequestMethod("POST");
                String postData="";
                for (String key : map.keySet()) {
                    postData+="&" + key + "="+ map.get(key);
                }
                postData = postData.substring(1);
                DataOutputStream postOut = new DataOutputStream(con.getOutputStream());
                postOut.writeBytes(postData);
                postOut.flush();
                postOut.close();
            }

            int responseCode = con.getResponseCode();
            System.out.println("Response Code : " + responseCode);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return response.toString();
    }

    @Override
    public void onPreExecute() {
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            return connect(urls[0]);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            System.out.println("doInBackground : " + e.getMessage());
            return "";
        }
    }

    @Override
    protected void onPostExecute(String json) {
        getJSONListener.onRemoteCallComplete(json);
    }
}
