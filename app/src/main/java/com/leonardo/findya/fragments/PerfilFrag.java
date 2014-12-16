package com.leonardo.findya.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leonardo.findya.LoginAct_;
import com.leonardo.findya.R;
import com.leonardo.findya.outros.App;
import com.leonardo.findya.outros.UsuarioDao;
import com.leonardo.findya.outros.Util;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.perfil_frag)
public class PerfilFrag extends Fragment {
	@ViewById
	ImageView foto;

	@ViewById
	TextView nome;

	@ViewById
	RelativeLayout layoutSair;

	@ViewById
	EditText editTextStatus;

	@ViewById
	CheckBox publicoCkBx;

	Handler h;
	private Runnable runSalvarStatus;

	@AfterViews
	public void aoCriar() {
        App.getImageLoader().DisplayImage(Util.pegarUrlFotoProfile(App.getUsuario().getIdFace()), foto);
        nome.setText(App.getUsuario().getNome());
		publicoCkBx.setChecked(!App.getUsuario().isPublico());
		h = new Handler();
		runSalvarStatus = new Runnable() {
			@Override
			public void run() {
				new SalvaStatus().execute(editTextStatus.getText().toString());
			}
		};

		layoutSair.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				App.logOff();
				LoginAct_.intent(getActivity())
						.flags(Intent.FLAG_ACTIVITY_NEW_TASK).start();
				getActivity().finish();
			}
		});

		editTextStatus.setText(App.getUsuario().getStatus());
	}

    @AfterTextChange
	public void editTextStatus() {
		if (editTextStatus.getText().toString()
				.equals(App.getUsuario().getStatus())) {
			return;
		}
		h.removeCallbacks(runSalvarStatus);
		h.postDelayed(runSalvarStatus, 10000);
	}

	@Background
	@Click
	public void publicoCkBx() {
		App.getUsuario().setPublico(!App.getUsuario().isPublico());
		App.salvarUsuario();
		UsuarioDao.atualizarEstadoPublico();
	}

	private class SalvaStatus extends AsyncTask<String, Void, Void> {
		@Override
		protected Void doInBackground(String... arg0) {
			App.getUsuario().setStatus(arg0[0]);
			App.salvarUsuario();
			UsuarioDao.salvarStatus();

			return null;
		}
	}
}
