package es.unican.carchargers.model;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

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

    public Charger() {
        this.operator = new Operator();
        this.address = new Address();
        this.connections = new ArrayList<>();
    }


}
