package com.leonardo.findya.outros;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
    private static final int INTERVALO_ATUALIZACAO = 15000;//15 segundos
    private static final int MAX_TENTATIVAS = 10;

    private PowerManager.WakeLock wl;

    private final String LOGTAG = "servico";

    private int tentativas;

    private Location localAtual;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(LOGTAG, "onCreate servico");

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "My Tag");
        wl.acquire();

        tentativas = 0;

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, INTERVALO_ATUALIZACAO, 0, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, INTERVALO_ATUALIZACAO, 0, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(LOGTAG, "onLocationChanged");

        if (isNovoLocalMelhor(location)) {
            localAtual = location;
        } else
        return;
        if (Util.isOnline()) {
            salvarLocalizacaoUsuario(location);
        }
    }

    private boolean isNovoLocalMelhor(Location location) {
        if (localAtual == null)
            return true;

        long timeDelta = location.getTime() - localAtual.getTime();
        boolean isSignificantlyNewer = timeDelta > INTERVALO_ATUALIZACAO;
        boolean isSignificantlyOlder = timeDelta < -INTERVALO_ATUALIZACAO;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - localAtual.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                localAtual.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
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
        Toast.makeText(getApplicationContext(), R.string.providerDesligado, Toast.LENGTH_LONG).show();
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
        /*
        switch (status) {
            case LocationProvider.OUT_OF_SERVICE:
                Toast.makeText(App.inst(), getText(R.string.providerOutOfService) + " - " + provider, Toast.LENGTH_LONG).show();
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Toast.makeText(App.inst(), getText(R.string.providerTemporarilyUnavailable) + " - " + provider, Toast.LENGTH_LONG).show();
                break;
            case LocationProvider.AVAILABLE:
                Toast.makeText(App.inst(), getText(R.string.providerAvailable) + " - " + provider, Toast.LENGTH_LONG).show();
                break;

            default:
                break;
        }
        */
    }
}
