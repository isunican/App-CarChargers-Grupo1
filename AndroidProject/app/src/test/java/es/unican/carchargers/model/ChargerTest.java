package es.unican.carchargers.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import es.unican.carchargers.model.Charger;
import es.unican.carchargers.model.Connection;

public class ChargerTest {

    @Test
    public void listarTiposConectorTest() {
        // caso sin conectores
        Charger sut = new Charger();
        List<String> res;
        res = sut.listarTiposConector();
        assertEquals(res.size(), 0);

        // caso con un conector
        Connection c1 = new Connection();
        c1.connectionType.title = "Manoli";
        sut.connections.add(c1);
        res = sut.listarTiposConector();
        assertEquals(res.size(), 1);
        assertEquals(res.get(0), c1.connectionType.title);

        // caso con varios conectores
        Connection c2 = new Connection();
        Connection c3 = new Connection();
        c2.connectionType.title = "Pepe";
        c3.connectionType.title = "Feli";
        sut.connections.add(c2);
        sut.connections.add(c3);
        res = sut.listarTiposConector();
        assertEquals(res.size(), 3);
        assertEquals(res.get(0), c1.connectionType.title);
        assertEquals(res.get(1), c2.connectionType.title);
        assertEquals(res.get(2), c3.connectionType.title);

        // caso con varios conectores y mismo titulo
        sut.connections = new ArrayList<>();
        c1 = new Connection();
        c2 = new Connection();
        c3 = new Connection();
        c1.connectionType.title = "Pepe";
        c2.connectionType.title = "Pepe";
        c3.connectionType.title = "Feli";
        sut.connections.add(c1);
        sut.connections.add(c2);
        sut.connections.add(c3);
        res = sut.listarTiposConector();
        assertEquals(res.size(), 3);
        assertEquals(res.get(0), c1.connectionType.title);
        assertEquals(res.get(1), c2.connectionType.title);
        assertEquals(res.get(2), c3.connectionType.title);

    }
}