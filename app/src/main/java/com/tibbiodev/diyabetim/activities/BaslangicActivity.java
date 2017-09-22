package com.tibbiodev.diyabetim.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tibbiodev.diyabetim.R;

public class BaslangicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baslangic);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(
                getApplicationContext()
        );
        boolean autoLogin = sharedPreferences.getBoolean("auto_login", false);
        if(autoLogin == true){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }


        final EditText editTextAdSoyad = (EditText) findViewById(R.id.editTextAdSoyad);
        final EditText editTextTelefon = (EditText) findViewById(R.id.editTextTelefon);

        Button btnGirisYap = (Button) findViewById(R.id.btnGirisYap);
        btnGirisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextAdSoyad.getText().length() == 0 ||
                        editTextTelefon.getText().length() == 0){
                    Toast.makeText(getApplicationContext(),
                            "Lütfen bilgileri eksiksiz doldurun",
                            Toast.LENGTH_SHORT).show();
                }
                else{

                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(
                            getApplicationContext()
                    ).edit();
                    editor.putString("kisi_ismi", editTextAdSoyad.getText().toString());
                    editor.putString("kisi_telefon", editTextTelefon.getText().toString());
                    editor.putBoolean("auto_login", true);
                    editor.apply();


                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }

            }
        });
    }
}
