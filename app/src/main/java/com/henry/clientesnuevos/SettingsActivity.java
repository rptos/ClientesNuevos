package com.henry.clientesnuevos;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import Model.Variables;

public class SettingsActivity extends AppCompatActivity {

    String bd = "";
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Switch p = (Switch) findViewById(R.id.switchP);
        final Switch r = (Switch) findViewById(R.id.switchR);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final EditText asunto = (EditText) findViewById(R.id.editTextAsunto);
        final EditText msg = (EditText) findViewById(R.id.editTextMsg);
        final Context c = (Context)this;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = c.getSharedPreferences("perfil",
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("asunto", asunto.getText().toString());
                String t = r.getTextOn().toString();
                editor.putString("msg", msg.getText().toString());
                if (bd.equals("Principal")) {
                    editor.putString("bd", "Principal");
                    Variables.setUrl("192.168.1.250");
                    Variables.setBd("Principal");
                    Variables.setDireccion();
                }
                if (bd.equals("Remoto")) {
                    editor.putString("bd", "Remoto");
                    Variables.setUrl("rptoscoreanos.myq-see.com");
                    Variables.setBd("Remoto");
                    Variables.setDireccion();
                }
                editor.commit();
            }
        });
        p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bd.equals("") || bd.equals("Remoto") || Variables.getBd().equals("Remoto")){
                    p.setChecked(true);
                    r.setChecked(false);
                    bd = "Principal";
                }
                else{
                    r.setChecked(true);
                    p.setChecked(false);
                    bd = "Remoto";
                }
            }
        });
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bd.equals("") || bd.equals("Principal") || Variables.getBd().equals("Principal")){
                    r.setChecked(true);
                    p.setChecked(false);
                    bd = "Remoto";
                }
                else{
                    r.setChecked(true);
                    p.setChecked(false);
                    bd = "Remoto";
                }
            }
        });
        asunto.setText(Variables.getAsunto());
        msg.setText(Variables.getMsg());
        if (Variables.getBd().equals("Remoto")){
            r.setChecked(true);
            p.setChecked(false);
        }
        else{
            p.setChecked(true);
            r.setChecked(false);
        }
    }
}
