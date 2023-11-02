package es.unican.carchargers.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import es.unican.carchargers.activities.main.IMainContract;
import es.unican.carchargers.constants.EConnectionType;
import es.unican.carchargers.model.Charger;
import es.unican.carchargers.model.Connection;
import es.unican.carchargers.repository.IRepository;
import es.unican.carchargers.repository.Repositories;


public class ChargerTest {

    //Comprueba el correcto funcionamiento de el metodo comprobarDisponibilidad()
    @Test
    public void comprobarDisponibilidadTest() {
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

    @Test
    public void contieneConectorTest(){
        Charger sut = new Charger();

        //caso 1: Tiene varios conectores y uno de ese tipo
        Connection c1 = new Connection();
        Connection c2 = new Connection();
        c1.connectionType.id = EConnectionType.CCS_TYPE_1.getId();
        c2.connectionType.id = EConnectionType.CCS_TYPE_2.getId();
        sut.connections.add(c1);
        sut.connections.add(c2);
        boolean result = sut.contieneConector(EConnectionType.CCS_TYPE_1);
        assertTrue(result);

        //caso 2: No tiene un conector de ese tipo
        result = sut.contieneConector(EConnectionType.TYPE_1_J1772);
        assertFalse(result);

        //caso 3: El punto de carga no tiene conectores
        sut.connections.clear();
        result = sut.contieneConector(EConnectionType.TYPE_1_J1772);
        assertFalse(result);

        //caso 4: Tiene varios conectores iguales y ambos coinciden con el buscado
        sut.connections.add(c1);
        c2.connectionType.id = EConnectionType.CCS_TYPE_1.getId();
        sut.connections.add(c2);
        result = sut.contieneConector(EConnectionType.CCS_TYPE_1);
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