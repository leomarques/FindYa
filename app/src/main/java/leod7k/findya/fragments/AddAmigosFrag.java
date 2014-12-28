package leod7k.findya.fragments;

import android.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import leod7k.findya.R;
import leod7k.findya.acoes.CarregaAmigosComum;
import leod7k.findya.acoes.CarregaAmigosFace;
import leod7k.findya.acoes.CarregaSolicitacoesAmizade;
import leod7k.findya.acoes.ProcuraUsuariosPublicos;
import leod7k.findya.outros.App;

@EFragment(R.layout.add_amigos_frag)
public class AddAmigosFrag extends Fragment {

    @ViewById
    RelativeLayout layoutAmigosFace;
    @ViewById
    RelativeLayout layoutAmigosComum;
    @ViewById
    RelativeLayout layoutUsuariosPublicos;
    @ViewById
    RelativeLayout layoutSolicitacoesAmizade;

    @AfterViews
    public void aoCriar() {
        atualizarSolicitados();

        layoutAmigosFace.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new CarregaAmigosFace(AddAmigosFrag.this.getActivity()).execute();
            }
        });

        layoutAmigosComum.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new CarregaAmigosComum(AddAmigosFrag.this.getActivity()).execute();
            }
        });

        layoutUsuariosPublicos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new ProcuraUsuariosPublicos(AddAmigosFrag.this.getActivity()).execute();
            }
        });

        layoutSolicitacoesAmizade.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new CarregaSolicitacoesAmizade(AddAmigosFrag.this.getActivity()).execute();
            }
        });
    }

    @Background
    public void atualizarSolicitados() {
        App.atualizarSolicitados();
    }

}
