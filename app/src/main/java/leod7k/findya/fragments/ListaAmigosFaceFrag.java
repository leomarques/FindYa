package leod7k.findya.fragments;

import android.app.Fragment;
import android.widget.ListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import leod7k.findya.R;
import leod7k.findya.adapters.AmigosFaceAdapter;
import leod7k.findya.outros.Usuario;

@EFragment(R.layout.lista_amigos_face_frag)
public class ListaAmigosFaceFrag extends Fragment {
	
	@ViewById
	public ListView listaAmigosFace;
	
	@FragmentArg
	public ArrayList<Usuario> usuarios;
	
	@AfterViews
	public void aoCriar() {
		listaAmigosFace.setAdapter(new AmigosFaceAdapter(usuarios));
	}
	
}
