package es.unican.carchargers.activities.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static es.unican.carchargers.constants.EConnectionType.CCS_Type_1;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import es.unican.carchargers.activities.main.IMainContract;
import es.unican.carchargers.activities.main.MainPresenter;
import es.unican.carchargers.constants.EConnectionType;
import es.unican.carchargers.model.Charger;
import es.unican.carchargers.model.Connection;
import es.unican.carchargers.repository.IRepository;
import es.unican.carchargers.repository.Repositories;

@RunWith(MockitoJUnitRunner.class)
public class MainPresenterTest {

    @Mock
    IMainContract.View mv;
    ArgumentCaptor<List<Charger>> captor;
    List<Charger> chargers;
    IRepository repository;
    IMainContract.Presenter sut;
    List<Double> potencias;
    List<Charger> captados;

    List<EConnectionType> conectores;

    @Before
    public void setup(){
        MockitoAnnotations.openMocks(this);
        captor = ArgumentCaptor.forClass(List.class);
        chargers = new ArrayList<>();
        repository = Repositories.getSyncFake(chargers);
        sut = new MainPresenter();
        potencias = new ArrayList<>();
        captados = new ArrayList<>();
    }
    @Test
    public void filtrarPorConectorTest(){
        //filtrare por CCS_Type_1
        conectores.add(CCS_Type_1);
        //creo los conectores
        Connection c1 = new Connection();
        c1.connectionType.id = EConnectionType.CCS_Type_1.getId();
        //c1.id = 1;
        Connection c2 = new Connection();
        c1.connectionType.id = EConnectionType.CCS_Type_2.getId();
        //c1.id = 2;
        Connection c3 = new Connection();
        c1.connectionType.id = EConnectionType.CCS_Type_1.getId();
        //c1.id = 3;
        //creo los puntos de carga
        Charger a = new Charger();
        a.connections.add(c1);
        Charger b = new Charger();
        b.connections.add(c2);
        Charger c = new Charger();
        c.connections.add(c3);
        //añado los cargadores
        chargers.add(a);
        chargers.add(b);
        chargers.add(c);

        when(mv.getRepository()).thenReturn(repository);
        sut.init(mv);
    }

    @Test
    public void filtrarPorPotTestCasoA() {
        // caso con varios cargadores y una potencia
        potencias.add(7.4);

        Connection c = new Connection();
        c.powerKW = 7.4;
        c.id = 1;
        Connection c2 = new Connection();
        c2.powerKW = 43;
        c2.id = 2;
        Connection c3 = new Connection();
        c3.powerKW = 7.4;
        c3.id = 3;

        Charger a = new Charger();
        a.connections.add(c);
        Charger a2 = new Charger();
        a2.connections.add(c2);
        Charger a3 = new Charger();
        a3.connections.add(c3);

        chargers.add(a);
        chargers.add(a2);
        chargers.add(a3);

        when(mv.getRepository()).thenReturn(repository);

        sut.init(mv);

        sut.filtrarOriginalesPorPotencia();
        verify(mv,atLeast(1)).showChargers(captor.capture());
        captados = captor.getValue();
        assertTrue(captados.get(0).equals(a));
        assertTrue(captados.get(1).equals(a3));

        // Verifica que el resultado sea el esperado
        assertEquals(captados.size(), 2);
    }

    @Test
    public void filtrarPorPotTestCasoB() {
        // Caso con varios cargadores y varias potencias
        potencias.add(7.4);
        potencias.add(43.0);

        Connection c = new Connection();
        c.powerKW = 7.4;
        c.id = 1;
        Connection c2 = new Connection();
        c2.powerKW = 7.4;
        c2.id = 2;
        Connection c3 = new Connection();
        c3.powerKW = 43;
        c3.id = 3;

        Charger a = new Charger();
        a.connections.add(c);
        Charger a2 = new Charger();
        a2.connections.add(c2);
        Charger a3 = new Charger();
        a3.connections.add(c3);

        chargers.add(a);
        chargers.add(a2);
        chargers.add(a3);

        when(mv.getRepository()).thenReturn(repository);

        sut.init(mv);

        sut.filtrarOriginalesPorPotencia();
        verify(mv,atLeast(1)).showChargers(captor.capture());
        captados = captor.getValue();
        assertTrue(captados.get(0).equals(a));
        assertTrue(captados.get(1).equals(a2));
        assertTrue(captados.get(2).equals(a3));

        // Verifica que el resultado sea el esperado
        assertEquals(captados.size(), 3);
    }

