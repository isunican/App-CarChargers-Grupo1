package es.unican.carchargers.activities.favourite;

import static es.unican.carchargers.common.AndroidUtils.validarYEstablecerString;
import static es.unican.carchargers.common.AndroidUtils.validarYEstablecerTextView;

import android.content.Context;
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
import es.unican.carchargers.constants.EOperator;
import es.unican.carchargers.model.Charger;
import es.unican.carchargers.model.Connection;

public class FavChargersArrayAdapter extends ArrayAdapter<Charger> {

    private IFavContract.Presenter presenter;

    public FavChargersArrayAdapter(@NonNull Context context, @NonNull List<Charger> objects, IFavContract.Presenter presenter) {
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
                    .inflate(R.layout.activity_main_item_favs, parent, false);
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
            validarYEstablecerTextView(tv, charger.operator.title);
        }

        // Address
        {
            TextView tv = convertView.findViewById(R.id.tvAddress);
            String str = String.format("%s (%s)",validarYEstablecerString(charger.address.title), validarYEstablecerString(charger.address.province));
            validarYEstablecerTextView(tv, str);
        }

        // Info
        {
            TextView tv = convertView.findViewById(R.id.tvInfo);
            validarYEstablecerTextView(tv, charger.usageCost);
        }

        {
            TextView[] conectores = new TextView[3];
            conectores[0] = convertView.findViewById(R.id.tvC1);
            conectores[1] = convertView.findViewById(R.id.tvC2);
            conectores[2] = convertView.findViewById(R.id.tvC3);

            TextView[] potencias = new TextView[3];
            potencias[0] = convertView.findViewById(R.id.tvP1);
            potencias[1] = convertView.findViewById(R.id.tvP2);
            potencias[2] = convertView.findViewById(R.id.tvP3);

            List<String> lista = charger.listarTiposConector();
            List<String> listaPotencias = new ArrayList<>();

            for (Connection c : charger.connections) {
                listaPotencias.add(c.powerKW + "kW");
            }

            for (int i = 0; i < lista.size(); i++) {
                potencias[i].setText(listaPotencias.get(i));
                conectores[i].setText(lista.get(i));

            }

        }
        {
            ImageView iv = convertView.findViewById(R.id.btnFavoritoChiquitin);
            iv.setImageResource(R.drawable.estrella_amarillita);

        }


        return convertView;
    }

}
