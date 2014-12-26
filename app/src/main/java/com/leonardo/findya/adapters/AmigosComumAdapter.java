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
import com.leonardo.findya.outros.RoundedImageView;
import com.leonardo.findya.outros.Usuario;
import com.leonardo.findya.outros.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AmigosComumAdapter extends BaseAdapter {

    private List<Usuario> usuarios;
    private HashMap<Usuario, Integer> mapa;

    public AmigosComumAdapter(HashMap<Usuario, Integer> paramMapa) {
        mapa = paramMapa;
        usuarios = new ArrayList<Usuario>(mapa.keySet());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final AmigosComumViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) App.inst()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lista_amigos_comum, parent,
                    false);

            viewHolder = new AmigosComumViewHolder();
            viewHolder.foto = (ImageView) convertView
                    .findViewById(R.id.listaAmigosComum_foto);
            viewHolder.nome = (TextView) convertView
                    .findViewById(R.id.listaAmigosComum_nome);
            viewHolder.freq = (TextView) convertView
                    .findViewById(R.id.listaAmigosComum_freq);
            viewHolder.add = (ImageView) convertView
                    .findViewById(R.id.listaAmigosComum_add);
            viewHolder.layout = (RelativeLayout) convertView
                    .findViewById(R.id.listaAmigosComum_layoutAmigosComum);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (AmigosComumViewHolder) convertView.getTag();
        }

        final Usuario amigo = usuarios.get(position);

        String url = Util.pegarUrlFotoProfile(amigo.getIdFace());
        Picasso.with(App.inst()).load(url).placeholder(R.drawable.fya_icon).transform(new RoundedImageView()).into(viewHolder.foto);

        viewHolder.nome.setText(amigo.getNome());
        viewHolder.freq.setText(mapa.get(amigo) + " amigos em comum");

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
        return usuarios.size();
    }

    @Override
    public Object getItem(int position) {
        return usuarios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class AmigosComumViewHolder {
        public ImageView foto;
        public TextView nome;
        public TextView freq;
        public ImageView add;
        public RelativeLayout layout;

    }
}
