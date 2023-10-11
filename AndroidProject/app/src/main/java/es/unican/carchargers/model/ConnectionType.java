package es.unican.carchargers.model;

import com.google.gson.annotations.SerializedName;

public class ConnectionType {

    @SerializedName("FormalName")           public String FormalName;
    @SerializedName("ID")                   public int id;
    @SerializedName("IsDiscontinued")       public boolean IsDiscontinued;
    @SerializedName("IsObsolete")           public boolean IsObsolete;
    @SerializedName("Title")                public String Title;


}
