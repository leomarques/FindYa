package leod7k.findya.comparators;

import leod7k.findya.outros.Usuario;

import java.util.Comparator;

public class UsuarioNomeComparator implements Comparator<Usuario> {
	public int compare(Usuario contato, Usuario outroContato) {
		return contato.getNome().compareTo(outroContato.getNome());
	}
}