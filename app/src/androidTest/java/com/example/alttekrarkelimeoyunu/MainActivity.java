package com.example.alttekrarkelimeoyunu;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // XML’deki ID'ler ile Java'da bağla
        TextView tv1 = findViewById(R.id.textView);   // eskiden text_title
        TextView tv2 = findViewById(R.id.textView2);  // eskiden button_start

        // Tıklama işlemi
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv1.setText("Oyun Başladı!");
            }
        });
    }
}