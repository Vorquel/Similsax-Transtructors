package vorquel.mod.similsaxtranstructors;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

class Config {

    public static int basicUses;
    public static int advancedUses;
    public static int basicRange;
    public static int advancedRange;
    public static boolean showOverlay;

    public static void init(File file) {
        Configuration config = new Configuration(file);
        config.load();
        basicUses = config.getInt("basicUses", "general", 200, 1, Short.MAX_VALUE, "How many times you can use the basic transtructor");
        advancedUses = config.getInt("advancedUses", "general", 1000, 1, Short.MAX_VALUE, "How many times you can use the advanced transtructor");
        basicRange = config.getInt("basicRange", "general", 16, 2, 128, "How far you can use the basic transtructor");
        advancedRange = config.getInt("advancedRange", "general", 64, 2, 128, "How far you can use the advanced transtructor");
        showOverlay = config.getBoolean("showOverlay", "general", true, "Should there be an overlay to show transtructor function?");
        if(config.hasChanged()) config.save();
    }
}
