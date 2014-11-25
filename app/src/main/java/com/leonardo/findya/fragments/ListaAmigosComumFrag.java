package com.leonardo.findya.fragments;

import android.app.Fragment;
import android.widget.ListView;

import com.leonardo.findya.R;
import com.leonardo.findya.adapters.AmigosComumAdapter;
import com.leonardo.findya.outros.Usuario;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;

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