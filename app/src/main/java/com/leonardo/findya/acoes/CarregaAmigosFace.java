package com.leonardo.findya.acoes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Request.GraphUserListCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.leonardo.findya.R;
import com.leonardo.findya.comparators.UsuarioNomeComparator;
import com.leonardo.findya.fragments.ListaAmigosFaceFrag;
import com.leonardo.findya.fragments.ListaAmigosFaceFrag_;
import com.leonardo.findya.outros.App;
import com.leonardo.findya.outros.Usuario;
import com.leonardo.findya.outros.UsuarioDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CarregaAmigosFace extends AsyncTask<Void, Void, Void> {
    private List<Usuario> todosAmigos;
    private List<String> idsAmigos;
    private ArrayList<Usuario> amigosFaceYa;
    private Activity activity;
    private ProgressDialog dialog;

    public CarregaAmigosFace(Activity paramActivity) {
        activity = paramActivity;
    }

    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(activity, "",
                "Procurando usuários", true);
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        todosAmigos = new ArrayList<Usuario>();
        idsAmigos = new ArrayList<String>();
        amigosFaceYa = new ArrayList<Usuario>();

        // Pegando todos os amigos do facebook
        if (Session.getActiveSession() == null) {
            Session.openActiveSessionFromCache(App.inst());
        }
        if (Session.getActiveSession() == null) {
            return null;
        }
        Request request = Request.newMyFriendsRequest(
                Session.getActiveSession(), new GraphUserListCallback() {
                    @Override
                    public void onCompleted(List<GraphUser> users,
                                            Response response) {
                        if (users == null)
                            return;

                        for (GraphUser friend : users) {
                            Usuario contato = new Usuario();
                            contato.setIdFace(friend.getId());
                            contato.setNome(friend.getName());
                            todosAmigos.add(contato);
                            idsAmigos.add(contato.getIdFace());
                        }
                    }
                });
        Request.executeBatchAndWait(request);

        idsAmigos = UsuarioDao.buscarIdsUsuariosNaoAmigos(idsAmigos);
        if (idsAmigos == null) {
            return null;
        }

        for (Usuario amigo : todosAmigos) {
            if (idsAmigos.contains(amigo.getIdFace())) {
                amigosFaceYa.add(amigo);
            }
        }

        Collections.sort(amigosFaceYa, new UsuarioNomeComparator());

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        dialog.dismiss();
        if (amigosFaceYa.isEmpty()) {
            Toast.makeText(App.inst(), R.string.amigosFaceNulo,
                    Toast.LENGTH_SHORT).show();
        } else {
            ListaAmigosFaceFrag f = ListaAmigosFaceFrag_.builder()
                    .usuarios(amigosFaceYa).build();
            activity.getFragmentManager().beginTransaction()
                    .replace(R.id.frame, f).addToBackStack(null).commit();
        }
    }
}
