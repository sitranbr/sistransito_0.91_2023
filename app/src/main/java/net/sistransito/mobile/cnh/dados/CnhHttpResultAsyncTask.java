package net.sistransito.mobile.cnh.dados;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.gc.materialdesign.widgets.ProgressDialog;

import net.sistransito.mobile.appobject.AppObject;
import net.sistransito.mobile.database.DatabaseCreator;
import net.sistransito.mobile.fragment.CallBackCnh;
import net.sistransito.mobile.http.WebClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CnhHttpResultAsyncTask {
    private final Context context;
    private final boolean isOffline;
    private final String sRegister;
    private final String sTypeSearch;
    private final String sCnh;
    private final CallBackCnh listener;
    private DataFromCnh dataFromCnh;
    private String jsonText;
    private CreateCnhRawDataFromJson createCNHRawData;
    private final ProgressDialog pDialog;
    private final ExecutorService executorService;
    private boolean hasError = false;

    public CnhHttpResultAsyncTask(CallBackCnh listener, Context context, boolean isOffline, DataFromCnh dataFromCnh, String sRegister, String sTypeSearch) {
        this.context = context;
        this.isOffline = isOffline;
        this.dataFromCnh = dataFromCnh;
        this.sRegister = sRegister;
        this.sTypeSearch = sTypeSearch;
        this.listener = listener;
        this.sCnh = null;  // Ajuste conforme necessário
        this.pDialog = new ProgressDialog(context, "Carregando\n" + " .....");
        this.pDialog.setCancelable(true);
        this.pDialog.setCanceledOnTouchOutside(true);
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
                if (dataFromCnh == null) {
                    DataFromCnh dataFromCNH = new DataFromCnh();
                    dataFromCNH.setRegister(sRegister);
                    this.dataFromCnh = DatabaseCreator.getSearchDataInCard(context).getCnhData(dataFromCNH, sTypeSearch);
                } else {
                    dataFromCnh = DatabaseCreator.getSearchDataInCard(context).getCnhData(dataFromCnh, sTypeSearch);
                }
            } catch (Exception e) {
                e.printStackTrace();
                showErrorToast("Erro ao acessar dados offline.");
                hasError = true;
            }
        } else {
            try {
                jsonText = AppObject.getHttpClient().executeHttpGet(WebClient.CNH_URL + sCnh);
                createCNHRawData = new CreateCnhRawDataFromJson(jsonText, context);
                dataFromCnh = createCNHRawData.getDataFromCNH();
                /*
                dataFromCnh.setLATITUDE(String.valueOf(location.getLatitude()));
                dataFromCnh.setLONGITUDE(String.valueOf(location.getLongitude()));
                DatabaseCreator.getPlacaSearchDatabaseAdapter(context).insertPlacaSearchData(dataFromCnh);
                */
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
            listener.callBack(dataFromCnh);
        }
    }

    private void showErrorToast(final String message) {
        new Handler(Looper.getMainLooper()).post(() ->
                Toast.makeText(context, message, Toast.LENGTH_LONG).show());
    }
}
