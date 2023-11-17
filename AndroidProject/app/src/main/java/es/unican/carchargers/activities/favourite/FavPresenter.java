package es.unican.carchargers.activities.favourite;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import es.unican.carchargers.activities.main.MainPresenter;
import es.unican.carchargers.constants.ECountry;
import es.unican.carchargers.constants.ELocation;
import es.unican.carchargers.model.Charger;
import es.unican.carchargers.repository.ICallBack;
import es.unican.carchargers.repository.IRepository;
import es.unican.carchargers.repository.service.APIArguments;

public class FavPresenter implements IFavContract.Presenter {

    /** the view controlled by this presenter */
    private IFavContract.View view;

    private IFavContract.Presenter presenter;

    // Lista que obtenemos al llamar a la API
    private List<Charger> shownChargers;

    // Lista que mostramos al user.
    private List<Charger> chargersActuales;

    private List<Charger> chargersFav;
    private List<Charger> chargersFinal;
    private List<Charger> chargersIni;


    @Override
    public void init(IFavContract.View view) {
        this.view = view;
        view.init();
        load();


    }

    private void load() {
        IRepository repository = view.getRepository();

        // set API arguments to retrieve charging stations that match some criteria
        APIArguments args = APIArguments.builder()
                .setCountryCode(ECountry.SPAIN.code)
                .setLocation(ELocation.SANTANDER.lat, ELocation.SANTANDER.lon)
                .setMaxResults(50);

        ICallBack callback = new ICallBack() {
            @Override
            public void onSuccess(List<Charger> chargers) {
                FavPresenter.this.shownChargers =
                        chargers != null ? chargers : Collections.emptyList();

                // Almacenar la lista que se va a mostrar para en caso de modificar un filtro
                // y tener que volver atrás no depender de la llamada original a la API
                // que tambien queremos conservar.

                chargersIni = new ArrayList<>(shownChargers);

                chargersActuales = new ArrayList<Charger>();

                chargersFav = view.getFavoriteChargers();

                view.showChargersFav(FavPresenter.this.chargersFav);
                view.showLoadCorrect(FavPresenter.this.chargersFav.size());
            }

            @Override
            public void onFailure(Throwable e) {

            }


        };

        repository.requestChargers(args, callback);

    }




    @Override
    public void onChargerClicked(int index) {
        if (chargersFav != null && index < chargersFav.size()) {
            Charger charger = chargersFav.get(index);
            view.showChargerDetails(charger);
        }
    }


    public Charger getChargerById(String id) {

        for (int i = 0; i < chargersIni.size(); i++) {
            if (chargersIni.get(i).id.equals(id)) {
                return chargersIni.get(i);
            }
        }

        return null;
    }




}

