package net.sistransito.mobile.plate.search;

import net.sistransito.mobile.plate.data.DataFromPlate;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlateService {
    @GET("vehicle/searchPlate2")
    Call<DataFromPlate> searchPlate(@Query("plate") String plate);
}
