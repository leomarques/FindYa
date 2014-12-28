package leod7k.findya.acoes;

import android.os.AsyncTask;
import android.widget.Toast;

import java.util.ArrayList;

import leod7k.findya.R;
import leod7k.findya.outros.App;
import leod7k.findya.outros.Usuario;
import leod7k.findya.outros.UsuarioDao;

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
