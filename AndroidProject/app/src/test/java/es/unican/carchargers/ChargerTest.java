package es.unican.carchargers;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import es.unican.carchargers.activities.main.IMainContract;
import es.unican.carchargers.model.Charger;
import es.unican.carchargers.model.Connection;
import es.unican.carchargers.repository.IRepository;
import es.unican.carchargers.repository.Repositories;

public class ChargerTest {

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

    @Mock
    IMainContract.View mv;
    IRepository repository;
    @Test
    public void comprobarDiponibilidadTest() {

        Charger sut = new Charger();
        List<Charger> cargadores = new ArrayList<Charger>();
        cargadores.add(sut);
        boolean r;
        Connection c1 = new Connection();
        Connection c2 = new Connection();
        Connection c3 = new Connection();

        sut.connections.add(c1);
        sut.connections.add(c2);
        sut.connections.add(c3);;

        repository = Repositories.getSyncFake(cargadores);

        //Al menos un conector esta disponible
        c1.statusType.isOperational = false;
        c2.statusType.isOperational = true;
        c3.statusType.isOperational = false;
        r = sut.comprobarDisponibilidad();
        assertEquals(r, true);

        //Todos los conectores estan disponibles
        c1.statusType.isOperational = true;
        c2.statusType.isOperational = true;
        c3.statusType.isOperational = true;
        r = sut.comprobarDisponibilidad();
        assertEquals(r, true);

        //Ningun conector esta disponible
        c1.statusType.isOperational = false;
        c2.statusType.isOperational = false;
        c3.statusType.isOperational = false;
        r = sut.comprobarDisponibilidad();
        assertEquals(r, false);


        //Todos los conectores tienen disponibilidad false o se desconoce la disponibilidad
        c1.statusType = null;
        c2.statusType = null;
        c3.statusType.isOperational = false;
        r = sut.comprobarDisponibilidad();
        assertEquals(r, false);


        //No se conoce la disponibilidad de ingun conector
        c1.statusType = null;
        c2.statusType = null;
        c3.statusType = null;
        r = sut.comprobarDisponibilidad();
        assertEquals(r, false);
    }
}