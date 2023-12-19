package org.example.telegram;

import java.util.HashMap;
import java.util.Map;

public class SelectedOptions {

    public static Map<String, String> banks = new HashMap<>();
    public static String precision = "2";


    public static void setDefault() {
        banks.put("mono", "✅");
        banks.put("pryvat", "");
        banks.put("nbu", "");
    }

    public static void setSelectedBank(String key) {
        banks.replaceAll((k, v) -> "");
        banks.replace(key, "✅");
    }


    public static String isSelectedPrecision(String key) {
        if (precision.equals(key))
            return "✅";
        return "";
    }

    public static String isBankSelected(String key) {
        return banks.get(key);
    }

}
