package com.example.alttekrarkelimeoyunu;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class KelimeEkleActivity extends AppCompatActivity {

    EditText editEng, editTr, editSample;
    Button btnKaydet, btnApi;
    VeritabaniYardimcisi vt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelime_ekle);

        // XML Bağlantıları
        editEng = findViewById(R.id.editIngilizce);
        editTr = findViewById(R.id.editTurkce);
        editSample = findViewById(R.id.editOrnekCumle);
        btnKaydet = findViewById(R.id.btnKelimeKaydet);
        btnApi = findViewById(R.id.btnApiCek);

        vt = new VeritabaniYardimcisi(this);

        // --- 1. API'DEN VERİ ÇEKME BUTONU ---
        btnApi.setOnClickListener(v -> {
            String kelime = editEng.getText().toString().trim();
            if (!kelime.isEmpty()) {
                apiVerileriniGetir(kelime);
            } else {
                Toast.makeText(this, "Lütfen önce bir İngilizce kelime yazın!", Toast.LENGTH_SHORT).show();
            }
        });

        // --- 2. VERİTABANINA KAYDETME BUTONU ---
        btnKaydet.setOnClickListener(v -> {
            String eng = editEng.getText().toString().trim();
            String tr = editTr.getText().toString().trim();
            String sample = editSample.getText().toString().trim();

            if (eng.isEmpty() || tr.isEmpty()) {
                Toast.makeText(this, "Kelime ve anlamı boş olamaz!", Toast.LENGTH_SHORT).show();
            } else {
                if (vt.kelimeEkle(eng, tr, "", sample)) {
                    Toast.makeText(this, "Kelime başarıyla kaydedildi!", Toast.LENGTH_SHORT).show();
                    // Temizle
                    editEng.setText(""); editTr.setText(""); editSample.setText("");
                }
            }
        });
    }

    private void apiVerileriniGetir(String kelime) {
        // Retrofit Yapılandırması
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.dictionaryapi.dev/api/v2/entries/en/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SozlukServisi servis = retrofit.create(SozlukServisi.class);
        Call<List<KelimeModel>> istek = servis.kelimeDetaylariniGetir(kelime);

        // Arka planda veriyi çekiyoruz
        istek.enqueue(new Callback<List<KelimeModel>>() {
            @Override
            public void onResponse(Call<List<KelimeModel>> call, Response<List<KelimeModel>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    KelimeModel model = response.body().get(0);

                    // Verileri kutulara doldur (İlk anlam ve varsa ilk örneği al)
                    if (!model.getMeanings().isEmpty() && !model.getMeanings().get(0).getDefinitions().isEmpty()) {
                        String tanim = model.getMeanings().get(0).getDefinitions().get(0).getDefinition();
                        String ornek = model.getMeanings().get(0).getDefinitions().get(0).getExample();

                        editTr.setText(tanim);
                        editSample.setText(ornek != null ? ornek : "Örnek cümle bu kelime için bulunamadı.");
                        Toast.makeText(KelimeEkleActivity.this, "Bilgiler API'den çekildi!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(KelimeEkleActivity.this, "Kelime sözlükte bulunamadı!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<KelimeModel>> call, Throwable t) {
                Toast.makeText(KelimeEkleActivity.this, "Bağlantı hatası: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}