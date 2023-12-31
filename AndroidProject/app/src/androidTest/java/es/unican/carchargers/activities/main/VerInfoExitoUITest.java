package es.unican.carchargers.activities.main;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withResourceName;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;
import static es.unican.carchargers.utils.Matchers.isNotEmpty;

import android.content.Context;

import androidx.test.espresso.DataInteraction;
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
public class VerInfoExitoUITest {

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
    public void showChargersTest() {
        onView(withId(R.id.lvChargers)).check(matches(isNotEmpty()));


        //Cojo el primer cargador del layout
        onData(anything()).inAdapterView(withId(R.id.lvChargers)).atPosition(0).perform(click());

        //Comprobacion elementos cargador en posicion 0
        onView(withId(R.id.tvTitle)).check(matches(withText("Zunder")));
        onView(withId(R.id.tvInfo)).check(matches(withText("https://www.zunder.com/en/")));
        onView(withId(R.id.tvProvincia)).check(matches(withText("Región de Murcia")));
        onView(withId(R.id.tvCiudad)).check(matches(withText("Balsicas - Centro Cívico ( Zunder)")));
        onView(withId(R.id.tvPrecio)).check(matches(withText(" 0,39€/kWh AC")));
        onView(withId(R.id.tvDisponibilidad)).check(matches(withText("Disponible")));

        //Comprobacion tipo de enchufe y correspondiente imagen
        onView(withId(R.id.tvConector1)).check(matches(withText("Type 2 (Socket Only)")));
        onView(withId(R.id.logo1));



    }

}
