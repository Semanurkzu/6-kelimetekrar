
package com.example.alttekrarkelimeoyunu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class VeritabaniYardimcisi extends SQLiteOpenHelper {

    // Veritabanı adı ve versiyonu
    private static final String DATABASE_NAME = "KelimeDB";
    private static final int DATABASE_VERSION = 1;

    public VeritabaniYardimcisi(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Kullanıcı tablosunu oluşturuyoruz
        db.execSQL("CREATE TABLE Kullanicilar (id INTEGER PRIMARY KEY AUTOINCREMENT, isim TEXT, sifre TEXT)");

        // Story 2 için Kelimeler tablosunu da şimdiden hazır edelim
        db.execSQL("CREATE TABLE Kelimeler (id INTEGER PRIMARY KEY AUTOINCREMENT, ingilizce TEXT, turkce TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Güncelleme gerekirse eski tabloları silip yeniden oluşturur
        db.execSQL("DROP TABLE IF EXISTS Kullanicilar");
        db.execSQL("DROP TABLE IF EXISTS Kelimeler");
        onCreate(db);
    }

    // KAYIT EKLEME FONKSİYONU
    public boolean kullaniciKaydet(String isim, String sifre) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues degerler = new ContentValues();
        degerler.put("isim", isim);
        degerler.put("sifre", sifre);

        // Veriyi ekle
        long sonuc = db.insert("Kullanicilar", null, degerler);

        // VERİTABANINI KAPAT (Donmayı engelleyen kritik satır!)
        db.close();

        return sonuc != -1; // -1 değilse başarılıdır
    }

    // GİRİŞ KONTROL FONKSİYONU
    public boolean girisKontrol(String isim, String sifre) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Kullanıcı adı ve şifreyi sorgula
        Cursor cursor = db.rawQuery("SELECT * FROM Kullanicilar WHERE isim=? AND sifre=?", new String[]{isim, sifre});

        boolean sonuc = cursor.getCount() > 0;

        cursor.close(); // Hafızayı boşalt
        db.close();     // Veritabanını kapat

        return sonuc;
    }
}