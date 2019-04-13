package enums;

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */
public enum HeaderConstants {

    COMPUTER_VISION("Computer Vision"),
    DATA_MINING("Data Mining"),
    ALGORITM_KNN("KNN"),
    ALGORITM_NEURAL_NETWORK("Neural Network"),
    ALGORITM_NAIVE_BAYES("Naive Bayes"),
    RESULT_VALID("Valid"),
    RESULT_NOTVALID("Not Valid");

    private final String responseCode;

    HeaderConstants(final String response) {
        responseCode = response;
    }

    @Override
    public String toString() {
        return responseCode;
    }
}
