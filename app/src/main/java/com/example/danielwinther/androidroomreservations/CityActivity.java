package com.example.danielwinther.androidroomreservations;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CityActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
    }

    @Override
    protected void onStart() {
        super.onStart();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(HelperClass.URL + "cities/",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        final List<City> cities = new ArrayList<>();
                        final ListView listView = (ListView) findViewById(R.id.listView);
                        ArrayAdapter<City> adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, cities);
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                City value = (City) listView.getItemAtPosition(position);
                                Intent intent = new Intent(CityActivity.this, BuildingActivity.class);
                                intent.putExtra("city", value);
                                startActivity(intent);
                            }
                        });

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                cities.add(new City(jsonObject.getInt("Id"), jsonObject.getString("Name")));
                            } catch (JSONException e) {
                                new HelperClass().ErrorDialog(getApplicationContext(), null, null);
                                Log.e(HelperClass.ERROR, e.toString());
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Intent intent = new Intent(CityActivity.this, BuildingActivity.class);
                Log.e(HelperClass.ERROR, error.toString());
            }
        });
        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }

    public void logout(View view) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void myReservation(View view) {
        Intent intent = new Intent(this, MyReservationActivity.class);
        startActivity(intent);
    }
}