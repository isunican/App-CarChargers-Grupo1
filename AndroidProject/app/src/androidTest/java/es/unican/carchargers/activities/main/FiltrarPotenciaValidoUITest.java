package es.unican.carchargers.activities.main;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.anything;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import androidx.test.espresso.DataInteraction;


import static es.unican.carchargers.utils.Matchers.countElements;

import android.content.Context;



import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;


import org.hamcrest.Matchers;
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
 * UI Test using Hilt dependency injection
 * Documentation: https://developer.android.com/training/dependency-injection/hilt-testing
 * This test also uses an HTTP Idling Resource
 */
@HiltAndroidTest
@UninstallModules(RepositoriesModule.class)
public class FiltrarPotenciaValidoUITest {

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
    public void filtrosDialogTest() {
        // CASO VALIDO

        // Verifica que el elemento de menú filtro
        onView(withId(R.id.filtro)).perform(click());

        // Verifica que el elemento de filtrar por potencia se muestra
        onView(withId(R.id.btnPotencia)).perform(click());

        // Verifica que el diálogo se muestra
        onView(withText("Marque las casillas que más se adapten a su búsqueda:")).check(matches(isDisplayed()));

        // Realiza una selección de elementos en el diálogo de selección múltiple
        onView(withText("43kW")).perform(click());
        onView(withText("7.4kW")).perform(click());

        // Verifica que las selecciones se realizaron correctamente
        onView(withText("43kW")).check(matches(isChecked()));
        onView(withText("7.4kW")).check(matches(isChecked()));

        // Acepta el diálogo
        onView(withText("Aceptar")).perform(click());

        // comprobamos que el primer punto de carga que aparece es el deseado
        DataInteraction interaction = onData(anything()).inAdapterView(withId(R.id.lvChargers)).atPosition(0);
        interaction.onChildView(withId(R.id.tvTitle)).check(matches(withText(Matchers.anyOf(
                Matchers.containsString("Repsol - Ibil (ES)"),
                Matchers.containsString("Repsol la Raza")
        ))));

        // comprobamos que el último punto de carga que aparece es el deseado
        interaction = onData(anything()).inAdapterView(withId(R.id.lvChargers)).atPosition(27);
        interaction.onChildView(withId(R.id.tvTitle)).check(matches(withText(Matchers.anyOf(
                Matchers.containsString("Ayunt. Huelva - Calle Puerto"),
                Matchers.containsString("Calle Puerto"),
                Matchers.containsString("(Unknown Operator)")
        ))));

        // comprobamos que la lista muestra el numero de puntos que deseamos
        onView(withId(R.id.lvChargers)).check(matches(countElements(28)));

    }

}
