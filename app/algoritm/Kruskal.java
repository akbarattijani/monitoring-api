package algoritm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author AKBAR <akbar.attijani@gmail.com>
 */
public class Kruskal {

    public int minimumSpanningTree(String[] from, String[] to, double[] weight) {
        int result = 0;
        Map<String, Double> choosedPoints = new HashMap<>();
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

        sorting(from, to, weight);

        for (int i = 0; i < weight.length; i++) {
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
                }
            }
        }

        for (Map.Entry<String, Double> map : choosedPoints.entrySet()) {
            System.out.println(map.getKey() + " => " + map.getValue());
        }

        return result;
    }

    private void sorting(String[] from, String[] to, double[] weight) {
        int sample = weight.length - 2;
        String tempFrom;
        String tempTo;
        double tempWeight;

        for (int p = 0; p <= sample; p++) {
            for (int q = p; q <= sample + 1; q++) {
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
            }
        }
    }
}
