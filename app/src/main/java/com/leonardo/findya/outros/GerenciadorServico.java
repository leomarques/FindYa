package com.leonardo.findya.outros;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

public class GerenciadorServico {
    private final static String NOME_SERVICO = LocalServ_.class.getName();

    public static void mudarServico() {
        if (isServicoAtivado()) {
            desativarServico();
        } else {
            ativarServico();
        }
    }

    public static boolean isServicoAtivado() {
        ActivityManager manager = (ActivityManager) App.inst().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (service.service.getClassName().equals(NOME_SERVICO)) {
                return true;
            }
        }
        return false;
    }

    public static void ativarServico() {
        UsuarioDao.ativarDispositivo();
        Intent i = new Intent(App.inst(), LocalServ_.class);
        App.inst().startService(i);
    }

    private static void desativarServico() {
        Intent i = new Intent(App.inst(), LocalServ_.class);
        App.inst().stopService(i);
    }
}
