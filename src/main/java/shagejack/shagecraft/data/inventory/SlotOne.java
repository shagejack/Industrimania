package shagejack.shagecraft.data.inventory;

public class SlotOne extends Slot{

    public SlotOne(boolean isMainSlot) {
        super(isMainSlot);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}
