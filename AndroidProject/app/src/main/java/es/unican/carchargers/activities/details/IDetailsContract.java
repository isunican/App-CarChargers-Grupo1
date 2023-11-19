package es.unican.carchargers.activities.details;

import android.content.SharedPreferences;

import es.unican.carchargers.model.Charger;

public interface IDetailsContract {


    public interface Presenter {

        void init(View view);
        void OnChargerBotonFavClicked(Charger c);


    }

    public interface View {


        SharedPreferences getActivityPreferencies();
        void anhadeCargadorAFavoritos(Charger c);


    }




}
