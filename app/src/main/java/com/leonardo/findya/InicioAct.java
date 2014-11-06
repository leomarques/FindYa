package com.leonardo.findya;

import android.app.Activity;
import android.os.Bundle;

import org.androidannotations.annotations.EActivity;

@EActivity
public class InicioAct extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LoginAct_.intent(this).start();
    }
}
