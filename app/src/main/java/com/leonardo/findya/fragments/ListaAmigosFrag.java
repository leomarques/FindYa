package com.leonardo.findya.fragments;


import android.app.Fragment;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import com.leonardo.findya.R;
import com.leonardo.findya.adapters.AmigosAdapter;
import com.leonardo.findya.comparators.UsuarioDistanciaComparator;
import com.leonardo.findya.outros.App;
import com.leonardo.findya.outros.Usuario;
import com.leonardo.findya.outros.Util;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EFragment(R.layout.lista_amigos_frag)
public class ListaAmigosFrag extends Fragment {
    @ViewById
    public ListView listaAmigos;

    @Override
    public void onResume() {
        super.onResume();

        Log.i("jabaliano", "id=" + App.getUsuario().getIdFace());

        new BaixaAmigos().execute();
    }

    private class BaixaAmigos extends AsyncTask<Void, Void, Void> {
        List<Usuario> amigos;

        @Override
        protected Void doInBackground(Void... arg0) {
            amigos = App.getAmigosAtualizados();
            Log.i("jabaliano", amigos == null ? "amigos nulos" : "qamigos = " + amigos.size());
            if (amigos == null) {
                amigos = new ArrayList<Usuario>();
                return null;
            }

            float[] dist = new float[2];
            for (Usuario amigo : amigos) {
                if (Util.semLocalizacao(App.getUsuario()) || Util.semLocalizacao(amigo)) {
                    amigo.setDistancia(-1.0);
                } else {
                    Location.distanceBetween(App.getUsuario().getLatitude(),
                            App.getUsuario().getLongitude(), amigo.getLatitude(),
                            amigo.getLongitude(), dist);
                    Double distanciaKM = dist[0] / 1000.0;
                    amigo.setDistancia(distanciaKM);
                }
            }

            Collections.sort(amigos, new UsuarioDistanciaComparator());
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                listaAmigos.setAdapter(new AmigosAdapter(getActivity(), amigos));
            } catch (Exception e) {
                Log.i("listaAmigos", "erro p/ popular lista de amigos");
            }
        }
    }
}