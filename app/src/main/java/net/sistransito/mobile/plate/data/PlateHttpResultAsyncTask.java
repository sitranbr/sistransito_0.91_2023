package net.sistransito.mobile.plate.data;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.gc.materialdesign.widgets.ProgressDialog;

import net.sistransito.mobile.appobject.AppObject;
import net.sistransito.mobile.database.DatabaseCreator;
import net.sistransito.mobile.fragment.CallBackPlate;
import net.sistransito.mobile.http.WebClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlateHttpResultAsyncTask {
    public ProgressDialog pDialog;
    private String jsonText = null;
    private Context context;
    private boolean isOffline;
    private DataFromPlate dataPlate;
    private String sPlate, sType;
    public CallBackPlate listener;
    private CreatePlateRawDataFromJson createPlateRawData;
    private Location location;
    private final ExecutorService executorService;
    private boolean hasError = false;

    public PlateHttpResultAsyncTask(final CallBackPlate listener, Context context, final boolean isOffline, String sPlate, String sType, Location location) {
        this.context = context;
        this.isOffline = isOffline;
        this.sPlate = sPlate;
        this.sType = sType;
        this.listener = listener;
        this.location = location;
        pDialog = new ProgressDialog(context, "Carregando\n" + " .....");
        pDialog.setCancelable(true);
        pDialog.setCanceledOnTouchOutside(true);
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public void execute(String... params) {
        onPreExecute();
        executorService.execute(() -> {
            try {
                doInBackground(params);
                new Handler(Looper.getMainLooper()).post(this::onPostExecute);
            } catch (Exception e) {
                e.printStackTrace();
                showErrorToast("Erro ao executar a tarefa.");
                hasError = true;
            }
        });
    }

    private void onPreExecute() {
        pDialog.show();
    }

    private void doInBackground(String... params) {
        if (isOffline) {
            try {
                dataPlate = DatabaseCreator.getSearchDataInCard(context).getPlateData(sPlate, sType);

                if (dataPlate != null) {
                    DatabaseCreator.getSearchPlateDatabaseAdapter(context).insertPlateSearchData(dataPlate);
                }
            } catch (Exception e) {
                showErrorToast("Erro ao acessar dados offline.");
                hasError = true;
            }
        } else {
            try {
                jsonText = AppObject.getHttpClient().executeHttpGet(WebClient.SEARCH_PLATE + sPlate);
                createPlateRawData = new CreatePlateRawDataFromJson(jsonText, context);
                dataPlate = createPlateRawData.getDataFromPlate();

                if (dataPlate != null) {
                    DatabaseCreator.getSearchPlateDatabaseAdapter(context).insertPlateSearchData(dataPlate);
                }
            } catch (Exception e) {
                e.printStackTrace();
                showErrorToast("Erro ao buscar dados online.");
                hasError = true;
            }
        }
    }

    private void onPostExecute() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
        if (!hasError) {
            listener.callBack(dataPlate, isOffline);
        }
    }

    private void showErrorToast(String message) {
        new Handler(Looper.getMainLooper()).post(() ->
                Toast.makeText(context, message, Toast.LENGTH_LONG).show());
    }
}
