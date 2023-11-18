package es.unican.carchargers.activities.details;

import es.unican.carchargers.model.Charger;

public class DetailsPresenter implements IDetailsContract.Presenter {

    private IDetailsContract.View view;

    public void init(IDetailsContract.View view) {
        this.view = view;
    }


    public void OnChargerBotonFavClicked(Charger c) {
        //Si esta seleccionado, se quita de favs (por implementar...)
        //...

        view.anhadeCargadorAFavoritos(c);


    }

}
