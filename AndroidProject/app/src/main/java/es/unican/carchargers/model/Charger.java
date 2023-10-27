package es.unican.carchargers.model;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A charging station according to the OpenChargeMap API
 * Documentation: https://openchargemap.org/site/develop/api#/operations/get-poi
 *
 * Currently it only includes a sub-set of the complete model returned by OpenChargeMap
 */
@Parcel
public class Charger {
    
    @SerializedName("ID")                   public String id;
    @SerializedName("NumberOfPoints")       public int numberOfPoints;
    @SerializedName("UsageCost")            public String usageCost;

    @SerializedName("OperatorInfo")         public Operator operator;
    @SerializedName("AddressInfo")          public Address address;

    @SerializedName("Connections")          public List<Connection> connections;

    /**
     * Lista los nombres de los tipos de conector
     * @return List<String>
     */
    public List<String> listarTiposConector(){
        List<String> lista = new ArrayList<>();
        for (Connection c:connections){
            lista.add(c.connectionType.title);
        }
        return lista;
    }



    /**
     * Comprueba la disponibilidad de los cargadores de un punto de carga.
     * @return true si esta disponible o el statusType es null.
     */
    public boolean comprobarDiponibilidad(){
        List<String> lista = new ArrayList<>();
        for (Connection c:connections){
            if (c.statusType == null || c.statusType.isOperational == true) {
                return true;
            }
        }
        return false;
    }


    /**
     * Comprueba si un cargador contiene la potencia indicada.
     * @param potencia potencia a comprobar
     * @return true si el cargador contiene la potencia indicada
     */
    public boolean contienePotencia(double potencia) {

        //Comprobar si alguno de sus valores Power (dentro de sus connections es el indicado)
        for (Connection c : connections) {
            if (c.powerKW == potencia) {

                return true;
            }
        }
        return false;
    }

    // Este metodo hace un best effort para extraer del string libre usageCost el coste de
    // uso del punto de carga.
    // Si no se puede extraer le ponemos el nº mas alto posible.
    // Si leemos varios precios usamos el mas alto.
    // Ya que el precio habitual podria ser de 0.3€/kWh a 0.7€/kWh si detectamos algo como
    // 10€, que se referira al parking, le ponemos el nº mas alto posible.
    // Si se da lo anterior y nº validos, cogemos el valido.
    public double extraerCosteCharger() {

        // Define el patrón regex para buscar un número con decimales en el String.
        Pattern pattern = Pattern.compile("\\d[,.]\\d\\d€/kWh");
        Matcher matcher = pattern.matcher(usageCost);

        if (matcher.find()) {
            // Si se encuentra un número, lo extraemos y lo parseamos a double.
            String numberStr = matcher.group(1);
            double number = Double.parseDouble(numberStr);
            return number;
        } else {

            return Double.MAX_VALUE; // Valor por defecto mas caro posible
        }
    }

    public Charger() {
        this.operator = new Operator();
        this.address = new Address();
        this.connections = new ArrayList<>();
    }


}
