package com.functionia.model;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Bitmine {

    private final Random random = new Random();

    private String type;
    private Double weight;

    public String getType() {
        if (null == type){
            type = getRandomType();
        }
        return type;
    }

    public Double getWeight() {
        if (null == weight){
            weight = random.nextDouble();
        }
        return weight;
    }

    private static final List<String> VALUES = Arrays.asList(
        "Adamantium",
        "Aether",
        "Carbonadium",
        "Uru",
        "Vibranium",
        "Transformium",
        "Thiotimoline",
        "Kryptonite",
        "Katchin",
        "Energon"
    );

    public static String getRandomType() {
        Random random = new Random();
        int index = random.nextInt(VALUES.size());
        return VALUES.get(index);
    }
}
