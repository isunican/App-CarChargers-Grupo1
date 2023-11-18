package es.unican.carchargers.activities.main;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.IsAnything.anything;
import static org.junit.Assert.assertTrue;
import static es.unican.carchargers.utils.Matchers.isFilteredByConnector;
import static es.unican.carchargers.utils.Matchers.isNotEmpty;


import android.app.Activity;
import android.content.Context;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import org.hamcrest.CoreMatchers;
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

@HiltAndroidTest
@UninstallModules(RepositoriesModule.class)
public class AnhadirfavoritosUITest {

    @Rule(order = 0)  // the Hilt rule must execute first
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Rule(order = 1)
    public ActivityScenarioRule<MainView> activityRule = new ActivityScenarioRule(MainView.class);

    // Necesito el context para acceder a recursos, por ejemplo un json con datos fake
    Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

    // inject a fake repository that loads the data from a local json file
    // IMPORTANT: all the tests in this class must use this repository
    @BindValue IRepository repository = Repositories
            .getFake(context.getResources().openRawResource(R.raw.chargers_es_100));

    @Test
    public void AnhadirFavVistaDetalle() {

        // Caso valido

        // Click en el primer cargador mostrado, para abrir su vista en detalle
        onData(anything()).inAdapterView(withId(R.id.lvChargers)).atPosition(0).perform(click());

        // Click en el boton de anhadir a favoritos
        onView(withId(R.id.btnFavs)).perform(click());

        // Comprobar que se muestra el mensaje de cargador anhadido a favorito
        /**activityRule.getScenario().onActivity(activity -> {
            View decorView = activity.getWindow().getDecorView();
            onView(withText("String del toast"))
                    .inRoot(withDecorView(not(is(decorView))))
                    .check(matches(isDisplayed()));
        });*/

        // Volver atras, para poder acceder al menu que lleva a la lista de favoritos
        pressBack();

        // Clicar en lista de favoritos para abrirla
        onView(withId(R.id.favoritos)).perform(click());

        // Comprobar que el elemento en la lista es el mismo que se anhadio
        onView(withId(R.id.lvChargers)).check(matches(isNotEmpty()));

        // Ver que el titulo del cargador en la lista de favs coincide con el del primer cargador, el clicado
        DataInteraction interaction = onData(CoreMatchers.anything())
                .inAdapterView(withId(R.id.lvChargers)).atPosition(0);
        interaction.onChildView(withId(R.id.tvTitle)).check(matches(withText("Zunder")));
    }

}
