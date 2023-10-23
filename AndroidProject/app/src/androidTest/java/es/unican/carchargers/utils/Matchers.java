package es.unican.carchargers.utils;

import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.ArrayList;
import java.util.List;

import es.unican.carchargers.activities.main.ChargersArrayAdapter;
import es.unican.carchargers.model.Charger;

public class Matchers {

    /**
     * Metodo para comprobar en los test de interfaz si una lista está vacía.
     * Para utilizarlo se usa:
     *      onView(withId(R.id.id_de_la_lista)).check(matches(hasElements()))
     * @return Matcher<View>
     */
    public static Matcher<View> isNotEmpty() {
        return new TypeSafeMatcher<View>() {
            @Override public boolean matchesSafely (final View view) {
                ListView lv = (ListView) view;
                int count = lv.getCount();
                return count > 0;
            }

            @Override public void describeTo (final Description description) {
                description.appendText ("ListView should not be empty");
            }
        };
    }

    public static Matcher<View> isFilteredByPower() {
        return new TypeSafeMatcher<View>() {
            @Override public boolean matchesSafely (final View view) {
            ListView lv = (ListView) view;
            ListAdapter adapter = lv.getAdapter();
            if (adapter instanceof ChargersArrayAdapter) {
                ChargersArrayAdapter chargersAdapter = (ChargersArrayAdapter) adapter;
                List<Charger> chargers = new ArrayList<>();
                for (int i = 0; i < adapter.getCount(); i++) {
                    chargers.add(chargersAdapter.getItem(i));
                }

                boolean isCharger1IdEqual = chargers.get(0).id.equals("213038");
                boolean isCharger27IdEqual = chargers.get(27).id.equals("212923");
                /*
                String a = chargers.get(0).id;
                String b = chargers.get(27).id;
                Log.d("MiTag", "Este es un mensaje de depuración."+ a + " " + b);
                */
                return chargers.size() == 28 && isCharger1IdEqual && isCharger27IdEqual;
            }
            return false;
        }

            @Override public void describeTo (final Description description) {
                description.appendText ("ListView should be filtered by power");
            }
        };
    }
}
