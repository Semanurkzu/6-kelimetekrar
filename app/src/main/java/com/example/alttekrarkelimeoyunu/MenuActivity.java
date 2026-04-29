package com.example.alttekrarkelimeoyunu;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    // Değişkenleri tanımlıyoruz
    EditText isimKutusu, sifreKutusu;
    Button girisButonu, kayitButonu;
    VeritabaniYardimcisi vt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // XML elemanlarını Java'ya bağlıyoruz
        isimKutusu = findViewById(R.id.editIsim);
        sifreKutusu = findViewById(R.id.editSifre);
        girisButonu = findViewById(R.id.btnGiris);
        kayitButonu = findViewById(R.id.btnKayit);

        vt = new VeritabaniYardimcisi(this);

        // KAYIT OLMA İŞLEMİ
        kayitButonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gelenIsim = isimKutusu.getText().toString();
                String gelenSifre = sifreKutusu.getText().toString();

                if (vt.kullaniciKaydet(gelenIsim, gelenSifre)) {
                    Toast.makeText(MenuActivity.this, "Kayıt Başarılı!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        girisButonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gelenIsim = isimKutusu.getText().toString();
                String gelenSifre = sifreKutusu.getText().toString();

                if (vt.girisKontrol(gelenIsim, gelenSifre)) {
                    Toast.makeText(MenuActivity.this, "Hoş geldin " + gelenIsim, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MenuActivity.this, "Hatalı Giriş!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}