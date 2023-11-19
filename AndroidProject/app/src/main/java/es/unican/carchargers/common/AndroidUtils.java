package es.unican.carchargers.common;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.TextView;

public class AndroidUtils {

    private AndroidUtils(){
        throw new IllegalStateException("Utility class");
    }

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


    /**
     *  Establece el valor de un campo en concreto de la vista a detalle de un punto de carga.
     * @param textView campo de la vista a detalle a establecer valor.
     * @param valor valor a establecer en el campo de la vista a detalle.
     */
    public static void validarYEstablecerTextView(TextView textView, String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            textView.setText("-");
        } else {
            textView.setText(valor);
        }
    }


    public static String validarYEstablecerString(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return "-";
        } else {
            return valor;
        }
    }

}
