/*package es.unican.carchargers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import es.unican.carchargers.activities.main.IMainContract;
import es.unican.carchargers.activities.main.MainPresenter;
import es.unican.carchargers.model.Charger;

public class OnChargerClickedCorrectoTest {



        @Mock
        private IMainContract view;

        private MainPresenter presenter;
        Charger c1, c2, c3, c4;

        @Before
        public void setup() {
            MockitoAnnotations.initMocks(this);
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
            List<Charger> chargers = new ArrayList<>();
            chargers.add(new Charger());
            chargers.add(new Charger());

            int validIndex = 1;
            presenter.onChargerClicked(validIndex);

            verify(view).showChargerDetails(chargers.get(validIndex));
        }

        @Test
        public void testOnChargerClickedIndiceInvalidoTest() {
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

 */

