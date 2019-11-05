package com.avazbaba.tst_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class Add_url extends AppCompatActivity {

    Button enter,rucksetzen;
    ImageButton back;
    TextInputLayout txt;
    EditText url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_url);
        back = (ImageButton) findViewById(R.id.backbtn);
        enter = (Button) findViewById(R.id.add_url_btn);
        rucksetzen = (Button) findViewById(R.id.rucksetzen);
        txt = (TextInputLayout) findViewById(R.id.textInputLayout);
        url =(EditText)findViewById(R.id.currenturl);
        String currenturl = Constants.getInstance(getApplicationContext()).getRoot_url();
        url.setText(currenturl);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urltxt =txt.getEditText().getText().toString().trim();
                SharedPrefManager.getInstance(getApplicationContext()).seturl(urltxt);
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });
        rucksetzen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefManager.getInstance(getApplicationContext()).clearurl();
                startActivity(new Intent(getApplicationContext(),Add_url.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}
