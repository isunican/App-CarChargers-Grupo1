package es.unican.carchargers.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

import es.unican.carchargers.constants.EConnectionType;
import es.unican.carchargers.model.Charger;
import es.unican.carchargers.model.Connection;

public class ChargerTest {
    @Test
    public void contieneConectorTest(){
        Charger sut = new Charger();

        //caso 1: Tiene varios conectores y uno de ese tipo
        Connection c1 = new Connection();
        Connection c2 = new Connection();
        c1.connectionType.id = EConnectionType.CCS_Type_1.getId();
        c2.connectionType.id = EConnectionType.CCS_Type_2.getId();
        sut.connections.add(c1);
        sut.connections.add(c2);
        boolean result = sut.contieneConector(EConnectionType.CCS_Type_1);
        assertTrue(result);

        //caso 2: No tiene un conector de ese tipo
        result = sut.contieneConector(EConnectionType.Type_1_J1772);
        assertFalse(result);

        //caso 3: El punto de carga no tiene conectores
        sut.connections.clear();
        result = sut.contieneConector(EConnectionType.Type_1_J1772);
        assertFalse(result);

        //caso 4: Tiene varios conectores iguales y ambos coinciden con el buscado
        sut.connections.add(c1);
        c2.connectionType.id = EConnectionType.CCS_Type_1.getId();
        sut.connections.add(c2);
        result = sut.contieneConector(EConnectionType.CCS_Type_1);
        assertTrue(result);
    }

    @Test
    public void listarTiposConectorTest() {
        // caso sin conectores
        Charger sut = new Charger();
        List<String> res;
        res = sut.listarTiposConector();
        assertEquals(res.size(), 0);

        // caso con un conector
        Connection c = new Connection();
        c.connectionType.title = "Manoli";
        sut.connections.add(c);
        res = sut.listarTiposConector();
        assertEquals(res.size(), 1);
        assertEquals(res.get(0), c.connectionType.title);

        // caso con varios conectores
        Connection c2 = new Connection();
        Connection c3 = new Connection();
        c2.connectionType.title = "Pepe";
        c3.connectionType.title = "Feli";
        sut.connections.add(c2);
        sut.connections.add(c3);
        res = sut.listarTiposConector();
        assertEquals(res.size(), 3);
        assertEquals(res.get(0), c.connectionType.title);
        assertEquals(res.get(1), c2.connectionType.title);
        assertEquals(res.get(2), c3.connectionType.title);

    }
}