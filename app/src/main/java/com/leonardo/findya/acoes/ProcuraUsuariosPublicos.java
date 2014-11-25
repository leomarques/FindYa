package com.leonardo.findya.acoes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.leonardo.findya.R;
import com.leonardo.findya.fragments.MapaFrag;
import com.leonardo.findya.fragments.MapaFrag_;
import com.leonardo.findya.outros.App;
import com.leonardo.findya.outros.Usuario;
import com.leonardo.findya.outros.UsuarioDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProcuraUsuariosPublicos extends AsyncTask<Void, Void, Void> {

    final double TAM_QUADRANTE = 15;

    ProgressDialog dialog;
    ArrayList<Usuario> usuarios;

    private Activity activity;

    public ProcuraUsuariosPublicos(Activity paramActivity) {
        activity = paramActivity;
    }

    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(activity, "",
                "Procurando usuários", true);
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        // Usuarios sem localização ficam no ponto 0-0
        Map<String, Double> quadrante = calcularQuadrante(App.getUsuario()
                .getLatitude(), App.getUsuario().getLongitude(), TAM_QUADRANTE);
        usuarios = (ArrayList<Usuario>) UsuarioDao
                .buscarUsuariosPublicosNoQuadrante(quadrante);

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        dialog.dismiss();
        if (usuarios == null || usuarios.isEmpty()) {
            Toast.makeText(App.inst(), R.string.usuariosPublicosNulo,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        MapaFrag f = MapaFrag_.builder().usuarios(usuarios).build();
        activity.getFragmentManager().beginTransaction()
                .replace(R.id.frame, f, "mapa").addToBackStack(null).commit();
    }

    /**
     * Este método calcula um quadrante com dois pontos de latitude e dois
     * pontos de longitude. Com isso é possível fechar um quadrado de pontos
     * interessantes (POI).
     */
    @SuppressLint("UseValueOf")
    public static Map<String, Double> calcularQuadrante(Double latitude,
                                                        Double longitude, Double distancia) {
        Map<String, Double> quadrante = new HashMap<String, Double>();

        // Double kilometroEmGrau = 152.58;
        Double kilometroEmGrau = 111.0;

		/*
         * Regra de trés (com exemplo de 20 Km) 152.58 100% ------ = --- 20 x
		 */
        Double x = (distancia * 100) / kilometroEmGrau;
        x = x / 100;

        Double latitudePonto1 = latitude + x;
        Double longitudePonto1 = longitude + x;

        Double latitudePonto2 = latitude - x;
        Double longitudePonto2 = longitude - x;

        // Como estamos trabalhando com valores negativos e positivos a ordem
        // interessa
        if (latitudePonto1 < 0) {
            quadrante.put("lat_1", latitudePonto2);
            quadrante.put("lat_2", latitudePonto1);
        } else {
            quadrante.put("lat_1", latitudePonto1);
            quadrante.put("lat_2", latitudePonto2);
        }

        if (longitudePonto2 < 0) {
            quadrante.put("long_1", longitudePonto2);
            quadrante.put("long_2", longitudePonto1);
        } else {
            quadrante.put("long_1", longitudePonto1);
            quadrante.put("long_2", longitudePonto2);
        }

        return quadrante;
    }
}
