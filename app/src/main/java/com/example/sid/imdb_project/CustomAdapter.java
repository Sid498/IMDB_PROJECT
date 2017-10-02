package com.example.sid.imdb_project;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;



public class CustomAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Model> modelArrayList;
    LayoutInflater inflater;

    //parameterized constructor
    public  CustomAdapter(Context context, ArrayList<Model> modelArrayList ){
        this.context = context;
        this.modelArrayList = modelArrayList;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return modelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return modelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.row,null);

        TextView title = (TextView)convertView.findViewById(R.id.title);
        title.setText(modelArrayList.get(position).getTitle());
        TextView release_date = (TextView)convertView.findViewById(R.id.ReleaseDate);
        release_date.setText("Release Date: "+modelArrayList.get(position).getRelease_date());

        TextView popularity = (TextView)convertView.findViewById(R.id.popularity);
        popularity.setText("Popularity: ");

        TextView Votecount =(TextView)convertView.findViewById(R.id.votes);
        Votecount.setText("("+modelArrayList.get(position).getVote_average()+"/10) voted by "+modelArrayList.get(position).getVote_count()+" users");

        RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.rating);
        ratingBar.setRating(Float.parseFloat(modelArrayList.get(position).getPopularity()) / 50);
        RatingBar ratingBar1 = (RatingBar) convertView.findViewById(R.id.rating1);
        ratingBar1.setRating(Float.parseFloat(modelArrayList.get(position).getVote_average()) / 10);
        SimpleDraweeView imageView = (SimpleDraweeView) convertView.findViewById(R.id.poster);
        Uri uri = Uri.parse("http://image.tmdb.org/t/p/w500"+modelArrayList.get(position).getPoster_path());
        imageView.setImageURI(uri);
        //returning view
        return convertView;
    }
}
