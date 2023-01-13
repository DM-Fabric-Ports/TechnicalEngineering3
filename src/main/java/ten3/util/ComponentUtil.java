package ten3.util;

import net.minecraft.network.chat.Component;

public class ComponentUtil {

    public static Component join(double e, double me) {

        if(e >= 0) {
            if(e < 1000) {
                return (TranslateKeyUtil.make(TranslateKeyUtil.RED, e + " FE / " + String.format("%.1f", me / 1000) + " kFE"));
            }
            else {
                return (TranslateKeyUtil.make(TranslateKeyUtil.RED, String.format("%.1f", e / 1000) + " kFE / " + String.format("%.1f", me / 1000) + " kFE"));
            }
        }

        return TranslateKeyUtil.make("");

    }

}
