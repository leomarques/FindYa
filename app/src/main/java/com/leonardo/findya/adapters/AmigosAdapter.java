package com.leonardo.findya.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leonardo.findya.R;
import com.leonardo.findya.fragments.MapaFrag;
import com.leonardo.findya.fragments.MapaFrag_;
import com.leonardo.findya.outros.App;
import com.leonardo.findya.outros.Usuario;
import com.leonardo.findya.outros.UsuarioDao;
import com.leonardo.findya.outros.Util;

import java.util.ArrayList;
import java.util.List;

public class AmigosAdapter extends BaseAdapter {

    private Context context;
    private List<Usuario> lista;
    private Usuario amigo;
    //public QuickAction mQuickAction;

    private final String QA_MSG = "Mensagem";
    private final String QA_CUTUCAR = "Cutucar";
    private final String QA_PROFILE_FACE = "Profile";
    private final String QA_EXCLUIR_AMIGO = "Excluir";

    public AmigosAdapter(final Context paramContext,
                         final List<Usuario> paramLista) {
        context = paramContext;
        lista = paramLista;
/*
        mQuickAction = new QuickAction(paramContext);

        ActionItem noMapa = new ActionItem();
        noMapa.setTitle(QA_MSG);
        noMapa.setIcon(paramContext.getResources().getDrawable(
                R.drawable.qa_msg));
        mQuickAction.addActionItem(noMapa);

        ActionItem cutucar = new ActionItem();
        cutucar.setTitle(QA_CUTUCAR);
        cutucar.setIcon(paramContext.getResources().getDrawable(
                R.drawable.qa_cutucar));
        mQuickAction.addActionItem(cutucar);

        ActionItem noFacebook = new ActionItem();
        noFacebook.setTitle(QA_PROFILE_FACE);
        noFacebook.setIcon(paramContext.getResources().getDrawable(
                R.drawable.qa_facebook));
        mQuickAction.addActionItem(noFacebook);

        ActionItem excluirAmigo = new ActionItem();
        excluirAmigo.setTitle(QA_EXCLUIR_AMIGO);
        excluirAmigo.setIcon(paramContext.getResources().getDrawable(
                R.drawable.qa_excluir));
        mQuickAction.addActionItem(excluirAmigo);

        mQuickAction
                .setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
                    @Override
                    public void onItemClick(QuickAction source, int pos,
                                            int actionId) {
                        if (pos == 0) {
                            String uri = "fb://messaging/" + amigo.getIdFace();
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri
                                    .parse(uri));
                            context.startActivity(intent);
                        }
                        if (pos == 1) {
                            final EditText input = new EditText(context);
                            new AlertDialog.Builder(context)
                                    .setTitle(
                                            "Deseja enviar uma mensagem junto?")
                                    .setView(input)
                                    .setPositiveButton(
                                            "Ok",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface dialog,
                                                        int whichButton) {
                                                    new CutucaAmigo(input
                                                            .getText()
                                                            .toString())
                                                            .execute(amigo);
                                                }
                                            })
                                    .setNegativeButton("Cancelar", null).show();
                        }
                        if (pos == 2) {
                            Intent browserIntent = new Intent(
                                    Intent.ACTION_VIEW, Uri
                                    .parse("fb://profile/"
                                            + amigo.getIdFace()));
                            paramContext.startActivity(browserIntent);
                        }
                        if (pos == 3) {
                            new AlertDialog.Builder(context)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setMessage(
                                            "Tem certeza que deseja excluir "
                                                    + amigo.getNome() + "?")
                                    .setPositiveButton(
                                            "Sim",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(
                                                        DialogInterface dialogInterface,
                                                        int which) {
                                                    new ExcluiAmigo()
                                                            .execute(amigo);
                                                }
                                            }).setNegativeButton("Não", null)
                                    .show();
                        }
                    }
                });
        */
    }

    public class ExcluiAmigo extends AsyncTask<Usuario, Void, Void> {

        @Override
        protected Void doInBackground(Usuario... arg0) {
            UsuarioDao.excluirAmigo(arg0[0]);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Toast.makeText(App.inst(), R.string.amigoExcluido,
                    Toast.LENGTH_SHORT).show();
            /*
            ((ActionBarActivity) context).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame, new ListaAmigosFrag_()).commit();
                    */
        }

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        AmigosViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater
                    .inflate(R.layout.lista_amigos, parent, false);

            viewHolder = new AmigosViewHolder();
            viewHolder.lh = (RelativeLayout) convertView
                    .findViewById(R.id.listaAmigos_layoutHeader);
            viewHolder.img = (ImageView) convertView
                    .findViewById(R.id.listaAmigos_foto);
            viewHolder.opcoes = (ImageView) convertView
                    .findViewById(R.id.listaAmigos_opcoes);
            viewHolder.nome = (TextView) convertView
                    .findViewById(R.id.listaAmigos_nome);
            viewHolder.distancia = (TextView) convertView
                    .findViewById(R.id.listaAmigos_distancia);
            viewHolder.status = (TextView) convertView
                    .findViewById(R.id.listaAmigos_status);
            viewHolder.data = (TextView) convertView
                    .findViewById(R.id.listaAmigos_data);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (AmigosViewHolder) convertView.getTag();
        }

        final Usuario contato = lista.get(position);
        viewHolder.lh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.semLocalizacao(contato)) {
                    return;
                }

                ArrayList<Usuario> lista = new ArrayList<Usuario>();
                lista.add(App.getUsuario());
                lista.add(contato);

                MapaFrag f = MapaFrag_.builder().usuarios(lista).build();
                ((Activity) context).getFragmentManager()
                        .beginTransaction().addToBackStack(null)
                        .replace(R.id.frame, f, "mapa").commit();

            }

        });

        String url = Util.pegarUrlFotoProfile(contato.getIdFace());
        App.getImageLoader().DisplayImage(url, viewHolder.img);

        viewHolder.opcoes.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AmigosAdapter.this.amigo = lista.get(position);
                //mQuickAction.show(v);
            }
        });

        viewHolder.nome.setText(contato.getNome());

        String statusTemp = contato.getStatus();
        viewHolder.status
                .setText(statusTemp == null || statusTemp.equals("") ? "" : "#"
                        + statusTemp);

        if (Util.semLocalizacao(App.getUsuario())) {
            viewHolder.distancia.setText("");
        } else {
            if (Util.semLocalizacao(contato)) {
                viewHolder.distancia.setText(R.string.semLocalizacao);
            } else {
                viewHolder.distancia.setText(String.format("%.1f",
                        contato.getDistancia())
                        + "km");
                viewHolder.data.setText(contato.getData().substring(5, 16)
                        .replace('-', '/'));
            }
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int posicao) {
        return lista.get(posicao);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class AmigosViewHolder {
        RelativeLayout lh;
        ImageView img;
        ImageView opcoes;
        TextView nome;
        TextView distancia;
        TextView data;
        TextView status;
        TextView local;
    }

}
