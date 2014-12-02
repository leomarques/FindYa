package com.leonardo.findya.outros;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

public class Util {
    public static final String LOGTAG = "findya";

    public static final String URL_PROFILE_PICTURE_INICIO = "https://graph.facebook.com/";
    public static final String URL_PROFILE_PICTURE_FIM = "/picture";

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

    public static BufferedReader jsonPost(String url, String json) {
        try {
            HttpClient client = new DefaultHttpClient();
            URI website = new URI(url);

            HttpPost post = new HttpPost();
            post.setURI(website);

            StringEntity se = new StringEntity(json);
            se.setContentType("text/xml");
            post.setEntity(se);

            HttpResponse response = client.execute(post);
            Log.i(Util.LOGTAG, response.getStatusLine().toString() + " - " + url);

            return new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));
        } catch (URISyntaxException e) {
            Log.e("httpiml", "URISyntaxException  -" + e.toString());
        } catch (ClientProtocolException e) {
            Log.e("httpiml", "ClientProtocolException  -" + e.toString());
        } catch (IOException e) {
            Log.e("httpiml", "IOException  -" + e.toString());
        }
        return null;
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
    public static Bitmap pegarFotoUsuario() {
        return App.getImageLoader().getBmp(pegarUrlFotoProfile(App.getUsuario().getIdFace()));
    }
}