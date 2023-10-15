package es.unican.carchargers.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class ConnectionType {

    @SerializedName("FormalName")           public String formalName;
    @SerializedName("ID")                   public int id;
    @SerializedName("IsDiscontinued")       public boolean isDiscontinued;
    @SerializedName("IsObsolete")           public boolean isObsolete;
    @SerializedName("Title")                public String title;


}
