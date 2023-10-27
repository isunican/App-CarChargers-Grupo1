package es.unican.carchargers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

import es.unican.carchargers.activities.details.DetailsView;
import es.unican.carchargers.activities.main.IMainContract;
import es.unican.carchargers.activities.main.MainPresenter;
import es.unican.carchargers.activities.main.MainView;
import es.unican.carchargers.model.Charger;
import es.unican.carchargers.repository.IRepository;
import es.unican.carchargers.repository.Repositories;

public class InitCorrectoTest {

    @Mock
    IMainContract.View mView;

    IMainContract.Presenter sut;
    List<Charger> listChargers;
    Charger c1, c2, c3, c4;

    ArgumentCaptor<List<Charger>> captorCargadores;
    ArgumentCaptor<Integer> captorNumCargadores;

    @Before
    public void inicializa() {
        MockitoAnnotations.openMocks(this); // Creación de los mocks definidos anteriormente con @Mock

        captorCargadores = ArgumentCaptor.forClass(List.class);
        captorNumCargadores = ArgumentCaptor.forClass(Integer.class);

        sut = new MainPresenter();
        c1 = new Charger();
        c1.operator.title = "Zunder";
        c2 = new Charger();
        c2.operator.title = "Repsol";
        c3 = new Charger();
        c3.operator.title = "Iberdrola";
        c4 = new Charger();
        c4.operator.title = "Particular";
    }


    @Test
    public void listaVaciaTest() {
        inicializa();
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
        inicializa();
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
        inicializa();
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

}