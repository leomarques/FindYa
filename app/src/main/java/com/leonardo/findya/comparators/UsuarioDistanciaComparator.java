package com.leonardo.findya.comparators;

import com.leonardo.findya.outros.Usuario;

import java.util.Comparator;

public class UsuarioDistanciaComparator implements Comparator<Usuario> {
    public int compare(Usuario contato, Usuario outroContato) {
        return contato.getDistancia().compareTo(outroContato.getDistancia());
    }
}