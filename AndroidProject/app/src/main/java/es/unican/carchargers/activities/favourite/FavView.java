package es.unican.carchargers.activities.favourite;

import static es.unican.carchargers.common.AndroidUtils.showLoadErrorDialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import es.unican.carchargers.R;
import es.unican.carchargers.activities.details.DetailsView;
import es.unican.carchargers.model.Charger;
import es.unican.carchargers.repository.IRepository;

@AndroidEntryPoint
public class FavView extends AppCompatActivity implements IFavContract.View {

    /** repository is injected with Hilt */
    @Inject IRepository repository;



    /** presenter that controls this view */
    IFavContract.Presenter presenter;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Initialize presenter-view connection
        presenter = new FavPresenter();
        presenter.init(this);
    }

    public void init() {

        ListView lv = findViewById(R.id.lvChargers);
        lv.setOnItemClickListener((parent, view, position, id) -> presenter.onChargerClicked(position));
    }

    @Override
    public IRepository getRepository() {
        return repository;
    }



    @Override
    public void showLoadCorrect(int chargers) {
        Toast.makeText(this, String.format("Cargados %d cargadores", chargers),
                Toast.LENGTH_LONG).show();
    }

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

    public SharedPreferences getActivityPreferencies() {
        //Accede al fichero de favoritos en modo privado
        return this.getSharedPreferences("Favoritos",Context.MODE_PRIVATE);
    }


    public List<Charger> getFavoriteChargers() {
        List<Charger> favoriteChargers = new ArrayList<>();

        // Obtén las preferencias compartidas
        SharedPreferences sharedPref = getActivityPreferencies();

        // Itera sobre las entradas de las preferencias compartidas
        Map<String, ?> allEntries = sharedPref.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            // Verifica si el valor asociado a la llave es true (indicando que es un cargador favorito)
            if (entry.getValue() instanceof Boolean && Boolean.TRUE.equals(entry.getValue())) {
                // Aquí, entry.getKey() sería el id del cargador favorito
                // Puedes usar este id para obtener el cargador correspondiente y agregarlo a la lista
                Charger favoriteCharger = presenter.getChargerById(entry.getKey());
                if (favoriteCharger != null) {
                    favoriteChargers.add(favoriteCharger);
                }
            }
        }

        return favoriteChargers;
    }


    public void showChargersFav(List<Charger> favs) {
        FavChargersArrayAdapter adapter = new FavChargersArrayAdapter(this, favs, presenter);
        ListView listView = findViewById(R.id.lvChargers);
        listView.setAdapter(adapter);
    }


    @Override
    public void showInfoNoFav() {
        Intent intent = new Intent(this, NoFavActivities.class);
        startActivity(intent);
    }

}