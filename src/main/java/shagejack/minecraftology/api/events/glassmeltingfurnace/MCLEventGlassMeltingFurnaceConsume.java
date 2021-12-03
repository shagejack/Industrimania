package shagejack.minecraftology.api.events.glassmeltingfurnace;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;

public class MCLEventGlassMeltingFurnaceConsume extends Event {

    public final Entity entity;
    public final BlockPos pos;

    public MCLEventGlassMeltingFurnaceConsume(Entity entity, BlockPos pos) {
        this.entity = entity;
        this.pos = pos;
    }

    public static class Pre extends MCLEventGlassMeltingFurnaceConsume {
        public Pre(Entity entity, BlockPos pos) {
            super(entity, pos);
        }

        @Override
        public boolean isCancelable() {
            return true;
        }
    }

    public static class Post extends MCLEventGlassMeltingFurnaceConsume {
        public Post(Entity entity, BlockPos pos) {
            super(entity, pos);
        }
    }
}

