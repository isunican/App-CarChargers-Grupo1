package es.unican.carchargers.activities.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    String criterioOrd;
    boolean asc;


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

        sut.onAceptarFiltroPotenciaClicked(potencias);
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

        sut.onAceptarFiltroPotenciaClicked(potencias);
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

        sut.onAceptarFiltroPotenciaClicked(potencias);
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

        sut.onAceptarFiltroPotenciaClicked(potencias);
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

        sut.onAceptarFiltroPotenciaClicked(potencias);
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

        sut.onAceptarFiltroPotenciaClicked(potencias);
        verify(mv,atLeast(1)).showChargers(captor.capture());
        captados = captor.getValue();
        assertTrue(captados.get(0).equals(a));
        assertTrue(captados.get(1).equals(a2));

        // Verifica que el resultado sea el esperado
        assertEquals(captados.size(), 2);
    }

    //TEST: OnClickedAceptarOrdenacion

    //CASO 1:
    @Test
    public void OnClickedAceptarOrdenacionCorrectaAscendenteTest() {
        Charger c1 = new Charger();
        Charger c2 = new Charger();
        Charger c3 = new Charger();
        Charger c4 = new Charger();

        criterioOrd = "Precio";
        asc = true;

        chargers.add(c1);
        chargers.add(c2);
        chargers.add(c3);
        chargers.add(c4);

        c1.usageCost = "0,35€/kWh";
        c2.usageCost = "0,43€/kWh";
        c3.usageCost = "0,30€/kWh";
        c4.usageCost = null;

        when(mv.getRepository()).thenReturn(repository);

        sut.init(mv);

        sut.onClickedAceptarOrdenacion(criterioOrd, asc);
        verify(mv,atLeast(1)).showChargers(captor.capture());
        captados = captor.getValue();

        //Comprobacion de los resultados esperados
        assertEquals(captados.get(0),c3);
        assertEquals(captados.get(1),c1);
        assertEquals(captados.get(2),c2);
        assertEquals(3, captados.size());

    }

    //CASO 2:
    @Test
    public void OnClickedAceptarOrdenacionCorrectaDescendenteTest() {
        Charger c1 = new Charger();
        Charger c2 = new Charger();
        Charger c3 = new Charger();
        Charger c4 = new Charger();

        chargers.add(c1);
        chargers.add(c2);
        chargers.add(c3);
        chargers.add(c4);

        c1.usageCost = "0,35€/kWh";
        c2.usageCost = "0,43€/kWh";
        c3.usageCost = "0,30€/kWh";
        c4.usageCost = null;

        criterioOrd = "Precio";
        asc = false;

        when(mv.getRepository()).thenReturn(repository);

        sut.init(mv);

        sut.onClickedAceptarOrdenacion(criterioOrd, asc);
        verify(mv,atLeast(1)).showChargers(captor.capture());
        captados = captor.getValue();

        //Comprobacion de los resultados esperados
        assertEquals(captados.get(0),c2);
        assertEquals(captados.get(1),c1);
        assertEquals(captados.get(2),c3);
        assertEquals(3, captados.size());

    }


    //CASO 3:
    @Test
    public void OnClickedAceptarOrdenacionAscSinPreciosTest() {
        Charger c1 = new Charger();
        Charger c2 = new Charger();
        c1.usageCost = null;
        c2.usageCost = null;

        chargers.add(c1);
        chargers.add(c2);

        criterioOrd = "Precio";
        asc = true;

        when(mv.getRepository()).thenReturn(repository);

        sut.init(mv);

        sut.onClickedAceptarOrdenacion(criterioOrd, asc);
        verify(mv,atLeast(1)).showChargers(captor.capture());
        captados = captor.getValue();

        //Comprobacion de los resultados esperados
        assertEquals(0, captados.size());

    }

    //CASO 4:
    @Test
    public void OnClickedAceptarOrdenacionDesSinPreciosTest() {
        Charger c1 = new Charger();
        Charger c2 = new Charger();
        c1.usageCost = null;
        c2.usageCost = null;

        chargers.add(c1);
        chargers.add(c2);

        criterioOrd = "Precio";
        asc = false;

        when(mv.getRepository()).thenReturn(repository);

        sut.init(mv);

        sut.onClickedAceptarOrdenacion(criterioOrd, asc);
        verify(mv,atLeast(1)).showChargers(captor.capture());
        captados = captor.getValue();

        //Comprobacion de los resultados esperados
        assertEquals(0, captados.size());

    }

    //CASO 5:
    @Test
    public void OnClickedAceptarOrdenacionCriterioInexistenteTest() {
        Charger c1 = new Charger();
        Charger c2 = new Charger();
        c1.usageCost = "0,76€/kWh";
        c2.usageCost = null;

        chargers.add(c1);
        chargers.add(c2);

        criterioOrd = "hola";
        asc = true;

        when(mv.getRepository()).thenReturn(repository);

        sut.init(mv);

        sut.onClickedAceptarOrdenacion(criterioOrd, asc);

        //Comprobar si salta el mensaje de error
        assertTrue("Esta ordenación no existe. Contacte con soporte para ver que ha ocurrido.", true);

    }


}