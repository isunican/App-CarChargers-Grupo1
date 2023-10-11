package es.unican.carchargers.model;

import com.google.gson.annotations.SerializedName;

public class Connection {
    /* LOS ELEMENTOS DE LA CLASE COMPLETA SON:
        ID integer
        ConnectionTypeID int
        ConnectionType
    Reference string
    StatusTypeID integer
    StatusType object
    LevelID integer
    Level object
    Amps integer
    Voltage number
        PowerKW number (double)
    CurrentTypeID integer
    CurrentType object
    Quantity integer
    Comments string
     */

    @SerializedName("ID")                   public int id;
    @SerializedName("ConnectionTypeID")     public int ConnectionTypeID;
    @SerializedName("ConnectionType")       public ConnectionType ConnectionType;
    @SerializedName("PowerKW")              public double PowerKW;
    @SerializedName("Title")                public String Title;

    public Connection() {
        this.ConnectionType = new ConnectionType();
    }

}
