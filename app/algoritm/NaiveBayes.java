package algoritm;

import model.ClassificationModel;

import java.math.BigDecimal;
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
        Map<Integer, BigDecimal> classStore = probCi(samples);
        Map<Integer, ArrayList<BigDecimal>> attributeStore = probXCi(samples, data);
        Map<Integer, BigDecimal> attributeProb = multiplyProb(attributeStore);
        Map<Integer, BigDecimal> probability = probXCiMultiplyCi(classStore, attributeProb);

        System.out.println("------------------ Naive Bayes (PROB CLASS) ----------------------");
        for (Map.Entry<Integer, BigDecimal> entry : classStore.entrySet()) {
            System.out.println("ID : " + entry.getKey() + "\tProb : " + entry.getValue());
        }
        System.out.println("------------------------------------------------------------------\n");

//        System.out.println("--------------- Naive Bayes (STORE ATTRIBUTE) --------------------");
//        for (Map.Entry<Integer, ArrayList<BigDecimal>> entry : attributeStore.entrySet()) {
//            System.out.print("ID : " + entry.getKey() + "\tData : ");
//            for (BigDecimal val : entry.getValue()) {
//                System.out.print(val + "\t");
//            }
//
//            System.out.println();
//        }
//        System.out.println("------------------------------------------------------------------\n");

        System.out.println("---------------- Naive Bayes (PROB ATTRIBUTE) --------------------");
        for (Map.Entry<Integer, BigDecimal> entry : attributeProb.entrySet()) {
            System.out.println("ID : " + entry.getKey() + "\tProb : " + entry.getValue());
        }
        System.out.println("------------------------------------------------------------------\n");

        System.out.println("----------------- Naive Bayes (PROBABILITY) ----------------------");
        for (Map.Entry<Integer, BigDecimal> entry : probability.entrySet()) {
            System.out.println("ID : " + entry.getKey() + "\tProb : " + entry.getValue());
        }
        System.out.println("------------------------------------------------------------------\n");

//        return result;
    }

    private Map<Integer, BigDecimal> probXCiMultiplyCi(Map<Integer, BigDecimal> probClass, Map<Integer, BigDecimal> probAttribute) {
        Map<Integer, BigDecimal> result = new LinkedHashMap<>();

        for (Map.Entry<Integer, BigDecimal> entry : probClass.entrySet()) {
            BigDecimal count = probAttribute.get(entry.getKey()).divide(entry.getValue());
            result.put(entry.getKey(), count);
        }

        return result;
    }

    private Map<Integer, BigDecimal> multiplyProb(Map<Integer, ArrayList<BigDecimal>> attribute) {
        Map<Integer, BigDecimal> result = new LinkedHashMap<>();

        for (Map.Entry<Integer, ArrayList<BigDecimal>> entry : attribute.entrySet()) {
            BigDecimal count = new BigDecimal(0.0);

            for (BigDecimal value : entry.getValue()) {
                count = count.multiply(value);
            }

            result.put(entry.getKey(), count);
        }

        return result;
    }

    private Map<Integer, ArrayList<BigDecimal>> probXCi(List<ClassificationModel> samples, String[] data) {
        Map<Integer, ArrayList<BigDecimal>> result = new LinkedHashMap<>();
        Map<Integer, Integer> probClass = new LinkedHashMap<>();

        for (ClassificationModel model : samples) {
            if (!probClass.containsKey(model.getId())) {
                probClass.put(model.getId(), 1);
                result.put(model.getId(), new ArrayList<>());
            } else {
                probClass.put(model.getId(), probClass.get(model.getId()) + 1);
            }
        }

        for (String value : data) {
            int index = 0;

            for (Map.Entry<Integer, Integer> entry : probClass.entrySet()) {
                BigDecimal count = new BigDecimal(0.0);

                for(ClassificationModel model : samples) {
                    if (value.equals(model.getBiner()[index]) && entry.getKey() == model.getId()) {
                        count = count.add(new BigDecimal(1));
                    }
                }

                ArrayList<BigDecimal> newData = result.get(entry.getKey());

                if (count.compareTo(new BigDecimal(0.0)) == 0) {
                    newData.add(new BigDecimal(1.0));
                } else {
                    newData.add(count.divide(new BigDecimal(entry.getValue())));
                }

                result.put(entry.getKey(), newData);
                index++;
            }
        }

        return result;
    }

    private Map<Integer, BigDecimal> probCi(List<ClassificationModel> samples) {
        Map<Integer, BigDecimal> result = new LinkedHashMap<>();

        for (ClassificationModel model : samples) {
            if (!result.containsKey(model.getId())) {
                result.put(model.getId(), new BigDecimal(1));
            } else {
                result.put(model.getId(), result.get(model.getId()).add(new BigDecimal(1)));
            }
        }

        for (Map.Entry<Integer, BigDecimal> entry : result.entrySet()) {
            entry.setValue(entry.getValue().divide(new BigDecimal(samples.size())));
        }

        return result;
    }
}
