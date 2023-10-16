package es.unican.carchargers.utils;

import android.view.View;
import android.widget.ListView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.List;

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
                int count = lv.getCount();
                List<Charger> c = (List<Charger>) lv.getAdapter();
                c.get(0).id.equals("213027");
                c.get(27).id.equals("212922");
                return count == 28;   // hay 7 de 7.4 y 43 hay 21
            }

            @Override public void describeTo (final Description description) {
                description.appendText ("ListView should be filtered by power");
            }
        };
    }
/*
    public static Matcher<View> isFilteredByPower() {
        return new TypeSafeMatcher<View>() {
            @Override public boolean matchesSafely (final View view) {
                ListView lv = (ListView) view;
                List<Charger> c = (List<Charger>) lv.getAdapter();
                for(int i = 0; i < c.size(); i++){
                    if (c.get(i).)
                }
            }

            @Override public void describeTo (final Description description) {
                description.appendText ("ListView should not be empty");
            }
        };
    }
*/
}