    @Test
    public void filtrarPorPotTestCasoC() {
        // Caso de ningun cargador con alguna potencia concreta
        potencias.add(7.4);
        potencias.add(43.0);

        Connection c = new Connection();
        c.powerKW = 50;
        c.id = 1;
        Connection c2 = new Connection();
        c2.powerKW = 50;
        c2.id = 2;
        Connection c3 = new Connection();
        c3.powerKW = 50;
        c3.id = 3;

        Charger a = new Charger();
        a.connections.add(c);
        Charger a2 = new Charger();
        a2.connections.add(c2);
        Charger a3 = new Charger();
        a3.connections.add(c3);

        chargers.add(a);
        chargers.add(a2);
        chargers.add(a3);

        when(mv.getRepository()).thenReturn(repository);

        sut.init(mv);

        sut.filtrarOriginalesPorPotencia();
        verify(mv,atLeast(1)).showChargers(captor.capture());
        captados = captor.getValue();

        // Verifica que el resultado sea el esperado
        assertEquals(captados.size(), 0);
    }

    @Test
    public void filtrarPorPotTestCasoD() {
        //Caso con 1 cargador y varias potencias
        potencias.add(7.4);
        potencias.add(43.0);

        Connection c = new Connection();
        c.powerKW = 7.4;
        c.id = 1;

        Charger a = new Charger();
        a.connections.add(c);

        chargers.add(a);

        when(mv.getRepository()).thenReturn(repository);

        sut.init(mv);

        sut.filtrarOriginalesPorPotencia();
        verify(mv,atLeast(1)).showChargers(captor.capture());
        captados = captor.getValue();
        assertTrue(captados.get(0).equals(a));

        // Verifica que el resultado sea el esperado
        assertEquals(captados.size(), 1);
    }
    @Test
    public void filtrarPorPotTestCasoE() {
        //Caso con 1 cargador y varias potencias
        potencias.add(7.4);
        potencias.add(43.0);

        Connection c = new Connection();
        c.powerKW = 50;
        c.id = 1;
        Connection c2 = new Connection();
        c2.powerKW = 7.4;
        c2.id = 2;
        Connection c3 = new Connection();
        c2.powerKW = 43.0;
        c2.id = 3;

        Charger a = new Charger();
        a.connections.add(c);
        a.connections.add(c2);
        a.connections.add(c3);

        Charger a2 = new Charger();
        a2.connections.add(c);

        chargers.add(a);
        chargers.add(a2);

        when(mv.getRepository()).thenReturn(repository);

        sut.init(mv);

        sut.filtrarOriginalesPorPotencia();
        verify(mv,atLeast(1)).showChargers(captor.capture());
        captados = captor.getValue();
        assertTrue(captados.get(0).equals(a));

        // Verifica que el resultado sea el esperado
        assertEquals(captados.size(), 1);
    }

    @Test
    public void filtrarPorPotTestCasoF() {
        //Caso sin potencias

        Connection c = new Connection();
        c.powerKW = 50;
        c.id = 1;
        Connection c2 = new Connection();
        c2.powerKW = 7.4;
        c2.id = 2;
        Connection c3 = new Connection();
        c2.powerKW = 43.0;
        c2.id = 3;

        Charger a = new Charger();
        a.connections.add(c);
        a.connections.add(c2);
        a.connections.add(c3);

        Charger a2 = new Charger();
        a2.connections.add(c);

        chargers.add(a);
        chargers.add(a2);

        when(mv.getRepository()).thenReturn(repository);

        sut.init(mv);

        sut.filtrarOriginalesPorPotencia();
        verify(mv,atLeast(1)).showChargers(captor.capture());
        captados = captor.getValue();
        assertTrue(captados.get(0).equals(a));
        assertTrue(captados.get(1).equals(a2));

        // Verifica que el resultado sea el esperado
        assertEquals(captados.size(), 2);
    }
}