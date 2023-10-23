package es.unican.carchargers.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
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
    @SerializedName("ConnectionTypeID")     public int connectionTypeID;
    @SerializedName("ConnectionType")       public ConnectionType connectionType;
    @SerializedName("PowerKW")              public double powerKW;
    @SerializedName("Title")                public String title;
    @SerializedName("StatusType")           public StatusType statusType;

    public Connection() {
        this.connectionType = new ConnectionType();
    }

}
