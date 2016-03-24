package com.example.danielwinther.androidroomreservations;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends FragmentActivity {
    private List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onStart() {
        super.onStart();
        users = new ArrayList<>();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(HelperClass.URL + "users/",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                users.add(new User(jsonObject.getInt("Id"), jsonObject.getString("Name"), jsonObject.getString("Username"), jsonObject.getString("Password")));
                            } catch (JSONException e) {
                                new HelperClass().ErrorDialog(LoginActivity.this, null, null);
                                Log.e(HelperClass.ERROR, e.toString());
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new HelperClass().ErrorDialog(LoginActivity.this, null, null);
                Log.e(HelperClass.ERROR, error.toString());
            }
        });
        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }

    public void login(View view) {
        EditText username = (EditText) findViewById(R.id.username);
        EditText password = (EditText) findViewById(R.id.password);

        String u = username.getText().toString();
        String p = password.getText().toString();
        if (u != null && !u.isEmpty() && !u.equals("null") && p != null && !p.isEmpty() && !p.equals("null")) {
            for (User user : users) {
                if (user.getUsername().equals(u) && user.getPassword().equals(p)) {
                    Intent intent = new Intent(this, CityActivity.class);
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("login", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putInt("userId", user.getUserId());
                    editor.commit();
                    startActivity(intent);
                    break;
                } else {
                    new HelperClass().ErrorDialog(this, null, "Wrong username/password!");
                }
            }
        } else {
            new HelperClass().ErrorDialog(this, null, "Please fill out the fields!");
        }
    }
}