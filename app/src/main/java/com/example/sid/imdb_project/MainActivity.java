package com.example.sid.imdb_project;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.ads.AdRequest;

import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DataListener {
    ListView listView;
    ArrayList<Model> modelArrayList;
    CustomAdapter customAdapter;
    String url;
    DBHelper dbHelper;
    LinearLayout linearLayout;
    private static final String TAG = "MainActivity";
    private InterstitialAd mInterstitialAd;
    int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //for ads
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3386419800120732/6385052679");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        Fresco.initialize(this);
        dbHelper = new DBHelper(MainActivity.this);
        linearLayout = (LinearLayout) findViewById(R.id.linear);
        modelArrayList = new ArrayList<>();
        url = "http://api.themoviedb.org/3/movie/upcoming?api_key=8496be0b2149805afa458ab8ec27560c";
        listView = (ListView) findViewById(R.id.listview);
        checkConnection();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("getid", modelArrayList.get(position).getId());
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        //showing ad on back button pressed
        Toast.makeText(this, "You Pressed Back Button", Toast.LENGTH_SHORT).show();
        if (mInterstitialAd.isLoaded())
            mInterstitialAd.show();
        super.onBackPressed();
    }

    public void checkConnection() {
        //TODO: shows is true even if wifi is not connected to the system.
        if (isConnectedToInternet()) {
            DataProcess dataProcess = new DataProcess(MainActivity.this, url, this, linearLayout);
            dataProcess.execute();
        } else {
            Snackbar snackbar = Snackbar.make(linearLayout, "No internet connection!", Snackbar.LENGTH_INDEFINITE).setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    checkConnection();
                }
            });
            snackbar.show();
        }

        customAdapter = new CustomAdapter(this, modelArrayList);
        //setting adapter to listview
        listView.setAdapter(customAdapter);

    }

    private boolean isConnectedToInternet() {
        boolean isconnected = false;
        //checking the internet connection
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            isconnected = true;
        }
        return isconnected;
    }

    @Override
    public void updatelist(String data) {
        try {
            //creating jsonObject
            JSONObject jsonObject = new JSONObject(data);
            //array from the json object
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String title = jsonObject1.getString("title");
                String release_date = jsonObject1.getString("release_date");
                String popularity = jsonObject1.getString("popularity");
                String vote_count = jsonObject1.getString("vote_count");
                String vote_average = jsonObject1.getString("vote_average");
                String poster_path = jsonObject1.getString("poster_path");
                String id = jsonObject1.getString("id");
                Model model = new Model();
                model.setTitle(title);
                model.setRelease_date(release_date);
                model.setPopularity(popularity);
                model.setVote_count(vote_count);
                model.setVote_average(vote_average);
                model.setPoster_path(poster_path);
                model.setId(id);
                modelArrayList.add(model);
            }
            customAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.MostPopular:
                Mostpop();
                break;
            case R.id.UpcomingMovies:
                upcoming();
                break;
            case R.id.LatestMovies:
                latestMovies();
                break;
            case R.id.toprated:
                topRated();
                break;
            case R.id.NowPlaying:
                nowPlaying();
                break;
            case R.id.Fav:
                Intent favintent = new Intent(MainActivity.this, FavoritesList.class);
                flag = 1;
                favintent.putExtra("fav", flag);
                startActivity(favintent);
                break;
            case R.id.watchlist:
                Intent watchintent = new Intent(MainActivity.this, FavoritesList.class);
                flag = 2;
                watchintent.putExtra("fav", flag);
                startActivity(watchintent);
                break;
            case R.id.refresh:
                String title = this.getTitle().toString();
                switch (title) {
                    case "Most Popular":
                        Mostpop();
                        break;
                    case "Nowplaying":
                        nowPlaying();
                        break;
                    case "Upcoming":
                        upcoming();
                        break;
                    case "Top Rated":
                        topRated();
                        break;
                    default:

                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void latestMovies() {
        Toast.makeText(this, "You Clicked Latest Movies", Toast.LENGTH_SHORT).show();
        url = "http://api.themoviedb.org/3/movie/upcoming?api_key=8496be0b2149805afa458ab8ec27560c";
        modelArrayList.clear();
        this.setTitle("Latest Movies");
        checkConnection();
    }

    private void topRated() {
        Toast.makeText(this, "You Clicked Top Rated", Toast.LENGTH_SHORT).show();
        url = "http://api.themoviedb.org/3/movie/top_rated?api_key=f47dd4de64c6ef630c2b0d50a087cc33";
        modelArrayList.clear();
        this.setTitle("Top Rated");
        checkConnection();
    }

    private void nowPlaying() {
        Toast.makeText(this, "You Clicked TNow Playing", Toast.LENGTH_SHORT).show();
        url = "http://api.themoviedb.org/3/movie/now_playing?api_key=8496be0b2149805afa458ab8ec27560c";
        modelArrayList.clear();
        this.setTitle("Nowplaying");
        checkConnection();
    }

    private void upcoming() {
        Toast.makeText(this, "You Clicked Upcoming", Toast.LENGTH_SHORT).show();
        url = "http://api.themoviedb.org/3/movie/upcoming?api_key=8496be0b2149805afa458ab8ec27560c";
        modelArrayList.clear();
        this.setTitle("Upcoming");
        checkConnection();
    }

    private void Mostpop() {
        Toast.makeText(this, "You Clicked Most Popular", Toast.LENGTH_SHORT).show();
        url = "http://api.themoviedb.org/3/movie/popular?api_key=8496be0b2149805afa458ab8ec27560c";
        modelArrayList.clear();
        this.setTitle("Most Popular");
        checkConnection();
    }


}

