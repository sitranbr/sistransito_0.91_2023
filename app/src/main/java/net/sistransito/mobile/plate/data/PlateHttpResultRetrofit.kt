package net.sistransito.mobile.plate.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PlateHttpResultRetrofit {
    private const val BASE_URL = "https://api.sistran.app/api/"

    fun create(): PlateService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(PlateService::class.java)
    }
}
