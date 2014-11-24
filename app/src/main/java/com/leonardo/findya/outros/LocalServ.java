package com.leonardo.findya.outros;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.leonardo.findya.R;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EService;

@EService
public class LocalServ extends Service implements LocationListener {
    private static final int INTERVALO_ATUALIZACAO = 5000;//30 segundos
    private static final int MAX_TENTATIVAS = 10;

    private PowerManager.WakeLock wl;

    private final String LOGTAG = "servico";

    private int tentativas;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(LOGTAG, "onCreate servico");

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "My Tag");
        wl.acquire();

        tentativas = 0;

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setCostAllowed(false);

        String provider = locationManager.getBestProvider(criteria, true);
        if (provider == null) {
            Toast.makeText(App.inst(), R.string.providerNulo, Toast.LENGTH_SHORT).show();
            stopSelf();
            return;
        }

        locationManager.requestLocationUpdates(provider, INTERVALO_ATUALIZACAO, 0, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(LOGTAG, "onLocationChanged");
        if (Util.isOnline()) {
            salvarLocalizacaoUsuario(location);
        }
    }

    @Background
    public void salvarLocalizacaoUsuario(Location location) {
        String idDispositivo = UsuarioDao.buscarDispositivoAtivo();
        if (idDispositivo == null || !App.getUsuario().getIdInstalacao().equals(idDispositivo)) {
            Log.i(LOGTAG, "salvaLocalizacaoUsuario - " + (idDispositivo == null ? "nulo" : "id = " + idDispositivo));
            if (++tentativas > MAX_TENTATIVAS) {
                Log.i(LOGTAG, "tentativas = " + tentativas);
                stopSelf();
            }
        } else {
            App.getUsuario().setLatitude(location.getLatitude());
            App.getUsuario().setLongitude(location.getLongitude());
            Log.i(LOGTAG, location.getLatitude() + " - " + location.getLongitude());
            UsuarioDao.atualizarLocalizacao();
            App.salvarUsuario();
            tentativas = 0;
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        if (provider.equals(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(getApplicationContext(), R.string.gpsDesligado, Toast.LENGTH_LONG).show();
        }
        Log.i(LOGTAG, "onProviderDisabled - " + provider);
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        wl.release();
        Log.i(LOGTAG, "onDestroy servico");
        ((LocationManager) getSystemService(Context.LOCATION_SERVICE)).removeUpdates(this);
        desativarDispositivoAsync();
    }

    @Background
    public void desativarDispositivoAsync() {
        UsuarioDao.desativarDispositivo();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i(LOGTAG, "onProviderEnabled - " + provider);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i(LOGTAG, "onStatusChanged - " + provider + " - " + status);
        switch (status) {
            case LocationProvider.OUT_OF_SERVICE:
                Toast.makeText(App.inst(), R.string.providerOutOfService, Toast.LENGTH_SHORT).show();
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Toast.makeText(App.inst(), R.string.providerTemporarilyUnavailable, Toast.LENGTH_SHORT).show();
                break;
            case LocationProvider.AVAILABLE:
                Toast.makeText(App.inst(), R.string.providerAvailable, Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }
}
