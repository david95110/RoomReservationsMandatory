package com.example.danielwinther.androidroomreservations;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class UpdateReservationActivity extends AppCompatActivity {
    private Spinner spinner;
    private Reservation reservation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_reservation);
    }
    @Override
    protected void onStart() {
        super.onStart();
        reservation = (Reservation) getIntent().getSerializableExtra("reservation");

        EditText purpose = (EditText) findViewById(R.id.updatePurpose);
        EditText from = (EditText) findViewById(R.id.updateFrom);
        EditText to = (EditText) findViewById(R.id.updateTo);
        purpose.setText(reservation.getPurpose());
        from.setText(reservation.getFromTimeString());
        to.setText(reservation.getToTimeString());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(HelperClass.URL + "rooms/",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        spinner = (Spinner) findViewById(R.id.updateRoomId);
                        final ArrayAdapter<Room> spinnerAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, android.R.id.text1);
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(spinnerAdapter);

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                spinnerAdapter.add(new Room(jsonObject.getInt("Id"), jsonObject.getString("Name"), jsonObject.getString("Description"), jsonObject.getInt("Capacity"), jsonObject.getString("Remarks"), jsonObject.getInt("BuildingId")));
                            } catch (JSONException e) {
                                //new HelperClass().ErrorDialog(CreateReservationActivity.this, null, null);
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
                //new HelperClass().ErrorDialog(CreateReservationActivity.this, null, null);
                Log.e(HelperClass.ERROR, error.toString());
            }
        });
        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }
    public void showFromDatePickerDialog(View v) {
        DialogFragment timeFragment = new UpdateTimePickerFromFragment();
        timeFragment.show(getFragmentManager(), "timePickerFrom");

        DialogFragment dateFragment = new UpdateDatePickerFromFragment();
        dateFragment.show(getFragmentManager(), "datePickerFrom");
    }
    public void showToDatePickerDialog(View v) {
        DialogFragment timeFragment = new UpdateTimePickerToFragment();
        timeFragment.show(getFragmentManager(), "timePickerTo");

        DialogFragment dateFragment = new UpdateDatePickerToFragment();
        dateFragment.show(getFragmentManager(), "datePickerTo");
    }
    public void update(View view) {
        String purpose = ((EditText) findViewById(R.id.updatePurpose)).getText().toString();
        String from = ((EditText) findViewById(R.id.updateFrom)).getText().toString();
        String to = ((EditText) findViewById(R.id.updateTo)).getText().toString();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("login", MODE_PRIVATE);
        JSONObject jsonObject = new JSONObject();
        try {
            Room roomId = (Room) spinner.getSelectedItem();
            jsonObject.put("DeviceId", Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID).toString());
            jsonObject.put("FromTimeString", from);
            jsonObject.put("Purpose", purpose);
            jsonObject.put("RoomId", roomId.getRoomId());
            jsonObject.put("ToTimeString", to);
            jsonObject.put("UserId", pref.getInt("userId", 0));
        } catch (JSONException e) {}
        Log.d("d", jsonObject.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.PUT, HelperClass.URL + "reservations/" + reservation.getReservationId(), jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        finish();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //new HelperClass().ErrorDialog(CreateReservationActivity.this, null, null);
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
