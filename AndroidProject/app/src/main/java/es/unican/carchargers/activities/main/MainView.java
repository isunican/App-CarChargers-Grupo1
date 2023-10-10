package es.unican.carchargers.activities.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.parceler.Parcels;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import es.unican.carchargers.R;
import es.unican.carchargers.activities.details.DetailsView;
import es.unican.carchargers.activities.info.InfoActivity;
import es.unican.carchargers.model.Charger;
import es.unican.carchargers.repository.ICallBack;
import es.unican.carchargers.repository.IRepository;
import es.unican.carchargers.repository.Repository;

@AndroidEntryPoint
public class MainView extends AppCompatActivity implements IMainContract.View {

    /** repository is injected with Hilt */
    @Inject IRepository repository;

    /** presenter that controls this view */
    IMainContract.Presenter presenter;

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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void init() {
        // initialize listener to react to touch selections in the list
        ListView lv = findViewById(R.id.lvChargers);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.onChargerClicked(position);
            }
        });
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

    public void showLoadErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Configurar el título y el mensaje de error
        builder.setTitle("Error");
        builder.setMessage("Error cargando cargadores");

        // Configurar un botón para cerrar el diálogo
        builder.setPositiveButton("Salir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cerrar el diálogo si el usuario hace clic en "Salir"
                dialog.dismiss();
            }
        });

        // Mostrar el AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public void showLoadError() {
<<<<<<< HEAD
=======
<<<<<<< HEAD
        //Toast.makeText(this, "Error cargando cargadores", Toast.LENGTH_LONG).show();
        showLoadErrorDialog();
=======
>>>>>>> nombre-de-la-nueva-rama
        
        //Toast.makeText(this, "Error cargando cargadores", Toast.LENGTH_LONG).show();
        // Crear un AlertDialog
        ICallBack cb = null;

        AlertDialog.Builder builder = new AlertDialog.Builder(MainView.this); // context es una referencia al contexto de la actividad

        // Configurar el título y el mensaje de error
        builder.setTitle("Error");
        builder.setMessage("Ha ocurrido un error: " + t.getMessage());

        // Configurar un botón para cerrar el diálogo
        builder.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cerrar el diálogo
                dialog.dismiss();
            }
        });

        // Mostrar el AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();

        // Llamar al callback de onFailure para que la lógica de manejo de errores continúe
        cb.onFailure(t);
<<<<<<< HEAD
=======
>>>>>>> pruebaErrorConectividad
>>>>>>> nombre-de-la-nueva-rama
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