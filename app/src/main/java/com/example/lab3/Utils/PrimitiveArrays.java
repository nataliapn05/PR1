package com.example.lab3.Utils;

import java.util.List;

public class PrimitiveArrays {
    public static boolean[] convertAsPrimitiveBoolArray(List<Boolean> list) {
        boolean[] boolArray = new boolean[list.size()];
        for (int i = 0; i < list.size(); i++) boolArray[i] = list.get(i);
        return boolArray;
    }
}
