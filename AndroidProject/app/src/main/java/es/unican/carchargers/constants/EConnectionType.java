package es.unican.carchargers.constants;

import es.unican.carchargers.R;

public enum EConnectionType {

    CCS_TYPE_1(32, R.drawable.type1, "CCS (Type 1)"),
    CCS_TYPE_2(33, R.drawable.type2,"CCS (Type 2)"),
    CHADEMO(2, R.drawable.chademo, "CHAdeMO"),
    CEE_74_SCHUKO_TYPE_F(28, R.drawable.schuko,"CEE 7/4 - Schuko - Type F"),
    TYPE_1_J1772(1, R.drawable.type1j1772, "Type 1 (J1772)"),
    TYPE_2_SOCKET_ONLY(25 , R.drawable.type2socket,"Type 2 (Socket Only)"),
    TYPE_2_TETHERED_CONNECTOR(1036, R.drawable.type2tethered,"Type 2 (Tethered Connector) "),

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
