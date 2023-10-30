package es.unican.carchargers.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

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

    public List<String> listarTiposConector(){
        List<String> lista = new ArrayList<>();
        for (Connection c:connections){
            lista.add(c.connectionType.title);
        }
        return lista;
    }

<<<<<<< HEAD
    // Devuelve true si alguno de los cargadores en este punto tiene la potencia indicada.
    public boolean contienePotencia(double potencia) {

        //Comprobar si alguno de sus valores Power (dentro de sus connections es el indicado)
        for (Connection c : connections) {
            if (c.powerKW == potencia) {
=======
    public boolean comprobarDiponibilidad(){
        List<String> lista = new ArrayList<>();
        for (Connection c:connections){
            if (c.statusType.isOperational == true) {
>>>>>>> 70a850fc3613dfb8e16baed5896dff1075f07477
                return true;
            }
        }
        return false;
    }

    public Charger() {
        this.operator = new Operator();
        this.address = new Address();
    }

}
