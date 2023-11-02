package es.unican.carchargers.utils;

import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    public static Matcher<View> isFilteredByConnector() {
        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(final View view) {
                ListView lv = (ListView) view;
                ListAdapter adapter = lv.getAdapter();
                if (adapter instanceof ChargersArrayAdapter) {
                    ChargersArrayAdapter chargersAdapter = (ChargersArrayAdapter) adapter;
                    List<Charger> chargers = new ArrayList<>();
                    for (int i = 0; i < adapter.getCount(); i++) {
                        chargers.add(chargersAdapter.getItem(i));
                    }
                    // Compara los conectores de los cargadores con los esperados
                    boolean isConnector1Equal = chargers.get(0).id.equals("213054");
                    boolean isConnector26Equal = chargers.get(25).id.equals("212978");
                    boolean isConnector52Equal = chargers.get(51).id.equals("212922");

                    return chargers.size() == 52 && isConnector1Equal && isConnector52Equal && isConnector26Equal;
                }
                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("ListView should be filtered by connector type");
            }
        };
    }

    // matcher que cuenta el numero de cargadores que aparecen por pantalla
    public static Matcher<View> countElements(final int expectedChargerCount) {
        return new TypeSafeMatcher<>() {
            @Override
            public boolean matchesSafely(final View view) {
                ListView lv = (ListView) view;
                ListAdapter adapter = lv.getAdapter();
                if (adapter instanceof ChargersArrayAdapter) {
                    return adapter.getCount() == expectedChargerCount;
                }
                return false;
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("ListView should be filtered by power");
            }
        };
    }
    
    public static Matcher<View> isListAscending() {
        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View view) {
                ListView lv = (ListView) view;
                ListAdapter adapter = lv.getAdapter();

                if (adapter instanceof ChargersArrayAdapter) {
                    ChargersArrayAdapter yourAdapter = (ChargersArrayAdapter) adapter;
                    List<Charger> chargers = new ArrayList<>();

                    for (int i = 0; i < adapter.getCount(); i++) {
                        chargers.add(yourAdapter.getItem(i));
                    }

                    // Ordenar la lista de cargadores en función del atributo usageCost
                    Collections.sort(chargers, new Comparator<Charger>() {
                        @Override
                        public int compare(Charger charger1, Charger charger2) {
                            // Extrae el coste de uso (usageCost) de cada cargador y compáralos como números.
                            double cost1 = charger1.extraerCosteCharger();
                            double cost2 = charger2.extraerCosteCharger();
                            return Double.compare(cost1, cost2);
                        }
                    });

                    // Verificar si la lista de cargadores original está ordenada de manera ascendente
                    return isSortedAscending(chargers);
                }
                return false;
            }
            @Override
            public void describeTo(Description description) {
                description.appendText("ListView should be sorted by usageCost in ascending order");
            }
            private boolean isSortedAscending(List<Charger> chargers) {
                for (int i = 1; i < chargers.size(); i++) {
                    double cost1 = chargers.get(i - 1).extraerCosteCharger();
                    double cost2 = chargers.get(i).extraerCosteCharger();
                    if (cost1 > cost2) {
                        return false;
                    }
                }
                return true;
            }
        };
    }


    public static Matcher<View> isListDescending() {
        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View view) {
                ListView lv = (ListView) view;
                ListAdapter adapter = lv.getAdapter();

                if (adapter instanceof ChargersArrayAdapter) {
                    ChargersArrayAdapter yourAdapter = (ChargersArrayAdapter) adapter;
                    List<Charger> chargers = new ArrayList<>();

                    for (int i = 0; i < adapter.getCount(); i++) {
                        chargers.add(yourAdapter.getItem(i));
                    }

                    // Ordenar la lista de cargadores en función del atributo usageCost en orden descendente
                    Collections.sort(chargers, new Comparator<Charger>() {
                        @Override
                        public int compare(Charger charger1, Charger charger2) {
                            // Extrae el coste de uso (usageCost) de cada cargador y compáralos como números en orden descendente.
                            double cost1 = charger1.extraerCosteCharger();
                            double cost2 = charger2.extraerCosteCharger();
                            return Double.compare(cost2, cost1);
                        }
                    });

                    // Verificar si la lista de cargadores original está ordenada de manera descendente
                    return isSortedDescending(chargers);
                }
                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("ListView should be sorted by usageCost in descending order");
            }

            private boolean isSortedDescending(List<Charger> chargers) {
                for (int i = 1; i < chargers.size(); i++) {
                    double cost1 = chargers.get(i - 1).extraerCosteCharger();
                    double cost2 = chargers.get(i).extraerCosteCharger();
                    if (cost1 < cost2) {
                        return false;
                    }
                }
                return true;
            }
        };
    }



}
