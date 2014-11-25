package com.leonardo.findya.comparators;

import com.leonardo.findya.outros.Usuario;

import java.util.Comparator;

public class UsuarioNomeComparator implements Comparator<Usuario> {
	public int compare(Usuario contato, Usuario outroContato) {
		return contato.getNome().compareTo(outroContato.getNome());
	}
}