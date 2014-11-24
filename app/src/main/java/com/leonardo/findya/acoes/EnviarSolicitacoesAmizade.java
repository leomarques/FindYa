package com.leonardo.findya.acoes;

import android.os.AsyncTask;
import android.widget.Toast;

import com.leonardo.findya.R;
import com.leonardo.findya.outros.App;
import com.leonardo.findya.outros.Usuario;
import com.leonardo.findya.outros.UsuarioDao;

import java.util.ArrayList;

public class EnviarSolicitacoesAmizade extends AsyncTask<Void, Void, Void> {
    private Usuario solicitado;
    private boolean enviada;

    public EnviarSolicitacoesAmizade(Usuario paramUsuario) {
        solicitado = paramUsuario;
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        ArrayList<Usuario> solicitantes = (ArrayList<Usuario>) UsuarioDao
                .buscarSolicitantes();

        if (solicitantes != null && solicitantes.contains(solicitado)) {
            UsuarioDao.aceitarSolicitacaoAmizade(solicitado);
            enviada = false;
        } else {
            UsuarioDao.enviarSolicitacaoAmizade(solicitado);
            App.adicionarSolicitado(solicitado);
            enviada = true;
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        if (enviada) {
            Toast.makeText(App.inst(), R.string.solicitacaoEnviada,
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(App.inst(), R.string.solicitacaoAceita,
                    Toast.LENGTH_SHORT).show();
        }
    }
}
