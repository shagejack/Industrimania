package shagejack.industrimania.content.world.nature.mulberry.silkworm;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import shagejack.industrimania.registers.item.AllItems;

public class SilkwormEggs extends Item {
    public SilkwormEggs(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        int count = 16 + level.getRandom().nextInt(17);

        for(int i = 0; i < count; i++) {
            ItemStack egg = new ItemStack(AllItems.silkworm.get());
            Silkworm.init(egg, level.getRandom());
            Silkworm.setAge(egg, 0);
            player.getInventory().placeItemBackInInventory(egg);
        }

        return super.use(level, player, hand);
    }
}
