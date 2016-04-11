package com.example.danielwinther.androidroomreservations;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class RoomActivity extends FragmentActivity {
    private Building building;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private double lon;
    private double lat;
    private double latitude;
    private double longitude;
    private double distance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
    }
    @Override
    protected void onPause() {
        super.onPause();
        try {
            locationManager.removeUpdates(locationListener);
        }
        catch (SecurityException ex) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }
        catch (SecurityException ex) {

        }
    }

    private void showLocation(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }
    @Override
    protected void onStart() {
        super.onStart();

        City city = (City) getIntent().getSerializableExtra("city");
        building = (Building) getIntent().getSerializableExtra("building");
        String query = null;
        try {
            query = URLEncoder.encode(building.getAddress() + "," + city.getName(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            showLocation(lastKnownLocation);
            locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    showLocation(location);
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                public void onProviderEnabled(String provider) {
                }

                public void onProviderDisabled(String provider) {
                }
            };
        }
        catch (SecurityException ex) {

        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, "http://maps.google.com/maps/api/geocode/json?address=" + query + "&ka&sensor=false", null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            lon = ((JSONArray) response.get("results")).getJSONObject(0)
                                    .getJSONObject("geometry").getJSONObject("location")
                                    .getDouble("lng");

                            lat = ((JSONArray) response.get("results")).getJSONObject(0)
                                    .getJSONObject("geometry").getJSONObject("location")
                                    .getDouble("lat");

                            Location locationA = new Location("A");
                            locationA.setLongitude(lon);
                            locationA.setLatitude(lat);

                            try {
                                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                                Location locationB = new Location("B");
                                locationB.setLongitude(longitude);
                                locationB.setLatitude(latitude);

                                distance = locationA.distanceTo(locationB);
                            }
                            catch (SecurityException ex) {

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error != null) Log.e("MainActivity", error.getMessage());

                    }
                });
        Volley.newRequestQueue(this).add(jsonObjectRequest);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SelectedItemFragment selectedItemFragment = new SelectedItemFragment();
        Bundle parameters = new Bundle();
        parameters.putString("text1", building.getName());
        parameters.putString("text2", building.getAddress());
        parameters.putString("text3", String.valueOf(String.format("%.3f", distance / 1000)) + " km");
        selectedItemFragment.setArguments(parameters);
        fragmentTransaction.replace(android.R.id.content, selectedItemFragment);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            fragmentTransaction.replace(android.R.id.content, selectedItemFragment);
        }
        else {
            fragmentTransaction.remove(selectedItemFragment);
        }
        fragmentTransaction.commit();

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
                                new HelperClass().ErrorDialog(RoomActivity.this, null, null);
                                Log.e(HelperClass.ERROR, e.toString());
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new HelperClass().ErrorDialog(RoomActivity.this, null, null);
                Log.e(HelperClass.ERROR, error.toString());
            }
        });
        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }

    public void googleMaps(View view) {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat + "," + lon);
        Intent intent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        intent.setPackage("com.google.android.apps.maps");

        startActivity(intent);
    }
    public void back(View view) {
        finish();
    }
}