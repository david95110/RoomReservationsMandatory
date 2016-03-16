package com.example.danielwinther.androidroomreservations;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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

public class MyReservationActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reservation);
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences pref = getApplicationContext().getSharedPreferences("login", MODE_PRIVATE);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(HelperClass.URL + "reservations/user/" + pref.getInt("userId", 0),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        final List<Reservation> myReservations = new ArrayList<>();
                        final ListView listView = (ListView) findViewById(R.id.listView);
                        ArrayAdapter<Reservation> adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, myReservations);
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Reservation value = (Reservation) listView.getItemAtPosition(position);
                                Intent intent = new Intent(getApplicationContext(), ReservationDetailActivity.class);
                                intent.putExtra("reservation", value);
                                startActivity(intent);
                            }
                        });

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                myReservations.add(new Reservation(jsonObject.getInt("Id"), jsonObject.getString("DeviceId"), jsonObject.getString("Purpose"), jsonObject.getString("FromTimeString"), jsonObject.getString("ToTimeString"), jsonObject.getInt("RoomId"), jsonObject.getInt("UserId")));
                            } catch (JSONException e) {
                                new HelperClass().ErrorDialog(MyReservationActivity.this, null, null);
                                Log.e(HelperClass.ERROR, e.toString());
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new HelperClass().ErrorDialog(MyReservationActivity.this, null, null);
                Log.e(HelperClass.ERROR, error.toString());
            }
        });
        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }

    public void back(View view) {
        finish();
    }
}