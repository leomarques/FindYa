package com.leonardo.findya.fragments;

import android.app.Fragment;
import android.widget.ListView;

import com.leonardo.findya.R;
import com.leonardo.findya.adapters.AmigosFaceAdapter;
import com.leonardo.findya.outros.Usuario;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

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
