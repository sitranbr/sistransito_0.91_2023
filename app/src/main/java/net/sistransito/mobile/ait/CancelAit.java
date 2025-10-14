package net.sistransito.mobile.ait;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.JsonObject;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import net.sistransito.mobile.database.DatabaseCreator;
import net.sistransito.mobile.database.InfractionDatabaseHelper;
import net.sistransito.mobile.http.services.ApiServiceCancelAit;

public class CancelAit {

    private SQLiteDatabase database;
    private InfractionDatabaseHelper databaseHelper;

    private Context context;

    String BASE_URL = "http://talonario.sistran.app/api/";

    public CancelAit(Context context) {
        databaseHelper = new InfractionDatabaseHelper(context);
        //database = databaseHelper.getReadableDatabase(ime.getIME());
        database = databaseHelper.getReadableDatabase();
        this.context = context;
    }

    private ApiServiceCancelAit createApiService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ApiServiceCancelAit.class);
    }

    public void sendCanceledAit(final String ait) {
        AitData aitData = DatabaseCreator
                .getInfractionDatabaseAdapter(context.getApplicationContext())
                .getDataFromAitNumber(ait);

        ApiServiceCancelAit apiService = createApiService();
        Call<JsonObject> call = apiService.sendCanceledAit(
                aitData.getAitNumber(),
                aitData.getCancellationStatus(),
                aitData.getReasonForCancellation(),
                aitData.getCompletedStatus()
        );

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject result = response.body();
                    if (result.get("success").getAsString().equals("true")) {
                        DatabaseCreator.getInfractionDatabaseAdapter(context).synchronizeAit(ait);
                    } else {
                        Log.d("Success: ", "NO");
                    }
                } else {
                    Log.e("sendCanceledAit", "Erro na resposta: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("sendCanceledAit", "Erro na requisição: " + t.getMessage());
            }
        });
    }

}
