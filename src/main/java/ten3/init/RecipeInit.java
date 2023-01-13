package ten3.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import ten3.TConst;
import ten3.core.recipe.*;

public class RecipeInit {
    public static void regAll() {
        regRcp(new SingleSerial<>(SingleRecipe::new, "pulverizer"));
        regRcp(new SingleSerial<>(SingleRecipe::new, "compressor"));
        regRcp(new MTSSerial<>(MTSRecipe::new, "psionicant", 2));
        regRcp(new MTSSerial<>(MTSRecipe::new, "induction_furnace", 3));
    }

    public static void regRcp(CmSerializer<?> s) {
		Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, s.id(), s);

        RecipeTypeCm<?> type = new RecipeTypeCm<>(s.id());
		Registry.register(BuiltInRegistries.RECIPE_TYPE, s.id() , type);
    }

    public static RecipeSerializer<?> getRcp(String id) {
        return BuiltInRegistries.RECIPE_SERIALIZER.get(TConst.asResource(id));
    }

    public static RecipeType<?> getRcpType(String id) {

        return BuiltInRegistries.RECIPE_TYPE.get(TConst.asResource(id));

    }

}
