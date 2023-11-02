package es.unican.carchargers.activities.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static es.unican.carchargers.constants.EConnectionType.*;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import es.unican.carchargers.constants.EConnectionType;
import es.unican.carchargers.model.Charger;
import es.unican.carchargers.model.Connection;
import es.unican.carchargers.repository.IRepository;
import es.unican.carchargers.repository.Repositories;

@RunWith(MockitoJUnitRunner.class)
public class MainPresenterTest {

    //Variables Perez
    @Mock
    IMainContract.View mv;
    ArgumentCaptor<List<Charger>> captor;
    List<Charger> chargers;
    IRepository repository;
    IMainContract.Presenter sut;
    List<Double> potencias;
    List<Charger> captados;

    //Variables Jesus
    @Mock
    IMainContract.View mainV;
    ArgumentCaptor<List<Charger>> listCaptor;
    List<Charger> listCharger;
    IRepository repo;
    List<Charger> capturados;
    List<EConnectionType> conectores;

    //Variables Samuel
    ArgumentCaptor<Charger> captorCharger;
    Charger c1, c2, c3, c4;
    @Mock
    IMainContract.View mView;
    List<Charger> listChargers;
    ArgumentCaptor<List<Charger>> captorCargadores;
    ArgumentCaptor<Integer> captorNumCargadores;

    @Before
    public void setup(){
        MockitoAnnotations.openMocks(this);
        sut = new MainPresenter();

        //Perez
        captor = ArgumentCaptor.forClass(List.class);
        chargers = new ArrayList<>();
        repository = Repositories.getSyncFake(chargers);
        potencias = new ArrayList<>();
        captados = new ArrayList<>();

        //Samuel
        captorCargadores = ArgumentCaptor.forClass(List.class);
        captorNumCargadores = ArgumentCaptor.forClass(Integer.class);
        captorCharger = ArgumentCaptor.forClass(Charger.class);
        c1 = new Charger();
        c1.operator.title = "Zunder";
        c2 = new Charger();
        c2.operator.title = "Repsol";
        c3 = new Charger();
        c3.operator.title = "Iberdrola";
        c4 = new Charger();
        c4.operator.title = "Particular";

        //Jesus
        listCaptor = ArgumentCaptor.forClass(List.class);
        listCharger = new ArrayList<>();
        repo = Repositories.getSyncFake(listCharger);
        conectores = new ArrayList<>();
        capturados = new ArrayList<>();
    }

