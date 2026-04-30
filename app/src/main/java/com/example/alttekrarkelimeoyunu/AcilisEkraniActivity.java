package com.example.alttekrarkelimeoyunu;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class AcilisEkraniActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acilis_ekrani);

        // 2500 milisaniye (2.5 saniye) sonra MainActivity'ye geç
        new Handler().postDelayed(() -> {
            Intent gecis = new Intent(AcilisEkraniActivity.this, MainActivity.class);
            startActivity(gecis);
            finish(); // Geri tuşuna basınca logoya dönmesin
        }, 2500);
    }
}