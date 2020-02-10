package algoritm;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author AKBAR <akbar.attijani@gmail.com>
 */
public class KMeans {
    String[][] dataset = {
            {"1562201001",	"RANA KHANSA",		"S1 Akuntansi",	"AKTIF",	"Peserta didik baru",	"P",	"Islam"},
            {"1562201002",	"ITA MULYANA",		"S1 Akuntansi",	"AKTIF",	"Peserta didik baru",	"P",	"Tidak diisi"},
            {"1562201003",	"UMUL KHABIBAH",	"S1 Akuntansi",	"AKTIF",	"Peserta didik baru",	"P",	"Kristen"},
            {"1562201004",	"HERI SETIAWAN",	"S1 Akuntansi",	"AKTIF",	"Peserta didik baru",	"L",	"Tidak diisi"},
            {"1574201001",	"ABDUL SYUKUR YAKUB",	"S1 Ilmu Hukum",	"Lulus",			"Peserta didik baru",	"L",	"Islam"},
            {"1574201002",	"ARDI SUTRO	S1 Ilmu", 	"S1 Ilmu Hukum",			"Lulus",			"Peserta didik baru",	"L",	"Islam"},
            {"1574201003",	"TEGUH IMAM SAPUTRA",	"S1 Ilmu Hukum",	"Putus Sekolah",	"Peserta didik baru",	"L",	"Islam"},
            {"1574201006",	"WILLY OCRIYANSYAH",	"S1 Ilmu Hukum",	"Lulus",			"Pindahan",				"L",	"Tidak diisi"},
            {"1570201002",	"MUHAMAD HANIF AL JANNAH",	"S1 Ilmu Komunikasi",	"AKTIF",	"Peserta didik baru",	"L",	"Tidak diisi"},
            {"1570201003",	"ABI RIDWAN MAULANA",		"S1 Ilmu Komunikasi",	"AKTIF",	"Peserta didik baru",	"L",	"Islam"},
            {"1570201004",	"INSYIAH RAHMAWATI",		"S1 Ilmu Komunikasi",	"Lulus",	"Peserta didik baru",	"P",	"Tidak diisi"}

    };

    public void test() {
        Map<String, Double> attribute0 = new LinkedHashMap<>();
        Map<String, Double> attribute1 = new LinkedHashMap<>();
        Map<String, Double> attribute2 = new LinkedHashMap<>();
        Map<String, Double> attribute3 = new LinkedHashMap<>();
        Map<String, Double> attribute4 = new LinkedHashMap<>();

        double initBobot0 = 1;
        double initBobot1 = 1;
        double initBobot2 = 1;
        double initBobot3 = 1;
        double initBobot4 = 1;

        for (String[] data : dataset) {
            if (!attribute0.containsKey(data[2])) {
                attribute0.put(data[2], initBobot0);
                initBobot0++;
            }

            if (!attribute1.containsKey(data[3])) {
                attribute1.put(data[3], initBobot1);
                initBobot1++;
            }

            if (!attribute2.containsKey(data[4])) {
                attribute2.put(data[4], initBobot2);
                initBobot2++;
            }

            if (!attribute3.containsKey(data[5])) {
                attribute3.put(data[5], initBobot3);
                initBobot3++;
            }

            if (!attribute4.containsKey(data[6])) {
                attribute4.put(data[6], initBobot4);
                initBobot4++;
            }
        }

        double[][] dataSample = new double[dataset.length][6];
        for (int i = 0; i < dataset.length; i++) {
            dataSample[i][0] = attribute0.get(dataset[i][2]);
            dataSample[i][1] = attribute1.get(dataset[i][3]);
            dataSample[i][2] = attribute2.get(dataset[i][4]);
            dataSample[i][3] = attribute3.get(dataset[i][5]);
            dataSample[i][4] = attribute4.get(dataset[i][6]);
        }

        clustering(dataSample, new String[] {"Cluster 1", "Cluster 2", "Cluster 3"});
    }

