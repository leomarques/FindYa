package com.leonardo.findya.fragments;

import android.app.Fragment;
import android.widget.ListView;

import com.leonardo.findya.R;
import com.leonardo.findya.adapters.SolicitacoesAdapter;
import com.leonardo.findya.outros.Usuario;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

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