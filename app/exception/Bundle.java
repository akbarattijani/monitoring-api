package exception;

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */
public class Bundle {
    private String[] bundles = {
            "RC-00 : SUCCESS",
            "RC-01 : FAILED",
            "RC-02 : First Name kosong",
            "RC-03 : Last Name kosong",
            "RC-04 : User name kosong",
            "RC-05 : Password kosong"
    };

    public String[] getBundles(){
        return bundles;
    }

    public String getBundle(String errCode) {
        for (String value : bundles) {
            if (value.contains(errCode)) {
                return value;
            }
        }

        return "RC-UNKNOWN : Unknown Error Code";
    }
}
