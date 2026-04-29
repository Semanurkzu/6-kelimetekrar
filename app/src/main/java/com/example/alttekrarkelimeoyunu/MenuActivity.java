package com.example.alttekrarkelimeoyunu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    Button btnEkle, btnListele;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu); // Menü tasarımı burada olmalı

        btnEkle = findViewById(R.id.btnKelimeEkle); // XML'deki ID'lerinle aynı olmalı
        btnListele = findViewById(R.id.btnKelimeListele);

        // Kelime Ekleme Sayfasına Git
        btnEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, KelimeEkleActivity.class);
                startActivity(intent);
            }
        });

        // Listeleme Sayfasına Git
        btnListele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, KelimeListeleActivity.class);
                startActivity(intent);
            }
        });
    }
}