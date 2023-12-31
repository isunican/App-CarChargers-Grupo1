package es.unican.carchargers.activities.main;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static es.unican.carchargers.utils.Matchers.isFilteredByConnector;


import android.content.Context;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Rule;
import org.junit.Test;

import dagger.hilt.android.testing.BindValue;
import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;
import dagger.hilt.android.testing.UninstallModules;
import es.unican.carchargers.R;
import es.unican.carchargers.common.RepositoriesModule;
import es.unican.carchargers.repository.IRepository;
import es.unican.carchargers.repository.Repositories;

/**
 * Example UI Test using Hilt dependency injection
 * Documentation: https://developer.android.com/training/dependency-injection/hilt-testing
 * This test also uses an HTTP Idling Resource
 */
@HiltAndroidTest
@UninstallModules(RepositoriesModule.class)
public class FiltrarConectorValidoUITest {

    @Rule(order = 0)  // the Hilt rule must execute first
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Rule(order = 1)
    public ActivityScenarioRule<MainView> activityRule = new ActivityScenarioRule(MainView.class);

    // necesito el context para acceder a recursos, por ejemplo un json con datos fake
    Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();


    // inject a fake repository that loads the data from a local json file
    // IMPORTANT: all the tests in this class must use this repository
    @BindValue IRepository repository = Repositories
            .getFake(context.getResources().openRawResource(R.raw.chargers_es_100));

    @Test
    public void filtroSimpleConectorDialogTest() {
        // Caso valido

        // Click en el elemento "filtro" del menu
        onView(withId(R.id.filtro)).perform(click());

        // Click en el boton para filtrar por tipo de conector
        onView(withId(R.id.btnConector)).perform(click());

        // Verifica que se muestra el texto
        onView(withText("Marque las casillas que más se adapten a su búsqueda:")).check(matches(isDisplayed()));

        // Realiza una selección de elementos en el diálogo de selección múltiple
        onView(withText("Type 2 (Socket Only)")).perform(click());

        // Verifica se han seleccionado los tipos de conectores
        onView(withText("Type 2 (Socket Only)")).check(matches(isChecked()));

        // Click en aceptar
        onView(withText("Aceptar")).perform(click());

        //lvChargers es un listView del layout
        onView(withId(R.id.lvChargers)).check(matches(isFilteredByConnector("213054", "212922", 52)));

    }
    @Test
    public void filtroDobleConectorDialogTest() {
        // Caso valido

        // Click en el elemento "filtro" del menu
        onView(withId(R.id.filtro)).perform(click());

        // Click en el boton para filtrar por tipo de conector
        onView(withId(R.id.btnConector)).perform(click());

        // Verifica que se muestra el texto
        onView(withText("Marque las casillas que más se adapten a su búsqueda:")).check(matches(isDisplayed()));

        // Realiza una selección de elementos en el diálogo de selección múltiple
        onView(withText("Type 2 (Socket Only)")).perform(click());
        onView(withText("CHAdeMO")).perform(click());

        // Verifica se han seleccionado los tipos de conectores
        onView(withText("Type 2 (Socket Only)")).check(matches(isChecked()));
        onView(withText("CHAdeMO")).check(matches(isChecked()));

        // Click en aceptar
        onView(withText("Aceptar")).perform(click());

        //lvChargers es un listView del layout
        onView(withId(R.id.lvChargers)).check(matches(isFilteredByConnector("213054","212922", 75)));
    }
}
