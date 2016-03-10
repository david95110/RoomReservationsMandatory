package com.example.danielwinther.androidroomreservations;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateReservationActivity extends AppCompatActivity {
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_reservation);
    }

    @Override
    protected void onStart() {
        super.onStart();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(HelperClass.URL + "rooms/",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        spinner = (Spinner) findViewById(R.id.createRoomId);
                        final ArrayAdapter<Room> spinnerAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, android.R.id.text1);
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(spinnerAdapter);

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                spinnerAdapter.add(new Room(jsonObject.getInt("Id"), jsonObject.getString("Name"), jsonObject.getString("Description"), jsonObject.getInt("Capacity"), jsonObject.getString("Remarks"), jsonObject.getInt("BuildingId")));
                            } catch (JSONException e) {
                                new HelperClass().ErrorDialog(CreateReservationActivity.this, null, null);
                                Log.e(HelperClass.ERROR, e.toString());
                            }
                        }
                        int position = getIntent().getIntExtra("position", 0);
                        spinner.setSelection(position);
                        spinnerAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new HelperClass().ErrorDialog(CreateReservationActivity.this, null, null);
                Log.e(HelperClass.ERROR, error.toString());
            }
        });
        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }

    public void create(View view) {
        String purpose = ((EditText) findViewById(R.id.createPurpose)).getText().toString();
        String from = ((EditText) findViewById(R.id.createFrom)).getText().toString();
        String to = ((EditText) findViewById(R.id.createTo)).getText().toString();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("login", MODE_PRIVATE);
        JSONObject jsonObject = new JSONObject();
        try {
            Room roomId = (Room) spinner.getSelectedItem();
            jsonObject.put("FromTimeString", from);
            jsonObject.put("Purpose", purpose);
            jsonObject.put("RoomId", roomId.getRoomId());
            jsonObject.put("ToTimeString", to);
            jsonObject.put("UserId", pref.getInt("userId", 0));
        } catch (JSONException e) {
            new HelperClass().ErrorDialog(CreateReservationActivity.this, null, null);
            Log.e(HelperClass.ERROR, jsonObject.toString());
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, HelperClass.URL + "reservations/", jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        finish();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                new HelperClass().ErrorDialog(CreateReservationActivity.this, null, null);
                Log.e(HelperClass.ERROR, error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        Volley.newRequestQueue(this).add(jsonObjReq);
    }

    public void back(View view) {
        finish();
    }
}