package es.unican.carchargers.common;

import android.app.AlertDialog;
import android.content.Context;

public class AndroidUtils {

    /**
     *  Muestra un alert dialog producido por un error conocido.
     * @param error Descripcion del error a mostrar.
     * @param context Siempre sera "this" para que se genere el alert dialog en la misma vista
     *                que lo llama.
     */
    public static void showLoadErrorDialog(String error, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Configurar el título y el mensaje de error
        builder.setTitle("Error");
        builder.setMessage(error);

        // Configurar un botón para cerrar el diálogo
        builder.setPositiveButton("Salir", (dialog, which) -> dialog.dismiss());

        // Mostrar el AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }



}
