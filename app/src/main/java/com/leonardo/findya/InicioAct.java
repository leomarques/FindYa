package com.leonardo.findya;

import android.app.Activity;
import android.os.Bundle;

import com.leonardo.findya.outros.App;

import org.androidannotations.annotations.EActivity;

@EActivity
public class InicioAct extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (App.getUsuario() == null) {
            LoginAct_.intent(this).start();
        } else {
            TelaPrincipalAct_.intent(this).start();
        }
        finish();
    }
}
