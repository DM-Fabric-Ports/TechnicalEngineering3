package ten3.init.template;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.lwjgl.glfw.GLFW;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;
import ten3.init.tab.DefGroup;
import ten3.util.TranslateKeyUtil;

import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DefItem extends Item {

    public DefItem() {

        this(DefGroup.ITEM);

    }

    public DefItem(int stack) {

        super(new QuiltItemSettings().stacksTo(stack));

    }

    public DefItem(CreativeModeTab g) {
		super(new Properties());

    }

    public DefItem(boolean noGroup) {

        super(new Properties());

    }

    @Override
    public Component getName(ItemStack p_41458_)
    {
        return TranslateKeyUtil.getKey(BuiltInRegistries.ITEM.getKey(this).getPath());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level p_41422_, List<Component> tooltip, TooltipFlag p_41424_)
    {
        List<Component> list = new ArrayList<>();

        for(int i = 0; true; i++) {
            //*getPATH!
            String k = "ten3."+ BuiltInRegistries.ITEM.getKey(this).getPath() +"."+i;
            Component ttc = TranslateKeyUtil.translated(TranslateKeyUtil.GOLD, k);
            if(ttc.getString().equals(k)) break;

            list.add(ttc);
        }

        if(shift()) {
            tooltip.addAll(list);
        } else if(list.size() > 0) {
            tooltip.add(TranslateKeyUtil.translated(TranslateKeyUtil.GOLD, "ten3.shift"));
        }
    }

    public boolean shift() {
        return GLFW.glfwGetKey(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS;
    }

}
