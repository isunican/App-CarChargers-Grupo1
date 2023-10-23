package es.unican.carchargers.activities.main;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static es.unican.carchargers.utils.Matchers.isFilteredByPower;

import android.content.Context;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.AfterClass;
import org.junit.BeforeClass;
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
import es.unican.carchargers.utils.HTTPIdlingResource;

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

    @BeforeClass
    public static void setupClass() {
        // Al usar un repository fake (que no accede por HTTP) no necesitamos activar este Idling Resource.
        HTTPIdlingResource.getInstance().init();
    }

    @AfterClass
    public static void teardownClass() {
        HTTPIdlingResource.getInstance().finish();
    }

    // inject a fake repository that loads the data from a local json file
    // IMPORTANT: all the tests in this class must use this repository
    @BindValue IRepository repository = Repositories
            .getFake(context.getResources().openRawResource(R.raw.chargers_es_100));

    @Test
    public void filtrosDialogTest() {
        // Caso valido

        // Click en el elemento "filtro" del menu
        onView(withId(R.id.filtro)).perform(click());

        // Click en el boton para filtrar por tipo de conector
        // TODO : Cambiar btnPotencia por el boton para filtrar por conector
        onView(withId(R.id.btnPotencia)).perform(click());

        // Verifica que se muestra el texto
        onView(withText("Marque las casillas que más se adapten a su búsqueda:")).check(matches(isDisplayed()));

        // Realiza una selección de elementos en el diálogo de selección múltiple
        // TODO : Selecciono dos tipos de conectores
        onView(withText("43kW")).perform(click());
        onView(withText("7.4kW")).perform(click());

        // Verifica se han seleccionado los tipos de conectores
        // TODO : Verifico que se han seleccionado los dos tipos de conectores
        onView(withText("43kW")).check(matches(isChecked()));
        onView(withText("7.4kW")).check(matches(isChecked()));

        // Click en aceptar
        onView(withText("Aceptar")).perform(click());

        onView(withId(R.id.lvChargers)).check(matches(isFilteredByPower()));

    }


}
