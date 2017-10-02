package com.example.sid.imdb_project;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class GetCastCrew extends AsyncTask<String,Void,List<Casts>> {
    private Context context;
    List<Casts> castList;
    private String tag;
    JSONArray castArray;
    String url;
    public GetCastCrew(Context context, String tag, String url) {

        this.context = context;
        this.tag = tag;
        this.url = url;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Casts> doInBackground(String... params) {
        castList = new ArrayList<>();
        OkHttpClient okHttpClient = new OkHttpClient();
        //setting timeout time limit
        okHttpClient.setReadTimeout(120, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(120, TimeUnit.SECONDS);

        //Requesting url builder
        Request request = new Request.Builder().url(url).build();
        String responsedata = null;
        try {
            //response of the httpclient
            Response response = okHttpClient.newCall(request).execute();
            if(response.isSuccessful()){
                responsedata = response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(responsedata!=null) {

            try {
                JSONObject jsonObj = new JSONObject(responsedata);
                castArray = jsonObj.getJSONArray(tag);

                if (tag.equals("cast")) {

                    for (int i = 0; i < castArray.length(); i++) {
                        JSONObject o = castArray.getJSONObject(i);
                        Casts cast = new Casts();
                        cast.setCharacter(o.getString("character"));
                        cast.setName(o.getString("name"));
                        cast.setProfilePath(o.getString("profile_path"));
                        castList.add(cast);
                    }
                } else if (tag.equals("crew")) {
                    for (int i = 0; i < castArray.length(); i++) {
                        JSONObject o = castArray.getJSONObject(i);
                        Casts cast = new Casts();
                        cast.setJob(o.getString("job"));
                        cast.setName(o.getString("name"));
                        cast.setProfilePath(o.getString("profile_path"));
                        castList.add(cast);
                    }
                } else if (tag.equals("results")) {
                    for (int i = 0; i < castArray.length(); i++) {
                        JSONObject o = castArray.getJSONObject(i);
                        Casts cast = new Casts();
                        cast.setName(o.getString("name"));
                        cast.setKey(o.getString("key"));
                        castList.add(cast);
                    }
                } else if (tag.equals("posters")) {
                    for (int i = 0; i < castArray.length(); i++) {
                        JSONObject o = castArray.getJSONObject(i);
                        Casts cast = new Casts();
                        cast.setProfilePath(o.getString("file_path"));
                        castList.add(cast);
                    }
                }
                return castList;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<Casts> result) {
        super.onPostExecute(result);
        if (result == null) {

            Toast.makeText(context, "Unable to fetch data from server", Toast.LENGTH_LONG).show();
        }
    }
}
