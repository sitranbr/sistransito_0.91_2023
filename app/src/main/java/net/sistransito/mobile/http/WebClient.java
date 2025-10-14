package net.sistransito.mobile.http;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import net.sistransito.mobile.database.TinyDB;

public class WebClient {

    //private static final String API_URl = "http://191.252.221.62:3000/";
    private static final String API_URL = "http://talonario.sistran.app/api/";
    private static final String BASE_URL = "http://talonario.sistran.app/api/";
    //private static final String BASE_URl = "http://sistransito.com.br/movel/dosis.pl?";
    //public static final String BASE_URl_TESTE = "http://131.221.217.144:8090/api/v1/renavam/consultaPorPlacaTeste?placa=QDA3455&idEstado=0";

    public static final String CNH_URL = BASE_URL + "op=cnh&dadoscnh=";

    public static final String SEARCH_PLATE = API_URL + "vehicle/searchPlate?plate=";
    public static final String LOGIN_URl = BASE_URL + "auth/dologin?email=";

    public static final String URL_AIT_NUMBER = BASE_URL + "ait_number";
    public static final String URL_TAV_NUMBER = BASE_URL + "tav_number";
    public static final String URL_TCA_NUMBER = BASE_URL + "tca_number";
    public static final String URL_RRD_NUMBER = BASE_URL + "rrd_number";
    public static final String URL_EQUIPMENTS = BASE_URL + "equipment";
    public static final String URL_CLOGIN = BASE_URL + "clogin";

    public static final String URL_REQUEST_SSL = "https://talonario.sistran.app/api/ait";
    public static final String URL_REQUEST = BASE_URL + "cancel";

    public static final String URL_REQUEST_CANCEL = "ait_canceled";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params,
                           AsyncHttpResponseHandler responseHandler, Context c) {
        TinyDB tdb = new TinyDB(c);
        params.put("X-API-KEY", tdb.getString("token"));
        client.cancelAllRequests(true);
        client.get(url, params, responseHandler);
    }

    public static void get(String url, RequestParams params,
                           AsyncHttpResponseHandler responseHandler) {
        client.setConnectTimeout(1000 * 1000);
        client.cancelAllRequests(true);
        client.get(url, params, responseHandler);
    }

    public static void post(String url, RequestParams params,
                            AsyncHttpResponseHandler responseHandler) {
        client.cancelAllRequests(true);
        client.setURLEncodingEnabled(true);
        client.post(url, params, responseHandler);
    }

    public static void post(String url, RequestParams params,
                            AsyncHttpResponseHandler responseHandler, Context c) {
        TinyDB tdb = new TinyDB(c);
        params.put("X-API-KEY", tdb.getString("token"));
        client.cancelAllRequests(true);
        client.post(url, params, responseHandler);
    }

    public static String getLoginUrl(String email, String password) {
        return LOGIN_URl + email + "&password=" + password;
    }
}
