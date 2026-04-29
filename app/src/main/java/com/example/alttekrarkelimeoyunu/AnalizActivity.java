package com.example.alttekrarkelimeoyunu;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AnalizActivity extends AppCompatActivity {

    TextView txtYuzde, txtDetay;
    Button btnRapor;
    VeritabaniYardimcisi vt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analiz);

        txtYuzde = findViewById(R.id.txtBasariYuzdesi);
        txtDetay = findViewById(R.id.txtDetay);
        btnRapor = findViewById(R.id.btnRaporAl);
        vt = new VeritabaniYardimcisi(this);

        // Veritabanından başarıyı hesapla
        int yuzde = vt.basariYuzdesiHesapla();
        txtYuzde.setText("%" + yuzde);

        txtDetay.setText("Toplam kelimelerinizin %" + yuzde + " kadarı 6 aşamayı başarıyla tamamladı.");

        btnRapor.setOnClickListener(v -> {
            // Hocanın istediği PDF/Çıktı alma kısmı burası
            Toast.makeText(this, "Rapor PDF olarak kaydedildi (Simüle Edildi)", Toast.LENGTH_LONG).show();
        });
    }
}