package algoritm;

import model.ClassificationModel;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author AKBAR <akbar.attijani@gmail.com>
 */
public class KNearestNeighbor {
    public List<ClassificationModel> classification(List<ClassificationModel> samples, String data, int K) {
        try {
            /*  index 0 : nilai euclidean distance
                index 1 : index dari samples
                index 2 : id dari kelas
             */
            double[][] ED = new double[samples.size()][3];
            String[] testing = data.split(" ");

            for (int i = 0; i < samples.size(); i++) {
                ClassificationModel sample = samples.get(i);
                ED[i][1] = i;
                ED[i][2] = sample.getId();

                for (int count = 0; count < testing.length; count++) {
                    ED[i][0] += (Integer.parseInt(testing[count]) - Integer.parseInt(sample.getBiner()[count])) * Integer.parseInt(testing[count]) - Integer.parseInt(sample.getBiner()[count]);
                }

                ED[i][0] = Math.sqrt(ED[i][0]);
            }

            sortingED(ED);
            return splitSample(ED, samples, K);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void sortingED(double[][] ED) {
        int sample = ED.length - 2;
//        double temp, temp1, temp2;
        double[] tempData;

        for (int p = 0; p <= sample; p++) {
            for (int q = p; q <= sample + 1; q++) {
                if (ED[p][0] > ED[q][0]) {
                    tempData = ED[p];
                    ED[p] = ED[q];
                    ED[q] = tempData;

//                    temp = ED[p][0];
//                    ED[p][0] = ED[q][0];
//                    ED[q][0] = temp;
//
//                    temp1 = ED[p][1];
//                    ED[p][1] = ED[q][1];
//                    ED[q][1] = temp1;
//
//                    temp2 = ED[p][2];
//                    ED[p][2] = ED[q][2];
//                    ED[q][2] = temp2;
                }
            }
        }
    }

    private List<ClassificationModel> splitSample(double[][] ED, List<ClassificationModel> samples, int K) {
        List<ClassificationModel> result = new ArrayList<>();

        for (int i = 0; i < K; i++) {
            int id = (int) ED[i][2];
            String[] biner = samples.get((int) ED[i][1]).getBiner();
            result.add(new ClassificationModel().setId(id).setBiner(biner));
        }

        return result;
    }
}
