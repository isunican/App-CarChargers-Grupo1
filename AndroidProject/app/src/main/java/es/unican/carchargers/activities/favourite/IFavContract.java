package es.unican.carchargers.activities.favourite;

import android.content.SharedPreferences;

import java.util.List;

import es.unican.carchargers.model.Charger;
import es.unican.carchargers.repository.IRepository;

/**
 * The Presenter-View contract for the Main activity.
 * The Main activity shows a list of charging stations.
 */
public interface IFavContract {

    /**
     * Methods that must be implemented in the Main Presenter.
     * Only the View should call these methods.
     */
    public interface Presenter {

        /**
         * Links the presenter with its view.
         * Only the View should call this method
         * @param view
         */
        public void init(View view);


        /**
         * The presenter is informed that a charging station has been clicked
         * Only the View should call this method
         * @param index the index of the clicked charging station
         */
        public void onChargerClicked(int index);

        Charger getChargerById(String id);



    }

    /**
     * Methods that must be implemented in the Main View.
     * Only the Presenter should call these methods.
     */
    public interface View {

        /**
         * Initialize the view. Typically this should initialize all the listeners in the view.
         * Only the Presenter should call this method
         */
        public void init();

        /**
         * Returns a repository that can be called by the Presenter to retrieve charging stations.
         * This method must be located in the view because Android resources must be accessed
         * in order to instantiate a repository (for example Internet Access). This requires
         * dependencies to Android. We want to keep the Presenter free of Android dependencies,
         * therefore the Presenter should be unable to instantiate repositories and must rely on
         * the view to create the repository.
         * Only the Presenter should call this method
         *
         * @return
         */
        public IRepository getRepository();





        /**
         * The view is requested to display the detailed view of the given charging station.
         * Only the Presenter should call this method
         *
         * @param charger the charging station
         */
        public void showChargerDetails(Charger charger);



        /**
         * Metodos de acceso y uso de cargadores favoritos
         */

        //Obtiene el fichero de favoritos de la mainView
        public SharedPreferences getActivityPreferencies();


        List<Charger> getFavoriteChargers();

        /**
         * Si la vista general de cargadores favoritos no tiene favs, se avisara.
         */
        void showInfoNoFav();

        void showLoadCorrect(int size);

        void showLoadError(String error);

        /**
         * The view is requested to display the given list of charging stations.
         * Only the Presenter should call this method
         *
         * @param chargersFav the list of charging stations
         */
        void showChargersFav(List<Charger> chargersFav);

    }
}