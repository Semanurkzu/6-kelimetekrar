package com.example.alttekrarkelimeoyunu;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class KelimeListeleActivity extends AppCompatActivity {

    ListView listeKelimeler;
    VeritabaniYardimcisi vt;
    ArrayList<String> ingList = new ArrayList<>();
    ArrayList<String> turList = new ArrayList<>();
    ArrayList<String> resimList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelime_listele);

        listeKelimeler = findViewById(R.id.listeKelimeler);
        vt = new VeritabaniYardimcisi(this);

        // Veritabanından tüm kelimeleri alıyoruz
        Cursor cursor = vt.tumKelimeleriGetir();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                ingList.add(cursor.getString(1)); // EngWordName
                turList.add(cursor.getString(2)); // TurWordName
                resimList.add(cursor.getString(3)); // Picture yolu
            }
            cursor.close();
        }

        // Listeyi ekrana bağlayan adaptör
        listeKelimeler.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() { return ingList.size(); }
            @Override
            public Object getItem(int i) { return null; }
            @Override
            public long getItemId(int i) { return 0; }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                View satir = getLayoutInflater().inflate(R.layout.kelime_satir, null);

                TextView txtIng = satir.findViewById(R.id.satirIngilizce);
                TextView txtTur = satir.findViewById(R.id.satirTurkce);
                ImageView img = satir.findViewById(R.id.satirResim);

                txtIng.setText(ingList.get(i));
                txtTur.setText(turList.get(i));

                // Resim yolu varsa ekranda göster
                if (resimList.get(i) != null && !resimList.get(i).isEmpty()) {
                    img.setImageURI(Uri.parse(resimList.get(i)));
                }

                return satir;
            }
        });
    }
}