package leod7k.findya.acoes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.ArrayList;

import leod7k.findya.R;
import leod7k.findya.fragments.ListaSolicitacoesFrag;
import leod7k.findya.fragments.ListaSolicitacoesFrag_;
import leod7k.findya.outros.App;
import leod7k.findya.outros.Usuario;
import leod7k.findya.outros.UsuarioDao;

public class CarregaSolicitacoesAmizade extends AsyncTask<Void, Void, Void> {
    ArrayList<Usuario> solicitantes;
    Activity activity;
    private ProgressDialog dialog;

    public CarregaSolicitacoesAmizade(Activity paramActivity) {
        activity = paramActivity;
    }

    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(activity, "", "Carregando Solicitações",
                true);
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        solicitantes = (ArrayList<Usuario>) UsuarioDao.buscarSolicitantes();

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        dialog.dismiss();
        if (solicitantes == null || solicitantes.isEmpty()) {
            Toast.makeText(App.inst(), R.string.solicitacoesNulo,
                    Toast.LENGTH_SHORT).show();
        } else {
            ListaSolicitacoesFrag f = ListaSolicitacoesFrag_.builder()
                    .usuarios(solicitantes).build();
            activity.getFragmentManager().beginTransaction()
                    .replace(R.id.frame, f).addToBackStack(null).commit();
        }
    }
}
