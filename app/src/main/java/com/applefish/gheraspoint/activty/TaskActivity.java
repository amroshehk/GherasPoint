package com.applefish.gheraspoint.activty;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.applefish.gheraspoint.R;
import com.applefish.gheraspoint.classes.ConnectChecked;
import com.applefish.gheraspoint.classes.Tasks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class TaskActivity extends AppCompatActivity {

    private String jsonResult;
    private static final String TAG_RESULTS="result";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_DESCRIBE= "describe";
    private static final String TAG_POINTS = "points";

   // private static final String TASK_URL ="http://gherasbirr.org/gheraspoints/task.php";
    private static final String TASK_URL ="http://192.168.1.2/gheraspoints/task.php";
    private static ArrayList<Tasks> TasksList;

    private static JSONArray tasksArray = null;


    private ScrollView scrollView;
    //keys
    private static final String LOGIN_KEY= "com.applefish.gheraspoint.LOGIN_KEY";
    //getString
    private String LOGIN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }});

                scrollView=(ScrollView)findViewById(R.id.Scroll_task) ;

       //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                TasksList = new ArrayList<>();
                LOGIN=getBaseContext().getString(R.string.login);

            if (ConnectChecked.isNetworkAvailable(getBaseContext()))
            {
//                Thread getOfferData = new Thread() {
//                    @Override
//                    public void run() {
//                        super.run();
                getJSON(TASK_URL,true);
//                    }
//                };
//
//                getOfferData.start();
            } else {
                Snackbar.make(toolbar, "No Internet Connection", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }


    }
    private void getJSON(String url,boolean check) {


        class GetJSON extends AsyncTask<String, Void, String> {
            private ProgressDialog dialog=new ProgressDialog(TaskActivity.this);

            @Override
            protected void onPreExecute()
            {
                this.dialog.setMessage("Loading.....");
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
                    // Log.i("getJSONOffers", "doInBackground: " +result);
                    return result;

                }catch(Exception e){
                    return null;
                }

            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                jsonResult = result;
                buidlOffersList();
                if(dialog.isShowing())
                    dialog.dismiss();

            }
            @Override
            protected void onCancelled() {
                if(dialog.isShowing())
                    dialog.dismiss();

            }
        }
        GetJSON gj = new GetJSON();

        if(check)
            gj.execute(url+"/?team="+readSharedPreference(LOGIN_KEY,LOGIN));
        else
        {
            if(!gj.isCancelled())
                gj.cancel(true);
        }

    }

    public void buidlOffersList() {

        try {

            if ( jsonResult != null) {

                JSONObject jsonObj = new JSONObject(jsonResult);

                if( !jsonResult.toString().equals("{\"result\":\"NoOffers\"}")) {
                    tasksArray = jsonObj.getJSONArray(TAG_RESULTS);


                    for (int i = 0; i < tasksArray.length(); i++) {

                        JSONObject c = tasksArray.getJSONObject(i);

                        int id = Integer.parseInt(c.getString(TAG_ID));
                        String name = c.getString(TAG_NAME);
                        String describe = c.getString(TAG_DESCRIBE);
                        int points = Integer.parseInt(c.getString(TAG_POINTS));


                        Tasks offer = new Tasks(id, name, describe, points);
                        TasksList.add(offer);

                    }


                    Thread setupTable = new Thread() {

                        @Override
                        public void run() {

                            super.run();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    final TableLayout mTlayout = (TableLayout)findViewById(R.id.table_task);
                                    final TableRow[] tr = {new TableRow(getBaseContext())};
                                    for ( int i=0; i < TasksList.size(); i++ ){


                                        TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                                                TableLayout.LayoutParams.MATCH_PARENT );
                                        params.rightMargin = 5;
                                        params.leftMargin = 5;
                                        params.topMargin = 5;
                                        params.bottomMargin = 5;
                                        tr[0] = new TableRow(getBaseContext());
                                        tr[0].setLayoutParams(params);
                                        //    tr[0].setBackgroundColor(Color.BLACK);
                                        tr[0].setGravity(Gravity.CENTER);

                                        mTlayout.addView(tr[0]);

                                        //create component
                                        //  RelativeLayout relativeLayout = new RelativeLayout(getBaseContext());
                                        LinearLayout linearLayout = new LinearLayout(getBaseContext());
                                        linearLayout.setOrientation(LinearLayout.VERTICAL);
                                        linearLayout.setGravity(Gravity.CENTER);
                                        // linearLayout.setBackgroundColor(Color.GREEN);


//                                        TextView title = new TextView(getBaseContext());
//                                        title.setText( offersList.get(i).getTitle() );
//                                        // title.setBackgroundResource(R.drawable.customborder3);
//                                        title.setTextSize(15);
//                                        title.setGravity(Gravity.CENTER);
//                                        title.setTextColor(Color.rgb(24, 155, 226));
//                                        title.setTypeface(null, Typeface.BOLD);
//
//                                        TextView date = new TextView(getBaseContext());
//                                        date.setText( "Added Date "+offersList.get(i).getDate() );
//                                        date.setBackgroundResource(R.drawable.customborder4);
//                                        date.setTextSize(15);
//                                        date.setGravity(Gravity.CENTER);
//                                        date.setTextColor(Color.WHITE);
//                                        date.setTypeface(null, Typeface.BOLD);
//
//                                        TextView numOfPages = new TextView(getBaseContext());
//                                        numOfPages.setText( "    Pages="+offersList.get(i).getNumberOfPages() );
//                                        numOfPages.setBackgroundResource(R.drawable.customborder7);
//                                        numOfPages.setTextSize(14);
//                                        numOfPages.setGravity(Gravity.CENTER);
//                                        numOfPages.setTextColor(Color.WHITE);
//                                        numOfPages.setTypeface(null, Typeface.BOLD);
//

                                        //  final ImageButton offerCover = new ImageButton(getBaseContext());

                                        LinearLayout.LayoutParams rlp2 = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT,
                                                LinearLayout.LayoutParams.MATCH_PARENT ,65);
                                        // LinearLayout  Params  apply on child (textView Number of pages)
                                        final LinearLayout.LayoutParams rlp3 = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                                LinearLayout.LayoutParams.MATCH_PARENT
                                                ,35
                                        );
                                        // LinearLayout  Params  apply on child (textView Date)
                                        final LinearLayout.LayoutParams rlp4= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                                                ,LinearLayout.LayoutParams.WRAP_CONTENT         );

                                        //  LinearLayout  Params  apply on  (textView Title)
                                        final LinearLayout.LayoutParams rlp5 = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT,1
                                        );


                                        final TableRow.LayoutParams rlp6 = new TableRow.LayoutParams(
                                                0,
                                                TableRow.LayoutParams.MATCH_PARENT
                                                ,1
                                        );
                                        rlp6.gravity= Gravity.CENTER;

                                        //set layout params
                                        linearLayout.setLayoutParams(rlp6);
//                                        // offerCover.setLayoutParams(rlp6);
//                                        date.setLayoutParams(rlp2);
//                                        numOfPages.setLayoutParams(rlp3);
//                                        title.setLayoutParams(rlp5);
//
//                                        tr[0].setBackgroundResource(R.drawable.mybutton_background);
//                                        tr[0].setAddStatesFromChildren(true); // <<<<  this line is the best in the world
//
//
//                                        tr[0].setId( 1100+i) ;
//
//                                        LinearLayout l4=new LinearLayout(getBaseContext());
//                                        l4.setOrientation(LinearLayout.HORIZONTAL);
//                                        // l4.setBackgroundColor(Color.RED);
//                                        l4.setLayoutParams(rlp4);
//                                        l4.addView(date);
//                                        l4.addView(numOfPages);
//                                        linearLayout.addView(title);
//                                        linearLayout.addView(l4);

                                        tr[0].addView(linearLayout);

                                        //  offersCoversList.add(offerCover);

                                        tr[0].setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                Intent pdfViewer = new Intent( );
                                                int tableRowId = ((TableRow)v).getId();
                                                int idoffer=TasksList.get(tableRowId-1100).getId();
                                                //  Toast.makeText(getBaseContext(),pdfUrl,Toast.LENGTH_SHORT).show();
//                                                Toast.makeText(getBaseContext(),"Please,wait.....",Toast.LENGTH_SHORT).show();
                                                // Log.i("getAllImages", "setOnClickListener: " +pdfUrl);
//                                                pdfViewer.putExtra(Key,pdfUrl);
//                                                pdfViewer.putExtra(Key2,idoffer);
//                                                pdfViewer.setClass( getBaseContext(), PdfViewerActivity.class );
                                                startActivity( pdfViewer);

                                            }
                                        });


                                    }
                                }
                            });


                        }
                    };


                    try {
                        setupTable.start();
                        setupTable.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }
                else {
                    Toast.makeText(getBaseContext(), "NO offers", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(getBaseContext(), "An error occurred", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public String readSharedPreference(String key,String s )
    {
        SharedPreferences sharedPref =getBaseContext().getSharedPreferences(key,MODE_PRIVATE);
        //"" is default_value if no vaule
        String loginType = sharedPref .getString(s,"");

        return loginType;
    }

    @Override
    public void onBackPressed() {
        //Stop Tasks
        getJSON( "" ,false);
        TasksList = new ArrayList<>();
        super.onBackPressed();


    }

}
