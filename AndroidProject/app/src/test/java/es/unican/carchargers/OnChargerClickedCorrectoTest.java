package es.unican.carchargers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import es.unican.carchargers.activities.main.IMainContract;
import es.unican.carchargers.activities.main.MainPresenter;
import es.unican.carchargers.model.Charger;
import es.unican.carchargers.repository.IRepository;
import es.unican.carchargers.repository.Repositories;

public class OnChargerClickedCorrectoTest {

    @Mock
    IMainContract.View view;

    ArgumentCaptor<Charger> captorCharger;


    IMainContract.Presenter presenter;
        Charger c1, c2, c3, c4;

        public void setup() {
            MockitoAnnotations.openMocks(this); // Creación de los mocks definidos anteriormente con @Mock

            captorCharger = ArgumentCaptor.forClass(Charger.class);
            presenter = new MainPresenter();
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
        public void testOnChargerClickedIndiceValidoTest() {
            setup();
            List<Charger> chargers = new ArrayList<Charger>();
            chargers.add(c1);
            chargers.add(c2);

            int indiceValid = 1;
            IRepository repository = Repositories.getFake(chargers);
            when(view.getRepository()).thenReturn(repository);

            presenter.init(view);
            presenter.onChargerClicked(indiceValid);

            verify(view).showChargerDetails(captorCharger.capture());
            assertEquals(c2.operator.title, captorCharger.getValue().operator.title);
        }

}



