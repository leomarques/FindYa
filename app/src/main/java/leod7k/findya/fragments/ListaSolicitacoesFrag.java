package leod7k.findya.fragments;

import android.app.Fragment;
import android.widget.ListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import leod7k.findya.R;
import leod7k.findya.adapters.SolicitacoesAdapter;
import leod7k.findya.outros.Usuario;

@EFragment(R.layout.lista_solicitacoes_frag)
public class ListaSolicitacoesFrag extends Fragment {
	
	@ViewById
	public ListView listaSolicitacoes;
	
	@FragmentArg
	public ArrayList<Usuario> usuarios;

	@AfterViews
	public void aoCriar() {
		listaSolicitacoes.setAdapter(new SolicitacoesAdapter(usuarios));
	}
	
}