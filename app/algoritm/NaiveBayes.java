package algoritm;

import model.Classification;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author AKBAR <akbar.attijani@gmail.com>
 */
public class NaiveBayes {
    public int classification(List<Classification> samples, String[] data, boolean printTrace) {
        Map<Integer, BigDecimal> classStore = probCi(samples);
        Map<Integer, ArrayList<BigDecimal>> attributeStore = probXCi(samples, data);
        Map<Integer, BigDecimal> attributeProb = multiplyProb(attributeStore);
        Map<Integer, BigDecimal> probability = probXCiMultiplyCi(classStore, attributeProb);

        BigDecimal compare = new BigDecimal(999999999);
        int result = -1;
        for (Map.Entry<Integer, BigDecimal> entry : probability.entrySet()) {
            if (compare.compareTo(entry.getValue()) < 0) {
                compare = entry.getValue();
                result = entry.getKey();
            }
        }

        if (printTrace) {
            System.out.println("------------------ Naive Bayes (PROB CLASS) ----------------------");
            for (Map.Entry<Integer, BigDecimal> entry : classStore.entrySet()) {
                System.out.println("ID : " + entry.getKey() + "\tProb : " + entry.getValue().setScale(5, BigDecimal.ROUND_HALF_UP));
            }
            System.out.println("------------------------------------------------------------------\n");

//            System.out.println("--------------- Naive Bayes (STORE ATTRIBUTE) --------------------");
//            for (Map.Entry<Integer, ArrayList<BigDecimal>> entry : attributeStore.entrySet()) {
//                System.out.print("ID : " + entry.getKey() + "\tData : ");
//                for (BigDecimal val : entry.getValue()) {
//                    System.out.print(val + "\t");
//                }
//
//                System.out.println();
//            }
//            System.out.println("------------------------------------------------------------------\n");

            System.out.println("---------------- Naive Bayes (PROB ATTRIBUTE) --------------------");
            for (Map.Entry<Integer, BigDecimal> entry : attributeProb.entrySet()) {
                System.out.println("ID : " + entry.getKey() + "\tProb : " + entry.getValue().setScale(5, BigDecimal.ROUND_HALF_UP));
            }
            System.out.println("------------------------------------------------------------------\n");

            System.out.println("----------------- Naive Bayes (PROBABILITY) ----------------------");
            for (Map.Entry<Integer, BigDecimal> entry : probability.entrySet()) {
                System.out.println("ID : " + entry.getKey() + "\tProb : " + entry.getValue().setScale(5, BigDecimal.ROUND_HALF_UP));
            }
            System.out.println("------------------------------------------------------------------\n");
        }

        return result;
    }

    private Map<Integer, BigDecimal> probXCiMultiplyCi(Map<Integer, BigDecimal> probClass, Map<Integer, BigDecimal> probAttribute) {
        Map<Integer, BigDecimal> result = new LinkedHashMap<>();

        for (Map.Entry<Integer, BigDecimal> entry : probClass.entrySet()) {
            BigDecimal count = probAttribute.get(entry.getKey())
                    .multiply(entry.getValue())
                    .add(new BigDecimal(2.0))
                    .divide(new BigDecimal(1.0), 5, RoundingMode.HALF_UP);
            count = new BigDecimal(100).subtract(count);
            result.put(entry.getKey(), count);
        }

        return result;
    }

    private Map<Integer, BigDecimal> multiplyProb(Map<Integer, ArrayList<BigDecimal>> attribute) {
        Map<Integer, BigDecimal> result = new LinkedHashMap<>();

        for (Map.Entry<Integer, ArrayList<BigDecimal>> entry : attribute.entrySet()) {
            BigDecimal count = null;
            int i = 0;
            for (;i < entry.getValue().size(); i++) {
                if (entry.getValue().get(i).compareTo(BigDecimal.ZERO) != 0) {
                    count = entry.getValue().get(i);
                    break;
                }
            }

            for (int ii = i; ii < entry.getValue().size(); ii++) {
                if (entry.getValue().get(ii).compareTo(BigDecimal.ZERO) != 0) {
                    assert count != null;
                    count = count
                            .multiply(entry.getValue().get(ii))
                            .add(new BigDecimal(1.0)) //Menggunakan Laplace Correction untuk mencegah nilai probabilitas 0
                            .divide(new BigDecimal(1.0), 5, RoundingMode.HALF_UP); //Konversi nilai probabilitas dengan 5 angka dibelakang koma
                }
            }

            result.put(entry.getKey(), count);
        }

        return result;
    }

    private Map<Integer, ArrayList<BigDecimal>> probXCi(List<Classification> samples, String[] data) {
        Map<Integer, ArrayList<BigDecimal>> result = new LinkedHashMap<>();
        Map<Integer, Integer> probClass = new LinkedHashMap<>();

        for (Classification model : samples) {
            if (!probClass.containsKey(model.getId())) {
                probClass.put(model.getId(), 1);
                result.put(model.getId(), new ArrayList<>());
            } else {
                probClass.put(model.getId(), probClass.get(model.getId()) + 1);
            }
        }

        for (Map.Entry<Integer, Integer> entry : probClass.entrySet()) {
            ArrayList<BigDecimal> arrProbAttribute = new ArrayList<>();

            for (int i = 0; i < data.length; i++) {
                int count = 0;

                for (Classification model : samples) {
                    if (entry.getKey() == model.getId() && data[i].equals(model.getBiner()[i])) {
                        count++;
                    }
                }

                BigDecimal prob = new BigDecimal(count);
                prob = prob.divide(new BigDecimal(entry.getValue()), 5, RoundingMode.HALF_UP);
                arrProbAttribute.add(prob);
            }

            result.put(entry.getKey(), arrProbAttribute);
        }

        return result;
    }

    private Map<Integer, BigDecimal> probCi(List<Classification> samples) {
        Map<Integer, BigDecimal> result = new LinkedHashMap<>();

        for (Classification model : samples) {
            if (!result.containsKey(model.getId())) {
                result.put(model.getId(), new BigDecimal(1));
            } else {
                result.put(model.getId(), result.get(model.getId()).add(new BigDecimal(1)));
            }
        }

        for (Map.Entry<Integer, BigDecimal> entry : result.entrySet()) {
            entry.setValue(entry.getValue().divide(new BigDecimal(samples.size()), 5, RoundingMode.HALF_UP));
        }

        return result;
    }
}
