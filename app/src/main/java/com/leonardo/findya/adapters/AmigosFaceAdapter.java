package com.leonardo.findya.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leonardo.findya.R;
import com.leonardo.findya.acoes.EnviarSolicitacoesAmizade;
import com.leonardo.findya.outros.App;
import com.leonardo.findya.outros.Usuario;
import com.leonardo.findya.outros.Util;

import java.util.List;

public class AmigosFaceAdapter extends BaseAdapter {

    private List<Usuario> lista;

    public AmigosFaceAdapter(List<Usuario> paramLista) {
        lista = paramLista;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final AmigosFaceViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) App.inst()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater
                    .inflate(R.layout.lista_amigos_face, parent, false);

            viewHolder = new AmigosFaceViewHolder();
            viewHolder.foto = (ImageView) convertView
                    .findViewById(R.id.listaAmigosFace_foto);
            viewHolder.nome = (TextView) convertView
                    .findViewById(R.id.listaAmigosFace_nome);
            viewHolder.add = (ImageView) convertView
                    .findViewById(R.id.listaAmigosFace_add);
            viewHolder.layout = (RelativeLayout) convertView
                    .findViewById(R.id.listaAmigosFace_layoutAmigosFace);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (AmigosFaceViewHolder) convertView.getTag();
        }

        final Usuario amigo = lista.get(position);

        String url = Util.pegarUrlFotoProfile(amigo.getIdFace());
        App.getImageLoader().DisplayImage(url, viewHolder.foto);

        viewHolder.nome.setText(amigo.getNome());

        if (App.getSolicitados().contains(amigo)) {
            viewHolder.layout.setBackgroundColor(App.inst().getResources().getColor(android.R.color.darker_gray));
            viewHolder.add.setVisibility(ImageView.INVISIBLE);
        } else {
            viewHolder.layout.setBackgroundColor(App.inst().getResources().getColor(R.color.light_green));
            viewHolder.add.setVisibility(ImageView.VISIBLE);
            viewHolder.add.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    new EnviarSolicitacoesAmizade(amigo).execute();
                    viewHolder.add.setVisibility(ImageView.INVISIBLE);
                }
            });
        }

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

    private class AmigosFaceViewHolder {

        public ImageView foto;
        public TextView nome;
        public ImageView add;
        public RelativeLayout layout;

    }
}
