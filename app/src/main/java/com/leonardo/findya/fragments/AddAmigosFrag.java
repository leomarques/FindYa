package com.leonardo.findya.fragments;

import android.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.leonardo.findya.R;
import com.leonardo.findya.acoes.CarregaAmigosComum;
import com.leonardo.findya.acoes.CarregaAmigosFace;
import com.leonardo.findya.acoes.CarregaSolicitacoesAmizade;
import com.leonardo.findya.acoes.ProcuraUsuariosPublicos;
import com.leonardo.findya.outros.App;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

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
