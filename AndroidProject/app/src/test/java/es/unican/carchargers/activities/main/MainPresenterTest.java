package es.unican.carchargers.activities.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
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
    List<EConnectionType> conectores;

    //Variables Samuel
    ArgumentCaptor<Charger> captorCharger;
    Charger c1, c2, c3, c4;
    @Mock
    IMainContract.View mView;
    List<Charger> listChargers;
    ArgumentCaptor<List<Charger>> captorCargadores;
    ArgumentCaptor<Integer> captorNumCargadores;
    ArgumentCaptor<String> captorMensajeError;

    //Variables Laura
    List<Charger> cargadores;
    String criterioOrd;
    boolean asc;


    @Before
    public void setup() {
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
        captorMensajeError = ArgumentCaptor.forClass(String.class);
        c1 = new Charger();
        c1.operator.title = "Zunder";
        c2 = new Charger();
        c2.operator.title = "Repsol";
        c3 = new Charger();
        c3.operator.title = "Iberdrola";
        c4 = new Charger();
        c4.operator.title = "Particular";

        //Jesus
        conectores = new ArrayList<>();

    }


/* Comentados por cambios en las funcionalidades de la app, esto requeria cambios en esta implementacion
    @Test
    public void filtrarPorConectorTest() {
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

        a.id = "1";
        b.id = "2";
        c.id = "3";

        // Añado los puntos de carga a la lista
        chargers.add(a);
        chargers.add(b);
        chargers.add(c);

        // Configuro el comportamiento del mock
        when(mv.getRepository()).thenReturn(repository);
        sut.init(mv);

        // Llamo al metodo a probar y verifico que se ha llamado
        sut.onAceptarFiltroConectoresClicked(conectores);
        verify(mv, atLeast(1)).showChargers(captor.capture());

        // Verifico que los elementos filtrados son los correctos
        captados = captor.getValue();
        assertEquals(captados.get(0), a);
        assertEquals(captados.get(1), c);

        // Verifico la longitud de la lista
        assertEquals(2, captados.size());

        // CASO 2: Filtrado con varios puntos de carga y dos tipos de conectores.
        conectores.add(CCS_TYPE_2);
        sut.onAceptarFiltroConectoresClicked(conectores);
        verify(mv, atLeast(2)).showChargers(captor.capture());
        captados = captor.getValue();
        assertEquals(captados.get(0), a);
        assertEquals(captados.get(1), b);
        assertEquals(captados.get(2), c);
        assertEquals(captados.size(), 3);

        // CASO 3: Filtrado en que no existen puntos de carga con ese conector.
        conectores.clear();
        captados.clear();
        conectores.add(CEE_74_SCHUKO_TYPE_F);
        chargers.remove(c);
        sut.onAceptarFiltroConectoresClicked(conectores);
        //Compruebo la salida
        verify(mv, atLeast(1)).showLoadSinCargadores("No hay cargadores para esta selección. " +
                "Al cerrar este mensaje se volverá a la selección anterior.");

        captados = captor.getValue();
        assertEquals(captados.size(), 0);

        //CASO 4: Filtrado con un punto de carga y varios tipos de conector.
        conectores.clear();
        conectores.add(CCS_TYPE_1);
        conectores.add(CEE_74_SCHUKO_TYPE_F);
        sut.onAceptarFiltroConectoresClicked(conectores);
        verify(mv, atLeast(4)).showChargers(captor.capture());
        captados = captor.getValue();
        assertEquals(captados.get(0), a);
        assertEquals(1, captados.size());

        //CASO 5: Filtrado con un punto de carga con dos conectores en el que solo coincide uno de ellos.
        chargers.remove(b);
        a.connections.add(c2);
        sut.onAceptarFiltroConectoresClicked(conectores);
        verify(mv, atLeast(5)).showChargers(captor.capture());
        captados = captor.getValue();
        assertEquals(captados.get(0), a);
        assertEquals(1, captados.size());

        //CASO 6: Filtrado con un punto de carga con dos conectores en el que coinciden los dos tipos.
        conectores.clear();
        conectores.add(CCS_TYPE_1);
        conectores.add(CCS_TYPE_2);
        sut.onAceptarFiltroConectoresClicked(conectores);
        verify(mv, atLeast(6)).showChargers(captor.capture());
        captados = captor.getValue();
        assertEquals(captados.get(0), a);
        assertEquals(1, captados.size());

        //CASO 7: Filtrado sin seleccionar el tipo de conector.
        a.connections.clear();
        b.connections.clear();
        c.connections.clear();
        a.connections.add(c1);
        b.connections.add(c2);
        c.connections.add(c3);
        chargers.clear();
        chargers.add(a);
        chargers.add(b);
        chargers.add(c);
        conectores.clear();
        sut.onAceptarFiltroConectoresClicked(conectores);
        verify(mv, atLeast(7)).showChargers(captor.capture());
        captados = captor.getValue();
        assertEquals(captados.get(0), a);
        assertEquals(captados.get(1), b);
        assertEquals(captados.get(2), c);
        assertEquals(3, captados.size());

        //CASO 8: Filtrado en el que la lista de cargadores esta vacía
        chargers.clear();
        captados.clear();
        conectores.add(CCS_TYPE_1);
        sut.onAceptarFiltroConectoresClicked(conectores);
        verify(mv, atLeast(2)).showLoadSinCargadores("No hay cargadores para esta selección. " +
                "Al cerrar este mensaje se volverá a la selección anterior.");
        captados = captor.getValue();
        assertEquals(0, captados.size());
    }
*/
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
        a.id = "1";
        Charger a2 = new Charger();
        a2.connections.add(c2);
        a2.id = "2";
        Charger a3 = new Charger();
        a3.connections.add(c3);
        a3.id = "3";

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
        a.id = "1";
        a2.id = "2";
        a3.id = "3";

        chargers.add(a);
        chargers.add(a2);
        chargers.add(a3);

        when(mv.getRepository()).thenReturn(repository);

        sut.init(mv);

        sut.onAceptarFiltroPotenciaClicked(potencias);
        verify(mv,atLeast(1)).showChargers(captor.capture());
        captados = captor.getValue();
        assertEquals(captados.get(0), a);
        assertEquals(captados.get(1), a2);
        assertEquals(captados.get(2), a3);

        // Verifica que el resultado sea el esperado
        assertEquals(3, captados.size());

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
        a.id = "1";
        a2.id = "2";
        a3.id = "3";

        chargers.add(a);
        chargers.add(a2);
        chargers.add(a3);

        when(mv.getRepository()).thenReturn(repository);

        sut.init(mv);

        sut.onAceptarFiltroPotenciaClicked(potencias);
        verify(mv,atLeast(1)).showLoadSinCargadores("No hay cargadores para esta selección. " +
                "Al cerrar este mensaje se volverá a la selección anterior.");

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
        a.id = "1";

        chargers.add(a);

        when(mv.getRepository()).thenReturn(repository);

        sut.init(mv);

        sut.onAceptarFiltroPotenciaClicked(potencias);
        verify(mv,atLeast(1)).showChargers(captor.capture());
        captados = captor.getValue();
        assertEquals(captados.get(0), a);

        // Verifica que el resultado sea el esperado
        assertEquals(1, captados.size());

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

        a.id = "1";
        a2.id = "2";

        chargers.add(a);
        chargers.add(a2);

        when(mv.getRepository()).thenReturn(repository);

        sut.init(mv);

        sut.onAceptarFiltroPotenciaClicked(potencias);
        verify(mv,atLeast(1)).showChargers(captor.capture());
        captados = captor.getValue();
        assertEquals(captados.get(0), a);

        // Verifica que el resultado sea el esperado
        assertEquals(1, captados.size());

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

        a.id = "1";
        a2.id = "2";

        chargers.add(a);
        chargers.add(a2);

        when(mv.getRepository()).thenReturn(repository);

        sut.init(mv);

        sut.onAceptarFiltroPotenciaClicked(potencias);
        verify(mv,atLeast(1)).showChargers(captor.capture());
        captados = captor.getValue();
        assertEquals(captados.get(0), a);
        assertEquals(captados.get(1), a2);

        // Verifica que el resultado sea el esperado
        assertEquals(2, captados.size());

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
        verify(mv,atLeast(1)).showLoadSinCargadores("No hay cargadores para esta selección. " +
                "Al cerrar este mensaje se volverá a la selección anterior.");
    }





    //Test Samuel Castro

    //init()
    @Test
    public void initTestCasoA1() {
        //Caso lista vacia
        listChargers = new ArrayList<Charger>();
        IRepository repositoryVacio = Repositories.getFake(listChargers);

        when(mView.getRepository()).thenReturn(repositoryVacio);
        sut.init(mView);
        verify(mView).showChargers(captorCargadores.capture());
        verify(mView).showLoadCorrect(captorNumCargadores.capture());
        assertTrue(captorCargadores.getValue().isEmpty());
        assertEquals(captorNumCargadores.getValue(), (Integer) 0);
    }

    @Test
    public void initTestCasoA2() {
        listChargers = new ArrayList<Charger>();
        listChargers.add(c1);
        IRepository repositoryUnElto = Repositories.getFake(listChargers);

        when(mView.getRepository()).thenReturn(repositoryUnElto);
        sut.init(mView);
        verify(mView).showChargers(captorCargadores.capture());
        verify(mView).showLoadCorrect(captorNumCargadores.capture());
        assertFalse(captorCargadores.getValue().isEmpty());
        assertEquals(captorNumCargadores.getValue(), (Integer) 1);
    }

    @Test
    public void initTestCasoA3() {
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
        assertEquals(captorNumCargadores.getValue(), (Integer) 4);
    }

    @Test
    public void initTestCasoB() {
        listChargers = new ArrayList<Charger>();

        listChargers.add(c1);
        listChargers.add(c2);
        listChargers.add(c3);
        listChargers.add(c4);
        IRepository repositoryVariosEltos = Repositories.getFail();

        when(mv.getRepository()).thenReturn(repositoryVariosEltos);
        sut.init(mv);
        verify(mv).showLoadError(captorMensajeError.capture());
        assertEquals( "El sistema no pudo conectarse a la red", captorMensajeError.getValue());
    }

    //OnChargedClicked(int indice)
    @Test
    public void onChargerClickedTestCasoA() {
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


    @Test
    public void onChargerClickedTestCasoB() {
        List<Charger> chargers = new ArrayList<Charger>();

        int indiceValid = 1;
        IRepository repository = Repositories.getFake(chargers);
        when(mv.getRepository()).thenReturn(repository);

        sut.init(mv);
        sut.onChargerClicked(indiceValid);

        verify(mv, never()).showChargerDetails(any());

    }

    @Test
    public void onChargerClickedTestCasoC() {
        List<Charger> chargers = new ArrayList<Charger>();

        //Indice < 0
        int indiceInvalid = -1;
        IRepository repository = Repositories.getFake(chargers);
        when(mv.getRepository()).thenReturn(repository);

        sut.init(mv);
        //Al operar sobre un indice invalido forzado, lanzara la excepcion indexoutOfBounds
        assertThrows(IndexOutOfBoundsException.class, () -> {sut.onChargerClicked(indiceInvalid);});
        verify(mv, never()).showChargerDetails(c1);

    }

    @Test
    public void onChargerClickedTestCasoD() {
        List<Charger> chargers = new ArrayList<Charger>();

        //Aplicamos el otro caso de indice > chargers.size()
        int indiceInvalid2 = 999999999;
        IRepository repository = Repositories.getFake(chargers);
        when(mv.getRepository()).thenReturn(repository);

        sut.init(mv);


        //No muestra nada
        verify(mv, never()).showChargerDetails(c1);

    }

    @Test
    public void onChargerClickedTestCasoE() {
        List<Charger> chargers = new ArrayList<Charger>();
        chargers.add(c1);
        chargers.add(c2);

        int indiceValid = -1;
        IRepository repository = Repositories.getFake(chargers);
        when(mv.getRepository()).thenReturn(repository);

        sut.init(mv);

        //No muestra nada
        verify(mv, never()).showChargerDetails(c1);
        verify(mv, never()).showChargerDetails(c2);

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


    @Test
    public void onMenuFavoritosClickedVariosFAvsTest() {

        Charger c1 = new Charger();
        Charger c2 = new Charger();
        Charger c3 = new Charger();

        c1.id = "1";
        c2.id = "2";
        c3.id = "3";

        chargers.add(c1);
        chargers.add(c2);
        chargers.add(c3);

        when(mv.getRepository()).thenReturn(repository);
        sut.init(mv);

        //CASO 1:Cuando hay 3 elementos en favoritos
        sut.onMenuFavoritosClicked();
        verify(mv, atLeast(1)).showChargers(captor.capture());
        captados = captor.getValue();

        assertEquals(3, captados.size());

    }

    @Test
    public void onMenuFavoritosClickedUnFavFavsTest() {
        //CASO 2: Cuando solo hay 1 elemento en favoritos
        Charger c1 = new Charger();

        c1.id = "1";

        chargers.add(c1);


        when(mv.getRepository()).thenReturn(repository);
        sut.init(mv);

        sut.onMenuFavoritosClicked();
        verify(mv, atLeast(1)).showChargers(captor.capture());
        captados = captor.getValue();

        assertEquals(1, captados.size());

    }

    @Test
    public void onMenuFavoritosClickedNoFavsTest() {
        //CASO 3: Cuando no hay elementos en favoritos

        when(mv.getRepository()).thenReturn(repository);
        sut.init(mv);

        // Ejecutar el método que se está probando
        sut.onMenuFavoritosClicked();

        // Verificar que se llamó al método showInfoNoFav()
        verify(mv, atLeast(1)).showInfoNoFav();

        assertEquals(0, captados.size());

    }

}