package es.unican.carchargers.activities.main;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static es.unican.carchargers.utils.Matchers.isFilteredByPower;

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
public class OrdenarPorPrecioUITest {

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
    public void ordenDialogTest() {

        // CASO VALIDO ASCENDENTE
        // Verifica el elemento de menú ordenar
        onView(withId(R.id.orden)).perform(click());

        // Verifica que el diálogo se muestra correctamente
        onView(withText("Marque las casillas que más se adapten a su búsqueda:")).check(matches(isDisplayed()));

        // Selecciona precio y radioButton ascendente.
        onView(withText("Precio")).perform(click());
        onView(withText("Ascendente")).perform(click());

        // Verifica que las selecciones se realizaron correctamente
        onView(withText("Precio")).check(matches(isChecked()));
        onView(withText("Ascendente")).check(matches(isChecked()));

        // Acepta el dialog
        onView(withText("Aceptar")).perform(click());

        //FALTA COMPROBACIÓN COMO LO HAGO
        //onView(withId(R.id.lvChargers)).check(matches()); ESTO ESTA MAL

 //----------------------------------------------------------------------------//

        //CASO VALIDO DESCENDENTE
        // Verifica el elemento de menú ordenar
        onView(withId(R.id.orden)).perform(click());

        // Verifica que el diálogo se muestra correctamente
        onView(withText("Marque las casillas que más se adapten a su búsqueda:")).check(matches(isDisplayed()));

        // Selecciona precio y radioButton ascendente.
        onView(withText("Precio")).perform(click());
        onView(withText("Descendente")).perform(click());

        // Verifica que las selecciones se realizaron correctamente
        onView(withText("Precio")).check(matches(isChecked()));
        onView(withText("Descendente")).check(matches(isChecked()));

        // Acepta el dialog
        onView(withText("Aceptar")).perform(click());

        //FALTA COMPROBACIÓN COMO LO HAGO
        //onView(withId(R.id.lvChargers)).check(matches()); ESTO ESTA MAL

    }


}
