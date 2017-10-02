package com.example.sid.imdb_project;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class DataProcess extends AsyncTask<Void,Void,String> {

    private Context context;
    private String url;

    private DataListener dataListener;
    View view;
    public DataProcess(Context context, String url, DataListener dataListener, View view) {
        this.context = context;
        this.url = url;
        this.view = view;
        this.dataListener = dataListener;
    }

    @Override
    protected void onPreExecute() {    }

    @Override
    protected String doInBackground(Void... params) {
        OkHttpClient okHttpClient = new OkHttpClient();

        okHttpClient.setReadTimeout(120, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(120, TimeUnit.SECONDS);

        Request request = new Request.Builder().url(url).build();
        String responsedata = null;
        try {
            Response response = okHttpClient.newCall(request).execute();
            if(response.isSuccessful()){
                responsedata = response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responsedata;
    }


    @Override
    protected void onPostExecute(String aVoid) {
        super.onPostExecute(aVoid);
            dataListener.updatelist(aVoid);


    }
}
