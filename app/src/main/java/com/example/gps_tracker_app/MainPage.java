package com.example.gps_tracker_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;

class Loc implements Serializable {

    String name;
    Double latitude;
    Double longitude;
    int distance;
    boolean visited;

    public Loc(String location_name, Double location_latitude, Double location_longitude,
               Integer location_distance, boolean location_visited) {

        name = location_name;
        latitude = location_latitude;
        longitude = location_longitude;
        distance = location_distance;
        visited = location_visited;

    }

    public String getName() {
        return name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getKmDistance() {
        return distance / 1000.0;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}

public class MainPage extends AppCompatActivity {


    ArrayList<Loc> location_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        Loc loc1 = new Loc("cathedral", 54.6858, 25.2877, 50, false);
        Loc loc2 = new Loc("riders", 54.6941, 25.2950, 50, false);
        Loc loc3 = new Loc("home", 54.7550, 25.2628, 50, false);
        Loc loc4 = new Loc("work", 54.7737, 25.2729, 50, false);

        location_list.add(loc1);
        location_list.add(loc2);
        location_list.add(loc3);
        location_list.add(loc4);

        Button btn1 = findViewById(R.id.go_to_map);
        btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Map.class);
                intent.putExtra("list", location_list);
                startActivity(intent);
            }
        });
    }


}
