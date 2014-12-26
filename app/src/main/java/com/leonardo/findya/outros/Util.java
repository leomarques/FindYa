package com.leonardo.findya.outros;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.BufferedReader;
import java.io.IOException;

public class Util {
    public static final String LOGTAG = "findya";

    public static final String URL_PROFILE_PICTURE_INICIO = "https://graph.facebook.com/";
    public static final String URL_PROFILE_PICTURE_FIM = "/picture";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static boolean semLocalizacao(Usuario usuario) {
        return usuario.getLatitude() == 0 && usuario.getLongitude() == 0;
    }

    public static boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) App.inst()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public static String pegarUrlFotoProfile(String idFace) {
        return URL_PROFILE_PICTURE_INICIO + idFace + URL_PROFILE_PICTURE_FIM;
    }

    public static void imprimeReader(BufferedReader reader) {
        String a;
        try {
            a = reader.readLine();
            while (a != null) {
                Log.i(LOGTAG, "Constants>: " + a);
                a = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String jsonPost(String url, String json) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        String resposta = null;
        try {
            Response response = App.inst().getClient().newCall(request).execute();
            Log.i(Util.LOGTAG, response.message() + " - " + url);
            resposta = response.body().string();
            return resposta;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resposta;
    }
/*
    public static String gcmPost(String apiKey, String deviceRegistrationId,
                                 Map<String, String> params) throws IOException {

        // Parâmetros necessários para o POST
        StringBuilder postBody = new StringBuilder();
        postBody.append("registration_id").append("=")
                .append(deviceRegistrationId);

        // Cria os parâmetros chave=valor
        Set<String> keys = params.keySet();
        for (String key : keys) {
            String value = params.get(key);
            postBody.append("&").append("data.").append(key).append("=")
                    .append(URLEncoder.encode(value, "UTF-8"));
        }

        // Cria a mensagem
        byte[] postData = postBody.toString().getBytes("UTF-8");

        // Faz POST
        URL url = new URL("https://android.googleapis.com/gcm/send");
        HttpsURLConnection
                .setDefaultHostnameVerifier(new CustomizedHostnameVerifier());
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded;charset=UTF-8");
        conn.setRequestProperty("Content-Length",
                Integer.toString(postData.length));
        conn.setRequestProperty("Authorization", "key=" + apiKey);

        // Lê a resposta
        OutputStream out = conn.getOutputStream();
        out.write(postData);
        out.close();

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            // OK
            String response = conn.getResponseMessage();
            return response;
        } else {
            System.err.println(responseCode + ": " + conn.getResponseMessage());
        }

        return null;
    }

    private static class CustomizedHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
*/
}