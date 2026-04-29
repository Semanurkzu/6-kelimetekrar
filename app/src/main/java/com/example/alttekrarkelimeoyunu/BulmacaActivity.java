package com.example.alttekrarkelimeoyunu;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BulmacaActivity extends AppCompatActivity {

    TextView txtKarisik;
    EditText editTahmin;
    Button btnKontrol;
    VeritabaniYardimcisi vt;
    String dogruKelime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulmaca);

        txtKarisik = findViewById(R.id.txtKarisikHarfler);
        editTahmin = findViewById(R.id.editTahmin);
        btnKontrol = findViewById(R.id.btnKontrolEt);
        vt = new VeritabaniYardimcisi(this);

        oyunuBaslat();

        btnKontrol.setOnClickListener(v -> {
            String tahmin = editTahmin.getText().toString().trim();
            if (tahmin.equalsIgnoreCase(dogruKelime)) {
                Toast.makeText(this, "MÜKEMMEL! Doğru Bildiniz.", Toast.LENGTH_LONG).show();
                oyunuBaslat(); // Yeni kelimeye geç
                editTahmin.setText("");
            } else {
                Toast.makeText(this, "Hatalı Tahmin, Tekrar Deneyin!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void oyunuBaslat() {
        Cursor cursor = vt.rastgeleKelimeGetir();
        if (cursor != null && cursor.moveToFirst()) {
            dogruKelime = cursor.getString(cursor.getColumnIndexOrThrow("ingilizce"));
            txtKarisik.setText(harfleriKaristir(dogruKelime));
            cursor.close();
        } else {
            txtKarisik.setText("Kayıtlı Kelime Yok!");
        }
    }

    // Kelimenin harflerini rastgele karıştıran algoritma
    private String harfleriKaristir(String kelime) {
        List<Character> harfler = new ArrayList<>();
        for (char c : kelime.toUpperCase().toCharArray()) {
            harfler.add(c);
        }
        Collections.shuffle(harfler); // Harfleri karıştır
        StringBuilder sonuc = new StringBuilder();
        for (char c : harfler) {
            sonuc.append(c).append(" ");
        }
        return sonuc.toString().trim();
    }
}