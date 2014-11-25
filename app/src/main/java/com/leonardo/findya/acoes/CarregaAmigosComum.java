package com.leonardo.findya.acoes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.leonardo.findya.R;
import com.leonardo.findya.fragments.ListaAmigosComumFrag;
import com.leonardo.findya.fragments.ListaAmigosComumFrag_;
import com.leonardo.findya.outros.App;
import com.leonardo.findya.outros.Usuario;
import com.leonardo.findya.outros.UsuarioDao;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CarregaAmigosComum extends AsyncTask<Void, Void, Void> {
    HashMap<Usuario, Integer> mapa;
    private ProgressDialog dialog;
    private Activity activity;

    public CarregaAmigosComum(Activity paramActivity) {
        activity = paramActivity;
    }

    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(activity, "",
                "Procurando usuários", true);
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        List<Usuario> amigos = UsuarioDao.buscarAmigosDosAmigos();
        if (amigos == null) {
            return null;
        }

        mapa = new HashMap<Usuario, Integer>();
        for (Usuario usuario : amigos) {
            int freq = Collections.frequency(amigos, usuario);
            if (freq > 1) {
                if (usuario.equals(App.getUsuario()) || App.getAmigos().contains(usuario)) {
                    continue;
                }
                mapa.put(usuario, freq);
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        dialog.dismiss();
        if (mapa == null || mapa.isEmpty()) {
            Toast.makeText(App.inst(), R.string.amigosFaceNulo,
                    Toast.LENGTH_SHORT).show();
        } else {
            ListaAmigosComumFrag f = ListaAmigosComumFrag_.builder()
                    .mapa(mapa).build();
            activity.getFragmentManager().beginTransaction()
                    .replace(R.id.frame, f).addToBackStack(null).commit();
        }
    }
}