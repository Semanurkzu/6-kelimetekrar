package com.example.alttekrarkelimeoyunu;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SozlukServisi {
    // API adresi: https://api.dictionaryapi.dev/api/v2/entries/en/{kelime}
    // @GET içine yazdığımız değişken kelimeyi API'ye gönderir
    @GET("{kelime}")
    Call<List<KelimeModel>> kelimeDetaylariniGetir(@Path("kelime") String kelime);
}
