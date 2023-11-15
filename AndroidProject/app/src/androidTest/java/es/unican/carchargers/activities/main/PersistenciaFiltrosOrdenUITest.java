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
public class PersistenciaFiltrosOrdenUITest {

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
    public void persistenciaAñadiendoUnaPotencia() {
        // caso A 12.b
        // Verifica que el elemento de menú filtro
        onView(withId(R.id.filtros)).perform(click());

        // Verifica que el elemento de filtrar por potencia se muestra
        onView(withId(R.id.btnPotencia)).perform(click());

        // Verifica que el diálogo se muestra
        onView(withText("Marque las casillas que más se adapten a su búsqueda:")).check(matches(isDisplayed()));

        // Realiza una selección de elementos en el diálogo de selección múltiple
        onView(withText("2kW")).perform(click());
        onView(withText("7.4kW")).perform(click());

        // Verifica que las selecciones se realizaron correctamente
        onView(withText("2kW")).check(matches(isChecked()));
        onView(withText("7.4kW")).check(matches(isChecked()));

        // Acepta el diálogo
        onView(withText("Aceptar")).perform(click());

        // Verifica que el elemento de menú filtro
        onView(withId(R.id.filtros)).perform(click());

        // Verifica que el elemento de filtrar por potencia se muestra
        onView(withId(R.id.btnPotencia)).perform(click());

        // Verifica que el diálogo se muestra
        onView(withText("Marque las casillas que más se adapten a su búsqueda:")).check(matches(isDisplayed()));

        // Verifica que las selecciones se realizaron correctamente
        onView(withText("2kW")).check(matches(isChecked()));
        onView(withText("7.4kW")).check(matches(isChecked()));

        onView(withText("43kW")).perform(click());

        onView(withText("Cancelar")).perform(click());

        // Verifica que el elemento de filtrar por potencia se muestra
        onView(withId(R.id.btnPotencia)).perform(click());

        // Verifica que el diálogo se muestra
        onView(withText("Marque las casillas que más se adapten a su búsqueda:")).check(matches(isDisplayed()));

        // Verifica que las selecciones se realizaron correctamente
        onView(withText("2kW")).check(matches(isChecked()));
        onView(withText("7.4kW")).check(matches(isChecked()));

    }

    @Test
    public void persistenciaAñadiendoUnaPotenciaLimpiando() {
        // A 12.a
        // Verifica que el elemento de menú filtro
        onView(withId(R.id.filtros)).perform(click());

        // Verifica que el elemento de filtrar por potencia se muestra
        onView(withId(R.id.btnPotencia)).perform(click());

        // Verifica que el diálogo se muestra
        onView(withText("Marque las casillas que más se adapten a su búsqueda:")).check(matches(isDisplayed()));

        // Realiza una selección de elementos en el diálogo de selección múltiple
        onView(withText("2kW")).perform(click());
        onView(withText("7.4kW")).perform(click());

        // Verifica que las selecciones se realizaron correctamente
        onView(withText("2kW")).check(matches(isChecked()));
        onView(withText("7.4kW")).check(matches(isChecked()));

        // Acepta el diálogo
        onView(withText("Aceptar")).perform(click());

        // Verifica que el elemento de menú filtro
        onView(withId(R.id.filtros)).perform(click());

        // Verifica que el elemento de filtrar por potencia se muestra
        onView(withId(R.id.btnPotencia)).perform(click());

        // Verifica que el diálogo se muestra
        onView(withText("Marque las casillas que más se adapten a su búsqueda:")).check(matches(isDisplayed()));

        // Verifica que las selecciones se realizaron correctamente
        onView(withText("2kW")).check(matches(isChecked()));
        onView(withText("7.4kW")).check(matches(isChecked()));

        onView(withText("2kW")).perform(click());
        onView(withText("7.4kW")).perform(click());
        onView(withText("43kW")).perform(click());

        onView(withText("Cancelar")).perform(click());

        // Verifica que el elemento de filtrar por potencia se muestra
        onView(withId(R.id.btnPotencia)).perform(click());

        // Verifica que el diálogo se muestra
        onView(withText("Marque las casillas que más se adapten a su búsqueda:")).check(matches(isDisplayed()));

        // Verifica que las selecciones se realizaron correctamente
        onView(withText("2kW")).check(matches(isChecked()));
        onView(withText("7.4kW")).check(matches(isChecked()));

    }
}