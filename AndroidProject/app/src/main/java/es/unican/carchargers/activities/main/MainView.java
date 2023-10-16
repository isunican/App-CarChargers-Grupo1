package es.unican.carchargers.activities.main;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import es.unican.carchargers.R;
import es.unican.carchargers.activities.details.DetailsView;
import es.unican.carchargers.activities.info.InfoActivity;
import es.unican.carchargers.model.Charger;
import es.unican.carchargers.repository.IRepository;

@AndroidEntryPoint
public class MainView extends AppCompatActivity implements IMainContract.View {

    /** repository is injected with Hilt */
    @Inject IRepository repository;

    /** presenter that controls this view */
    IMainContract.Presenter presenter;

    //Para elegir filtros
    AlertDialog dialogFiltros;

    //Lista de cargadores obtenida por la llamada inicial a la API.
    List<Charger> chargersIniciales;
    List<Charger> chargersFiltrados;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize presenter-view connection
        presenter = new MainPresenter();
        presenter.init(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuItemInfo:
                presenter.onMenuInfoClicked();
                return true;
            case R.id.filtro:
                // inicializar el dialogo de filtros
                filtrosDialog();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Crea un alertDialog para elegir los filtros.
     */
    public void filtrosDialog() {
        LayoutInflater inflater= LayoutInflater.from(this);
        View view=inflater.inflate(R.layout.menu_filtros, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        Button btnPotencia = (Button)view.findViewById(R.id.btnPotencia);
        btnPotencia.setOnClickListener(v -> {
            dialogFiltros.dismiss();
            filtradoPotenciaDialog();
        });

        Button btnAceptar = (Button)view.findViewById(R.id.btnAceptar);
        btnAceptar.setOnClickListener(v -> {
            // TODO guardar seleccion de filtros
            dialogFiltros.dismiss();
        });

        Button btnCancelar = (Button)view.findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(v -> {
            dialogFiltros.dismiss();
        });



        // Configurar el título y el mensaje de error
        builder.setTitle("Filtros");

        // Mostrar el AlertDialog
        dialogFiltros = builder.create();
        // Mostrar el AlertDialog para elegir filtros
        dialogFiltros.show();
    }

    public void filtradoPotenciaDialog() {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(MainView.this, R.style.AlertDialogTema);
        builder.setTitle("Marque las casillas que más se adapten a su búsqueda:");
        builder.setIcon(R.drawable.icono_filtro);

        //True = Si pinchas fuera se cierra la ventana
        builder.setCancelable(true);
        String[] potencias = new String[] {
                "2kW", "7.4kW", "22kW", "43kW", "50kW"
        };
        //Lista con los valores de potencias, igual que potencias
        double[] potenciasEnteras = new double[] {
                2000, 7400, 22000, 43000, 50000
        };

        //Por defecto no estará seleccionada ninguna opción
        boolean[] checkItems = new boolean[] {
                false, false, false, false, false
        };
        //Convierte el array de potencias en una lista
        final List<String> potenciaList = Arrays.asList(potencias);

        builder.setMultiChoiceItems(potencias, checkItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                //Se verifica que hay un item seleccionado
                checkItems[which] = isChecked;
            }
        });

        //Al pulsar aceptar
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                List<Double> potenciasSeleccionadas = new ArrayList<>();
                for (int i = 0; i < checkItems.length; i++) {
                    if (checkItems[i]) {
                        potenciasSeleccionadas.add(potenciasEnteras[i]);
                    }
                }

                filtraPorPot(potenciasSeleccionadas);
            }
        });

        //Al pulsar cancelar
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                filtrosDialog();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void filtraPorPot(List<Double> potencias) {
        //List<Charger> chargersIniciales;
        //List<Charger> chargersFiltrados;

        //Si alguna de las potencias que se pasan esta, se busca si un Charger la tiene.

        for (Charger charger : chargersIniciales) {
            for (Double potencia : potencias) {
                if (charger.contienePotencia(potencia)) {
                    chargersFiltrados.add(charger);
                }
            }
        }

        ChargersArrayAdapter adapter = new ChargersArrayAdapter(this, chargersFiltrados);
        ListView listView = findViewById(R.id.lvChargers);
        listView.setAdapter(adapter);

    }

    @Override
    public void init() {
        ListView lv = findViewById(R.id.lvChargers);
        lv.setOnItemClickListener((parent, view, position, id) -> presenter.onChargerClicked(position));

    }


    @Override
    public IRepository getRepository() {
        return repository;
    }

    @Override
    public void showChargers(List<Charger> chargers) {
        chargersIniciales = chargers;
        ChargersArrayAdapter adapter = new ChargersArrayAdapter(this, chargers);
        ListView listView = findViewById(R.id.lvChargers);
        listView.setAdapter(adapter);
    }

    @Override
    public void showLoadCorrect(int chargers) {
        Toast.makeText(this, String.format("Cargados %d cargadores", chargers),
                Toast.LENGTH_LONG).show();
    }

    /**
     * Crea un alertDialog que avisa de un error determinado
     * TODO: Pasar por parametro un string que rellene el campo de setMessage con el string de parametro
     */
    public void showLoadErrorDialog(String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Configurar el título y el mensaje de error
        builder.setTitle("Error");
        builder.setMessage(error);

        // Configurar un botón para cerrar el diálogo
        builder.setPositiveButton("Salir", (dialog, which) -> dialog.dismiss());

        // Mostrar el AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public void showLoadError(String error) {
        //Toast.makeText(this, "Error cargando cargadores", Toast.LENGTH_LONG).show();
        showLoadErrorDialog(error);
    }


    @Override
    public void showChargerDetails(Charger charger) {
        Intent intent = new Intent(this, DetailsView.class);
        intent.putExtra(DetailsView.INTENT_CHARGER, Parcels.wrap(charger));
        startActivity(intent);
    }

    @Override
    public void showInfoActivity() {
        Intent intent = new Intent(this, InfoActivity.class);
        startActivity(intent);
    }

}