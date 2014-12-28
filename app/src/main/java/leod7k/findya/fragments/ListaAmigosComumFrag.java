package leod7k.findya.fragments;

import android.app.Fragment;
import android.widget.ListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;

import leod7k.findya.R;
import leod7k.findya.adapters.AmigosComumAdapter;
import leod7k.findya.outros.Usuario;

@EFragment(R.layout.lista_amigos_comum_frag)
public class ListaAmigosComumFrag extends Fragment {
	
	@ViewById
	public ListView listaAmigosComum;
	
	@FragmentArg
	public HashMap<Usuario, Integer> mapa;

	@AfterViews
	public void aoCriar() {
		listaAmigosComum.setAdapter(new AmigosComumAdapter(mapa));
	}
	
}