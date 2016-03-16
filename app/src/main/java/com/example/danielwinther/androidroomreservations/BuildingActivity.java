package com.example.danielwinther.androidroomreservations;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BuildingActivity extends FragmentActivity {
    private City city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);

    }

    @Override
    protected void onStart() {
        super.onStart();

        city = (City) getIntent().getSerializableExtra("city");

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SelectedItemFragment selectedItemFragment = new SelectedItemFragment();
        Bundle parameters = new Bundle();
        parameters.putString("text1", city.getName());
        selectedItemFragment.setArguments(parameters);
        fragmentTransaction.replace(android.R.id.content, selectedItemFragment);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            fragmentTransaction.replace(android.R.id.content, selectedItemFragment);
        }
        else {
            fragmentTransaction.remove(selectedItemFragment);
        }
        fragmentTransaction.commit();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(HelperClass.URL + "buildings/city/" + city.getCityId(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        final List<Building> buildings = new ArrayList<>();
                        final ListView listView = (ListView) findViewById(R.id.listView);
                        ArrayAdapter<Building> adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, buildings);
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Building value = (Building) listView.getItemAtPosition(position);
                                Intent intent = new Intent(getApplicationContext(), RoomActivity.class);
                                intent.putExtra("building", value);
                                startActivity(intent);
                            }
                        });

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                buildings.add(new Building(jsonObject.getInt("Id"), jsonObject.getString("Name"), jsonObject.getString("Address"), jsonObject.getInt("CityId")));
                            } catch (JSONException e) {
                                new HelperClass().ErrorDialog(BuildingActivity.this, null, null);
                                Log.e(HelperClass.ERROR, e.toString());
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new HelperClass().ErrorDialog(BuildingActivity.this, null, null);
                Log.e(HelperClass.ERROR, error.toString());
            }
        });
        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }

    public void back(View view) {
        finish();
    }
}