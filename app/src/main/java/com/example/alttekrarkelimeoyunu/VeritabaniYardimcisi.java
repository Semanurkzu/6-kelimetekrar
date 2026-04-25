package com.example.alttekrarkelimeoyunu;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class VeritabaniYardimcisi extends SQLiteOpenHelper {

    // Veritabanı sabitleri
    private static final String VERITABANI_ADI = "KelimeEzberle.db";
    private static final int VERITABANI_VERSIYON = 1;

    public VeritabaniYardimcisi(Context bağlam) {
        super(bağlam, VERITABANI_ADI, null, VERITABANI_VERSIYON);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Story 1: Kullanıcı Kayıt, şifremi unuttum ve giriş bölümü için tablo
        db.execSQL("CREATE TABLE Kullanicilar (" +
                "KullaniciID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "KullaniciAdi TEXT, " +
                "Sifre TEXT)");

        // Story 2: Kelime Ekleme modülü için tablo
        db.execSQL("CREATE TABLE Kelimeler (" +
                "KelimeID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "IngilizceKelime TEXT, " +
                "TurkceKarsiligi TEXT, " +
                "ResimYolu TEXT, " +
                "BilmeSayisi INTEGER DEFAULT 0, " +
                "SonrakiSoruTarihi LONG)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int eskiSürüm, int yeniSürüm) {
        db.execSQL("DROP TABLE IF EXISTS Kullanicilar");
        db.execSQL("DROP TABLE IF EXISTS Kelimeler");
        onCreate(db);
    }

    // Kullanıcı Kayıt Metodu (Story 1)[cite: 1]
    public boolean kullaniciKaydet(String ad, String sifre) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues degerler = new ContentValues();
        degerler.put("KullaniciAdi", ad);
        degerler.put("Sifre", sifre);

        long sonuc = db.insert("Kullanicilar", null, degerler);
        return sonuc != -1; // -1 değilse kayıt başarılıdır
    }
}