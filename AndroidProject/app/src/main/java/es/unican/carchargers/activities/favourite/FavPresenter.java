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
        //chargersFav = view.getFavoriteChargers();
        //view.showChargersFav(FavPresenter.this.chargersFav);
        //view.showLoadCorrect(FavPresenter.this.chargersFav.size());
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

