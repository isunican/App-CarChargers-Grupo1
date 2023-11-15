package es.unican.carchargers.activities.favourite;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import es.unican.carchargers.activities.main.IMainContract;
import es.unican.carchargers.constants.EConnectionType;
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


    /** Filtros activos */
    private List<Double> potenciasFiltro = new ArrayList<>();
    private List<EConnectionType> conectoresFiltro = new ArrayList<>();
    /** Filtros a devolver y guardar */
    private List<Double> potenciasFiltroAplicados;
    private List<EConnectionType> conectoresFiltroAplicados;

    /** Ordenacion aplicada actualmente */
    private String ordenacionAplicada;
    private Boolean ascendenteAplicado;

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
        if (chargersActuales != null && index < chargersActuales.size()) {
            Charger charger = chargersActuales.get(index);
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
                    //En cuanto sabemos que un charger vale, no seguimos comprobando.
                    break;
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
                    //En cuanto sabemos que un charger vale, no seguimos comprobando.
                    break;
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

    //Devuelve los filtros aplicados de manera que mainView sea capaz de mostrar los filtros
    //Aplicados anteriormente
    public List<Double> devolverFiltrosAplicadosPotencia() {
        return potenciasFiltroAplicados;
    }
    //Devuelve los filtros aplicados de manera que mainView sea capaz de mostrar los filtros
    //Aplicados anteriormente
    public List<EConnectionType> devolverFiltrosAplicadosConectores() {
        return conectoresFiltroAplicados;
    }

    //Devuelve los filtros aplicados de manera que mainView sea capaz de mostrar los filtros
    //Aplicados anteriormente TODO
    public void devolverOrdenAplicado() {

    }

    private void aplicarFiltros() {
        // Coger lista og
        List<Charger> chargersFiltrados = new ArrayList<>(shownChargers);

        // Ir aplicandoles todos los filtros que se haya.

        chargersFiltrados.retainAll(filtrarOriginalesPorPotencia());
        if (chargersFiltrados.isEmpty()) {
            // tratar lista vacia con error y volver atras
            view.showLoadSinCargadores("No hay cargadores para esta selección. " +
                    "Al cerrar este mensaje se volverá a la selección anterior.");
            return;

        }

        chargersFiltrados.retainAll(filtrarOriginalesPorConector());
        if (chargersFiltrados.isEmpty()) {
            // tratar lista vacia y volver atras
            view.showLoadSinCargadores("No hay cargadores para esta selección. " +
                    "Al cerrar este mensaje se volverá a la selección anterior.");
            return;
        }

        //Si tenemos exito y los nuevos filtros del usuario dan lugar a una lista
        //valida guardamos los filtros para devolverlos si son pedidos.
        potenciasFiltroAplicados = new ArrayList<>(potenciasFiltro);
        conectoresFiltroAplicados = new ArrayList<>(conectoresFiltro);

        // Mostrar el resultado y guardarlo por separado, en caso de que la
        // proxima llamada de este método acabe con una lista vacia.
        chargersActuales = new ArrayList<>(chargersFiltrados);
        view.showChargers(FavPresenter.this.chargersActuales);
        view.showLoadCorrect(FavPresenter.this.chargersActuales.size());
    }

    //Ordena la lista en funcion de un parametro
    public void onClickedAceptarOrdenacion(String criterioOrdenacion, boolean ascendente) {

        if (criterioOrdenacion == null) {
            view.showLoadError("No ha seleccionado ningún criterio.");
            return;
        }

        switch (criterioOrdenacion) {
            case "Precio":
                ordenaChargersPrecio(ascendente);
                break;
            default:
                // usar el dialog error de android.utils para indicar error.
                view.showLoadError("Esta ordenación no existe. Contacte con soporte para ver que ha ocurrido.");
                return;
        }

        ordenacionAplicada = criterioOrdenacion;
        ascendenteAplicado = ascendente;

        view.showChargers(FavPresenter.this.chargersActuales);
        view.showLoadCorrect(FavPresenter.this.chargersActuales.size());
    }

    public String getOrdenacionAplicada(){
        return ordenacionAplicada;
    }
    public Boolean getAscendenteAplicado() {
        return ascendenteAplicado;
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
        chargersActuales.removeIf(c -> c.extraerCosteCharger() == -1);

        // Usamos Collections.sort() para ordenar la lista
        chargersActuales.sort(comparadorPrecio);

    }

/*
    /**
     * Carga la vista con la lista inicial de cargadores.
     */
    public void listaActual() {
        view.showChargers(FavPresenter.this.chargersActuales);
        view.showLoadCorrect(FavPresenter.this.chargersActuales.size());
    }
*/


/*
    public void OnChargerBotonFavClicked(Charger c) {
        //Si esta seleccionado, se quita de favs (por implementar...)
        //...

        view.anhadeCargadorAFavoritos(c);
        //chargersFavoritos.add(c);

    }
*/


    public Charger getChargerById(String id) {

        for (int i = 0; i < chargersActuales.size(); i++) {
            if (chargersActuales.get(i).id.equals(id)) {
                return chargersActuales.get(i);
            }
        }

        return null;
    }

/*
    @Override
    public void onMenuFavoritosClicked() {
        List<Charger> chargersFavoritos = new ArrayList<>();
        chargersFavoritos = view.getFavoriteChargers();
        //Si vacia, lanzo actividad de aviso que no hay favs
        if (chargersFavoritos.isEmpty()) {
            view.showInfoNoFav();
        } else {
            chargersFav = view.getFavoriteChargers();
            view.showChargersFav(FavPresenter.this.chargersFav);
            view.showLoadCorrect(FavPresenter.this.chargersFav.size());
        }
    }
*/
}

