package algoritm;

import model.ClassificationModel;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author AKBAR <akbar.attijani@gmail.com>
 */
public class Naive {
    private static int[][] x;
    private static int[] dataUji;
    private static String[]  Kelas;
    private static String[][] prob_Kelas;
    private static Map<Integer, Double> result;

    public static void initObject(int size, List<ClassificationModel> samples) {
        dataUji = new int[size];
        result = new LinkedHashMap<>();
        for (ClassificationModel model : samples) {
            if (!result.containsKey(model.getId())) {
                result.put(model.getId(), 1.0);
            } else {
                result.put(model.getId(), result.get(model.getId()) + 1);
            }
        }

        System.out.println("------------------ Naive Bayes (PROB CLASS) ----------------------");
        for (Map.Entry<Integer, Double> entry : result.entrySet()) {
            System.out.println("ID : " + entry.getKey() + "\tProb : " + entry.getValue());
        }
        System.out.println("------------------------------------------------------------------\n");
    }

    public static String Naive_Bayes(int[] data_uji) {
        String hasil = "";

        try {
            System.arraycopy(data_uji, 0, dataUji, 0, dataUji.length);

            double[][] prob_uji = probabilitasVektor();
            double[] jum_prob = count_Probabilitas(prob_uji);
            jum_prob = Normalisasi_Probabilitas(jum_prob);
            hasil = getChar(jum_prob);

            System.out.println("Hasilnya : + " + hasil);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return hasil;
    }

    private static String getChar(double[] pro) {
        String karakternya = "";
        double max = 0.0;

        for(int ma=0; ma<pro.length; ma++) {
            if(pro[ma] > max)
            {
                max = pro[ma];
                karakternya = prob_Kelas[ma][0];
            }
        }

        return karakternya;
    }

    //Tahap 4 - Normalisasi Nilai Probabilitas yang telah didapat dari proses Tahap 3 - Naive Bayes
    private static double[] Normalisasi_Probabilitas(double[] probab) {
        double jumlah;
        for (int yj = 0; yj < result.size(); yj++) {
            jumlah = 0.0;

            for (int xj = 0; xj < result.size(); xj++) {
                jumlah += probab[xj];
            }

            probab[yj] = probab[yj]/jumlah;
//            System.out.println(probab[yj]+"\t"+prob_Kelas[yj][0]);
        }

        return probab;
    }

    //Tahap 3 - Mengalikan Probabilitas setiap Vektor dengan Proabilitas Kelasnya yang sama - Naive Bayes
    private static double[] count_Probabilitas(double[][] probabilitas) {
        double[] sum_prob = new double[result.size()];

        System.out.println("------------------ Naive Bayes (count_Probability) ----------------------");
        for (int yi = 0; yi < result.size(); yi++) {
            sum_prob[yi] = probabilitas[yi][0];

            for (int xi = 1; xi < dataUji.length; xi++) {
                sum_prob[yi] *= probabilitas[yi][xi];
            }

            sum_prob[yi] *= Double.parseDouble(prob_Kelas[yi][2]);

            System.out.println(sum_prob[yi]);
        }
        System.out.println("-------------------------------------------------------------------------\n");

        return sum_prob;
    }

    //Tahap 2 - Menghitung Probabilitas setiap Vektor dalam Kelas yang sama - Naive Bayes
    private static double[][] probabilitasVektor() {
        int jml;
        double[][] prob_vek = new double[result.size()][dataUji.length];

        for (int klas = 0; klas < result.size(); klas++) {
            for (int vek = 0; vek < dataUji.length; vek++) {
                jml = 0;

                for (int dt = 0; dt < Kelas.length; dt++) {
                    if(dataUji[vek] == x[dt][vek] && Kelas[dt].equals(prob_Kelas[klas][0])) {
                        jml++;
                    }
                }

                prob_vek[klas][vek] = jml;
                prob_vek[klas][vek] = (1 + prob_vek[klas][vek]) / (Double.parseDouble(prob_Kelas[klas][1]) + 2);
            }
        }

//        System.out.println("---------------- Naive Bayes (PROB ATTRIBUTE) --------------------");
//        for (double[] row : prob_vek) {
//            for (double val : row) {
//                System.out.print(val + "\t");
//            }
//            System.out.println();
//        }
//        System.out.println("------------------------------------------------------------------\n");

        return prob_vek;
    }

    public static void inisialisasi(List<ClassificationModel> data) {
        try {
            x = new int[data.size()][data.get(0).getBiner().length];
            Kelas = new String[data.size()];
            prob_Kelas = new String[result.size()][3];
            int kelasnya = 0;

            for (int i = 0; i < data.size(); i++) {
                for (int j = 0; j < data.get(i).getBiner().length; j++) {
                    x[i][j] = Integer.parseInt(data.get(i).getBiner()[j]);
                }

                Kelas[i] = String.valueOf(data.get(i).getId());
                kelasnya++;
            }
        } catch (Exception ignored) {}

        prob_Kelas = probabilitasClass(Kelas, data);

        System.out.println("------------------ Naive Bayes (prob_Kelas) ----------------------");
        for (String[] row : prob_Kelas) {
            for (String val : row) {
                System.out.print(val + "\t");
            }

            System.out.println();
        }
        System.out.println("------------------------------------------------------------------\n");
    }

    //Tahap 1 - Menghitung Probabilitas setiap Kelas - Naive Bayes
    private static String[][] probabilitasClass(String[] kelas_in, List<ClassificationModel> samples) {
        /*
            kelas_baru
                [0] = karakter / kelas
                [1] = jml karakter per kelas
                [2] = nilai probabilitas per kelas
        */
        int jml_kelasnya;
        int idx = 0;
        String[][] kelas_baru = new String[result.size()][3];

        for (Map.Entry<Integer, Double> entry : result.entrySet()) {
            jml_kelasnya = 0;

            for (String aKelas_in : kelas_in) {
                if (String.valueOf(entry.getKey()).equals(aKelas_in)) {
                    jml_kelasnya++;
                }
            }

            if(jml_kelasnya > 0) {
                kelas_baru[idx][0] = String.valueOf(entry.getKey());
                kelas_baru[idx][1] = String.valueOf(jml_kelasnya);
                kelas_baru[idx][2] = String.valueOf(Double.parseDouble(kelas_baru[idx][1]) / Kelas.length);
            }

            idx++;
//            System.out.println(kelas_baru[kl][0]+"\t"+kelas_baru[kl][1]+"\t"+kelas_baru[kl][2]);
        }

        return kelas_baru;
    }
}
