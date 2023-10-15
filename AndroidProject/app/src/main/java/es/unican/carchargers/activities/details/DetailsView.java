package es.unican.carchargers.activities.details;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.List;

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


        // Get Charger from the intent that triggered this activity
        Charger charger = Parcels.unwrap(getIntent().getExtras().getParcelable(INTENT_CHARGER));

        // Set logo
        int resourceId = EOperator.fromId(charger.operator.id).logo;
        ivLogo.setImageResource(resourceId);

        // Set Infos
        // Validar y establecer el texto para tcTitle
        validarYEstablecerTextView(tvTitle, charger.operator.title);
        //Validar y establecer el texto para tvInfo
        validarYEstablecerTextView(tvInfo, charger.operator.website);
        // Validar y establecer el texto para tvId
        validarYEstablecerTextView(tvId, charger.id);
        // Validar y establecer el texto para tvProvincia
        validarYEstablecerTextView(tvProvincia, charger.address.province);
        // Validar y establecer el texto para tvCiudad
        validarYEstablecerTextView(tvCiudad, charger.address.title);
        // Validar y establecer el texto para tvPrecio
        validarYEstablecerTextView(tvPrecio, charger.usageCost);



}

    private static void validarYEstablecerTextView(TextView textView, String valor, String mensajeError) {

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
                default:
                    logos[i].setImageResource(R.drawable.unknown);
            }
        }
}




}