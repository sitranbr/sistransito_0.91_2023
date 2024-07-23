package net.sistransito.mobile.plate.data

import android.content.Context
import android.location.Location
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.gc.materialdesign.widgets.ProgressDialog
import net.sistransito.mobile.database.DatabaseCreator
import net.sistransito.mobile.fragment.CallBackPlate
import net.sistransito.mobile.network.NetworkConnection
import net.sistransito.mobile.plate.search.PlateService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PlateHttpResultRetrofit(
    private val listener: CallBackPlate,
    private val context: Context,
    private val isOffline: Boolean,
    private val sPlate: String,
    private val sType: String,
    private val location: Location?
) {
    private val pDialog: ProgressDialog = ProgressDialog(context, "Carregando\n .....")
    private var dataPlate: DataFromPlate? = null
    private val service: PlateService

    init {
        pDialog.setCancelable(true)
        pDialog.setCanceledOnTouchOutside(true)

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(PlateService::class.java)
    }

    fun execute(vararg params: String?) {
        onPreExecute()
        if (isOffline) {
            doInBackgroundOffline()
        } else {
            doInBackgroundOnline()
        }
    }

    private fun onPreExecute() {
        Log.d(TAG, "onPreExecute: Exibindo diálogo de progresso")
        pDialog.show()
    }

    private fun doInBackgroundOffline() {
        Log.d(TAG, "doInBackgroundOffline: Iniciando busca offline")
        try {
            dataPlate = DatabaseCreator.getSearchDataInCard(context).getPlateData(sPlate, sType)
            if (dataPlate != null) {
                DatabaseCreator.getSearchPlateDatabaseAdapter(context)
                    .insertPlateSearchData(dataPlate)
            }
            Handler(Looper.getMainLooper()).post { onPostExecute() }
        } catch (e: Exception) {
            Log.e(TAG, "doInBackgroundOffline: Erro ao acessar dados offline", e)
            showErrorToast("Erro ao acessar dados offline.")
            Handler(Looper.getMainLooper()).post { onPostExecute() }
        }
    }

    private fun doInBackgroundOnline() {
        if (!NetworkConnection.isNetworkAvailable(context)) {
            Log.d(TAG, "doInBackgroundOnline: Sem conexão de rede")
            showErrorToast("Sem conexão de rede.")
            Handler(Looper.getMainLooper()).post { onPostExecute() }
            return
        }

        Log.d(TAG, "doInBackgroundOnline: Iniciando busca online")
        val call = service.searchPlate(sPlate)
        call.enqueue(object : Callback<DataFromPlate?> {
            override fun onResponse(
                call: Call<DataFromPlate?>,
                response: Response<DataFromPlate?>
            ) {
                if (response.isSuccessful) {
                    Log.d(TAG, "onResponse: Busca online bem-sucedida, código de resposta: ${response.code()}")
                    dataPlate = response.body()
                    if (dataPlate != null) {
                        DatabaseCreator.getSearchPlateDatabaseAdapter(context)
                            .insertPlateSearchData(dataPlate)
                        Log.d(TAG, "onResponse: Dados inseridos no banco de dados com sucesso")
                    }
                    Handler(Looper.getMainLooper()).post {
                        listener.callBack(dataPlate, false)
                    }
                } else {
                    Log.e(TAG, "onResponse: Erro ao buscar dados online, código de resposta: ${response.code()}")
                    showErrorToast("Erro ao buscar dados online. Código de resposta: ${response.code()}")
                }
                onPostExecute()
            }

            override fun onFailure(call: Call<DataFromPlate?>, t: Throwable) {
                Log.e(TAG, "onFailure: Erro ao buscar dados online", t)
                showErrorToast("Erro ao buscar dados online. Mensagem: ${t.message}")
                onPostExecute()
            }
        })
    }

    private fun onPostExecute() {
        Log.d(TAG, "onPostExecute: Fechando diálogo de progresso")
        if (pDialog.isShowing) {
            pDialog.dismiss()
        }
    }

    private fun showErrorToast(message: String) {
        Log.d(TAG, "showErrorToast: $message")
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        private const val BASE_URL = "https://api.sistran.app/api/"
        private const val TAG = "PlateHttpResultRetrofit"
    }
}
