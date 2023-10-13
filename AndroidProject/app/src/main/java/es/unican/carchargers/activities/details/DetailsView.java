package es.unican.carchargers.activities.details;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.parceler.Parcels;

import es.unican.carchargers.R;
import es.unican.carchargers.constants.EOperator;
import es.unican.carchargers.model.Charger;

/**
 * Charging station details view. Shows the basic information of a charging station.
 */
public class DetailsView extends AppCompatActivity {

    public static final String INTENT_CHARGER = "INTENT_CHARGER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_view);

        // Link to view elements
        ImageView ivLogo = findViewById(R.id.ivLogo);
        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvId = findViewById(R.id.tvId);

        TextView tvProvincia = findViewById(R.id.tvProvincia);
        TextView tvCiudad = findViewById(R.id.tvCiudad);
        TextView tvPrecio = findViewById(R.id.tvPrecio);
        TextView tvInfo = findViewById(R.id.tvInfo);
        TextView tvTipoConector = findViewById(R.id.tvTipoConector);




        // Get Charger from the intent that triggered this activity
        Charger charger = Parcels.unwrap(getIntent().getExtras().getParcelable(INTENT_CHARGER));

        // Set logo
        int resourceId = EOperator.fromId(charger.operator.id).logo;
        ivLogo.setImageResource(resourceId);

        // Set Infos


        // Validar y establecer el texto para tcTitle
        validarYEstablecerTextView(tvTitle, charger.operator.title, "No hay título");

        //Validar y establecer el texto para tvInfo
        validarYEstablecerTextView(tvInfo, charger.operator.website, "No hay info adicional");

        // Validar y establecer el texto para tvId
        validarYEstablecerTextView(tvId, charger.id, "No hay ID");

        // Validar y establecer el texto para tvProvincia
        validarYEstablecerTextView(tvProvincia, charger.address.province, "No hay provincia");

        // Validar y establecer el texto para tvCiudad
        validarYEstablecerTextView(tvCiudad, charger.address.title, "No hay ciudad");

        // Validar y establecer el texto para tvPrecio
        validarYEstablecerTextView(tvPrecio, charger.usageCost, "No hay precio");

        //TODO tipo conector + foto descriptiva

        //validarYEstablecerTextView(tvTipoConector, charger);
}

    private void validarYEstablecerTextView(TextView textView, String valor, String mensajeError) {
        if (valor == null || valor.trim().isEmpty()) {
            textView.setText(mensajeError);
        } else {
            textView.setText(valor);
        }
    }



}