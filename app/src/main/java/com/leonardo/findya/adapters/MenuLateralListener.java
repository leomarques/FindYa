package com.leonardo.findya.adapters;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.leonardo.findya.TelaPrincipalAct;

public class MenuLateralListener implements ListView.OnItemClickListener {

    private TelaPrincipalAct telaPrincipalAct;

    public MenuLateralListener(TelaPrincipalAct paramTelaPrincipalAct) {
        telaPrincipalAct = paramTelaPrincipalAct;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 1:
                /*
                MapaFrag f = (MapaFrag) telaPrincipalAct.getFragmentManager().findFragmentByTag("mapa");
                if (f != null && f.isVisible()) {
                    telaPrincipalAct.getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.frame, new ListaAmigosFrag_()).commit();
                    telaPrincipalAct.drawerLayout.closeDrawer(telaPrincipalAct.menuLateral);
                    return;
                }

                ArrayList<Usuario> lista = (ArrayList<Usuario>) App.getAmigos();
                lista.add(App.getUsuario());
                f = MapaFrag_.builder().usuarios(lista).build();
                telaPrincipalAct.getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.frame, f, "mapa").commit();
                */
                telaPrincipalAct.drawerLayout.closeDrawer(telaPrincipalAct.menuLateral);
                break;
            case 2:
                //telaPrincipalAct.getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.frame, new AddAmigosFrag_()).commit();
                telaPrincipalAct.drawerLayout.closeDrawer(telaPrincipalAct.menuLateral);
                break;
            case 3:
                //telaPrincipalAct.getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.frame, new PerfilFrag_()).commit();
                telaPrincipalAct.drawerLayout.closeDrawer(telaPrincipalAct.menuLateral);
                break;
            default:
                break;
        }
    }

}
