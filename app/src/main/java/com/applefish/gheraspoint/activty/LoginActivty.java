package com.applefish.gheraspoint.activty;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.applefish.gheraspoint.R;
import com.applefish.gheraspoint.classes.ConnectChecked;
import com.applefish.gheraspoint.fcm.EndPoints;
import com.applefish.gheraspoint.fcm.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;




public class LoginActivty extends AppCompatActivity {
    //activity component
    private Button login;
    private Button visit;
    private AppCompatEditText teamname;
    private AppCompatEditText password;

            //keys
            private static final String LOGIN_KEY= "com.applefish.gheraspoint.LOGIN_KEY";
            //getString
            private String LOGIN;
    //connect to server
  //  private static final String LOGIN_URL = "http://gherasbirr.org/gheraspoints/login.php";
   private static final String LOGIN_URL = "http://192.168.1.2/gheraspoints/login.php";
   // private static final String LOGIN_URL = "http://192.168.1.43/gheraspoints/login.php";
    private String jsonResult;
    //
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activty);
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
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        login= (Button) findViewById(R.id.login);
        visit= (Button) findViewById(R.id.visit);
        teamname= (AppCompatEditText) findViewById(R.id.teamname);
        password= (AppCompatEditText) findViewById(R.id.password);
        LOGIN=getBaseContext().getString(R.string.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tname=teamname.getText().toString();
                String pw=password.getText().toString();

                if ( !ConnectChecked.isNetworkAvailable(getBaseContext()) && !ConnectChecked.isOnline() )
                {
                    Snackbar.make(view, "No Internet Connection", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else
                {
                    sendTokenToServer(tname,pw);

                }

            }
        });
        visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ( !ConnectChecked.isNetworkAvailable(getBaseContext()) && !ConnectChecked.isOnline() )
                {
                    Snackbar.make(view, "No Internet Connection", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }
                else
                {
                    sendTokenToServer("visitor","");


                }
            }
        });
    }

    @Override
    public void onBackPressed()
    {
      //  super.onBackPressed();
        moveTaskToBack(true);
    }

    public String readSharedPreference(String key,String s )
    {
        SharedPreferences sharedPref =getBaseContext().getSharedPreferences(key,MODE_PRIVATE);
        //"" is default_value if no vaule
        String loginType = sharedPref .getString(s,"");

        return loginType;
    }
    public  void  writeSharedPreference(String loginType,String key,String s )
    {
        SharedPreferences sharedPref =getBaseContext().getSharedPreferences(key,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(s, loginType);
        editor.commit();
    }

    private void getResultJSON(String url, final String team, String pw, boolean check) {

        class GetJSON extends AsyncTask<String, Void, String> {
            private ProgressDialog dialog=new ProgressDialog(LoginActivty.this);

            @Override
            protected void onPreExecute()
            {
                this.dialog.setMessage("Login.....");
                this.dialog.show();
            }
            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];
                String result ="";
                BufferedReader bufferedReader = null;
                try {

                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
                    }
                    result=sb.toString().trim();
                     Log.i("get_result", "doInBackground: " +result);
                    return result;

                }catch(Exception e){
                    return null;
                }

            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                jsonResult = result;
                if (dialog.isShowing())
                    dialog.dismiss();
                if ( jsonResult != null)
                {
                        if( jsonResult.toString().equals("{\"result\":\"success\"}")) {
                        //result
                        //save type login as Team name
                        writeSharedPreference(team, LOGIN_KEY, LOGIN);
                        //move to main
                        Intent intent = new Intent();
                        intent.setClass(getBaseContext(), MainActivity.class);
                        startActivity(intent);

                    }
                    else
                        {
                    Toast.makeText(getBaseContext(), "Team name or passowrd error", Toast.LENGTH_SHORT).show();
                       }
                 }
                 else
                {
                Toast.makeText(getBaseContext(), "ERROR", Toast.LENGTH_LONG).show();
                  }

            }
            @Override
            protected void onCancelled()
            {
                if(dialog.isShowing())
                    dialog.dismiss();

            }
        }
        GetJSON gj = new GetJSON();
        if(check)
            gj.execute(url+"?team="+team+"&pw="+pw);
        else
        {
            if(!gj.isCancelled())
                gj.cancel(true);
        }

    }

//    //storing token to mysql server
    private void sendTokenToServer(final String registerType, final String pw) {
//         progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Registering Device...");
//       // progressDialog.setMessage("Notification  Switch...");
//        progressDialog.show();
//
//        final String token = SharedPrefManager.getInstance(this).getDeviceToken();
//        //final String email = editTextEmail.getText().toString();
//
//        if (token == null) {
//            progressDialog.dismiss();
//            Toast.makeText(this, "Token not generated", Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_REGISTER_DEVICE,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        progressDialog.dismiss();
//                        try {
//                            JSONObject obj = new JSONObject(response);
//                            Toast.makeText(LoginActivty.this, obj.getString("message"), Toast.LENGTH_LONG).show();
                            if(registerType.equals("visitor"))
                            {
                                //save type login
                                writeSharedPreference(registerType,LOGIN_KEY,LOGIN);
                                //move to main
                                Intent intent = new Intent();
                                intent.setClass( getBaseContext(), MainActivity.class );
                                startActivity( intent );
                            }
                            else
                            {
                                //connect to server and send pw and tname
                              getResultJSON(LOGIN_URL,registerType,pw,true);
//
                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        progressDialog.dismiss();
//                        Toast.makeText(LoginActivty.this, error.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                }) {
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("registerType", registerType);
//                params.put("token", token);
//                params.put("email", token);
//                return params;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
    }
}
