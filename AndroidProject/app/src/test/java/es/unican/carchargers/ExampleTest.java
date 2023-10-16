package es.unican.carchargers;

import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import es.unican.carchargers.activities.main.MainPresenter;
import es.unican.carchargers.model.Charger;
import es.unican.carchargers.repository.ICallBack;
import es.unican.carchargers.repository.IRepository;
import es.unican.carchargers.repository.service.APIArguments;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;


import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.view.View;

public class ExampleTest {

    @Mock
    private View view;

    @Mock
    private IRepository repository;

    private MainPresenter mainPresenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mainPresenter = new MainPresenter(view);
        mainPresenter.setRepository(repository);
    }

    @Test
    public void testLoadSuccess() {
        List<Charger> chargers = new ArrayList<>();
        // Configura el repositorio para devolver una lista de cargadores
        when(repository.getChargers(any(APIArguments.class), any(ICallBack.class))
                .thenAnswer((Answer<Void>) invocation -> {
                    ICallBack callback = invocation.getArgument(1);
                    callback.onSuccess(chargers);
                    return null;
                }));

        mainPresenter.load();

        // Verifica que se llamaron a los métodos adecuados en la vista
        verify(view).showChargers(chargers);
        verify(view).showLoadCorrect(chargers.size());
    }

    @Test
    public void testLoadFailure() {
        // Configura el repositorio para devolver un error
        when(repository.getChargers(any(APIArguments.class), any(ICallBack.class))
                .thenAnswer((Answer<Void>) invocation -> {
                    ICallBack callback = invocation.getArgument(1);
                    callback.onFailure(new Throwable("Error"));
                    return null;
                }));

        mainPresenter.load();

        // Verifica que se llamaron a los métodos adecuados en la vista
        verify(view).showLoadError("El sistema no pudo conectarse a la red");
    }
}
