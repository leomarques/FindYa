package com.leonardo.findya.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.leonardo.findya.R;
import com.leonardo.findya.outros.App;

public class MenuLateralAdapter extends BaseAdapter {

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MenuLateralViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) App.inst()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.menu_lateral, parent, false);

            viewHolder = new MenuLateralViewHolder();
            viewHolder.icone = (ImageView) convertView.findViewById(R.id.menuLateral_icone);
            viewHolder.texto = (TextView) convertView.findViewById(R.id.menuLateral_texto);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MenuLateralViewHolder) convertView.getTag();
        }

        switch (position) {
            case 0:
                viewHolder.icone.setImageResource(R.drawable.menu_lateral_ver_mapa);
                viewHolder.texto.setText("Ver amigos");
                break;
            case 1:
                viewHolder.icone.setImageResource(R.drawable.menu_lateral_add_amigos);
                viewHolder.texto.setText("+ Amigos");
                break;
            case 2:
                viewHolder.icone.setImageResource(R.drawable.menu_lateral_perfil);
                viewHolder.texto.setText("Meu Perfil");
                break;

            default:
                break;
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class MenuLateralViewHolder {
        ImageView icone;
        TextView texto;
    }

}
