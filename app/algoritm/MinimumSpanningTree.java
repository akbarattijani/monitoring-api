package algoritm;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author AKBAR <akbar.attijani@gmail.com>
 */
public class MinimumSpanningTree {
    private final int SMALLEST = 0;
    private final int BIGGEST = 1;

    public int kruskal(String[] from, String[] to, double[] weight) {
        int result = 0;
        Map<String, Double> choosedPoints = new LinkedHashMap<>();
        Map<String, Double> missedPoints = new LinkedHashMap<>();
        ArrayList<String> points = new ArrayList<>();
        for (String value : from) {
            if (!points.contains(value)) {
                points.add(value);
            }
        }

        for (String value : to) {
            if (!points.contains(value)) {
                points.add(value);
            }
        }

        // Sorting from smallest
        sorting(SMALLEST, from, to, weight);

        // Searching point
        for (int i = 0; i < weight.length; i++) {
            System.out.println(from[i] + to[i] + " => " + weight[i]);
            if (choosedPoints.size() == 0) {
                choosedPoints.put(from[i] + to[i], weight[i]);
            } else {
                boolean first = false;
                boolean last = false;

                for (Map.Entry<String, Double> map : choosedPoints.entrySet()) {
                    String key = map.getKey();
                    if (key.contains(from[i])) {
                        first = true;
                    }

                    if (key.contains(to[i])) {
                        last = true;
                    }
                }

                if (!first || !last) {
                    choosedPoints.put(from[i] + to[i], weight[i]);
                } else {
                    missedPoints.put(from[i] + "|" + to[i], weight[i]);
                }
            }
        }

        // Checking point if missed
        for (Map.Entry<String, Double> map : missedPoints.entrySet()) {
            String f = map.getKey().split("\\|")[0]; // e1
            String t = map.getKey().split("\\|")[1]; // e2
            System.out.println("Missed point : " + map.getKey() + "\tFrom : " + f + "\tTo : " + t);
            ArrayList<String> firstPoint = new ArrayList<>();
            ArrayList<String> lastPoint = new ArrayList<>();

            // get all point
            getAllRoute(firstPoint, f, choosedPoints);
            getAllRoute(lastPoint, t, choosedPoints);

            for (String valFirst : firstPoint) {
                for (String valLast : lastPoint) {
                    if (valFirst.equals(valLast)) {
                        choosedPoints.put(f + t, map.getValue());
                        missedPoints.remove(map.getKey());
                    }
                }
            }
        }


        System.out.println("=============================================");
        for (Map.Entry<String, Double> map : choosedPoints.entrySet()) {
            System.out.println(map.getKey() + " => " + map.getValue());
        }

        return result;
    }

    private void getAllRoute(ArrayList<String> list, String route, Map<String, Double> storage) {
        for (Map.Entry<String, Double> map : storage.entrySet()) {
            if (map.getKey().contains(route)) {
                if (!list.contains(map.getKey())) {
                    list.add(map.getKey());
                    String changeRoute = map.getKey().replace(route, "");
                    getAllRoute(list, changeRoute, storage);
                }
            }
        }
    }

    private void sorting(int start, String[] from, String[] to, double[] weight) {
        int sample = weight.length - 2;
        String tempFrom;
        String tempTo;
        double tempWeight;

        for (int p = 0; p <= sample; p++) {
            for (int q = p; q <= sample + 1; q++) {
                if (start == SMALLEST) {
                    if (weight[p] > weight[q]) {
                        tempFrom = from[p];
                        from[p] = from[q];
                        from[q] = tempFrom;

                        tempTo = to[p];
                        to[p] = to[q];
                        to[q] = tempTo;

                        tempWeight = weight[p];
                        weight[p] = weight[q];
                        weight[q] = tempWeight;
                    }
                } else if (start == BIGGEST) {
                    if (weight[p] < weight[q]) {
                        tempFrom = from[p];
                        from[p] = from[q];
                        from[q] = tempFrom;

                        tempTo = to[p];
                        to[p] = to[q];
                        to[q] = tempTo;

                        tempWeight = weight[p];
                        weight[p] = weight[q];
                        weight[q] = tempWeight;
                    }
                }
            }
        }
    }
}