    @Test
    public void filtrarPorConectorTest(){
        // CASO 1: Filtrado con varios puntos de carga y un tipo de conector.

        // Filtraré por CCS_Type_1
        conectores.add(CCS_TYPE_1);

        // Creo los conectores
        Connection c1 = new Connection();
        c1.connectionType.id = CCS_TYPE_1.getId();
        Connection c2 = new Connection();
        c2.connectionType.id = CCS_TYPE_2.getId();
        Connection c3 = new Connection();
        c3.connectionType.id = CCS_TYPE_1.getId();

        // Creo los puntos de carga
        Charger a = new Charger();
        a.connections.add(c1);
        Charger b = new Charger();
        b.connections.add(c2);
        Charger c = new Charger();
        c.connections.add(c3);

        // Añado los puntos de carga a la lista
        listCharger.add(a);
        listCharger.add(b);
        listCharger.add(c);

        // Configuro el comportamiento del mock
        when(mainV.getRepository()).thenReturn(repo);
        sut.init(mainV);

        // Llamo al metodo a probar y verifico que se ha llamado
        sut.onAceptarFiltroConectoresClicked(conectores);
        verify(mainV,atLeast(1)).showChargers(captor.capture());

        // Verifico que los elementos filtrados son los correctos
        capturados = captor.getValue();
        assertEquals(capturados.get(0), a);
        assertEquals(capturados.get(1), c);

        // Verifico la longitud de la lista
        assertEquals(capturados.size(), 2);

        // CASO 2: Filtrado con varios puntos de carga y dos tipos de conectores.
        conectores.add(CCS_TYPE_2);
        sut.onAceptarFiltroConectoresClicked(conectores);
        verify(mainV,atLeast(2)).showChargers(captor.capture());
        capturados = captor.getValue();
        assertEquals(capturados.get(0), a);
        assertEquals(capturados.get(1), b);
        assertEquals(capturados.get(2), c);
        assertEquals(capturados.size(), 3);

        // CASO 3: Filtrado en que no existen puntos de carga con ese conector.
        conectores.clear();
        capturados.clear();
        conectores.add(CEE_74_SCHUKO_TYPE_F);
        listCharger.remove(c);
        sut.onAceptarFiltroConectoresClicked(conectores);
        //Compruebo la salida
        verify(mainV,atLeast(1)).showLoadSinCargadores("No hay cargadores para esta selección. " +
                "Al cerrar este mensaje se volverá a la selección anterior.");

        capturados = captor.getValue();
        assertEquals(capturados.size(), 0);

        //CASO 4: Filtrado con un punto de carga y varios tipos de conector.
        conectores.clear();
        conectores.add(CCS_TYPE_1);
        conectores.add(CEE_74_SCHUKO_TYPE_F);
        sut.onAceptarFiltroConectoresClicked(conectores);
        verify(mainV,atLeast(4)).showChargers(captor.capture());
        capturados = captor.getValue();
        assertEquals(capturados.get(0), a);
        assertEquals(capturados.size(), 1);

        //CASO 5: Filtrado con un punto de carga con dos conectores en el que solo coincide uno de ellos.
        listCharger.remove(b);
        a.connections.add(c2);
        sut.onAceptarFiltroConectoresClicked(conectores);
        verify(mainV,atLeast(5)).showChargers(captor.capture());
        capturados = captor.getValue();
        assertEquals(capturados.get(0), a);
        assertEquals(capturados.size(), 1);

        //CASO 6: Filtrado con un punto de carga con dos conectores en el que coinciden los dos tipos.
        conectores.clear();
        conectores.add(CCS_TYPE_1);
        conectores.add(CCS_TYPE_2);
        sut.onAceptarFiltroConectoresClicked(conectores);
        verify(mainV,atLeast(6)).showChargers(captor.capture());
        capturados = captor.getValue();
        assertEquals(capturados.get(0), a);
        assertEquals(capturados.size(), 1);

        //CASO 7: Filtrado sin seleccionar el tipo de conector.
        a.connections.clear();
        b.connections.clear();
        c.connections.clear();
        a.connections.add(c1);
        b.connections.add(c2);
        c.connections.add(c3);
        listCharger.clear();
        listCharger.add(a);
        listCharger.add(b);
        listCharger.add(c);
        conectores.clear();
        sut.onAceptarFiltroConectoresClicked(conectores);
        verify(mainV,atLeast(7)).showChargers(captor.capture());
        capturados = captor.getValue();
        assertEquals(capturados.get(0), a);
        assertEquals(capturados.get(1), b);
        assertEquals(capturados.get(2), c);
        assertEquals(capturados.size(), 3);

        //CASO 8: Filtrado en el que la lista de cargadores esta vacía
        listCharger.clear();
        capturados.clear();
        conectores.add(CCS_TYPE_1);
        sut.onAceptarFiltroConectoresClicked(conectores);
        verify(mainV,atLeast(2)).showLoadSinCargadores("No hay cargadores para esta selección. " +
                "Al cerrar este mensaje se volverá a la selección anterior.");
        capturados = captor.getValue();
        assertEquals(capturados.size(), 0);
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
        verify(mv,atLeast(1)).showLoadSinCargadores("No hay cargadores para esta selección. " +
                "Al cerrar este mensaje se volverá a la selección anterior.");

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






    @Test
    public void listaVaciaTest() {
        //Caso lista vacia
        listChargers = new ArrayList<Charger>();
        IRepository repositoryVacio = Repositories.getFake(listChargers);

        when(mView.getRepository()).thenReturn(repositoryVacio);
        sut.init(mView);
        verify(mView).showChargers(captorCargadores.capture());
        verify(mView).showLoadCorrect(captorNumCargadores.capture());
        assertTrue(captorCargadores.getValue().isEmpty());
        assertEquals(captorNumCargadores.getValue(), (Integer)0);
    }

    @Test
    public void listaUnEltoTest() {
        listChargers = new ArrayList<Charger>();
        listChargers.add(c1);
        IRepository repositoryUnElto = Repositories.getFake(listChargers);

        when(mView.getRepository()).thenReturn(repositoryUnElto);
        sut.init(mView);
        verify(mView).showChargers(captorCargadores.capture());
        verify(mView).showLoadCorrect(captorNumCargadores.capture());
        assertFalse(captorCargadores.getValue().isEmpty());
        assertEquals(captorNumCargadores.getValue(), (Integer)1);
    }

    @Test
    public void listaVariosElto() {
        listChargers = new ArrayList<Charger>();

        listChargers.add(c1);
        listChargers.add(c2);
        listChargers.add(c3);
        listChargers.add(c4);
        IRepository repositoryVariosEltos = Repositories.getFake(listChargers);

        when(mView.getRepository()).thenReturn(repositoryVariosEltos);
        sut.init(mView);
        verify(mView).showChargers(captorCargadores.capture());
        verify(mView).showLoadCorrect(captorNumCargadores.capture());
        assertFalse(captorCargadores.getValue().isEmpty());
        assertEquals(captorNumCargadores.getValue(), (Integer)4);
    }

    @Test
    public void testOnChargerClickedIndiceValidoTest() {
        List<Charger> chargers = new ArrayList<Charger>();
        chargers.add(c1);
        chargers.add(c2);

        int indiceValid = 1;
        IRepository repository = Repositories.getFake(chargers);
        when(mv.getRepository()).thenReturn(repository);

        sut.init(mv);
        sut.onChargerClicked(indiceValid);

        verify(mv).showChargerDetails(captorCharger.capture());
        assertEquals(c2.operator.title, captorCharger.getValue().operator.title);
    }

}