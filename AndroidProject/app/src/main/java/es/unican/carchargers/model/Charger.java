package es.unican.carchargers.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    // Devuelve true si alguno de los cargadores en este punto tiene la potencia indicada.
    public boolean contienePotencia(double potencia) {

        //Comprobar si alguno de sus valores Power (dentro de sus connections es el indicado)
        for (Connection c : connections) {
            if (c.powerKW == potencia) {
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

    @Override
    public int hashCode() {
        return Objects.hash(id, numberOfPoints, usageCost, operator, address, connections);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Charger charger = (Charger) o;
        return Objects.equals(id, charger.id);
    }
}
