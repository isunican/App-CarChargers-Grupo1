package es.unican.carchargers.constants;

import es.unican.carchargers.R;

public enum EConnectionType {

    CCS_Type_1(32, R.drawable.type1, "CCS (Type 1)"),
    CCS_Type_2(33, R.drawable.type2,"CCS (Type 2)"),
    CHAdeMO(2, R.drawable.chademo, "CHAdeMO"),
    CEE_74_Schuko_Type_F(28, R.drawable.schuko,"CEE 7/4 - Schuko - Type F"),
    Type_1_J1772(1, R.drawable.type1j1772, "Type 1 (J1772)"),
    Type_2_Socket_Only(25 , R.drawable.type2socket,"Type 2 (Socket Only)"),
    Type_2_Tethered_Connector(1036, R.drawable.type2tethered,"Type 2 (Tethered Connector) "),

    GENERIC(-1, R.drawable.generic, "Sin especificar");

    private EConnectionType(int id, int logo, String nombre) {
        this.id = id;
        this.logo = logo;
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public int getId() {
        return id;
    }

    public static EConnectionType fromId(int id) {
        for (EConnectionType connectionType : EConnectionType.values()) {
            if (id == connectionType.id) {
                return connectionType;
            }
        }
        return GENERIC;
    }

    public static String[] obtenerNombres() {
        EConnectionType[] valores = EConnectionType.values();
        String[] nombres = new String[valores.length - 1];

        // No retorna el genérico.
        for (int i = 0; i < valores.length - 1; i++) {
            nombres[i] = valores[i].getNombre();
        }

        return nombres;
    }

    public static EConnectionType obtenerConnectionTypePorPos(int pos) {
        EConnectionType[] valores = EConnectionType.values();

        return valores[pos];
    }

    /** logo resource id */
    public final int logo;

    public final int id;

    public final String nombre;

}
