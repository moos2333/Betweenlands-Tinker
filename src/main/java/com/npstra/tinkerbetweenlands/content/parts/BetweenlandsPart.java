package com.npstra.tinkerbetweenlands.content.parts;

import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tools.ToolPart;

public class BetweenlandsPart extends ToolPart {
    private final String statType;

    public BetweenlandsPart(int cost, String statType) {
        super(cost);
        this.statType = statType;
    }

    @Override
    public boolean canUseMaterial(Material mat) {
        return mat.hasStats(statType);
    }

    @Override
    public boolean hasUseForStat(String stat) {
        return statType.equals(stat);
    }

    @Override
    public boolean canBeCrafted() {
        return false;
    }

    @Override
    public boolean canBeCasted() {
        return false;
    }
}