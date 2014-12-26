package com.leonardo.findya.outros;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class ConversorJson {

    public static Usuario getJsonToUsuario(String jsonResposta) {
        Usuario usuario = null;

        JsonParser parser = new JsonParser();
        Object obj = null;
        try {
            obj = parser.parse(jsonResposta);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if (obj != null && obj.getClass() != JsonNull.class) {
            JsonObject jsonobjeto = (JsonObject) obj;

            JsonObject dado = jsonobjeto.getAsJsonObject();
            usuario = new Gson().fromJson(dado, Usuario.class);

            JsonElement publico = jsonobjeto.get("publico");
            usuario.setPublico(publico.getAsInt() == 1);
        }

        return usuario;
    }

    public static List<Usuario> getJsonToUsuarios(String jsonResposta) {
        List<Usuario> usuarios = null;

        JsonParser parser = new JsonParser();
        Object obj = null;
        try {
            obj = parser.parse(jsonResposta);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if (obj != null && obj.getClass() != JsonNull.class) {
            JsonArray array = (JsonArray) obj;
            usuarios = new ArrayList<Usuario>();

            Gson gson = new Gson();
            for (JsonElement jsonElement : array) {
                JsonObject dado = jsonElement.getAsJsonObject();
                Usuario usuario = gson.fromJson(dado, Usuario.class);
                JsonElement publico = dado.get("publico");
                usuario.setPublico(publico.getAsInt() == 1);
                usuarios.add(usuario);
            }
        }

        return usuarios;
    }

    public static List<String> getJsonToListaIds(String jsonResposta) {
        List<String> listaIds = null;

        JsonParser parser = new JsonParser();
        Object obj = null;
        try {
            obj = parser.parse(jsonResposta);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if (obj != null && obj.getClass() != JsonNull.class) {
            JsonArray array = (JsonArray) obj;
            listaIds = new ArrayList<String>();

            for (JsonElement jsonElement : array) {
                JsonObject jsonobjeto = jsonElement.getAsJsonObject();

                String id = jsonobjeto.get("idFace").getAsString();

                listaIds.add(id);
            }
        }

        return listaIds;
    }

    public static String getJsonToIdInstalacao(String jsonResposta) {
        String idInstalacao = null;

        JsonParser parser = new JsonParser();
        Object obj = null;
        try {
            obj = parser.parse(jsonResposta);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if (obj != null && obj.getClass() != JsonNull.class) {
            JsonElement jsonElement = (JsonElement) obj;

            JsonObject jsonobjeto = jsonElement.getAsJsonObject();
            idInstalacao = jsonobjeto.get("idInstalacao").getAsString();
        }

        return idInstalacao;
    }

    public static String getJsonToStatus(String jsonResposta) {
        String idInstalacao = null;

        JsonParser parser = new JsonParser();
        Object obj = null;
        try {
            obj = parser.parse(jsonResposta);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if (obj != null && obj.getClass() != JsonNull.class) {
            JsonElement jsonElement = (JsonElement) obj;

            JsonObject jsonobjeto = jsonElement.getAsJsonObject();
            idInstalacao = jsonobjeto.get("status").getAsString();
        }

        return idInstalacao;
    }
}
