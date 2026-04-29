package com.example.alttekrarkelimeoyunu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class VeritabaniYardimcisi extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "KelimeEzberleme.db";
    private static final int DATABASE_VERSION = 3;

    public VeritabaniYardimcisi(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // STORY 1: Kullanıcılar
        db.execSQL("CREATE TABLE Users (" +
                "UserID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "UserName TEXT, " +
                "Password TEXT)");

        // STORY 2 & 5: Kelimeler
        db.execSQL("CREATE TABLE Words (" +
                "WordID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "UserID INTEGER, " +
                "EngWordName TEXT, " +
                "TurWordName TEXT, " +
                "Picture TEXT, " +
                "Counter INTEGER DEFAULT 0, " +
                "FOREIGN KEY(UserID) REFERENCES Users(UserID))");

        // Örnek Cümleler
        db.execSQL("CREATE TABLE WordSamples (" +
                "SampleID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "WordID INTEGER, " +
                "SampleSentence TEXT, " +
                "FOREIGN KEY(WordID) REFERENCES Words(WordID))");

        // STORY 4: Ayarlar
        db.execSQL("CREATE TABLE Settings (" +
                "SettingID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "UserID INTEGER, " +
                "DailyLimit INTEGER DEFAULT 10, " +
                "FOREIGN KEY(UserID) REFERENCES Users(UserID))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS WordSamples");
        db.execSQL("DROP TABLE IF EXISTS Words");
        db.execSQL("DROP TABLE IF EXISTS Settings");
        db.execSQL("DROP TABLE IF EXISTS Users");
        onCreate(db);
    }

    // --- STORY 1: KULLANICI İŞLEMLERİ ---
    public boolean kullaniciKaydet(String isim, String sifre) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues degerler = new ContentValues();
        degerler.put("UserName", isim);
        degerler.put("Password", sifre);
        long userId = db.insert("Users", null, degerler);
        if (userId != -1) {
            ContentValues ayarlar = new ContentValues();
            ayarlar.put("UserID", userId);
            ayarlar.put("DailyLimit", 10);
            db.insert("Settings", null, ayarlar);
            return true;
        }
        return false;
    }

    public boolean girisKontrol(String isim, String sifre) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE UserName=? AND Password=?", new String[]{isim, sifre});
        boolean sonuc = cursor.getCount() > 0;
        cursor.close();
        return sonuc;
    }

    // MainActivity'deki hatayı çözen kritik metot:
    public String sifreGetir(String isim) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Password FROM Users WHERE UserName=?", new String[]{isim});
        if (cursor.moveToFirst()) {
            String sifre = cursor.getString(0);
            cursor.close();
            return sifre;
        }
        cursor.close();
        return null;
    }

    // --- STORY 2: KELİME EKLEME (API UYUMLU) ---
    public boolean kelimeEkle(String eng, String tur, String picPath, String sample) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues wordValues = new ContentValues();
            wordValues.put("EngWordName", eng);
            wordValues.put("TurWordName", tur);
            wordValues.put("Picture", picPath);
            wordValues.put("UserID", 1);
            wordValues.put("Counter", 0);

            long wordId = db.insert("Words", null, wordValues);
            if (wordId != -1 && sample != null && !sample.isEmpty()) {
                ContentValues sampleValues = new ContentValues();
                sampleValues.put("WordID", wordId);
                sampleValues.put("SampleSentence", sample);
                db.insert("WordSamples", null, sampleValues);
            }
            db.setTransactionSuccessful();
            return true;
        } finally {
            db.endTransaction();
        }
    }

    // --- STORY 5: SINAV MODU ---
    public Cursor rastgeleKelimeGetir() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Words WHERE Counter < 6 ORDER BY RANDOM() LIMIT 1", null);
    }

    public void sayaciArtir(int wordId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE Words SET Counter = Counter + 1 WHERE WordID = " + wordId);
    }

    // --- ANALİZ MODÜLÜ ---
    public Cursor getIstatistikler() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT " +
                "(SELECT COUNT(*) FROM Words) as Toplam, " +
                "(SELECT COUNT(*) FROM Words WHERE Counter >= 6) as Ogrenilen", null);
    }

    // --- STORY 4: AYARLAR GÜNCELLEME ---
    public boolean gunlukLimitiGuncelle(int yeniLimit) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues degerler = new ContentValues();
        degerler.put("DailyLimit", yeniLimit);
        return db.update("Settings", degerler, "UserID=?", new String[]{"1"}) > 0;
    }

    // --- KELİME LİSTELEME ---
    public Cursor tumKelimeleriGetir() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT Words.*, WordSamples.SampleSentence FROM Words " +
                "LEFT JOIN WordSamples ON Words.WordID = WordSamples.WordID", null);
    }
}