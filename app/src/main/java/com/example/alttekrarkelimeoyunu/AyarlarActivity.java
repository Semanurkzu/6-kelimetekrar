package com.example.alttekrarkelimeoyunu;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AyarlarActivity extends AppCompatActivity {
    EditText editLimit;
    Button btnGuncelle;
    VeritabaniYardimcisi vt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayarlar);

        editLimit = findViewById(R.id.editDailyLimit);
        btnGuncelle = findViewById(R.id.btnLimitGuncelle);
        vt = new VeritabaniYardimcisi(this);

        btnGuncelle.setOnClickListener(v -> {
            String limit = editLimit.getText().toString();
            if (!limit.isEmpty()) {
                if (vt.gunlukLimitiGuncelle(Integer.parseInt(limit))) {
                    Toast.makeText(this, "Günlük limit güncellendi!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else {
                Toast.makeText(this, "Lütfen bir sayı girin!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}