package leod7k.findya.comparators;

import leod7k.findya.outros.Usuario;

import java.util.Comparator;

public class UsuarioDistanciaComparator implements Comparator<Usuario> {
    public int compare(Usuario contato, Usuario outroContato) {
        return contato.getDistancia().compareTo(outroContato.getDistancia());
    }
}