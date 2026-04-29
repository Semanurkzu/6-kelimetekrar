
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


package com.example.alttekrarkelimeoyunu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class VeritabaniYardimcisi extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "KelimeDB";
    // Versiyonu 2 yapıyoruz ki tablolar güncellensin
    private static final int DATABASE_VERSION = 2;

    public VeritabaniYardimcisi(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 1. Story: Kullanıcılar [cite: 8]
        db.execSQL("CREATE TABLE Kullanicilar (id INTEGER PRIMARY KEY AUTOINCREMENT, isim TEXT, sifre TEXT)");

        // 2. ve 3. Story: Kelimeler ve 6 Sefer Takip Verileri [cite: 12, 15]
        // "cumleler" kısmını hoca ayrı tablo istemiş ama başlangıçta kolaylık için buraya ekliyoruz [cite: 13]
        db.execSQL("CREATE TABLE Kelimeler (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ingilizce TEXT, " +
                "turkce TEXT, " +
                "cumleler TEXT, " +        // Örnek cümleler [cite: 11]
                "resimYolu TEXT, " +       // Resim yolu (C://words/...)
                "dogruSayisi INTEGER DEFAULT 0, " + // 6 sefer algoritması için [cite: 15]
                "sonDogruTarihi LONG DEFAULT 0)");   // Sınav zamanı hesaplama için [cite: 18]
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Kullanicilar");
        db.execSQL("DROP TABLE IF EXISTS Kelimeler");
        onCreate(db);
    }

    // --- KULLANICI İŞLEMLERİ ---
    public boolean kullaniciKaydet(String isim, String sifre) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues degerler = new ContentValues();
        degerler.put("isim", isim);
        degerler.put("sifre", sifre);
        long sonuc = db.insert("Kullanicilar", null, degerler);
        db.close();
        return sonuc != -1;
    }

    public boolean girisKontrol(String isim, String sifre) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Kullanicilar WHERE isim=? AND sifre=?", new String[]{isim, sifre});
        boolean sonuc = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return sonuc;
    }

    // --- KELİME EKLEME (Senin Görevin - Story 2) --- [cite: 9]
    public boolean kelimeEkle(String ing, String tr, String cumleler, String resim) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues degerler = new ContentValues();
        degerler.put("ingilizce", ing);
        degerler.put("turkce", tr);
        degerler.put("cumleler", cumleler);
        degerler.put("resimYolu", resim);

        long sonuc = db.insert("Kelimeler", null, degerler);
        db.close();
        return sonuc != -1;
    }
}

// Sınav zamanı gelmiş kelimeleri getiren fonksiyon (Story 3)
public Cursor sinavKelimeGetir() {
    SQLiteDatabase db = this.getReadableDatabase();
    long su an = System.currentTimeMillis();

    // Sadece zamanı gelmiş ve henüz 6 kez doğru bilinmemiş kelimeleri seç
    return db.rawQuery("SELECT * FROM Kelimeler WHERE sonDogruTarihi <= ? AND dogruSayisi < 6",
            new String[]{String.valueOf(su an)});
}


public long sonrakiTarihiHesapla(int dogruSayisi) {
    long gunMs = 24 * 60 * 60 * 1000L; // 1 günün milisaniye karşılığı
    switch (dogruSayisi) {
        case 1: return System.currentTimeMillis() + (1 * gunMs);      // 1 gün sonra
        case 2: return System.currentTimeMillis() + (7 * gunMs);      // 1 hafta sonra
        case 3: return System.currentTimeMillis() + (30 * gunMs);     // 1 ay sonra
        case 4: return System.currentTimeMillis() + (90 * gunMs);     // 3 ay sonra
        case 5: return System.currentTimeMillis() + (180 * gunMs);    // 6 ay sonra
        case 6: return System.currentTimeMillis() + (365 * gunMs);    // 1 yıl sonra
        default: return System.currentTimeMillis();
    }
}
// Bulmaca için rastgele bir kelime seçer (Story 6)
public Cursor rastgeleKelimeGetir() {
    SQLiteDatabase db = this.getReadableDatabase();
    // RANDOM() fonksiyonu ile her seferinde farklı kelime gelir
    return db.rawQuery("SELECT * FROM Kelimeler ORDER BY RANDOM() LIMIT 1", null);
}