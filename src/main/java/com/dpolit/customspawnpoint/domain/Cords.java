package com.dpolit.customspawnpoint.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Cords {
    private double x;
    private double y;
    private double z;
    private SpawnEvent type;
}
