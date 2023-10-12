package es.unican.carchargers.activities.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;

import es.unican.carchargers.R;

public class popUpPotencias extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_potencias);

        //Ajustar medidas de la ventana emergente
        DisplayMetrics medidasVentana = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(medidasVentana);

        int anchoV = medidasVentana.widthPixels;
        int altoV = medidasVentana.heightPixels;

        //Indica la ubicacion del popUp en la interfaz
        getWindow().setLayout((int)(anchoV * 0.8), (int)(altoV * 0.5));



    }
}