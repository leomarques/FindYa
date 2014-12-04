package com.leonardo.findya;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.leonardo.findya.adapters.MenuLateralAdapter;
import com.leonardo.findya.adapters.MenuLateralListener;
import com.leonardo.findya.fragments.AddAmigosFrag_;
import com.leonardo.findya.fragments.ListaAmigosFrag_;
import com.leonardo.findya.outros.App;
import com.leonardo.findya.outros.GerenciadorServico;
import com.leonardo.findya.outros.UsuarioDao;
import com.leonardo.findya.outros.Util;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.tela_principal_act)
public class TelaPrincipalAct extends Activity {

    @ViewById
    public ListView menuLateral;

    @ViewById
    public DrawerLayout drawerLayout;

    public ActionBarDrawerToggle mDrawerToggle;

    private MenuItem menuItemServ;

    private static final int VOLTANDO_ATIVAR_GPS = 352;

    @SuppressWarnings("ConstantConditions")
    @AfterViews
    public void aoCriar() {
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.ab_drawer, R.string.drawer_open,
                R.string.drawer_close);
        drawerLayout.setDrawerListener(mDrawerToggle);

        View row = getLayoutInflater().inflate(R.layout.menu_lateral_header,
                menuLateral, false);
        ImageView foto = (ImageView) row.findViewById(R.id.menuLateral_foto);
        pegarFotoAsync(foto);
        TextView nome = (TextView) row.findViewById(R.id.menuLateral_nome);
        nome.setText(App.getUsuario().getNome().split(" ")[0]);

        menuLateral.addHeaderView(row);
        menuLateral.setAdapter(new MenuLateralAdapter());
        menuLateral.setOnItemClickListener(new MenuLateralListener(this));
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        Fragment f = App.getAmigos().isEmpty() ? new AddAmigosFrag_() : new ListaAmigosFrag_();
        getFragmentManager().beginTransaction().replace(R.id.frame, f).commit();

        if (!GerenciadorServico.isServicoAtivado()) {
            limparDispositivoNoBancoAsync();
        }
    }

    @Background
    public void pegarFotoAsync(ImageView foto) {
        foto.setImageBitmap(Util.pegarFotoUsuario());
    }

    @Background
    public void limparDispositivoNoBancoAsync() {
        UsuarioDao.desativarDispositivo();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (menuItemServ != null) {
            mudarIconeServ();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ab_action_item, menu);

        menuItemServ = menu.getItem(0);
        mudarIconeServ();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (item.getItemId() == menuItemServ.getItemId()) {
            mudarServico();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void mudarServico() {
        if (GerenciadorServico.isServicoAtivado()) {
            new MudaServico().execute();
        } else {
            boolean gpsEnabled = ((LocationManager) getSystemService(Context.LOCATION_SERVICE))
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (!gpsEnabled) {
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setMessage(R.string.desejaLigarGps)
                        .setPositiveButton("Sim",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(
                                            DialogInterface dialogInterface,
                                            int which) {
                                        Intent intent = new Intent(
                                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                        startActivityForResult(intent,
                                                VOLTANDO_ATIVAR_GPS);
                                    }
                                })
                        .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                new MudaServico().execute();
                            }
                        }).show();
            } else {
                new MudaServico().execute();
            }
        }
    }

    private class MudaServico extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            GerenciadorServico.mudarServico();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mudarIconeServ();
        }
    }

    private void mudarIconeServ() {
        if (GerenciadorServico.isServicoAtivado()) {
            menuItemServ.setIcon(R.drawable.ab_servico_on);
        } else {
            menuItemServ.setIcon(R.drawable.ab_servico_off);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VOLTANDO_ATIVAR_GPS) {
            new MudaServico().execute();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        if (mDrawerToggle != null) {
            mDrawerToggle.syncState();
        }
    }
}
