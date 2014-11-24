package com.leonardo.findya.fragments;


import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.leonardo.findya.R;
import com.leonardo.findya.acoes.EnviarSolicitacoesAmizade;
import com.leonardo.findya.outros.App;
import com.leonardo.findya.outros.Usuario;
import com.leonardo.findya.outros.UsuarioDao;
import com.leonardo.findya.outros.Util;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@EFragment(R.layout.mapa_frag)
public class MapaFrag extends Fragment {
    private static final int INTERVALO_ATUALIZACAO = 30000; //30 segundos
    private static final int ANIMATION_DURATION = 3000;//3 segundos
    private static final int BOUND_PADDING = 80;
    private static final float ZOOM = 13;
    private static final int MINDISTMOVE = 10;

    public static final String USUARIOS_EXTRA = "usuariosExtra";
    @FragmentArg
    ArrayList<Usuario> usuarios;

    @ViewById
    ViewGroup mapHost;

    private GoogleMap mapa;
    private MapFragment mf;

    private HashMap<Usuario, Marker> usuarioMarcador;
    private HashMap<Usuario, LatLng> usuarioPosAnt;

    private Handler h;
    private Runnable r;

    @AfterViews
    public void aoCriar() {
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity()) != ConnectionResult.SUCCESS) {
            Toast.makeText(App.inst(), R.string.gplayServIndisponivel, Toast.LENGTH_SHORT).show();
            getFragmentManager().popBackStack();
            return;
        }


        if (mapa == null) {
            FragmentManager fm = getFragmentManager();
            mf = (MapFragment) fm.findFragmentById(R.id.map);
            mapa = mf.getMap();
        }

        if (mapa == null || usuarios == null) {
            getFragmentManager().popBackStack();
            return;
        }

        mapHost.requestTransparentRegion(mapHost);

        usuarioMarcador = new HashMap<Usuario, Marker>();
        usuarioPosAnt = new HashMap<Usuario, LatLng>();
        for (Iterator<Usuario> iterator = usuarios.iterator(); iterator.hasNext();) {
            Usuario usuario = (Usuario) iterator.next();
            if (Util.semLocalizacao(usuario)) {
                iterator.remove();
            } else {
                new MarcaUsuario(usuario).execute();
            }
        }

        try {
            mf.getView().getViewTreeObserver().addOnGlobalLayoutListener(new CalculoZoom());
        } catch (Exception e) {
            getFragmentManager().popBackStack();
        }
        //mapa.setOnInfoWindowClickListener(new AdicionarAmigo());

        h = new Handler();
        r = new Runnable() {
            @Override
            public void run() {
                new MoveMarcadores().execute();
                h.postDelayed(this, INTERVALO_ATUALIZACAO);
            }
        };
    }

    private class AdicionarAmigo implements OnInfoWindowClickListener {
        protected Usuario usuarioSelecionado;

        @Override
        public void onInfoWindowClick(Marker marker) {
            usuarioSelecionado = null;
            for (Usuario usuario : usuarioMarcador.keySet()) {
                if (usuarioMarcador.get(usuario).getId().equals(marker.getId())) {
                    usuarioSelecionado = usuario;
                    break;
                }
            }

            if (usuarioSelecionado == null)
                return;

            //Não permite adicionar você mesmo como amigo.
            if (usuarioSelecionado.equals(App.getUsuario()))
                return;

            //Não permite adicionar quem já é seu amigo.
            List<Usuario> amigos = App.getAmigos();
            if (amigos != null && amigos.contains(usuarioSelecionado)) {
                Toast.makeText(App.inst(), usuarioSelecionado.getNome() + " já é seu amigo.", Toast.LENGTH_SHORT).show();
                return;
            }

            new AlertDialog.Builder(getActivity())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setMessage("Deseja adicionar " + usuarioSelecionado.getNome() + " como amigo?")
                    .setPositiveButton("Sim",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    new EnviarSolicitacoesAmizade(AdicionarAmigo.this.usuarioSelecionado).execute();
                                }
                            }).setNegativeButton("Não", null).show();
        }
    }

    private class CalculoZoom implements OnGlobalLayoutListener {
        @SuppressWarnings("deprecation")
        @Override
        public void onGlobalLayout() {
        	/*
        	 * Esse método é chamado depois que o layout é feito mas antes da exibição, assim podemos
        	 * pegar o tamanho da tela para usar o newLatLngBounds.
        	 */
            if (usuarios.size() == 1) {
                mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(getLatLng(usuarios.get(0)), ZOOM));
                return;
            }

            double menorLat, menorLng, maiorLat, maiorLng;
            LatLng latlng = getLatLng(usuarios.get(0));
            menorLat = latlng.latitude;
            menorLng = latlng.longitude;
            maiorLat = latlng.latitude;
            maiorLng = latlng.longitude;

            for (Usuario usuario : usuarios) {
                latlng = getLatLng(usuario);
    			
    			/* Montamos o LatLngBounds com o southwest de menor lat, lng e
    			 * northeast de maior lat,lng.
    			 */
                menorLat = Math.min(latlng.latitude, menorLat);
                menorLng = Math.min(latlng.longitude, menorLng);
                maiorLat = Math.max(latlng.latitude, maiorLat);
                maiorLng = Math.max(latlng.longitude, maiorLng);
            }

            mapa.moveCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(new LatLng(menorLat, menorLng), new LatLng(maiorLat, maiorLng)),
                    mf.getView().getWidth(), mf.getView().getHeight(), BOUND_PADDING));

            mf.getView().getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
    }

    private class MarcaUsuario extends AsyncTask<Void, Void, Void> {
        Usuario usuario;
        MarkerOptions markerOptions;

        public MarcaUsuario(Usuario paramUsuario) {
            usuario = paramUsuario;
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            String idFace = usuario.getIdFace();
            String nome = usuario.getNome();
            LatLng latlng = getLatLng(usuario);

            try {
                String urlFoto = Util.pegarUrlFotoProfile(idFace);
                Bitmap foto = App.getImageLoader().getBmp(urlFoto);
                BitmapDescriptor bmp = BitmapDescriptorFactory.fromBitmap(foto);
                markerOptions = new MarkerOptions().position(latlng).title(nome).icon(bmp);
            } catch (Exception e) {
                markerOptions = new MarkerOptions().position(latlng).title(nome);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                usuarioMarcador.put(usuario, mapa.addMarker(markerOptions));
                usuarioPosAnt.put(usuario, new LatLng(usuario.getLatitude(), usuario.getLongitude()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class MoveMarcadores extends AsyncTask<Void, Void, Void> {
        List<Usuario> usuariosMarcados;

        @Override
        protected Void doInBackground(Void... arg0) {
            ArrayList<String> idsUsuarios = new ArrayList<String>();
            for (Usuario usuario : usuarioMarcador.keySet()) {
                idsUsuarios.add(usuario.getIdFace());
            }
            usuariosMarcados = UsuarioDao.buscarUsuariosPorIdFace(idsUsuarios);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (usuariosMarcados == null || usuariosMarcados.isEmpty()) {
                return;
            }

            for (Usuario usuario : usuariosMarcados) {
                if (distMaiorMin(usuario)) {
                    LatLng posUsuario = getLatLng(usuario);
                    animateMarker(usuarioMarcador.get(usuario), posUsuario, false, false);
                    usuarioPosAnt.put(usuario, posUsuario);
                }
            }
        }

        private boolean distMaiorMin(Usuario usuario) {
            //Se a distância for menor que MINDISTMOVE, não atualize o marcador
            float[] dist = new float[2];
            Location.distanceBetween(usuario.getLatitude(), usuario.getLongitude(),
                    usuarioPosAnt.get(usuario).latitude, usuarioPosAnt.get(usuario).longitude, dist);

            return dist[0] > MINDISTMOVE;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (h != null)
            h.postDelayed(r, INTERVALO_ATUALIZACAO);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (h != null)
            h.removeCallbacks(r);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MapFragment f = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        if (f != null)
            getFragmentManager().beginTransaction().remove(f).commit();
    }

    public void animateMarker(final Marker marker, final LatLng toPosition, final boolean hideMarker, final boolean follow) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mapa.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);

        final LinearInterpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / ANIMATION_DURATION);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (follow)
                    mapa.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lng)));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

    private LatLng getLatLng(Usuario usuario) {
        double latitude = usuario.getLatitude();
        double longitude = usuario.getLongitude();
        return new LatLng(latitude, longitude);
    }
}