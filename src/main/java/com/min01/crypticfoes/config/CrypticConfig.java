package com.min01.crypticfoes.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CrypticConfig 
{
	public static final ForgeConfigSpec CONFIG_SPEC = build();
	
	public static ForgeConfigSpec.BooleanValue cameraShakes;
	public static ForgeConfigSpec.BooleanValue enableFarmersDelightCompat;
	
    public static ForgeConfigSpec build() 
    {
    	ForgeConfigSpec.Builder config = new ForgeConfigSpec.Builder();
    	
    	config.push("Client Settings");
    	cameraShakes = config.comment("whether camera shaking effects should be enabled in various situations.").define("cameraShakes", true);
        config.pop();
        
        config.push("Common Settings");
        enableFarmersDelightCompat = config.comment("whether enable farmer's delight compat for some foods.").define("enableFarmersDelightCompat", true);
        config.pop();
        
        return config.build();
    }
}
