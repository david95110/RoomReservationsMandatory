package com.example.danielwinther.androidroomreservations;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ReservationDetailActivity extends FragmentActivity {
    private Reservation reservation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_detail);
    }

    @Override
    protected void onStart() {
        super.onStart();

        reservation = (Reservation) getIntent().getSerializableExtra("reservation");
        TextView purpose = (TextView) findViewById(R.id.purpose);
        TextView fromTime = (TextView) findViewById(R.id.fromTime);
        TextView toTime = (TextView) findViewById(R.id.toTime);

        purpose.setText(reservation.getPurpose());
        fromTime.setText(reservation.getFromTimeString());
        toTime.setText(reservation.getToTimeString());

        SharedPreferences pref = getApplicationContext().getSharedPreferences("login", MODE_PRIVATE);
        if (pref.getInt("userId", 0) != reservation.getUserId()) {
            Button updateButton = (Button) findViewById(R.id.updateReservation);
            updateButton.setVisibility(View.GONE);
            Button deleteButton = (Button) findViewById(R.id.deleteReservation);
            deleteButton.setVisibility(View.GONE);
        }
    }
    public void updateReservation(View view) {
        Intent intent = new Intent(this, UpdateReservationActivity.class);
        intent.putExtra("reservation", reservation);
        startActivity(intent);
    }
    public void deleteReservation(View view) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.DELETE, HelperClass.URL + "reservations/" + reservation.getReservationId(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        finish();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //new HelperClass().ErrorDialog(ReservationDetailActivity.this, null, null);
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
        finish();
    }
    public void back(View view) {
        finish();
    }

}