package algoritm;

import model.ClassificationModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author AKBAR <akbar.attijani@gmail.com>
 */
public class NaiveBayes {
    public void classification(List<ClassificationModel> samples, String[] data) {
//        List<ClassificationModel> result = new ArrayList<>();
        Map<Integer, Double> classStore = probCi(samples);
        Map<Integer, ArrayList<Double>> attributeStore = probXCi(samples, data);
        Map<Integer, Double> attributeProb = multiplyProb(attributeStore);
        Map<Integer, Double> probability = probXCiMultiplyCi(classStore, attributeProb);

        System.out.println("------------------ Naive Bayes (PROB CLASS) ----------------------");
        for (Map.Entry<Integer, Double> entry : classStore.entrySet()) {
            System.out.println("ID : " + entry.getKey() + "\tProb : " + entry.getValue());
        }
        System.out.println("------------------------------------------------------------------\n");

//        System.out.println("--------------- Naive Bayes (STORE ATTRIBUTE) --------------------");
//        for (Map.Entry<Integer, ArrayList<Double>> entry : attributeStore.entrySet()) {
//            System.out.print("ID : " + entry.getKey() + "\tData : ");
//            for (Double val : entry.getValue()) {
//                System.out.print(val + "\t");
//            }
//
//            System.out.println();
//        }

        System.out.println("---------------- Naive Bayes (PROB ATTRIBUTE) --------------------");
        for (Map.Entry<Integer, Double> entry : attributeProb.entrySet()) {
            System.out.println("ID : " + entry.getKey() + "\tProb : " + entry.getValue());
        }
        System.out.println("------------------------------------------------------------------\n");

        System.out.println("----------------- Naive Bayes (PROBABILITY) ----------------------");
        for (Map.Entry<Integer, Double> entry : probability.entrySet()) {
            System.out.println("ID : " + entry.getKey() + "\tProb : " + entry.getValue());
        }
        System.out.println("------------------------------------------------------------------\n");

//        return result;
    }

    private Map<Integer, Double> probXCiMultiplyCi(Map<Integer, Double> probClass, Map<Integer, Double> probAttribute) {
        Map<Integer, Double> result = new LinkedHashMap<>();

        for (Map.Entry<Integer, Double> entry : probClass.entrySet()) {
            double count = probAttribute.get(entry.getKey()) / entry.getValue();
            result.put(entry.getKey(), count);
        }

        return result;
    }

    private Map<Integer, Double> multiplyProb(Map<Integer, ArrayList<Double>> attribute) {
        Map<Integer, Double> result = new LinkedHashMap<>();

        for (Map.Entry<Integer, ArrayList<Double>> entry : attribute.entrySet()) {
            double count = 0.0;

            for (Double value : entry.getValue()) {
                count *= value;
            }

            result.put(entry.getKey(), count);
        }

        return result;
    }

    private Map<Integer, ArrayList<Double>> probXCi(List<ClassificationModel> samples, String[] data) {
        Map<Integer, ArrayList<Double>> result = new LinkedHashMap<>();
        Map<Integer, Integer> probClass = new LinkedHashMap<>();

        for (ClassificationModel model : samples) {
            if (!probClass.containsKey(model.getId())) {
                probClass.put(model.getId(), 1);
                result.put(model.getId(), new ArrayList<>());
            } else {
                probClass.put(model.getId(), probClass.get(model.getId()) + 1);
            }
        }

        for (Map.Entry<Integer, Integer> entry : probClass.entrySet()) {
            System.out.println("ID : " + entry + "\tJumalah : " + entry.getValue());
        }

        for (String value : data) {
            int index = 0;

            for (Map.Entry<Integer, Integer> entry : probClass.entrySet()) {
                double count = 0.0;

                for(ClassificationModel model : samples) {
                    String[] biner = model.getBiner();
                    if (value.equals(biner[index]) && entry.getKey() == model.getId()) {
                        count++;
                    }
                }

                ArrayList<Double> newData = result.get(entry.getKey());

                if (count == 0.0) {
                    newData.add(1.0);
                } else {
                    newData.add(count / entry.getValue());
                }

                result.put(entry.getKey(), newData);
                index++;
            }
        }

        return result;
    }

    private Map<Integer, Double> probCi(List<ClassificationModel> samples) {
        Map<Integer, Double> result = new LinkedHashMap<>();

        for (ClassificationModel model : samples) {
            if (!result.containsKey(model.getId())) {
                result.put(model.getId(), 1.0);
            } else {
                result.put(model.getId(), result.get(model.getId()) + 1);
            }
        }

        for (Map.Entry<Integer, Double> entry : result.entrySet()) {
            entry.setValue(entry.getValue() / samples.size());
        }

        return result;
    }
}
