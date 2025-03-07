package net.sistransito.mobile.plate.data

import retrofit2.http.GET
import retrofit2.http.Query

interface PlateService {
    @GET("vehicle/searchPlate")
    suspend fun searchPlate(@Query("plate") plate: String, @Query("type") type: String): DataFromPlate?
}
