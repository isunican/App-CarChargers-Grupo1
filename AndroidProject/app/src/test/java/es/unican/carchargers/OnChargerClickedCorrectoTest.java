package es.unican.carchargers;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import es.unican.carchargers.activities.main.IMainContract;
import es.unican.carchargers.activities.main.MainPresenter;
import es.unican.carchargers.model.Charger;

public class OnChargerClickedCorrectoTest {

   import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;



        @Mock
        private IMainContract view;

        private MainPresenter presenter;

        @Before
        public void setup() {
            MockitoAnnotations.initMocks(this);
            presenter = new MainPresenter();
        }

        @Test
        public void testOnChargerClickedWithValidIndex() {
            List<Charger> chargers = new ArrayList<>();
            chargers.add(new Charger("Charger 1"));
            chargers.add(new Charger("Charger 2"));

            int validIndex = 1;
            presenter.setShownChargers(chargers);
            presenter.onChargerClicked(validIndex);

            Mockito.verify(view).showChargerDetails(chargers.get(validIndex));
        }

        @Test
        public void testOnChargerClickedWithInvalidIndex() {
            List<Charger> chargers = new ArrayList<>();
            chargers.add(new Charger("Charger 1"));
            chargers.add(new Charger("Charger 2"));

            int invalidIndex = 2; // Index out of bounds

            presenter.setShownChargers(chargers);
            presenter.onChargerClicked(invalidIndex);

            // Verify that view.showChargerDetails is never called when the index is invalid
            Mockito.verify(view, Mockito.never()).showChargerDetails(Mockito.any(Charger.class));
        }
    }

