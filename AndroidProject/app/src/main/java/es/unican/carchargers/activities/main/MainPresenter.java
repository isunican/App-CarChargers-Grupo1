package es.unican.carchargers.activities.main;

import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import es.unican.carchargers.R;
import es.unican.carchargers.constants.EConnectionType;
import es.unican.carchargers.model.ConnectionType;
import es.unican.carchargers.repository.ICallBack;
import es.unican.carchargers.constants.ECountry;
import es.unican.carchargers.constants.ELocation;
import es.unican.carchargers.constants.EOperator;
import es.unican.carchargers.model.Charger;
import es.unican.carchargers.repository.IRepository;
import es.unican.carchargers.repository.service.APIArguments;

public class MainPresenter implements IMainContract.Presenter {

    /** the view controlled by this presenter */
    private IMainContract.View view;

    /** a cached list of charging stations currently shown */
    private List<Charger> shownChargers;
    private List<Charger> chargersFiltrados;

    /** Filtros activos */
    List<Double> potenciasFiltro = new ArrayList<>();
    List<EConnectionType> conectoresFiltro = new ArrayList<>();

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

    public List<Charger> filtrarOriginalesPorPotencia() {

        //Si el usuario no elige potencias y da a aceptar, interpretamos que no quiere filtrar y mostramos todos.
        if (potenciasFiltro.isEmpty()) {
            return shownChargers;
        }

        //Si alguna de las potencias que se pasan esta, se busca si un Charger la tiene.
        List<Charger> resultadoFiltro = new ArrayList<>();

        for (Charger charger : shownChargers) {
            for (Double potencia : potenciasFiltro) {
                if (charger.contienePotencia(potencia)) {
                    resultadoFiltro.add(charger);
                }
            }
        }

        if(resultadoFiltro.isEmpty()) {
            //Para indicar que este filtro te deja sin puntos
            return Collections.emptyList();
        } else {
            return resultadoFiltro;
        }

    }

    public List<Charger> filtrarOriginalesPorConector() {

        //Si el usuario no elige potencias y da a aceptar, interpretamos que no quiere filtrar y mostramos todos.
        if (conectoresFiltro.isEmpty()) {
            return shownChargers;
        }

        //Si alguna de las potencias que se pasan esta, se busca si un Charger la tiene.
        List<Charger> resultadoFiltro = new ArrayList<>();

        for (Charger charger : shownChargers) {
            for (EConnectionType conector : conectoresFiltro) {
                if (charger.contieneConector(conector)) {
                    resultadoFiltro.add(charger);
                }
            }
        }

        if(resultadoFiltro.isEmpty()) {
            //Para indicar que este filtro te deja sin puntos
            return Collections.emptyList();
        } else {
            return resultadoFiltro;
        }

    }

    public void onAceptarFiltroConectoresClicked(List<EConnectionType> conectores) {
        conectoresFiltro = conectores;
        aplicarFiltros();
    }
    public void onAceptarFiltroPotenciaClicked(List<Double> potencias) {
        potenciasFiltro = potencias;
        aplicarFiltros();
    }

    private void aplicarFiltros() {

        // Coger lista og
        chargersFiltrados = shownChargers;

        // Ir aplicandoles todos los filtros que se haya.

        chargersFiltrados.retainAll(filtrarOriginalesPorPotencia());
        if (chargersFiltrados.isEmpty()) {
            // TODO tratar lista vacia con error y volver atras
        }

        chargersFiltrados.retainAll(filtrarOriginalesPorConector());
        if (!conectoresFiltro.isEmpty()) {
            // TODO tratar lista vacia y volver atras
        }

        //mostrar el resultado
        view.showChargers(MainPresenter.this.chargersFiltrados);
        view.showLoadCorrect(MainPresenter.this.chargersFiltrados.size());
    }

    public void listaOriginal() {
        view.showChargers(MainPresenter.this.shownChargers);
        view.showLoadCorrect(MainPresenter.this.shownChargers.size());
    }

}
