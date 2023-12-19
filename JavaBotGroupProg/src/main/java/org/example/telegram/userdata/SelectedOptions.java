package org.example.telegram.userdata;

import java.util.HashMap;
import java.util.Map;

public class SelectedOptions {

    public  Map<String, String> banks = new HashMap<>();
    public  String precision = "2";

    public SelectedOptions() {
        setDefault();
    }

    public  void setDefault() {
        banks.put("mono", "✅");
        banks.put("pryvat", "");
        banks.put("nbu", "");
    }

    public  void setSelectedBank(String key) {
        banks.replaceAll((k, v) -> "");
        banks.replace(key, "✅");
    }

    public  String isBankSelected(String key) {
        return banks.get(key);
    }

    public  String isSelectedPrecision(String key) {
        if (precision.equals(key))
            return "✅";
        return "";
    }


    public void setPrecision(String precision) {
        this.precision = precision;
    }
}
