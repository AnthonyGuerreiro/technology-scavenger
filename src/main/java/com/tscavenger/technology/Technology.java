package com.tscavenger.technology;

import java.util.HashSet;
import java.util.Set;

public class Technology {
    private static Set<String> list = new HashSet<String>();

    public static Set<String> getList() {
        if (list.isEmpty()) {
            init();
        }
        return list;
    }

    private static void init() {
        list.add("Shopify");
    }
}