    public String[][] clustering(double[][] data, String[] cluster) {
        String[][] result = new String[data.length][data[0].length + 1];

        // Get inisialisation cluster
        ArrayList<Integer> checkRandom = new ArrayList<>();
        double[][] clusterMap = new double[cluster.length][data[0].length];
        Random rand = new Random();
        for (int c = 0; c < cluster.length; c++) {
            int random = rand.nextInt(data.length);
            if (!checkRandom.contains(random)) {
                clusterMap[c] = data[random];
                checkRandom.add(random);
            }
        }

        System.out.println("================ CLUSTER CENTER 1 ==================================");
        for (double[] c : clusterMap) {
            for (double v : c) {
                System.out.print(v + "\t");
            }

            System.out.println();
        }
        System.out.println("====================================================================");

        // Count Distance
        String[][] distanceDataFirst = new String[data.length][cluster.length + 1];
        String[][] distanceDataLast = new String[data.length][cluster.length + 1];

        boolean isPass = false;
        createDistance(data, distanceDataFirst, clusterMap);

        System.out.println("======================= LOOP 1 =====================================");
        for (String[] row : distanceDataFirst) {
            for (String value : row) {
                System.out.print(value + "\t");
            }

            System.out.println();
        }
        System.out.println("=====================================================================");

        updateClusterCenter(cluster, data, clusterMap);

        System.out.println("================ CLUSTER CENTER 2 ==================================");
        for (double[] c : clusterMap) {
            for (double v : c) {
                System.out.print(v + "\t");
            }

            System.out.println();
        }
        System.out.println("====================================================================");

        createDistance(data, distanceDataLast, clusterMap);

        System.out.println("======================= LOOP 2 =====================================");
        for (String[] row : distanceDataLast) {
            for (String value : row) {
                System.out.print(value + "\t");
            }

            System.out.println();
        }
        System.out.println("=====================================================================");

        for (int i = 0; i < distanceDataFirst.length - 1; i++) {
            if (!distanceDataFirst[i][distanceDataFirst[i].length - 1].equals(distanceDataLast[i][distanceDataLast[i].length - 1])) {
                int loop = 3;
                while (!isPass) {
                    updateClusterCenter(cluster, data, clusterMap);
                    System.out.println("================ CLUSTER CENTER " + loop + " ==================================");
                    for (double[] c : clusterMap) {
                        for (double v : c) {
                            System.out.print(v + "\t");
                        }

                        System.out.println();
                    }
                    System.out.println("=====================================================================");

                    distanceDataFirst = distanceDataLast;

                    createDistance(data, distanceDataLast, clusterMap);

                    System.out.println("======================= COUNTER DATA ================================");
                    for (String[] row : distanceDataFirst) {
                        for (String value : row) {
                            System.out.print(value + "\t");
                        }

                        System.out.println();
                    }
                    System.out.println("=====================================================================");

                    System.out.println("======================= LOOP " + loop + " =====================================");
                    for (String[] row : distanceDataLast) {
                        for (String value : row) {
                            System.out.print(value + "\t");
                        }

                        System.out.println();
                    }
                    System.out.println("=====================================================================");

                    int countSame = 0;
                    for (int ii = 0; ii < distanceDataFirst.length - 1; ii++) {
                        if (distanceDataFirst[ii][distanceDataFirst[ii].length - 1].equals(distanceDataLast[ii][distanceDataLast[ii].length - 1])) {
                            countSame++;
                        }
                    }

                    if (countSame == distanceDataLast.length) {
                        isPass = true;
                    } else {
                        loop++;
                    }
                }
            }
        }

        return result;
    }

    private void updateClusterCenter(String[] cluster, double[][] data, double[][] clusterMap) {
        for (int i = 0; i < cluster.length; i++) {
            String updateCluster = "";
            for (int j = 0; j < data.length; j++) {
                int value = (int) data[j][data[j].length - 1];
                if (value == i) {
                    updateCluster += String.valueOf(j);
                }
            }

            double count = 0;
            String[] indexs = updateCluster.split("");
            for (int index = 0; index < indexs.length; index++) {
                double[] rowData = data[Integer.parseInt(indexs[index])];

                if (index == 0) {
                    clusterMap[i] = rowData;
                } else {
                    for (int r = 0; r < rowData.length; r++) {
                        clusterMap[i][r] += rowData[r];
                    }
                }
            }

            for (int d = 0; d < clusterMap[i].length; d++) {
                clusterMap[i][d] = clusterMap[i][d] / indexs.length;
            }
        }
    }

    private void createDistance(double[][] dataset, String[][] distanceData, double[][] clusterMap) {
        for (int c = 0; c < clusterMap.length; c++) {
            for (int i = 0; i < dataset.length; i++) {
                double distance = 0;

                for (int d = 0; d < dataset[i].length; d++) {
                    distance += (dataset[i][d] - clusterMap[c][d]) * (dataset[i][d] - clusterMap[c][d]);
                }

                distance = Math.sqrt(distance);
                distanceData[i][c] = String.valueOf(distance);
            }
        }

        for (int i = 0; i < distanceData.length; i++) {
            double lowerDistance = Double.parseDouble(distanceData[i][0]);
            int indexCluster = 0;

            for (int j = 1; j < distanceData[i].length - 1; j++) {
                double distance = Double.parseDouble(distanceData[i][j]);
                if (distance < lowerDistance) {
                    lowerDistance = distance;
                    indexCluster = j;
                }
            }

            System.out.println("lower : " + lowerDistance + "\tindex : " + indexCluster);

            distanceData[i][distanceData[i].length - 1] = String.valueOf(indexCluster);
            dataset[i][dataset[i].length - 1] = indexCluster;
        }
    }
}
