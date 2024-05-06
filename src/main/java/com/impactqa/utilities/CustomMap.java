package com.impactqa.utilities;
import com.impactqa.exceptions.CustomRunTimeException;
import java.util.LinkedHashMap;
import java.util.Map;
public class CustomMap {

    static public Map of(String... keyvalues){
        if (keyvalues.length % 2 == 0) {
            Map<String, String> map = new LinkedHashMap<>();
            for (int i = 0; i < keyvalues.length; i++) {
                map.put(keyvalues[i], keyvalues[i+1]);
                i++;
            }
            return map;
        }
        else
        {
            throw new CustomRunTimeException("number of paramters should be even like k1, v1, k2, v2.....");
        }
    }

}
