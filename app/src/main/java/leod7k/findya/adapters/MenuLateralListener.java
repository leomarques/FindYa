package leod7k.findya.adapters;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import leod7k.findya.R;
import leod7k.findya.TelaPrincipalAct;
import leod7k.findya.fragments.AddAmigosFrag_;
import leod7k.findya.fragments.ListaAmigosFrag_;
import leod7k.findya.fragments.PerfilFrag_;

public class MenuLateralListener implements ListView.OnItemClickListener {

    private TelaPrincipalAct telaPrincipalAct;

    public MenuLateralListener(TelaPrincipalAct paramTelaPrincipalAct) {
        telaPrincipalAct = paramTelaPrincipalAct;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 1:
                telaPrincipalAct.getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.frame, new ListaAmigosFrag_()).commit();
                break;
            case 2:
                telaPrincipalAct.getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.frame, new AddAmigosFrag_()).commit();
                break;
            case 3:
                telaPrincipalAct.getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.frame, new PerfilFrag_()).commit();
                break;
            default:
                break;
        }
        telaPrincipalAct.drawerLayout.closeDrawer(telaPrincipalAct.menuLateral);
    }

}
