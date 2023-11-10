package es.unican.carchargers.activities.details;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.parceler.Parcels;

import java.util.List;
import java.util.Objects;

import es.unican.carchargers.R;
import es.unican.carchargers.activities.main.IMainContract;
import es.unican.carchargers.activities.main.MainPresenter;
import es.unican.carchargers.constants.EOperator;
import es.unican.carchargers.model.Charger;

/**
 * Charging station details view. Shows the basic information of a charging station.
 */
public class DetailsView extends AppCompatActivity implements IDetailsContract.View {


    public static final String INTENT_CHARGER = "INTENT_CHARGER";
    private IDetailsContract.Presenter detailsPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_view);
        // Initialize presenter-view connection
        detailsPresenter = new DetailsPresenter();
        detailsPresenter.init(this);

        // Link to view elements
        ImageView ivLogo = findViewById(R.id.ivLogo);
        TextView tvTitle = findViewById(R.id.tvTitle);

        TextView tvProvincia = findViewById(R.id.tvProvincia);
        TextView tvCiudad = findViewById(R.id.tvCiudad);
        TextView tvPrecio = findViewById(R.id.tvPrecio);
        TextView tvInfo = findViewById(R.id.tvInfo);

        TextView tvDisponibilidad = findViewById(R.id.tvDisponibilidad);



        //Obtiene el cargador del intent que produjo esta actividad (Obsoleto, requiere api33 para implementar metodo actualizado getParcelable(string, clazz))
        Charger charger = Parcels.unwrap(getIntent().getExtras().getParcelable(INTENT_CHARGER));



        // Set logo
        int resourceId = EOperator.fromId(charger.operator.id).logo;
        ivLogo.setImageResource(resourceId);

        // Mostrar detalles del punto de carga
        validarYEstablecerTextView(tvTitle, charger.operator.title);
        validarYEstablecerTextView(tvInfo, charger.operator.website);
        validarYEstablecerTextView(tvProvincia, charger.address.province);
        validarYEstablecerTextView(tvCiudad, charger.address.title);
        validarYEstablecerTextView(tvPrecio, charger.usageCost);


        if(charger.comprobarDisponibilidad() == true) {
            tvDisponibilidad.setText("Disponible");
        } else {
            tvDisponibilidad.setText("No Disponible");
        }

        //Mostrar LOGO-CONECTOR
        ImageView[] logos = new ImageView[3];
        logos[0] = findViewById(R.id.logo1);
        logos[1] = findViewById(R.id.logo2);
        logos[2] = findViewById(R.id.logo3);

        TextView[] conectores = new TextView[3];
        conectores[0] = findViewById(R.id.tvConector1);
        conectores[1] = findViewById(R.id.tvConector2);
        conectores[2] = findViewById(R.id.tvConector3);

        List<String> lista = charger.listarTiposConector();

        for (int i = 0; i < lista.size() && i < 3; i++) {
            validarYEstablecerTextView(conectores[i], lista.get(i));
            switch(lista.get(i)){
                case "CCS (Type 1)":
                    logos[i].setImageResource(R.drawable.type1);
                    break;
                case "CCS (Type 2)":
                    logos[i].setImageResource(R.drawable.type2);
                    break;
                case "CHAdeMO":
                    logos[i].setImageResource(R.drawable.chademo);
                    break;
                case "CEE 74 - Schuko - Type F":
                    logos[i].setImageResource(R.drawable.schuko);
                    break;
                case "Type 1 (J1772)":
                    logos[i].setImageResource(R.drawable.type1j1772);
                    break;
                case "Type 2 (Socket Only)":
                    logos[i].setImageResource(R.drawable.type2socket);
                    break;
                case "Type 2 (Tethered Connector)":
                    logos[i].setImageResource(R.drawable.type2tethered);
                    break;
                default:
                    logos[i].setImageResource(R.drawable.unknown);
            }
        }

        Button btnFavs = findViewById(R.id.btnFavs);
        btnFavs.setOnClickListener((v) -> detailsPresenter.OnChargerBotonFavClicked(charger));

}

    /**
     *  Establece el valor de un campo en concreto de la vista a detalle de un punto de carga.
     * @param textView campo de la vista a detalle a establecer valor.
     * @param valor valor a establecer en el campo de la vista a detalle.
     */
    private void validarYEstablecerTextView(TextView textView, String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            textView.setText("-");
        } else {
            textView.setText(valor);
        }
    }

    public SharedPreferences getActivityPreferencies() {
        //Accede al fichero de favoritos en modo privado
        return this.getSharedPreferences("Favoritos",Context.MODE_PRIVATE);
    }

    public void anhadeCargadorAFavoritos(Charger c) {
        //Si esta seleccionado, se quita de favs (por implementar...)
        //...

        //Se coge con el getActivity la actividad en el mainView
        SharedPreferences sharedPref = getActivityPreferencies();

        SharedPreferences.Editor editor = sharedPref.edit();
        //Asigno el id del cargador a la llave generada por el id del boton
        editor.putBoolean(c.id, true);
        editor.apply();

        Toast.makeText((Context) this, String.format("Añadido 1 cargador a favoritos"),
                Toast.LENGTH_LONG).show();
    }




}