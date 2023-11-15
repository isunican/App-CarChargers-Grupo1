package es.unican.carchargers.activities.favourite;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import es.unican.carchargers.constants.ECountry;
import es.unican.carchargers.constants.ELocation;
import es.unican.carchargers.model.Charger;
import es.unican.carchargers.repository.ICallBack;
import es.unican.carchargers.repository.IRepository;
import es.unican.carchargers.repository.service.APIArguments;

public class FavPresenter implements IFavContract.Presenter {

    /** the view controlled by this presenter */
    private IFavContract.View view;

    // Lista que obtenemos al llamar a la API
    private List<Charger> shownChargers;

    // Lista que mostramos al user.
    private List<Charger> chargersActuales;

    private List<Charger> chargersFav;
    private List<Charger> chargersFinal;
    private List<Charger> chargersIni;


    @Override
    public void init(IFavContract.View view) {
        this.view = (IFavContract.View) view;
        ((IFavContract.View) view).init();
        load();
    }

    /**
     * This method requests a list of charging stations from the repository, and requests
     * the view to show them.
     */

    //TIENE QUE CARGAR DE EL FICHERO FAVORITOS NO DEL REPOSITORIO GLOBAL
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

                chargersActuales = new ArrayList<>();


                //chargersFav = view.getFavoriteChargers();
                chargersIni.removeIf(c -> chargersFav.contains(c));

                chargersActuales.addAll(chargersFav);
                chargersActuales.addAll(chargersIni);

                view.showChargers(FavPresenter.this.chargersActuales);
                view.showLoadCorrect(FavPresenter.this.chargersActuales.size());
            }

            @Override
            public void onFailure(Throwable e) {
                FavPresenter.this.shownChargers = Collections.emptyList();
                String error = "El sistema no pudo conectarse a la red";
                view.showLoadError(error);
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

        for (int i = 0; i < chargersActuales.size(); i++) {
            if (chargersActuales.get(i).id.equals(id)) {
                return chargersActuales.get(i);
            }
        }

        return null;
    }
}

