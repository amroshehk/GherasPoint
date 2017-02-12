package com.applefish.gheraspoint.activty;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.applefish.gheraspoint.R;
import com.applefish.gheraspoint.classes.ConnectChecked;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AppCompatActivity {

    private int count = 0;
    private TextView connectState;
    Thread connectThread;

    private Handler handler = new Handler( Looper.getMainLooper() );
    private Timer splashTimer = new Timer();
    private TimerTask splashTimerTask;
    private int timerCount = 0;

    //keys
    private static final String LOGIN_KEY= "com.applefish.gheraspoint.LOGIN_KEY";
    //getString
    private String LOGIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView( R.layout.activity_splash_screen );

        connectState = (TextView)findViewById( R.id.connectState );
        LOGIN=getBaseContext().getString(R.string.login);

        connectThread = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(500);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if(count == 0)
                                {connectState.setText("Loading .");}
                                else if(count == 1)
                                {connectState.setText("Loading ..");}
                                else if(count == 2)
                                {connectState.setText("Loading ...");}
                                else if(count == 3)
                                {connectState.setText("Loading ....");}
                                else if(count == 4)
                                {connectState.setText("Loading .....");}
                                else if(count == 5)
                                    count=-1;
                                count++;

                            }
                        });
                    }
                } catch (InterruptedException e) {
                   Log.d("yes","connectThread noooooooooooooooooo" +e);
                }
            }
        };




        splashTimerTask = new TimerTask() {
            @Override
            public void run() {

                handler.postDelayed( new Runnable() {

                    public void run() {

                        try {

                            if ( !ConnectChecked.isNetworkAvailable(getBaseContext()) && !ConnectChecked.isOnline() ) {

                                if(timerCount<3)
                                {
                                    timerCount++;
                                }
                                else
                                {

                                    connectThread.interrupt();

                                    splashTimer.cancel();

                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            connectState.setText("Sorry,Connection Problem");

                                        }
                                    },200);

                                }
                            }

                            else {

                                connectThread.interrupt();
                                connectThread.join();
                                splashTimer.cancel();

                                String checkAlreadyLogin = readSharedPreference(LOGIN_KEY, LOGIN);
                                if (checkAlreadyLogin.equals("visitor") || checkAlreadyLogin.equals(""))
                                {
                                Intent intent = new Intent();
                                intent.setClass( getBaseContext(), LoginActivty.class );
                                startActivity( intent );
                                }
                                else
                                {
                                    Intent intent = new Intent();
                                    intent.setClass( getBaseContext(), MainActivity.class );
                                    startActivity( intent );
                                }

                            }

                        } catch (Exception e) {
                           Log.d("yes","splashTimerTask noooooooooooooooooo"+e);
                        }
                    }
                }, 3000);
            }
        };

        splashTimer.schedule(splashTimerTask, 0, 3500);
        connectThread.start();
    }
    public String readSharedPreference(String key,String s )
    {
        SharedPreferences sharedPref =getBaseContext().getSharedPreferences(key,MODE_PRIVATE);
        //"" is default_value if no vaule
        String loginType = sharedPref .getString(s,"");

        return loginType;
    }

}
