package es.unican.carchargers.activities.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    public ChargersArrayAdapter(@NonNull Context context, @NonNull List<Charger> objects, IMainContract.Presenter presenter) {
        super(context, 0, objects);
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // this is the car charger we want to show here
        Charger charger = getItem(position);

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
            //Onclick sobre un textview que esta dentro de un listview
            TextView imgFavoritoChiquitin = convertView.findViewById(R.id.imgFavoritoChiquitin);
            imgFavoritoChiquitin.setOnClickListener((v) -> presenter.OnChargerBotonFavClicked(charger));

        }


        return convertView;
    }

}
