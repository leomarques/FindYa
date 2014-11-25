package com.leonardo.findya.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.leonardo.findya.R;
import com.leonardo.findya.acoes.EnviarSolicitacoesAmizade;
import com.leonardo.findya.outros.App;
import com.leonardo.findya.outros.Usuario;
import com.leonardo.findya.outros.Util;

import java.util.List;

public class SolicitacoesAdapter extends BaseAdapter {

    private List<Usuario> lista;

    public SolicitacoesAdapter(List<Usuario> paramLista) {
        lista = paramLista;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SolicitacoesViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) App.inst()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater
                    .inflate(R.layout.lista_amigos_face, parent, false);

            viewHolder = new SolicitacoesViewHolder();
            viewHolder.foto = (ImageView) convertView
                    .findViewById(R.id.listaAmigosFace_foto);
            viewHolder.nome = (TextView) convertView
                    .findViewById(R.id.listaAmigosFace_nome);
            viewHolder.add = (ImageView) convertView
                    .findViewById(R.id.listaAmigosFace_add);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SolicitacoesViewHolder) convertView.getTag();
        }

        final Usuario amigo = lista.get(position);

        String url = Util.pegarUrlFotoProfile(amigo.getIdFace());
        App.getImageLoader().DisplayImage(url, viewHolder.foto);

        viewHolder.nome.setText(amigo.getNome());

        viewHolder.add.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                new EnviarSolicitacoesAmizade(amigo).execute();
                viewHolder.add.setVisibility(ImageView.INVISIBLE);
            }
        });

        return convertView;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class SolicitacoesViewHolder {

        public ImageView foto;
        public TextView nome;
        public ImageView add;

    }
}
