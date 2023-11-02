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
    public void filtrarPorPotTest() {
        // caso con varios cargadores y una potencia -> caso A
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

        // inicializamos de nuevo
        chargers = new ArrayList<>();
        repository = Repositories.getSyncFake(chargers);
        potencias = new ArrayList<>();
        captados = new ArrayList<>();

        // Caso con varios cargadores y varias potencias -> Caso B
        potencias.add(7.4);
        potencias.add(43.0);

        c = new Connection();
        c.powerKW = 7.4;
        c.id = 1;
        c2 = new Connection();
        c2.powerKW = 7.4;
        c2.id = 2;
        c3 = new Connection();
        c3.powerKW = 43;
        c3.id = 3;

        a = new Charger();
        a.connections.add(c);
        a2 = new Charger();
        a2.connections.add(c2);
        a3 = new Charger();
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

        // inicializamos de nuevo
        chargers = new ArrayList<>();
        repository = Repositories.getSyncFake(chargers);
        potencias = new ArrayList<>();
        captados = new ArrayList<>();

        // Caso de ningun cargador con alguna potencia concreta -> Caso C
        potencias.add(7.4);
        potencias.add(43.0);

        c = new Connection();
        c.powerKW = 50;
        c.id = 1;
        c2 = new Connection();
        c2.powerKW = 50;
        c2.id = 2;
        c3 = new Connection();
        c3.powerKW = 50;
        c3.id = 3;

        a = new Charger();
        a.connections.add(c);
        a2 = new Charger();
        a2.connections.add(c2);
        a3 = new Charger();
        a3.connections.add(c3);

        chargers.add(a);
        chargers.add(a2);
        chargers.add(a3);

        when(mv.getRepository()).thenReturn(repository);

        sut.init(mv);

        sut.onAceptarFiltroPotenciaClicked(potencias);
        verify(mv,atLeast(1)).showLoadSinCargadores("No se han encontrado cargadores que se ajusten a tu busqueda");

        // inicializamos de nuevo
        chargers = new ArrayList<>();
        repository = Repositories.getSyncFake(chargers);
        potencias = new ArrayList<>();
        captados = new ArrayList<>();

        //Caso con 1 cargador y varias potencias -> Caso D
        potencias.add(7.4);
        potencias.add(43.0);

        c = new Connection();
        c.powerKW = 7.4;
        c.id = 1;

        a = new Charger();
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

        // inicializamos de nuevo
        chargers = new ArrayList<>();
        repository = Repositories.getSyncFake(chargers);
        potencias = new ArrayList<>();
        captados = new ArrayList<>();

        //Caso con 1 cargador y varias potencias -> Caso E
        potencias.add(7.4);
        potencias.add(43.0);

        c = new Connection();
        c.powerKW = 50;
        c.id = 1;
        c2 = new Connection();
        c2.powerKW = 7.4;
        c2.id = 2;
        c3 = new Connection();
        c2.powerKW = 43.0;
        c2.id = 3;

        a = new Charger();
        a.connections.add(c);
        a.connections.add(c2);
        a.connections.add(c3);

        a2 = new Charger();
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

        // inicializamos de nuevo
        chargers = new ArrayList<>();
        repository = Repositories.getSyncFake(chargers);
        potencias = new ArrayList<>();
        captados = new ArrayList<>();

        //Caso sin potencias -> Caso F
        c = new Connection();
        c.powerKW = 50;
        c.id = 1;
        c2 = new Connection();
        c2.powerKW = 7.4;
        c2.id = 2;
        c3 = new Connection();
        c2.powerKW = 43.0;
        c2.id = 3;

        a = new Charger();
        a.connections.add(c);
        a.connections.add(c2);
        a.connections.add(c3);

        a2 = new Charger();
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

        // inicializamos de nuevo
        chargers = new ArrayList<>();
        repository = Repositories.getSyncFake(chargers);
        potencias = new ArrayList<>();
        captados = new ArrayList<>();

        //Caso no existe cargadores -> Caso G
        potencias.add(7.4);
        potencias.add(43.0);

        when(mv.getRepository()).thenReturn(repository);

        sut.init(mv);

        sut.onAceptarFiltroPotenciaClicked(potencias);
        verify(mv,atLeast(1)).showLoadSinCargadores("No se han encontrado cargadores que se ajusten a tu busqueda");
    }
}