package es.unican.carchargers.activities.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import es.unican.carchargers.repository.ICallBack;
import es.unican.carchargers.constants.ECountry;
import es.unican.carchargers.constants.ELocation;
import es.unican.carchargers.model.Charger;
import es.unican.carchargers.repository.IRepository;
import es.unican.carchargers.repository.service.APIArguments;

public class MainPresenter implements IMainContract.Presenter {

    /** the view controlled by this presenter */
    private IMainContract.View view;

    /** a cached list of charging stations currently shown */
    private List<Charger> shownChargers;
    private List<Charger> chargersFiltrados;

    @Override
    public void init(IMainContract.View view) {
        this.view = view;
        view.init();
        load();
    }

    /**
     * This method requests a list of charging stations from the repository, and requests
     * the view to show them.
     */
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
                MainPresenter.this.shownChargers =
                        chargers != null ? chargers : Collections.emptyList();
                view.showChargers(MainPresenter.this.shownChargers);
                view.showLoadCorrect(MainPresenter.this.shownChargers.size());
            }

            @Override
            public void onFailure(Throwable e) {
                MainPresenter.this.shownChargers = Collections.emptyList();
                String error = "El sistema no pudo conectarse a la red";
                view.showLoadError(error);
            }
        };

        repository.requestChargers(args, callback);

    }

    @Override
    public void onChargerClicked(int index) {
        if (shownChargers != null && index < shownChargers.size()) {
            Charger charger = shownChargers.get(index);
            view.showChargerDetails(charger);
        }
    }

    @Override
    public void onMenuInfoClicked() {
        view.showInfoActivity();
    }

    public void onAceptarFiltroPotenciaClicked(List<Double> potencias) {

        //Si el usuario no elige potencias y da a aceptar, interpretamos que no quiere filtrar y mostramos todos.
        if (potencias.isEmpty()) {
            listaOriginal();
            return;
        }

        //Si alguna de las potencias que se pasan esta, se busca si un Charger la tiene.
        chargersFiltrados = new ArrayList<>();

        for (Charger charger : shownChargers) {
            for (Double potencia : potencias) {
                if (charger.contienePotencia(potencia)) {
                    chargersFiltrados.add(charger);
                }
            }
        }

        if(chargersFiltrados.isEmpty()) {
            view.showLoadSinCargadores("No se han encontrado cargadores que se ajusten a tu busqueda");
            view.showChargers(MainPresenter.this.chargersFiltrados);
            view.showLoadCorrect(MainPresenter.this.chargersFiltrados.size());
            return;
        }

        view.showChargers(MainPresenter.this.chargersFiltrados);
        view.showLoadCorrect(MainPresenter.this.chargersFiltrados.size());

    }

    //Ordena la lista en funcion de un parametro
    public void onClickedAceptarOrdenacion(String criterioOrdenacion, boolean ascendente) {

        switch (criterioOrdenacion) {
            case "Precio":
                ordenaChargersPrecio(ascendente);
                break;
            default:
                // usar el dialog error de android.utils para indicar error.
                view.showLoadError("Esta ordenación no existe. Contacte con soporte para ver que ha ocurrido.");
                return;
        }

        view.showChargers(MainPresenter.this.shownChargers);
        view.showLoadCorrect(MainPresenter.this.shownChargers.size());
    }

    public void ordenaChargersPrecio(boolean ascendente) {
        // Creamos un comparador personalizado para ordenar por el precio
        Comparator<Charger> comparadorPrecio = (charger1, charger2) -> {

            double precio1 = charger1.extraerCosteCharger();
            double precio2 = charger2.extraerCosteCharger();

            if (ascendente) {
                return Double.compare(precio1, precio2);
            } else {
                return Double.compare(precio2, precio1);
            }

        };

        // Equivalente a recorrer todos los objetos charger y quitarlos con el criterio de la lambda
        // Sugerenci de sonar.
        shownChargers.removeIf(c -> c.extraerCosteCharger() == -1);

        // Usamos Collections.sort() para ordenar la lista
        shownChargers.sort(comparadorPrecio);

    }



    /**
     * Carga la vista con la lista inicial de cargadores.
     */
    public void listaOriginal() {
        view.showChargers(MainPresenter.this.shownChargers);
        view.showLoadCorrect(MainPresenter.this.shownChargers.size());
    }



}
