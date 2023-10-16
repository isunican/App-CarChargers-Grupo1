package es.unican.carchargers.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class StatusType {

    @SerializedName("IsOperational")      public boolean isOperational;

    public StatusType() {
    }
}
