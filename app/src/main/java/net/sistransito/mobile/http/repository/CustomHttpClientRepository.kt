package net.sistransito.mobile.http.repository

import android.util.Log
import net.sistransito.mobile.plate.data.DataFromPlate
import net.sistransito.mobile.plate.data.PlateHttpResultRetrofit
import net.sistransito.mobile.plate.data.PlateService

class CustomHttpClientRepository private constructor(private val plateService: PlateService) {

    suspend fun searchPlate(plate: String, type: String): DataFromPlate? {
        Log.d(TAG, "Searching plate: $plate, type: $type")
        return plateService.searchPlate(plate, type)
    }

    companion object {
        private const val TAG = "CustomHttpClientRepo"
        fun create(): CustomHttpClientRepository {
            val plateService = PlateHttpResultRetrofit.create()
            return CustomHttpClientRepository(plateService)
        }
    }
}
