package shagejack.shagecraft.tile;

//You Should Not Use This
/*
public abstract class ShageTileEntityContainer extends MCLTileEntity implements IMCLTileEntity, ISidedInventory, ITickable {

    protected final Inventory inventory;
    protected final IItemHandler inventoryHandler;

    @SideOnly(Side.CLIENT)
    protected MachineSound sound;
    protected boolean redstoneState;
    protected boolean redstoneStateDirty = true;
    protected final List<IMachineComponent> components;
    protected UUID owner;
    protected boolean playerSlotsHotbar, playerSlotsMain;
    protected ComponentConfigs configs;
    //client syncs
    private boolean lastActive;
    private boolean activeState;
    private boolean awoken;
    private boolean forceClientUpdate;

    public MCLTileEntityContainer(){
        components = new ArrayList<>();
        inventory = new TileEntityInventory(this, "");
        inventoryHandler = new InvWrapper(this);
        RegisterSlots(inventory);
    }

    protected void RegisterSlots(Inventory inventory) {
    }


    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public void update() {
        if (!awoken) {
            awoken = true;
            onAwake(world.isRemote ? Side.CLIENT : Side.SERVER);
        }

        if (world.isRemote) {
            manageSound();

            if (lastActive != isActive()) {
                onActiveChange();
                lastActive = isActive();
            }
        } else {
            activeState = getServerActive();
            if (lastActive != activeState) {
                forceSync();
                onActiveChange();
                lastActive = activeState;
            }

            manageClientSync();
        }

        components.stream().filter(component -> component instanceof ITickable).forEach(component -> {
            try {
                ((ITickable) component).update();
            } catch (Exception e) {
                LogMCL.log(Level.FATAL, e, "There was a problem while ticking %s component %s", this, component);
            }
        });
    }

    public void forceSync() {
        forceClientUpdate = true;
    }

    public abstract SoundEvent getSound();

    public abstract boolean hasSound();

    public abstract boolean getServerActive();

    public abstract float soundVolume();

    protected void onActiveChange() {
        MachineEvent event = new MachineEvent.ActiveChange();
        onMachineEvent(event);
        onMachineEventCompoments(event);
    }

    protected void onMachineEventCompoments(MachineEvent event) {
        for (IMachineComponent component : components) {
            component.onMachineEvent(event);
        }
    }

    protected abstract void onMachineEvent(MachineEvent event);

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return isItemValidForSlot(index, itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return false;
    }

    @Override
    public int getSizeInventory() {
        if (getInventory() != null) {
            return getInventory().getSizeInventory();
        } else {
            return 0;
        }
    }

    @Override
    @Nonnull
    public ItemStack getStackInSlot(int slot) {
        if (getInventory() != null) {
            return getInventory().getStackInSlot(slot);
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public ItemStack decrStackSize(int slot, int size) {
        if (getInventory() != null) {
            return getInventory().decrStackSize(slot, size);
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if (getInventory() != null) {
            return getInventory().removeStackFromSlot(index);
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack) {
        if (getInventory() != null) {
            getInventory().setInventorySlotContents(slot, itemStack);
        }
    }

    @Override
    public ITextComponent getDisplayName() {
        if (getInventory() != null && getInventory().getDisplayName() != null) {
            return getInventory().getDisplayName();
        } else if (getBlockType() != null) {
            return new TextComponentString(getBlockType().getLocalizedName());
        } else {
            return new TextComponentString("");
        }
    }

    @Override
    public int getInventoryStackLimit() {
        if (getInventory() != null) {
            return getInventory().getInventoryStackLimit();
        } else {
            return 0;
        }
    }

    @Override
    public void openInventory(EntityPlayer player) {
        if (getInventory() != null) {
            getInventory().openInventory(player);
        }
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        if (getInventory() != null) {
            getInventory().closeInventory(player);
        }
    }

    public boolean isItemValidForSlot(int slot, ItemStack item) {
        return getInventory() != null && getInventory().isItemValidForSlot(slot, item);
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {

    }

    public IInventory getInventory() {
        return inventory;
    }

    public Inventory getInventoryContainer() {
        return inventory;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }


    @Override
    public void onAdded(World world, BlockPos pos, IBlockState state) {
    }

    @Override
    public void onPlaced(World world, EntityLivingBase entityLiving) {

    }

    @Override
    public void onDestroyed(World worldIn, BlockPos pos, IBlockState state) {
    }

    @Override
    public void onNeighborBlockChange(IBlockAccess world, BlockPos pos, IBlockState state, Block neighborBlock) {

    }

    @Override
    public void writeToDropItem(ItemStack itemStack) {

    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void readFromPlaceItem(ItemStack itemStack) {

    }

    public void onChunkUnload() {
    }

    @Override
    protected void onAwake(Side side) {
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories, boolean toDisk) {
        if (categories.contains(MachineNBTCategory.DATA)) {
        }
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories) {
        if (categories.contains(MachineNBTCategory.DATA)) {
        }
    }

    @SideOnly(Side.CLIENT)
    //TODO: manage sound
    protected void manageSound() {
    }

    @SideOnly(Side.CLIENT)
    void stopSound() {
        if (sound != null) {
            sound.stopPlaying();
            FMLClientHandler.instance().getClient().getSoundHandler().stopSound(sound);
            sound = null;
        }
    }

    public boolean isActive() {
        return activeState;
    }

    public void setActive(boolean active) {
        activeState = active;
    }

    protected void manageClientSync() {
        if (forceClientUpdate) {
            forceClientUpdate = false;
            Minecraftology.NETWORK.sendToAllAround(new PacketSendMachineNBT(MachineNBTCategory.ALL_OPTS, this, false, false), this, 64);
            markDirty();
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if (world.isRemote) {
            manageSound();
        }
    }

}

 */
