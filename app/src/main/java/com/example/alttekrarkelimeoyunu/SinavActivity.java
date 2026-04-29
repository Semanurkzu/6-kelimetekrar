package com.example.alttekrarkelimeoyunu;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SinavActivity extends AppCompatActivity {

    // XML Elemanları
    TextView tvIngilizce, tvCounter;
    EditText editCevap;
    Button btnKontrol;

    // Veritabanı ve Değişkenler
    VeritabaniYardimcisi vt;
    String dogruCevap;
    int aktifKelimeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinav);

        // Görsel nesneleri bağlıyoruz
        tvIngilizce = findViewById(R.id.tvIngilizceKelime);
        tvCounter = findViewById(R.id.tvCounter);
        editCevap = findViewById(R.id.editCevap);
        btnKontrol = findViewById(R.id.btnKontrolEt);

        vt = new VeritabaniYardimcisi(this);

        // İlk soruyu yükle
        yeniSoruGetir();

        // Kontrol Et Butonu İşlemi
        btnKontrol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kullaniciCevabi = editCevap.getText().toString().trim();

                if (kullaniciCevabi.isEmpty()) {
                    Toast.makeText(SinavActivity.this, "Lütfen bir cevap yazın!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Cevap kontrolü (Büyük-küçük harf duyarsız)
                if (kullaniciCevabi.equalsIgnoreCase(dogruCevap)) {
                    // STORY 5: Doğru bildiğinde sayacı artır
                    vt.sayaciArtir(aktifKelimeId);
                    Toast.makeText(SinavActivity.this, "Tebrikler! Doğru Bildin.", Toast.LENGTH_SHORT).show();

                    editCevap.setText(""); // Kutuyu temizle
                    yeniSoruGetir(); // Hemen yeni soruya geç
                } else {
                    // Yanlış cevap durumu
                    Toast.makeText(SinavActivity.this, "Yanlış Cevap! Doğrusu: " + dogruCevap, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void yeniSoruGetir() {
        // Veritabanından sadece Counter < 6 olanlardan rastgele bir tane çekiyoruz
        Cursor cursor = vt.rastgeleKelimeGetir();

        if (cursor != null && cursor.moveToFirst()) {
            // Veritabanındaki sütun isimleriyle birebir eşleşmeli!
            aktifKelimeId = cursor.getInt(cursor.getColumnIndexOrThrow("WordID"));
            String ingilizce = cursor.getString(cursor.getColumnIndexOrThrow("EngWordName"));
            dogruCevap = cursor.getString(cursor.getColumnIndexOrThrow("TurWordName"));
            int sayac = cursor.getInt(cursor.getColumnIndexOrThrow("Counter"));

            tvIngilizce.setText(ingilizce);
            tvCounter.setText("Öğrenilme Seviyesi: " + sayac + "/6");

            cursor.close(); // Bellek sızıntısını önlemek için kapatıyoruz
        } else {
            // Eğer sorgu boş dönerse (Tüm kelimeler 6 olduysa)
            tvIngilizce.setText("Harika! Öğrenilecek kelime kalmadı.");
            tvCounter.setText("İlerleme: 100%");
            btnKontrol.setEnabled(false); // Butonu pasif yap
            editCevap.setVisibility(View.GONE); // Giriş kutusunu gizle

            if (cursor != null) cursor.close();
        }
    }
}