package net.sistransito.mobile.http.services;

import com.google.gson.JsonObject;

import net.sistransito.mobile.database.InfractionDatabaseHelper;
import net.sistransito.mobile.http.WebClient;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiServiceCancelAit {
    @POST(WebClient.URL_REQUEST_CANCEL)
    @FormUrlEncoded
    Call<JsonObject> sendCanceledAit(
            @Field(InfractionDatabaseHelper.AIT_NUMBER) String aitNumber,
            @Field(InfractionDatabaseHelper.CANCEL_STATUS) String cancelStatus,
            @Field(InfractionDatabaseHelper.REASON_FOR_CANCEL) String reasonForCancel,
            @Field(InfractionDatabaseHelper.COMPLETED_STATUS) String completedStatus
    );
}
