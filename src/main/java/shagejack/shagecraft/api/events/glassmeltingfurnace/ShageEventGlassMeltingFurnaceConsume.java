package shagejack.shagecraft.api.events.glassmeltingfurnace;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ShageEventGlassMeltingFurnaceConsume extends Event {

    public final Entity entity;
    public final BlockPos pos;

    public ShageEventGlassMeltingFurnaceConsume(Entity entity, BlockPos pos) {
        this.entity = entity;
        this.pos = pos;
    }

    public static class Pre extends ShageEventGlassMeltingFurnaceConsume {
        public Pre(Entity entity, BlockPos pos) {
            super(entity, pos);
        }

        @Override
        public boolean isCancelable() {
            return true;
        }
    }

    public static class Post extends ShageEventGlassMeltingFurnaceConsume {
        public Post(Entity entity, BlockPos pos) {
            super(entity, pos);
        }
    }
}

