package es.unican.carchargers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import es.unican.carchargers.activities.main.IMainContract;
import es.unican.carchargers.activities.main.MainPresenter;
import es.unican.carchargers.activities.main.MainView;
import es.unican.carchargers.model.Charger;
import es.unican.carchargers.model.Connection;
import es.unican.carchargers.repository.IRepository;
import es.unican.carchargers.repository.Repositories;

@RunWith(MockitoJUnitRunner.class)
public class MainPresenterTest {

    @Mock
    IMainContract.View mv = Mockito.mock(MainView.class);

    @Test
    public void filtrarPorPotTest() {
        List<Charger> chargers = new ArrayList<>();
        IRepository repository = Repositories.getSyncFake(chargers);

        // Creamos main presenter
        IMainContract.Presenter sut = new MainPresenter();

        // Caso con varios cargadores y varias potencias
        List<Double> potencias = new ArrayList<>();
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

        sut.filtraPorPot(potencias);

        // Verifica que el resultado sea el esperado
        assertEquals(chargers.size(), 3);
    }
}