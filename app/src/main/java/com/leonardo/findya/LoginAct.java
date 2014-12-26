package com.leonardo.findya;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.leonardo.findya.outros.App;
import com.leonardo.findya.outros.InstallationId;
import com.leonardo.findya.outros.Usuario;
import com.leonardo.findya.outros.UsuarioDao;
import com.leonardo.findya.outros.Util;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.login)
public class LoginAct extends Activity {
    @ViewById
    public LoginButton loginButton;
    private UiLifecycleHelper uiHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!Util.isOnline()) {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.semConexao)
                    .setPositiveButton("Fechar",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    LoginAct.this.finish();
                                }
                            }).show();
        }

        uiHelper = new UiLifecycleHelper(this, null);
        uiHelper.onCreate(savedInstanceState);
    }

    @AfterViews
    public void aoCriar() {
        List<String> permissions = new ArrayList<String>();
        permissions.add("user_friends");
        loginButton.setReadPermissions(permissions);
        loginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                if (user != null) {
                    new CarregaUsuario(user).execute();
                }
            }
        });
    }

    private class CarregaUsuario extends AsyncTask<Void, Void, Void> {
        private GraphUser user;
        ProgressDialog dialog;

        public CarregaUsuario(GraphUser paramUser) {
            user = paramUser;
        }

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(LoginAct.this, "", "Carregando", true);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Usuario usuario = UsuarioDao.buscarUsuarioPorIdFace(user.getId());
            if (usuario == null) {
                usuario = new Usuario();
                usuario.setIdFace(user.getId());
                usuario.setNome(user.getName());
                App.setUsuario(usuario);
                UsuarioDao.cadastrarUsuario();
            } else {
                App.setUsuario(usuario);
            }
/*
            GCMRegistrar.register(App.inst(), App.GCM_PROJECT_NUMBER);
            App.getUsuario().setIdGcm(GCMRegistrar.getRegistrationId(App.inst()));
            UsuarioDao.salvarIdGcm();
*/
            usuario.setIdInstalacao(InstallationId.id());
            UsuarioDao.cadastrarDispositivo();

            App.atualizarAmigos();
            App.salvarUsuario();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            dialog.dismiss();
            TelaPrincipalAct_.intent(LoginAct.this).start();
            finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

}
