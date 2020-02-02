package algoritm;

import model.Classification;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author AKBAR <akbar.attijani@gmail.com>
 */
public class KNearestNeighbor {
    public List<Classification> classification(List<Classification> samples, String data, int K) {
        try {
            /*  index 0 : nilai euclidean distance
                index 1 : index dari samples
                index 2 : id dari kelas
             */
            double[][] ED = new double[samples.size()][3];
            String[] testing = data.split(" ");

            for (int i = 0; i < samples.size(); i++) {
                Classification sample = samples.get(i);
                ED[i][1] = i;
                ED[i][2] = sample.getId();

                for (int count = 0; count < testing.length; count++) {
                    ED[i][0] += (Integer.parseInt(sample.getBiner()[count]) - Integer.parseInt(testing[count])) * (Integer.parseInt(sample.getBiner()[count]) - Integer.parseInt(testing[count]));
                }

                ED[i][0] = Math.sqrt(ED[i][0]);
            }

            sortingED(ED);
            return splitSample(ED, samples, K, true);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int classification(List<Classification> samples, String[] data, int K) {
        try {
            /*  index 0 : nilai euclidean distance
                index 1 : index dari samples
                index 2 : id dari kelas
             */
            double[][] ED = new double[samples.size()][3];
            for (int i = 0; i < samples.size(); i++) {
                Classification sample = samples.get(i);
                ED[i][1] = i;
                ED[i][2] = sample.getId();

                for (int count = 0; count < data.length; count++) {
                    ED[i][0] += (Integer.parseInt(sample.getBiner()[count]) - Integer.parseInt(data[count])) * (Integer.parseInt(sample.getBiner()[count]) - Integer.parseInt(data[count]));
                }

                ED[i][0] = Math.sqrt(ED[i][0]);
            }

            sortingED(ED);
            return majority(splitSample(ED, samples, K, true));
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void sortingED(double[][] ED) {
        int sample = ED.length - 2;
        double[] tempData;

        for (int p = 0; p <= sample; p++) {
            for (int q = p; q <= sample + 1; q++) {
                if (ED[p][0] > ED[q][0]) {
                    tempData = ED[p];
                    ED[p] = ED[q];
                    ED[q] = tempData;
                }
            }
        }

        System.out.println("------------------------ KNN (SORTING ED) ---------------------------");
        for (double[] aED : ED) {
            System.out.println("Id : " + aED[2] + "\tDistance : " + aED[0]);
        }
        System.out.println("---------------------------------------------------------------------\n");
    }

    private List<Classification> splitSample(double[][] ED, List<Classification> samples, int K, boolean print) {
        List<Classification> result = new ArrayList<>();

        for (int i = 0; i < K; i++) {
            int id = (int) ED[i][2];
            String[] biner = samples.get((int) ED[i][1]).getBiner();
            result.add(new Classification().setId(id).setBiner(biner).setDistance(ED[i][0]));
        }

        if (print) {
            System.out.println("------------------- KNN (SPLIT WITH K) -----------------------");
            for (Classification model : result) {
                System.out.println("ID : " + model.getId() + "\tDistance : " + model.getDistance());
            }
            System.out.println("--------------------------------------------------------------\n");
        }

        return result;
    }

    private int majority(List<Classification> datas) {
        int result = -1;
        int count = 0;
        Map<Integer, Integer> counting = new LinkedHashMap<>();

        for (Classification model : datas) {
            if (!counting.containsKey(model.getId())) {
                counting.put(model.getId(), 1);
            } else {
                counting.put(model.getId(), counting.get(model.getId()) + 1);
            }
        }

        System.out.println("-----------------------------KNN (MAJORITY) --------------------------");
        for (Map.Entry<Integer, Integer> entry : counting.entrySet()) {
            if (count < entry.getValue()) {
                count = entry.getValue();
                result = entry.getKey();
            }

            System.out.println("ID : " + entry.getKey() + "\tVal : " + entry.getValue() + "\tCount : " + count + "\tResult : " + result);
        }
        System.out.println("----------------------------------------------------------------------\n");

        return result;
    }
}
