package com.example.sannewinther.mandatoryassignment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RoomActivity extends Activity {
    private Building building;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
    }

    @Override
    protected void onStart() {
        super.onStart();

        building = (Building) getIntent().getSerializableExtra("building");
        TextView name = (TextView) findViewById(R.id.name);
        TextView address = (TextView) findViewById(R.id.address);

        name.setText(building.getName());
        address.setText(building.getAddress());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(HelperClass.URL + "rooms/building/" + building.getBuildingId(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        final List<Room> rooms = new ArrayList<>();
                        final ListView listView = (ListView) findViewById(R.id.listView);
                        ArrayAdapter<Room> adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, rooms);
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Room value = (Room) listView.getItemAtPosition(position);
                                Intent intent = new Intent(getApplicationContext(), ReservationActivity.class);
                                intent.putExtra("room", value);
                                intent.putExtra("position", position);
                                startActivity(intent);
                            }
                        });

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                rooms.add(new Room(jsonObject.getInt("Id"), jsonObject.getString("Name"), jsonObject.getString("Description"), jsonObject.getInt("Capacity"), jsonObject.getString("Remarks"), jsonObject.getInt("BuildingId")));
                            } catch (JSONException e) {
                                new HelperClass().ErrorDialog(getApplicationContext(), null, null);
                                Log.e(HelperClass.ERROR, e.toString());
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new HelperClass().ErrorDialog(getApplicationContext(), null, null);
                Log.e(HelperClass.ERROR, error.toString());
            }
        });
        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }

    public void back(View view) {
        finish();
    }
}