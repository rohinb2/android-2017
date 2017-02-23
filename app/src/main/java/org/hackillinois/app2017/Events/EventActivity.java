package org.hackillinois.app2017.Events;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.hackillinois.app2017.R;
import org.hackillinois.app2017.UI.CenteredToolbar;
import org.hackillinois.app2017.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_events) CenteredToolbar toolbar;
    @BindView(R.id.event_title) TextView title;
    @BindView(R.id.event_start_time) TextView startTime;
    @BindView(R.id.event_location_container) LinearLayout locationContainer;
    @BindView(R.id.event_description) TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        ButterKnife.bind(this);

        setUpActionBar(toolbar);

        Typeface brandon_med = Typeface.createFromAsset(getAssets(), "fonts/Brandon_med.otf");
        Typeface brandon_reg = Typeface.createFromAsset(getAssets(), "fonts/Brandon_reg.otf");

        title.setTypeface(brandon_med);
        startTime.setTypeface(brandon_med);
        description.setTypeface(brandon_reg);

        Bundle bundle = getIntent().getExtras();

        ArrayList<String> locations = bundle.getStringArrayList("location");
        for(String location : locations) {
            TextView locationView = Utils.generateLocationTextView(getApplicationContext(),location);
            locationView.setTypeface(brandon_med);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMarginStart((int) Utils.convertDpToPixel(20,getApplicationContext()));
            locationView.setLayoutParams(layoutParams);
            locationView.setTextSize(18);
            locationContainer.addView(locationView);
        }
        //TODO CREATE INTENT TO OPEN MAPS PAGE FROM LOCATION
        startTime.setText(bundle.getString("starttime"));
        description.setText(bundle.getString("description","No description"));
    }

    private void setUpActionBar(CenteredToolbar toolbar) {
        setSupportActionBar(toolbar);
        setTitle("Event Details");
        // TODO: toolbar.setNavigationLogo();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
