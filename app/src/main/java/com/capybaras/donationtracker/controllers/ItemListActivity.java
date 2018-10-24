package com.capybaras.donationtracker.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.capybaras.donationtracker.R;
import com.capybaras.donationtracker.models.Item;
import com.capybaras.donationtracker.models.Location;
import com.capybaras.donationtracker.models.Model;
import com.capybaras.donationtracker.models.User;
import com.capybaras.donationtracker.models.UserTypes;

import java.util.LinkedList;
import java.util.List;

public class ItemListActivity extends Activity {

    private static final String TAG = "ItemListActivity";
    private Spinner spinner;
    private Location selectedLocation;
    private RecyclerView recyclerView;
    private Button addItemButton;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        user = Model.getInstance().getLoggedInUser();
        setUpSpinner();
        setUpButton();
    }


    private void setUpSpinner() {
        spinner = findViewById(R.id.spinner);
        final List<Location> locationList = Model.getInstance().getLocations();
        List<String> locationNameList = new LinkedList<>();
        for (Location l: locationList) {
            locationNameList.add(l.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, locationNameList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "Username: " + user.getUsername());
                Log.d(TAG, Model.getInstance().getLoggedInUser().getLocation().getName());
                selectedLocation = locationList.get(position);
                if (user.getType() == UserTypes.LOCATION_EMPLOYEE
                        && selectedLocation.equals(user.getLocation())) {
                    addItemButton.setVisibility(View.VISIBLE);
                } else {
                    addItemButton.setVisibility(View.GONE);
                }
                setUpRecycler(selectedLocation.getItems());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                addItemButton.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
            }
        });
    }

    private void setUpRecycler(List<Item> locationItems) {
        recyclerView = findViewById(R.id.recycler);
        RecyclerAdapter adapter = new RecyclerAdapter(this, locationItems);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void setUpButton() {
        addItemButton = findViewById(R.id.add_item_button);
        addItemButton.setVisibility(View.GONE);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewItemActivity.class);
                startActivity(intent);
                Log.d(TAG, "Button clicked");
            }
        });
        Log.d(TAG, "Button on click listener set up");
    }

    private Activity getActivity(){
        return this;
    }

    public Location getSelectedLocation() {
        return selectedLocation;
    }

}
