package es.unican.carchargers.repository;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.MenuInflater;

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
class Repository implements IRepository {

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
                    public void onFailure(Call<List<Charger>> call, Throwable t, MainPresenter mp) {

                        // Crear un AlertDialog
                        Context c = this;
                        AlertDialog.Builder builder = new AlertDialog.Builder(c); // context es una referencia al contexto de la actividad

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
                });
    }
/*
    @Override
    public void onFailure(Call<List<Charger>> call, Throwable t) {
        // Crear un AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context); // context es una referencia al contexto de la actividad

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

    /**
     * Cuadro de error.
     * @param menu Menu sobre el que desplegar el popUp de error.
     * @return true si ha sido correcto.
     */
    public boolean onCreateAvisoError(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

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
