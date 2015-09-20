package leod7k.findya;

import android.app.Activity;
import android.os.Bundle;

import org.androidannotations.annotations.EActivity;

import leod7k.findya.outros.App;

@EActivity
public class InicioAct extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (App.getUsuario() == null) {//
            LoginAct_.intent(this).start();
        } else {
            TelaPrincipalAct_.intent(this).start();
        }
        finish();
    }
}
