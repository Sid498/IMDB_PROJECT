package com.example.sid.imdb_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class FavoritesList extends AppCompatActivity {
    DBHelper dbHelper;
    CustomAdapter customAdapter;
    ArrayList<Model> completelist;
    ListView listView;
    Intent intent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(this);
        intent = getIntent();
        listView = (ListView) findViewById(R.id.listview);
        int i = intent.getIntExtra("fav", 3);
        if (i == 1) {
            completelist = dbHelper.getFav();
            customAdapter = new CustomAdapter(this, completelist);
            this.setTitle("Favorites");
            listView.setAdapter(customAdapter);
        }
        if (i == 2) {
            completelist = dbHelper.getWatchList();
            this.setTitle("WatchList");
            customAdapter = new CustomAdapter(this, completelist);
            listView.setAdapter(customAdapter);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("getid", completelist.get(position).getId());
                Intent intent = new Intent(FavoritesList.this, DetailsActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO remove from favorite
                completelist.remove(i);
                customAdapter.notifyDataSetChanged();
                listView.setAdapter(customAdapter);
                Toast.makeText(FavoritesList.this, "dfhfhhhfhfh", Toast.LENGTH_SHORT).show();
                return false;
            }
        });


    }


}
