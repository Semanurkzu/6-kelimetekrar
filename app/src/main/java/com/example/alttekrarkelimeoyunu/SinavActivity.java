package com.example.alttekrarkelimeoyunu;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SinavActivity extends AppCompatActivity {

    ImageView imgKelime;
    TextView txtSoru;
    Button btnA, btnB, btnC, btnD;
    VeritabaniYardimcisi vt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinav);

        // XML Bağlantıları
        imgKelime = findViewById(R.id.imgKelime);
        txtSoru = findViewById(R.id.txtSoru);
        btnA = findViewById(R.id.btnSikA);
        btnB = findViewById(R.id.btnSikB);
        btnC = findViewById(R.id.btnSikC);
        btnD = findViewById(R.id.btnSikD);

        vt = new VeritabaniYardimcisi(this);

        yeniSoruGetir();
    }

    public void yeniSoruGetir() {
        Cursor cursor = vt.sinavKelimeGetir();

        if (cursor != null && cursor.moveToFirst()) {
            // Verileri çek
            final int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            final String ing = cursor.getString(cursor.getColumnIndexOrThrow("ingilizce"));
            final String tr = cursor.getString(cursor.getColumnIndexOrThrow("turkce"));
            final int dogruSayisi = cursor.getInt(cursor.getColumnIndexOrThrow("dogruSayisi"));

            // Ekrana yazdır
            txtSoru.setText("Bu kelimenin anlamı nedir?\n\n" + ing.toUpperCase());

            // Şimdilik test için doğru cevabı A şıkkına koyuyoruz
            btnA.setText(tr);
            btnB.setText("Yanlış Cevap 1");
            btnC.setText("Yanlış Cevap 2");
            btnD.setText("Yanlış Cevap 3");

            // --- DOĞRU CEVAP KONTROLÜ ---
            btnA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Story 3 Algoritması: Doğru bilirse sayacı artır ve tarihi güncelle
                    vt.kelimeDurumGuncelle(id, true);
                    Toast.makeText(SinavActivity.this, "Tebrikler! Seviye: " + (dogruSayisi + 1), Toast.LENGTH_SHORT).show();
                    yeniSoruGetir();
                }
            });

            // --- YANLIŞ CEVAP KONTROLÜ ---
            View.OnClickListener yanlisDinleyici = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Story 3 Algoritması: Yanlış bilirse sayacı 0'la
                    vt.kelimeDurumGuncelle(id, false);
                    Toast.makeText(SinavActivity.this, "Yanlış! Kelime sıfırlandı.", Toast.LENGTH_SHORT).show();
                    yeniSoruGetir();
                }
            };

            btnB.setOnClickListener(yanlisDinleyici);
            btnC.setOnClickListener(yanlisDinleyici);
            btnD.setOnClickListener(yanlisDinleyici);

        } else {
            // Soru kalmadıysa
            txtSoru.setText("Harika! Şimdilik sorulacak kelime kalmadı.");
            btnA.setVisibility(View.GONE);
            btnB.setVisibility(View.GONE);
            btnC.setVisibility(View.GONE);
            btnD.setVisibility(View.GONE);
        }

        if (cursor != null) cursor.close();
    }
}