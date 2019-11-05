package com.avazbaba.tst_android;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.constraint.Group;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.avazbaba.tst_android.Helpers.FileDownloader;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WB_Activity extends AppCompatActivity {
    Button logout, aktualisieren, tour_start, finish, download,aktualisieren2;
    TextView wb1, wb2, uhrzeit1, hellotxt, tor1, tor2, ziel1, ziel2;
    String id = "", pdfname = "", pdfurl = "";
    boolean startcommand = false;
    Group ladezeit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wb__view);
        logout = (Button) findViewById(R.id.logout);
        finish = (Button) findViewById(R.id.finish);
        download = (Button) findViewById(R.id.pdf);
        aktualisieren = (Button) findViewById(R.id.button);
        aktualisieren2 = (Button) findViewById(R.id.aktualisieren);
        tour_start = (Button) findViewById(R.id.tour_start);
        wb1 = (TextView) findViewById(R.id.textView6);
        wb2 = (TextView) findViewById(R.id.textView7);
        tor1 = (TextView) findViewById(R.id.tor1);
        tor2 = (TextView) findViewById(R.id.tor2);
        ziel1 = (TextView) findViewById(R.id.ziel1);
        ziel2 = (TextView) findViewById(R.id.ziel2);
        uhrzeit1 = (TextView) findViewById(R.id.textView);
        hellotxt = (TextView) findViewById(R.id.textView2);
        ladezeit = (Group)findViewById(R.id.groupladezeit);
        getwbdata();
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefManager.getInstance(getApplicationContext()).logout();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        aktualisieren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getwbdata();
            }
        });
        aktualisieren2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getwbdata();
            }
        });
        tour_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTour_start();
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish_tour();
            }
        });
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pdfurl.trim().isEmpty() && !pdfname.trim().isEmpty()) {
                    new DownloadFile().execute(pdfurl, pdfname);
                }
            }
        });
    }

    private void finish_tour() {

        if (id != "") {
            final String jwt = SharedPrefManager.getInstance(getApplicationContext()).getJwt().toString().trim();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.getInstance(getApplicationContext()).getUrl_tour_finish(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                if (!obj.getBoolean("error")) {
                                    Toast.makeText(WB_Activity.this, "Erfolgreich beendet", Toast.LENGTH_LONG);
                                    getwbdata();
                                } else {
                                    Toast.makeText(
                                            getApplicationContext(),
                                            obj.getString("message"),
                                            Toast.LENGTH_LONG
                                    ).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("id", id);
                    params.put("jwt", jwt);
                    return params;
                }
            };
            RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        }
    }

    public void setTour_start() {
        if (id != "") {
            final String jwt = SharedPrefManager.getInstance(getApplicationContext()).getJwt().toString().trim();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.getInstance(getApplicationContext()).getUrl_tour_start(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                if (!obj.getBoolean("error")) {
                                    Toast.makeText(WB_Activity.this, "Erfolgreich gestartet", Toast.LENGTH_LONG);
                                    startcommand = true;
                                    getwbdata();
                                } else {
                                    Toast.makeText(
                                            getApplicationContext(),
                                            obj.getString("message"),
                                            Toast.LENGTH_LONG
                                    ).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("id", id);
                    params.put("jwt", jwt);
                    return params;
                }
            };
            RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        }
    }

    private void getwbdata() {
        pdfurl = "";
        pdfname = "";
        id = "";
        final String user_name = SharedPrefManager.getInstance(getApplicationContext()).getUsername().toString().trim();
        final String user_id = SharedPrefManager.getInstance(getApplicationContext()).getUserId().toString().trim();
        final String jwt = SharedPrefManager.getInstance(getApplicationContext()).getJwt().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.getInstance(getApplicationContext()).getUrl_wb(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                hellotxt.setText("Hallo Herr " + user_name);
                                if (!obj.isNull("wb1_first") && !obj.getString("wb1_first").trim().isEmpty()) {
                                    wb1.setText(obj.getString("wb1_first"));
                                }else{
                                    wb1.setText("");
                                    finish.setVisibility(View.GONE);
                                    tour_start.setVisibility(View.GONE);
                                    aktualisieren2.setVisibility(View.VISIBLE);

                                }
                                if (!obj.isNull("wb2_first") && !obj.getString("wb2_first").trim().isEmpty()) {
                                    wb2.setText(obj.getString("wb2_first"));
                                }else{
                                    wb2.setText("");
                                    finish.setVisibility(View.GONE);
                                    tour_start.setVisibility(View.GONE);
                                    aktualisieren2.setVisibility(View.VISIBLE);
                                }
                                if (!obj.isNull("uhrzeit_first") && !obj.getString("uhrzeit_first").trim().isEmpty()) {
                                    uhrzeit1.setText("Ladezeit ist " + obj.getString("uhrzeit_first"));
                                }
                                if (!obj.isNull("tour_id") && !obj.getString("tour_id").trim().isEmpty()) {
                                    id = obj.getString("tour_id");
                                }
                                if (!obj.isNull("pdf_name") && !obj.getString("pdf_name").trim().isEmpty()) {
                                    pdfname = obj.getString("pdf_name");
                                }
                                if (!obj.isNull("pdf_url") && !obj.getString("pdf_url").trim().isEmpty()) {
                                    pdfurl = obj.getString( "pdf_url");
                                }
                                if (!obj.isNull("tour_start")) {
                                    ladezeit.setVisibility(View.VISIBLE);
                                    tour_start.setEnabled(false);
                                    finish.setVisibility(View.VISIBLE);
                                    tour_start.setVisibility(View.GONE);
                                } else {
                                    finish.setVisibility(View.GONE);
                                    tour_start.setVisibility(View.VISIBLE);
                                    ladezeit.setVisibility(View.GONE);
                                    tour_start.setEnabled(true);
                                }
                                if (obj.isNull("ziel_1") || obj.isNull("ziel_2")) {
                                    ziel1.setText("");
                                    ziel2.setText("");
                                    finish.setVisibility(View.GONE);
                                    tour_start.setVisibility(View.GONE);
                                    hellotxt.setText("Ziel nicht definiert.");
                                    aktualisieren2.setVisibility(View.VISIBLE);
                                } else {
                                    aktualisieren2.setVisibility(View.GONE);
                                    ziel1.setText(obj.getString("ziel_1"));
                                    ziel2.setText(obj.getString("ziel_2"));
                                }
                                if (obj.isNull("tor_1") || obj.isNull("tor_2")) {
                                    tor1.setText(" ");
                                    tor2.setText(" ");
                                    finish.setVisibility(View.GONE);
                                    tour_start.setVisibility(View.GONE);
                                    aktualisieren2.setVisibility(View.VISIBLE);
                                    hellotxt.setText("Bitte Dispo anrufen ");
                                } else {
                                    tor1.setText(obj.getString("tor_1"));
                                    tor2.setText(obj.getString("tor_2"));
                                }
                            } else {
                                wb1.setText("");
                                wb2.setText("");
                                tor1.setText("");
                                tor2.setText("");
                                if (obj.getBoolean("noassigned")) {
                                    hellotxt.setText("Keine zugewiesene Tour für Sie");
                                    uhrzeit1.setText("Ladezeit");

                                    finish.setVisibility(View.GONE);
                                    tour_start.setVisibility(View.GONE);
                                }
                                Toast.makeText(
                                        getApplicationContext(),
                                        obj.getString("message"),
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("bearbeiter", user_id);
                params.put("jwt", jwt);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
    private class DownloadFile extends AsyncTask<String, Void, Void>{
        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];
            String fileName = strings[1];
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "tst_android_pdf");
            folder.mkdir();

            File pdfFile = new File(folder, fileName);
            try{
                pdfFile.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
            FileDownloader.downloadFile(fileUrl, pdfFile);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            File pdfFile = new File(Environment.getExternalStorageDirectory() + "/tst_android_pdf/" + pdfname);
            Uri path = Uri.fromFile(pdfFile);
            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setDataAndType(path, "application/pdf");
            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            try{
                startActivity(pdfIntent);
            }catch(ActivityNotFoundException e){
                Toast.makeText(WB_Activity.this, "No app", Toast.LENGTH_SHORT).show();
            }        }
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }


}
