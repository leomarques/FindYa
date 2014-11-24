package com.leonardo.findya.outros;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UsuarioDao {
    private static final String URL_ACEITARSOLICITACOESAMIZADE			= "http://findya-env.elasticbeanstalk.com/aceitarSolicitacoesAmizade.php";
    private static final String URL_ATIVARDISPOSITIVO					= "http://findya-env.elasticbeanstalk.com/ativarDispositivo.php";
    private static final String URL_ATUALIZARESTADOPUBLICO 				= "http://findya-env.elasticbeanstalk.com/atualizarEstadoPublico.php";
    private static final String URL_ATUALIZARLOCALIZACAO				= "http://findya-env.elasticbeanstalk.com/atualizarLocalizacao.php";
    private static final String URL_BUSCARAMIGOSDOSAMIGOS				= "http://findya-env.elasticbeanstalk.com/buscarAmigosDosAmigos.php";
    private static final String URL_BUSCARDISPOSITIVO					= "http://findya-env.elasticbeanstalk.com/buscarDispositivo.php";
    private static final String URL_BUSCARDISPOSITIVOATIVO				= "http://findya-env.elasticbeanstalk.com/buscarDispositivoAtivo.php";
    private static final String URL_BUSCARIDSUSUARIOSNAOAMIGOS			= "http://findya-env.elasticbeanstalk.com/buscarIdsUsuariosNaoAmigos.php";
    private static final String URL_BUSCARSOLICITADOS 					= "http://findya-env.elasticbeanstalk.com/buscarSolicitados.php";
    private static final String URL_BUSCARSOLICITANTES					= "http://findya-env.elasticbeanstalk.com/buscarSolicitantes.php";
    private static final String URL_BUSCARSOLICITANTESOCULTOS			= "http://findya-env.elasticbeanstalk.com/buscarSolicitantesOcultos.php";
    private static final String URL_BUSCARTODOSAMIGOS					= "http://findya-env.elasticbeanstalk.com/buscarTodosAmigos.php";
    private static final String URL_BUSCARTODOSUSUARIOSPUBLICOS			= "http://findya-env.elasticbeanstalk.com/buscarTodosUsuariosPublicos.php";
    private static final String URL_BUSCARUSUARIOPORIDFACE				= "http://findya-env.elasticbeanstalk.com/buscarUsuarioPorIdFace.php";
    private static final String URL_BUSCARUSUARIOSPORIDFACE				= "http://findya-env.elasticbeanstalk.com/buscarUsuariosPorIdFace.php";
    private static final String URL_BUSCARUSUARIOSPUBLICOSNOQUADRANTE 	= "http://findya-env.elasticbeanstalk.com/buscarUsuariosPublicosNoQuadrante.php";
    private static final String URL_CADASTRARDISPOSITIVO				= "http://findya-env.elasticbeanstalk.com/cadastrarDispositivo.php";
    private static final String URL_CADASTRARUSUARIO                    = "http://findya-env.elasticbeanstalk.com/cadastrarUsuario.php";
    private static final String URL_DESATIVARDISPOSITIVO				= "http://findya-env.elasticbeanstalk.com/desativarDispositivo.php";
    private static final String URL_ENVIARSOLICITACOESAMIZADE			= "http://findya-env.elasticbeanstalk.com/enviarSolicitacoesAmizade.php";
    private static final String URL_EXCLUIRAMIGO						= "http://findya-env.elasticbeanstalk.com/excluirAmigo.php";
    private static final String URL_OCULTARSOLICITACOESAMIZADE 			= "http://findya-env.elasticbeanstalk.com/ocultarSolicitacoesAmizade.php";
    private static final String URL_SALVARIDGCM							= "http://findya-env.elasticbeanstalk.com/salvarIdGcm.php";
    private static final String URL_SALVARSTATUS						= "http://findya-env.elasticbeanstalk.com/salvarStatus.php";

    public static void aceitarSolicitacoesAmizade(List<Usuario> usuarios) {
        LinkedList<Usuario> lista = new LinkedList<Usuario>();
        lista.add(App.getUsuario());
        lista.addAll(usuarios);
        String usuarioJson = new Gson().toJson(lista);

        Util.jsonPost(URL_ACEITARSOLICITACOESAMIZADE, usuarioJson);
    }

    public static void aceitarSolicitacaoAmizade(Usuario usuario) {
        LinkedList<Usuario> lista = new LinkedList<Usuario>();
        lista.add(usuario);

        aceitarSolicitacoesAmizade(lista);
    }

    public static void ativarDispositivo() {
        String usuarioJson = new Gson().toJson(App.getUsuario());

        Util.jsonPost(URL_ATIVARDISPOSITIVO, usuarioJson);
    }

    public static void atualizarEstadoPublico() {
        String usuarioJson = new Gson().toJson(App.getUsuario());

        Util.jsonPost(URL_ATUALIZARESTADOPUBLICO, usuarioJson);
    }

    public static void atualizarLocalizacao() {
        String usuarioJson = new Gson().toJson(App.getUsuario());

        Util.jsonPost(URL_ATUALIZARLOCALIZACAO, usuarioJson);
    }

    public static List<Usuario> buscarAmigosDosAmigos() {
        String usuarioJson = new Gson().toJson(App.getUsuario().getIdFace());

        BufferedReader reader = Util.jsonPost(URL_BUSCARAMIGOSDOSAMIGOS, usuarioJson);

        return ConversorJson.getJsonToUsuarios(reader);
    }

    public static String buscarDispositivo() {
        String usuarioJson = new Gson().toJson(App.getUsuario());

        BufferedReader reader = Util.jsonPost(URL_BUSCARDISPOSITIVO, usuarioJson);

        return ConversorJson.getJsonToIdInstalacao(reader);
    }

    public static String buscarDispositivoAtivo() {
        String idJson = new Gson().toJson(App.getUsuario().getIdFace());

        BufferedReader reader = Util.jsonPost(URL_BUSCARDISPOSITIVOATIVO, idJson);

        return ConversorJson.getJsonToIdInstalacao(reader);
    }

    /*
     * Esse método busca idsFace que estão contidos na lista de parâmetro e que não são amigos
     * do primeiro id da lista.
     */
    public static List<String> buscarIdsUsuariosNaoAmigos(List<String> idsAmigos) {
        idsAmigos.add(0, App.getUsuario().getIdFace());
        String idsAmigosJson = new Gson().toJson(idsAmigos);

        BufferedReader reader = Util.jsonPost(URL_BUSCARIDSUSUARIOSNAOAMIGOS, idsAmigosJson);

        return ConversorJson.getJsonToListaIds(reader);
    }

    public static List<Usuario> buscarSolicitados() {
        String usuarioJson = new Gson().toJson(App.getUsuario().getIdFace());

        BufferedReader reader = Util.jsonPost(URL_BUSCARSOLICITADOS, usuarioJson);

        return ConversorJson.getJsonToUsuarios(reader);
    }

    public static List<Usuario> buscarSolicitantes() {
        String usuarioJson = new Gson().toJson(App.getUsuario().getIdFace());

        BufferedReader reader = Util.jsonPost(URL_BUSCARSOLICITANTES, usuarioJson);

        return ConversorJson.getJsonToUsuarios(reader);
    }

    public static List<Usuario> buscarSolicitantesOcultos() {
        String usuarioJson = new Gson().toJson(App.getUsuario().getIdFace());

        BufferedReader reader = Util.jsonPost(URL_BUSCARSOLICITANTESOCULTOS, usuarioJson);

        return ConversorJson.getJsonToUsuarios(reader);
    }

    public static List<Usuario> buscarTodosAmigos() {
        String usuarioJson = new Gson().toJson(App.getUsuario().getIdFace());

        BufferedReader reader = Util.jsonPost(URL_BUSCARTODOSAMIGOS, usuarioJson);

        return ConversorJson.getJsonToUsuarios(reader);
    }

    public static List<Usuario> buscarTodosUsuariosPublicos() {
        String usuarioJson = new Gson().toJson(App.getUsuario().getIdFace());

        BufferedReader reader = Util.jsonPost(URL_BUSCARTODOSUSUARIOSPUBLICOS, usuarioJson);

        return ConversorJson.getJsonToUsuarios(reader);
    }

    public static Usuario buscarUsuarioPorIdFace(String id) {
        String usuarioJson = new Gson().toJson(id);

        BufferedReader reader = Util.jsonPost(URL_BUSCARUSUARIOPORIDFACE, usuarioJson);

        return ConversorJson.getJsonToUsuario(reader);
    }

    public static List<Usuario> buscarUsuariosPorIdFace(ArrayList<String> idsUsuarios) {
        String usuarioJson = new Gson().toJson(idsUsuarios);

        BufferedReader reader = Util.jsonPost(URL_BUSCARUSUARIOSPORIDFACE, usuarioJson);

        return ConversorJson.getJsonToUsuarios(reader);
    }

    public static List<Usuario> buscarUsuariosPublicosNoQuadrante(Map<String, Double> quadrante) {
        String usuarioJson = new Gson().toJson(quadrante);

        BufferedReader reader = Util.jsonPost(URL_BUSCARUSUARIOSPUBLICOSNOQUADRANTE, usuarioJson);

        return ConversorJson.getJsonToUsuarios(reader);
    }

    public static void cadastrarUsuario() {
        String usuarioJson = new Gson().toJson(App.getUsuario());

        Usuario contatoTemp = buscarUsuarioPorIdFace(App.getUsuario().getIdFace());
        if (contatoTemp != null) {
            return;
        }

        Util.jsonPost(URL_CADASTRARUSUARIO, usuarioJson);
    }

    public static void cadastrarDispositivo() {
        String usuarioJson = new Gson().toJson(App.getUsuario());

        if (buscarDispositivo() != null) {
            return;
        }

        Util.jsonPost(URL_CADASTRARDISPOSITIVO, usuarioJson);
    }

    public static void desativarDispositivo() {
        String usuarioJson = new Gson().toJson(App.getUsuario());

        Util.jsonPost(URL_DESATIVARDISPOSITIVO, usuarioJson);
    }

    public static void enviarSolicitacoesAmizade(List<Usuario> usuarios) {
        LinkedList<Usuario> lista = new LinkedList<Usuario>();
        lista.add(App.getUsuario());
        lista.addAll(usuarios);
        String usuarioJson = new Gson().toJson(lista);

        Util.jsonPost(URL_ENVIARSOLICITACOESAMIZADE, usuarioJson);
    }

    public static void enviarSolicitacaoAmizade(Usuario usuario) {
        LinkedList<Usuario> lista = new LinkedList<Usuario>();
        lista.add(usuario);

        enviarSolicitacoesAmizade(lista);
    }

    public static void excluirAmigo(Usuario usuario) {
        LinkedList<Usuario> lista = new LinkedList<Usuario>();
        lista.add(App.getUsuario());
        lista.add(usuario);
        String usuarioJson = new Gson().toJson(lista);

        Util.jsonPost(URL_EXCLUIRAMIGO, usuarioJson);
    }

    public static void ocultarSolicitacoesAmizade(List<Usuario> usuarios) {
        LinkedList<Usuario> lista = new LinkedList<Usuario>();
        lista.add(App.getUsuario());
        lista.addAll(usuarios);
        String usuarioJson = new Gson().toJson(lista);

        Util.jsonPost(URL_OCULTARSOLICITACOESAMIZADE, usuarioJson);
    }

    public static void salvarIdGcm() {
        String usuarioJson = new Gson().toJson(App.getUsuario());

        Util.jsonPost(URL_SALVARIDGCM, usuarioJson);
    }

    public static void salvarStatus() {
        String usuarioJson = new Gson().toJson(App.getUsuario());

        Util.jsonPost(URL_SALVARSTATUS, usuarioJson);
    }
}
