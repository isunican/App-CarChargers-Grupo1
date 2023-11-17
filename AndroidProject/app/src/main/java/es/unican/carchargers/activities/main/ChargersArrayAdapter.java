package es.unican.carchargers.activities.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import es.unican.carchargers.R;
import es.unican.carchargers.activities.details.DetailsView;
import es.unican.carchargers.constants.EOperator;
import es.unican.carchargers.model.Charger;
import es.unican.carchargers.model.Connection;

public class ChargersArrayAdapter extends ArrayAdapter<Charger> {

    private IMainContract.Presenter presenter;
    private SharedPreferences sharedPref;

    public ChargersArrayAdapter(@NonNull Context context, @NonNull List<Charger> objects, IMainContract.Presenter presenter, SharedPreferences sharedPref) {
        super(context, 0, objects);
        this.presenter = presenter;
        this.sharedPref = sharedPref;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // this is the car charger we want to show here
        final Charger charger = getItem(position);

        // create the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.activity_main_item, parent, false);
        }

        // logo
        {
            ImageView iv = convertView.findViewById(R.id.ivLogo);
            String operatorName = charger.operator.title;
            EOperator operator = EOperator.fromId(charger.operator.id);
            iv.setImageResource(operator.logo);
        }

        // Title
        {
            TextView tv = convertView.findViewById(R.id.tvTitle);
            tv.setText(charger.operator.title);
        }

        // Address
        {
            TextView tv = convertView.findViewById(R.id.tvAddress);
            String str = String.format("%s (%s)", charger.address.title, charger.address.province);
            tv.setText(str);
        }

        // Info
        {
            TextView tv = convertView.findViewById(R.id.tvInfo);
            tv.setText(charger.usageCost);
        }

        {

            TextView[] potencias = new TextView[3];
            potencias[0] = convertView.findViewById(R.id.tvPot1);
            potencias[1] = convertView.findViewById(R.id.tvPot2);
            potencias[2] = convertView.findViewById(R.id.tvPot3);

            List<String> lista = charger.listarTiposConector();
            List<String> listaPotencias = new ArrayList<>();

            for (Connection c : charger.connections) {
                listaPotencias.add(c.powerKW + "kW");
            }

            for (int i = 0; i < Math.min(lista.size(), potencias.length); i++) {
                potencias[i].setText(listaPotencias.get(i));
            }

        }

        //Meter el listener del boton chiquitin del layout de la vista general
        {
            TextView imgFavoritoChiquitin = convertView.findViewById(R.id.imgFavoritoChiquitin);
            // comprueba inicialmente si ya está en favoritos
            if (sharedPref.getBoolean(charger.id, false)) {
                imgFavoritoChiquitin.setCompoundDrawablesWithIntrinsicBounds(R.drawable.estrella_amarillita, 0, 0, 0);
            } else {
                imgFavoritoChiquitin.setCompoundDrawablesWithIntrinsicBounds(R.drawable.estrella_gris, 0, 0, 0);

            }
        }
        {
            {
                TextView imgFavoritoChiquitin = convertView.findViewById(R.id.imgFavoritoChiquitin);
                //Onclick sobre un textview que esta dentro de un listview
                imgFavoritoChiquitin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.OnChargerBotonFavClicked(charger);
                        if (sharedPref.getBoolean(charger.id, false)) {
                            ((TextView) v).setCompoundDrawablesWithIntrinsicBounds(R.drawable.estrella_amarillita, 0, 0, 0);
                        }
                    }
                });


                return convertView;
            }

        }
    }
}
