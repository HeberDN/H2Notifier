package com.heber.hh.H2Notifier.utilities;

import java.util.Map;

public class TemplateProcessor {
    public static String process (String template, Map<String, String> values){
        String result = template;
        for (Map.Entry<String, String> entry : values.entrySet()){
            result = result.replace("{" + entry.getKey()+"}", entry.getValue());
        }
        return result;
    }
}
