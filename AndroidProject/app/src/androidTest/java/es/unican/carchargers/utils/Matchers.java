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
                            double cost1 = charger1.extraerCosteCharger(true);
                            double cost2 = charger2.extraerCosteCharger(true);
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
                    double cost1 = chargers.get(i - 1).extraerCosteCharger(true);
                    double cost2 = chargers.get(i).extraerCosteCharger(true);
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
                            double cost1 = charger1.extraerCosteCharger(false);
                            double cost2 = charger2.extraerCosteCharger(false);
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
                    double cost1 = chargers.get(i - 1).extraerCosteCharger(false);
                    double cost2 = chargers.get(i).extraerCosteCharger(false);
                    if (cost1 < cost2) {
                        return false;
                    }
                }
                return true;
            }
        };
    }



}
