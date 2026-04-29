package com.example.alttekrarkelimeoyunu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // XML Elemanları
    EditText isimKutusu, sifreKutusu;
    Button girisButonu, kayitButonu;
    TextView tvSifreUnuttum;

    // Veritabanı Nesnesi
    VeritabaniYardimcisi vt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bağlantıları Kuruyoruz
        isimKutusu = findViewById(R.id.editIsim);
        sifreKutusu = findViewById(R.id.editSifre);
        girisButonu = findViewById(R.id.btnGiris);
        kayitButonu = findViewById(R.id.btnKayit);
        tvSifreUnuttum = findViewById(R.id.tvSifreUnuttum); // XML'de bu ID ile bir TextView eklemelisin

        vt = new VeritabaniYardimcisi(this);

        // --- STORY 1: KAYIT OLMA ---
        kayitButonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String isim = isimKutusu.getText().toString().trim();
                String sifre = sifreKutusu.getText().toString().trim();

                if (isim.isEmpty() || sifre.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Lütfen tüm alanları doldurun!", Toast.LENGTH_SHORT).show();
                } else {
                    if (vt.kullaniciKaydet(isim, sifre)) {
                        Toast.makeText(MainActivity.this, "Kayıt Başarılı! Giriş yapabilirsiniz.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Kayıt sırasında hata oluştu!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // --- STORY 1: GİRİŞ YAPMA ---
        girisButonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String isim = isimKutusu.getText().toString().trim();
                String sifre = sifreKutusu.getText().toString().trim();

                if (vt.girisKontrol(isim, sifre)) {
                    Toast.makeText(MainActivity.this, "Hoş geldin " + isim, Toast.LENGTH_SHORT).show();

                    // Giriş Başarılı: Menüye Aktar (Bonus Hedef: Intent Geçişi)
                    Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                    startActivity(intent);
                    finish(); // Giriş ekranını kapat
                } else {
                    Toast.makeText(MainActivity.this, "Hatalı kullanıcı adı veya şifre!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // --- STORY 1: ŞİFREMİ UNUTTUM ---
        tvSifreUnuttum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String isim = isimKutusu.getText().toString().trim();

                if (isim.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Şifrenizi görmek için kullanıcı adınızı yazın!", Toast.LENGTH_SHORT).show();
                } else {
                    String bulunanSifre = vt.sifreGetir(isim);
                    if (bulunanSifre != null) {
                        // Şifreyi güvenli bir diyalog kutusunda gösteriyoruz
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Şifre Hatırlatıcı")
                                .setMessage("Sayın " + isim + ", şifreniz: " + bulunanSifre)
                                .setPositiveButton("Anladım", null)
                                .show();
                    } else {
                        Toast.makeText(MainActivity.this, "Bu kullanıcı adına ait kayıt bulunamadı!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}