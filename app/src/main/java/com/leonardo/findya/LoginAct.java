package com.leonardo.findya;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.leonardo.findya.outros.App;
import com.leonardo.findya.outros.InstallationId;
import com.leonardo.findya.outros.Usuario;
import com.leonardo.findya.outros.UsuarioDao;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.login)
public class LoginAct extends FragmentActivity {
    @ViewById
    public LoginButton loginButton;
    private UiLifecycleHelper uiHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*
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
*/
        uiHelper = new UiLifecycleHelper(this, callback);
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

            App.getImageLoader().clearCache();
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

        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState(), null);
        }
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

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.i("fb", "Logged in...");
        } else if (state.isClosed()) {
            Log.i("fb", "Logged out...");
        }
    }

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

}