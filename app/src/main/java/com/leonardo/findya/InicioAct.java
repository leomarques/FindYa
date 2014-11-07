package com.leonardo.findya;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.login)
public class InicioAct extends Activity {

    @ViewById
    TextView texto;

    GraphUser usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Session.openActiveSession(this, true, new Session.StatusCallback() {

            // callback when session changes state
            @Override
            public void call(Session session, SessionState state, Exception exception) {
                Log.i("fb", "call");
                if (session.isOpened()) {
                    Log.i("fb", "session.isOpened()");
                    // make request to the /me API
                    Request.newMeRequest(session, new Request.GraphUserCallback() {

                        // callback after Graph API response with user object
                        @Override
                        public void onCompleted(GraphUser user, Response response) {
                            Log.i("fb", "onCompleted");
                            if (user != null) {
                                Log.i("fb", "user != null");
                                usuario = user;
                                aoCriar();
                            }
                        }
                    }).executeAsync();
                }
            }
        });
    }

    @AfterViews
    public void aoCriar() {
        if (usuario != null)
            texto.setText(usuario.getFirstName());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }
}
