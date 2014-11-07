package com.leonardo.findya;

import android.app.Activity;
import android.widget.TextView;

import com.leonardo.findya.outros.App;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.tela_principal_act)
public class TelaPrincipalAct extends Activity {

    @ViewById
    TextView textView;

    @AfterViews
    public void aoCriar() {
        textView.setText(App.getUsuario().getNome());
    }
}
