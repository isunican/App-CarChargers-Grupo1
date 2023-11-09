package es.unican.carchargers.activities.main;

import static android.app.PendingIntent.getActivity;
import static es.unican.carchargers.common.AndroidUtils.showLoadErrorDialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;

import android.widget.RadioButton;
import android.widget.RadioGroup;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import es.unican.carchargers.R;
import es.unican.carchargers.activities.details.DetailsView;
import es.unican.carchargers.activities.info.InfoActivity;
import es.unican.carchargers.constants.EConnectionType;
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

    AlertDialog ordenDialog;



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
                return true;

            case R.id.orden:
                // inicializar el dialogo de filtros
                ordenDialog();
                return true;
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

        Button btnConector = (Button)view.findViewById(R.id.btnConector);
        btnConector.setOnClickListener(v -> {
            dialogFiltros.dismiss();
            filtradoConectorDialog();
        });

        TextView btnCancelar = (TextView) view.findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(v -> dialogFiltros.dismiss());

        // Configurar el título y el mensaje de error
        builder.setTitle("Filtros");

        // Mostrar el AlertDialog
        dialogFiltros = builder.create();
        // Mostrar el AlertDialog para elegir filtros
        dialogFiltros.show();
    }

    public void ordenDialog() {
        LayoutInflater inflater= LayoutInflater.from(this);
        View view=inflater.inflate(R.layout.activity_menu_orden, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainView.this);
        builder.setView(view);

        // Configurar el título y el mensaje de error
        builder.setTitle("Ordenar");
        // Mostrar el AlertDialog
        ordenDialog = builder.create();
        // Mostrar el AlertDialog para elegir filtros
        ordenDialog.show();

        //True = Si pinchas fuera se cierra la ventana
        builder.setCancelable(true);

        CheckBox checkBox1 = view.findViewById(R.id.checkbox_precio);
        String[] tiposOrden = new String[] {
                "Precio"
        };

        //Por defecto no estará seleccionada ninguna opción
        boolean[] checkItems = new boolean[] {
                false
        };

        // Configurar los OnCheckedChangeListener para cada CheckBox
        checkBox1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            checkItems[0] = isChecked;
        });

        final boolean[] tipoOrdenAscDesc = {true};

        builder.setMultiChoiceItems(tiposOrden, checkItems, (dialog, which, isChecked) -> {
            //Se verifica que hay un item seleccionado
            checkItems[which] = isChecked;
        });


        RadioButton radioButtonAsc = view.findViewById(R.id.radioButtonAsc);
        RadioButton radioButtonDesc = view.findViewById(R.id.radioButtonDesc);

        radioButtonAsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipoOrdenAscDesc[0] = true;
            }
        });

        radioButtonDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipoOrdenAscDesc[0] = false;
            }
        });


        TextView btnAceptarOrden = (TextView)view.findViewById(R.id.btnAceptarOrden);
        btnAceptarOrden.setOnClickListener(v -> {
            String orden = null;
            for (int i = 0; i < checkItems.length; i++) {
                if (checkItems[i]) {
                    orden = tiposOrden[i];
                }
            }
            presenter.onClickedAceptarOrdenacion(orden, tipoOrdenAscDesc[0]);
            ordenDialog.dismiss();
        });

        TextView btnCancelarOrden = (TextView) view.findViewById(R.id.btnCancelarOrden);
        btnCancelarOrden.setOnClickListener(v -> {
            ordenDialog.dismiss();
        });

    }

    /**
     * Muestra un dialog con los checkboxs a seleccionar en base a las distintas
     * potencias disponibles.
     */
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
                2, 7.4, 22, 43, 50
        };

        //Por defecto no estará seleccionada ninguna opción
        boolean[] checkItems = new boolean[] {
                false, false, false, false, false
        };

        builder.setMultiChoiceItems(potencias, checkItems, (dialog, which, isChecked) -> {
            //Se verifica que hay un item seleccionado
            checkItems[which] = isChecked;
        });

        //Al pulsar aceptar
        builder.setPositiveButton("Aceptar", (dialog, which) -> {

            List<Double> potenciasSeleccionadas = new ArrayList<>();
            for (int i = 0; i < checkItems.length; i++) {
                if (checkItems[i]) {
                    potenciasSeleccionadas.add(potenciasEnteras[i]);
                }
            }

            presenter.onAceptarFiltroPotenciaClicked(potenciasSeleccionadas);
        });

        //Al pulsar cancelar
        builder.setNegativeButton("Cancelar", (dialog, which) -> filtrosDialog());

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void filtradoConectorDialog() {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(MainView.this, R.style.AlertDialogTema);
        builder.setTitle("Marque las casillas que más se adapten a su búsqueda:");
        builder.setIcon(R.drawable.icono_filtro);

        //True = Si pinchas fuera se cierra la ventana
        builder.setCancelable(true);

        String[] conectores = EConnectionType.obtenerNombres();

        //Por defecto no estará seleccionada ninguna opción
        boolean[] checkItemsConector = new boolean[] {
                false, false, false, false, false, false, false, false, false
        };


        builder.setMultiChoiceItems(conectores, checkItemsConector, (dialog, which, isChecked) -> {
            //Se verifica que hay un item seleccionado
            checkItemsConector[which] = isChecked;
        });

        //Al pulsar aceptar
        builder.setPositiveButton("Aceptar", (dialog, which) -> {

            List<EConnectionType> conectoresSeleccionados = new ArrayList<>();

            for (int i = 0; i < checkItemsConector.length; i++) {
                if (checkItemsConector[i]) {
                    conectoresSeleccionados.add(EConnectionType.obtenerConnectionTypePorPos(i));
                }
            }

            presenter.onAceptarFiltroConectoresClicked(conectoresSeleccionados);
        });

        //Al pulsar cancelar
        builder.setNegativeButton("Cancelar", (dialog, which) -> filtrosDialog());

        AlertDialog dialog = builder.create();
        dialog.show();

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
     * Gestiona errores cuando no hay cargadores, volviendo a la lista de cargadorres inicial.
     * @param error mensaje explicativo del error.
     */
    public void showLoadSinCargadores(String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Configurar el título y el mensaje de error
        builder.setTitle("Error");
        builder.setMessage(error);

        // Configurar un botón para cerrar el diálogo
        builder.setPositiveButton("Salir", (dialog, which) -> presenter.listaActual());

        // Mostrar el AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    /**
     * Implementacion de gestion de errores general.
     * @param error mensaje explicativo del error.
     */
    @Override
    public void showLoadError(String error) {
        showLoadErrorDialog(error, this);
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

    public SharedPreferences getActivityPreferencies() {
        //Accede al fichero de favoritos en modo privado
        return this.getPreferences(Context.MODE_PRIVATE);
       /* Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);*/
    }




}