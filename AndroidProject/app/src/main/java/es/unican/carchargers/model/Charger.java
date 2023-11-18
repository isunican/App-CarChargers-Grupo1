package es.unican.carchargers.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.unican.carchargers.constants.EConnectionType;

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
    public boolean comprobarDisponibilidad() {
        Iterator<Connection> iterator = connections.iterator();

        while (iterator.hasNext()) {
            Connection c = iterator.next();
            if (c.statusType == null) {
                return false;
            } else if (c.statusType.isOperational) {

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

    // Devuelve true si alguno de los cargadores en este punto tiene el tipo de conector indicado.
    public boolean contieneConector(EConnectionType conector) {

        //Comprobar si alguno de sus valores Power (dentro de sus connections es el indicado)
        for (Connection c : connections) {
            if (c.connectionType.id == conector.getId()) {

                return true;
            }
        }
        return false;
    }
    // Este metodo hace un best effort para extraer del string libre usageCost el coste de
    // uso del punto de carga.
    public double extraerCosteCharger() {

        if (usageCost != null && !usageCost.isEmpty())  {

            Pattern pattern = Pattern.compile("(\\d[,.]\\d{1,2})€/kWh");
            Matcher matcher = pattern.matcher(usageCost);

            if (matcher.find()) {
                // Si se encuentra un número, lo extraemos y lo parseamos a double.
                String numberStr = matcher.group(1);

                // Reemplaza la coma por un punto en la cadena
                numberStr = numberStr.replace(",", ".");

                return Double.parseDouble(numberStr);

            }

        }

        // Esto indica que no se puede extraer el precio, para indicar que no se quiere en la lista ordenada.
        return -1;

    }

    public Charger() {
        this.operator = new Operator();
        this.address = new Address();
        this.connections = new ArrayList<>();

    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numberOfPoints, usageCost, operator, address, connections);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Charger charger = (Charger) o;
        return Objects.equals(id, charger.id);
    }

}
