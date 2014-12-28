package leod7k.findya.fragments;

import android.app.Fragment;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import leod7k.findya.R;
import leod7k.findya.adapters.AmigosAdapter;
import leod7k.findya.comparators.UsuarioDistanciaComparator;
import leod7k.findya.outros.App;
import leod7k.findya.outros.Usuario;
import leod7k.findya.outros.Util;

@EFragment(R.layout.lista_amigos_frag)
public class ListaAmigosFrag extends Fragment {
    @ViewById
    public ListView listaAmigos;
    @ViewById
    public TextView listaVaziaText;

    @Override
    public void onResume() {
        super.onResume();

        List<Usuario> amigos = App.getAmigos();
        if (amigos != null && !amigos.isEmpty()) {
            try {
                listaAmigos.setAdapter(new AmigosAdapter(getActivity(), amigos));
                listaVaziaText.setVisibility(TextView.GONE);
            } catch (Exception e) {
                Log.i(Util.LOGTAG, "erro p/ popular lista de amigos");
            }
        } else {
            listaVaziaText.setVisibility(TextView.VISIBLE);
        }

        new BaixaAmigos().execute();
    }

    private class BaixaAmigos extends AsyncTask<Void, Void, Void> {
        List<Usuario> amigos;

        @Override
        protected Void doInBackground(Void... arg0) {
            amigos = App.getAmigosAtualizados();
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
                if (amigos != null && !amigos.isEmpty()) {
                    listaAmigos.setAdapter(new AmigosAdapter(getActivity(), amigos));
                    listaVaziaText.setVisibility(TextView.GONE);
                }
            } catch (Exception e) {
                Log.i(Util.LOGTAG, "erro p/ popular lista de amigos");
            }
        }
    }
}