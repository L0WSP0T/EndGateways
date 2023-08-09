package com.craftix.portals;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;


@Mod("portals")
public class PlusFix
{
    public PlusFix() {
        MinecraftForge.EVENT_BUS.register(this);
    }

}
