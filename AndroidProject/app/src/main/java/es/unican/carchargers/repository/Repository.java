package es.unican.carchargers.repository;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import es.unican.carchargers.activities.main.MainPresenter;
import es.unican.carchargers.model.Charger;
import es.unican.carchargers.repository.service.APIArguments;
import es.unican.carchargers.repository.service.IOpenChargeMapAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * An implementation of a repository that uses the OpenChargeMap API to retrieve charging stations.
 */
class Repository extends AppCompatActivity implements IRepository {

    private final IOpenChargeMapAPI api;

    Repository(IOpenChargeMapAPI api) {
        this.api = api;
    }

    @Override
    public void requestChargers(APIArguments args, ICallBack cb) {
        Map<String, Object> map = args != null ? args.toMap() : null;
        cleanArguments(map);
        api.chargers(map)
                .enqueue(new Callback<List<Charger>>() {
                    @Override
                    public void onResponse(Call<List<Charger>> call, Response<List<Charger>> response) {
                        cb.onSuccess(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<Charger>> call, Throwable t) {

<<<<<<< HEAD
=======
<<<<<<< HEAD
=======
>>>>>>> nombre-de-la-nueva-rama
                        // Crear un AlertDialog

                        AlertDialog.Builder builder = new AlertDialog.Builder(Repository.this); // context es una referencia al contexto de la actividad

                        // Configurar el título y el mensaje de error
                        builder.setTitle("Error");
                        builder.setMessage("Ha ocurrido un error: " + t.getMessage());

                        // Configurar un botón para cerrar el diálogo
                        builder.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Cerrar el diálogo
                                dialog.dismiss();
                            }
                        });

                        // Mostrar el AlertDialog
                        AlertDialog dialog = builder.create();
                        dialog.show();

                        // Llamar al callback de onFailure para que la lógica de manejo de errores continúe
<<<<<<< HEAD
=======
>>>>>>> pruebaErrorConectividad
>>>>>>> nombre-de-la-nueva-rama
                        cb.onFailure(t);
                    }
                });
    }
/*
    @Override
    public void onFailure(Call<List<Charger>> call, Throwable t) {
        // Crear un AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context); // context es una referencia al contexto de la actividad

<<<<<<< HEAD
=======
<<<<<<< HEAD
=======
>>>>>>> nombre-de-la-nueva-rama
        // Configurar el título y el mensaje de error
        builder.setTitle("Error");
        builder.setMessage("Ha ocurrido un error: " + t.getMessage());

        // Configurar un botón para cerrar el diálogo
        builder.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cerrar el diálogo
                dialog.dismiss();
            }
        });

        // Mostrar el AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();

        // Llamar al callback de onFailure para que la lógica de manejo de errores continúe
        cb.onFailure(t);
    }
*/
<<<<<<< HEAD
=======
>>>>>>> pruebaErrorConectividad
>>>>>>> nombre-de-la-nueva-rama


    /**
     * Cleans the argument map. This mas is used by the QueryMap feature of Retrofit.
     * This method makes sure the map is compatible with Retrofit's QueryMap.
     *
     * Retrofit QueryMap does not support List values. Transform them into a comma separated String
     * ref: https://github.com/square/retrofit/issues/1324
     *
     * Retrofit QueryMap does not support null values. Remove them to avoid retrofit errors.
     * ref: https://github.com/square/retrofit/issues/2741
     *
     * @param map
     */
    private void cleanArguments(Map<String, Object> map) {
        // transform List values into a comma-separated string
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object value = entry.getValue();

            if (value instanceof List) {
                List<?> list = (List<?>) value;

                if (list.isEmpty()) {
                    entry.setValue(null);
                } else {
                    String str = list.stream()
                            .map(i -> String.valueOf(i))
                            .collect(Collectors.joining(","));
                    entry.setValue(str);
                }
            }
        }

        // remove null values
        map.values().removeAll(Collections.singleton(null));
    }
}
