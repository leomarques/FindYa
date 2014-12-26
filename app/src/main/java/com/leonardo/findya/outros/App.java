package com.leonardo.findya.outros;

import android.app.Application;

import com.facebook.Session;
import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;
import java.util.List;

public class App extends Application {
    private static App inst;
    private static Usuario usuario;
    private static List<Usuario> amigos;
    private static List<Usuario> solicitados;
    private OkHttpClient client;

    public static final String ARQUIVO_USUARIO = "usuario";
    private static final String ARQUIVO_AMIGOS = "amigos";
    private static final String ARQUIVO_SOLICITADOS = "solicitados";

    //public static final String GCM_PROJECT_NUMBER = "113853949739";

    public App() {
        inst = this;
    }

    public static App inst() {
        return inst;
    }

    public static void setUsuario(Usuario paramUsuario) {
        usuario = paramUsuario;
    }

    public static Usuario getUsuario() {
        if (usuario == null) {
            usuario = (Usuario) LocalPersistence.readObjectFromFile(ARQUIVO_USUARIO);
        }
        return usuario;
    }

    @SuppressWarnings("unchecked")
    public static List<Usuario> getAmigos() {
        if (amigos == null) {
            amigos = (List<Usuario>) LocalPersistence.readObjectFromFile(ARQUIVO_AMIGOS);
        }

        return amigos;
    }

    @SuppressWarnings("unchecked")
    public static List<Usuario> getSolicitados() {
        if (solicitados == null) {
            solicitados = (List<Usuario>) LocalPersistence.readObjectFromFile(ARQUIVO_SOLICITADOS);
            if (solicitados == null) {
                solicitados = new ArrayList<Usuario>();
                LocalPersistence.writeObjectToFile(solicitados, ARQUIVO_SOLICITADOS);
            }
        }

        return solicitados;
    }

    public static void adicionarSolicitado(Usuario solicitado) {
        solicitados.add(solicitado);
        LocalPersistence.writeObjectToFile(solicitados, ARQUIVO_SOLICITADOS);
    }

    public static void atualizarAmigos() {
        amigos = UsuarioDao.buscarTodosAmigos();
        if (amigos == null) {
            amigos = new ArrayList<Usuario>();
        }
        LocalPersistence.writeObjectToFile(amigos, ARQUIVO_AMIGOS);
    }

    public static void atualizarSolicitados() {
        solicitados = UsuarioDao.buscarSolicitados();
        if (solicitados == null) {
            solicitados = new ArrayList<Usuario>();
        }
        LocalPersistence.writeObjectToFile(solicitados, ARQUIVO_SOLICITADOS);
    }

    public static List<Usuario> getAmigosAtualizados() {
        atualizarAmigos();
        return amigos;
    }

    public static void salvarUsuario() {
        LocalPersistence.writeObjectToFile(usuario, ARQUIVO_USUARIO);
    }

    public OkHttpClient getClient() {
        if (client == null)
            client = new OkHttpClient();
        return client;
    }

    public static void logOff() {
        usuario = null;
        LocalPersistence.deleteFile(ARQUIVO_USUARIO);
        Session session = Session.getActiveSession();
        if (session == null) {
            session = Session.openActiveSessionFromCache(inst());
        }
        if (session != null) {
            session.closeAndClearTokenInformation();
        }
    }
}