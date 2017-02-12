package com.applefish.gheraspoint.activty;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.applefish.gheraspoint.classes.ConnectChecked;
import com.applefish.gheraspoint.fcm.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.applefish.gheraspoint.fcm.EndPoints.URL_REGISTER_DEVICE;
import static com.applefish.gheraspoint.fcm.EndPoints.URL_UNREGISTER_DEVICE;

import com.applefish.gheraspoint.R;

public class SettingActivity extends AppCompatActivity {
    //defining views
    // private Button buttonRegister;
    // private EditText editTextEmail;
    private SwitchCompat switchNotification;
    private SwitchCompat switchNotification2;
    private SwitchCompat switchNotification3;
    //keys
    private static final String SETTING_KEY_PUSH = "com.applefish.smartshop.SETTING_KEY_PUSH";
    private static final String SETTING_KEY_SOUND = "com.applefish.smartshop.SETTING_KEY_SOUND";
    private static final String SETTING_KEY_VIBRATE = "com.applefish.smartshop.SETTING_KEY_VIBRATE";
    //getString
    private   String SETTING_PUSH;
    private   String SETTING_SOUND;
    private   String SETTING_VIBRATE;

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //getting views from xml
        // editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        switchNotification=(SwitchCompat) findViewById(R.id.switch1);
        switchNotification2=(SwitchCompat) findViewById(R.id.switch2);
        switchNotification3=(SwitchCompat) findViewById(R.id.switch3);
        SETTING_PUSH=getBaseContext().getString(R.string.saved_setting_push);
        SETTING_SOUND=getBaseContext().getString(R.string.saved_setting_sound);
        SETTING_VIBRATE=getBaseContext().getString(R.string.saved_setting_vibrate);
        //buttonRegister = (Button) findViewById(R.id.buttonRegister);

        String sp_value=readSharedPreference(SETTING_KEY_PUSH,SETTING_PUSH);
        if(sp_value.equals("") || sp_value.equals("off"))
        { switchNotification.setChecked(false);}
        else
        { switchNotification.setChecked(true);}

        String sp_value_sound=readSharedPreference(SETTING_KEY_SOUND,SETTING_SOUND);
        if(sp_value_sound.equals("") || sp_value_sound.equals("off"))
        { switchNotification2.setChecked(false);}
        else
        { switchNotification2.setChecked(true);}

        String sp_value_vibrate=readSharedPreference(SETTING_KEY_VIBRATE,SETTING_VIBRATE);
        if(sp_value_vibrate.equals("") || sp_value_vibrate.equals("off"))
        { switchNotification3.setChecked(false);}
        else
        { switchNotification3.setChecked(true);}


        switchNotification.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                String sp_value=readSharedPreference(SETTING_KEY_PUSH,SETTING_PUSH);
                if(switchNotification.isChecked())
                {
                    if(ConnectChecked.isNetworkAvailable(getBaseContext())&& ConnectChecked.isOnline())
                    {
                        if(sp_value.equals("") || sp_value.equals("off"))
                        {
                            sendTokenToServer();
                            writeSharedPreference("on",SETTING_KEY_PUSH,SETTING_PUSH);
                            switchNotification2.setChecked(true);
                            writeSharedPreference("on",SETTING_KEY_SOUND,SETTING_SOUND);
                            switchNotification3.setChecked(true);
                            writeSharedPreference("on",SETTING_KEY_VIBRATE,SETTING_VIBRATE);
                            //Toast.makeText(getBaseContext(), "Switch on", Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        switchNotification.setChecked(false);
                        Snackbar.make(buttonView, "No Internet Connection", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                }
                else
                {
                    if(ConnectChecked.isNetworkAvailable(getBaseContext())&& ConnectChecked.isOnline())
                    {
                        if(sp_value.equals("on"))
                        {
                            deleteTokenToServer();
                            writeSharedPreference("off",SETTING_KEY_PUSH,SETTING_PUSH);

                            switchNotification2.setChecked(false);
                            writeSharedPreference("off",SETTING_KEY_SOUND,SETTING_SOUND);
                            switchNotification3.setChecked(false);
                            writeSharedPreference("off",SETTING_KEY_VIBRATE,SETTING_VIBRATE);

                            //  Toast.makeText(getBaseContext(), "Switch off", Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        switchNotification.setChecked(true);
                        Snackbar.make(buttonView, "No Internet Connection", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
            }


        });

        //Sound
        switchNotification2.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                String sp_value_sound=readSharedPreference(SETTING_KEY_SOUND,SETTING_SOUND);
                if(switchNotification2.isChecked())
                {

                    if(sp_value_sound.equals("") || sp_value_sound.equals("off"))
                    {
                        writeSharedPreference("on",SETTING_KEY_SOUND,SETTING_SOUND);
                        Toast.makeText(getBaseContext(), "SOUND ON", Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {

                    if(sp_value_sound.equals("on"))
                    {
                        writeSharedPreference("off",SETTING_KEY_SOUND,SETTING_SOUND);
                        Toast.makeText(getBaseContext(), "SOUND OFF", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        //Vibrate
        switchNotification3.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                String sp_value_vibrate=readSharedPreference(SETTING_KEY_VIBRATE,SETTING_VIBRATE);
                if(switchNotification3.isChecked())
                {

                    if(sp_value_vibrate.equals("") || sp_value_vibrate.equals("off"))
                    {
                        writeSharedPreference("on",SETTING_KEY_VIBRATE,SETTING_VIBRATE);
                        Toast.makeText(getBaseContext(), "VIBRATE ON", Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {

                    if(sp_value_vibrate.equals("on"))
                    {
                        writeSharedPreference("off",SETTING_KEY_VIBRATE,SETTING_VIBRATE);
                        Toast.makeText(getBaseContext(), "VIBRATE OFF", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }
    //storing token to mysql server
    private void sendTokenToServer() {
        progressDialog = new ProgressDialog(this);
        // progressDialog.setMessage("Registering Device...");
        progressDialog.setMessage("Notification  Switch...");
        progressDialog.show();

        final String token = SharedPrefManager.getInstance(this).getDeviceToken();
        //final String email = editTextEmail.getText().toString();

        if (token == null) {
            progressDialog.dismiss();
            Toast.makeText(this, "Token not generated", Toast.LENGTH_LONG).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER_DEVICE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            Toast.makeText(SettingActivity.this, obj.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(SettingActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", token);
                params.put("token", token);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    //delete token from mysql server
    private void deleteTokenToServer() {
        progressDialog = new ProgressDialog(this);
        // progressDialog.setMessage("Registering Device...");
        progressDialog.setMessage("Notification switch.....");
        progressDialog.show();

        final String token = SharedPrefManager.getInstance(this).getDeviceToken();
        //final String email = editTextEmail.getText().toString();

        if (token == null) {
            progressDialog.dismiss();
            Toast.makeText(this, "Token not generated", Toast.LENGTH_LONG).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UNREGISTER_DEVICE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            Toast.makeText(SettingActivity.this, obj.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(SettingActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", token);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

//    public void Click(View view) {
//        if (view == buttonRegister) {
//            sendTokenToServer();
//        }
//    }

    public String readSharedPreference(String key,String s )
    {
        SharedPreferences sharedPref =getBaseContext().getSharedPreferences(key,MODE_PRIVATE);
        //0 is default_value if no vaule
        String savedSetting = sharedPref .getString(s,"");

        return savedSetting;
    }
    public  void  writeSharedPreference(String savedSetting,String key,String s )
    {
        SharedPreferences sharedPref =getBaseContext().getSharedPreferences(key,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(s, savedSetting);
        editor.commit();
    }


}
