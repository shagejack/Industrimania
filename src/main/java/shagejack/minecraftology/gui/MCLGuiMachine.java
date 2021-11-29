package shagejack.minecraftology.gui;

import shagejack.minecraftology.Reference;
import shagejack.minecraftology.container.ContainerMachine;
import shagejack.minecraftology.container.MCLBaseContainer;
import shagejack.minecraftology.data.inventory.Slot;
import shagejack.minecraftology.gui.element.ElementBaseGroup;
import shagejack.minecraftology.gui.element.ElementIndicator;
import shagejack.minecraftology.gui.element.ElementSlotsList;
import shagejack.minecraftology.gui.pages.AutoConfigPage;
import shagejack.minecraftology.machines.MCLTileEntityMachine;
import shagejack.minecraftology.proxy.ClientProxy;
import shagejack.minecraftology.util.MCLStringHelper;

public class MCLGuiMachine<T extends MCLTileEntityMachine> extends MCLGuiBase {
    T machine;
    ElementSlotsList slotsList;
    ElementIndicator indicator;

    public MCLGuiMachine(ContainerMachine<T> container, T machine) {
        this(container, machine, 225, 186);
    }

    public MCLGuiMachine(ContainerMachine<T> container, T machine, int width, int height) {
        super(container, width, height);
        this.machine = machine;

        indicator = new ElementIndicator(this, 6, ySize - 18);

        slotsList = new ElementSlotsList(this, 5, 52, 80, 200, machine.getInventoryContainer(), 0);
        slotsList.setMargin(5);

        registerPages(container, machine);
    }

    public void registerPages(MCLBaseContainer container, T machine) {
        ElementBaseGroup homePage = new ElementBaseGroup(this, 0, 0, xSize, ySize);
        homePage.setName("Home");
        AutoConfigPage configPage = new AutoConfigPage(this, 48, 32, xSize - 76, ySize);
        configPage.setName("Configurations");

        AddPage(homePage, ClientProxy.holoIcons.getIcon("page_icon_home"), MCLStringHelper.translateToLocal("gui.tooltip.page.home")).setIconColor(Reference.COLOR_MCL);
        AddPage(configPage, ClientProxy.holoIcons.getIcon("page_icon_config"), MCLStringHelper.translateToLocal("gui.tooltip.page.configurations"));

        boolean hasUpgrades = false;
        for (Slot slot : machine.getInventoryContainer().getSlots()) {
        }

        setPage(0);
    }

    @Override
    public void initGui() {
        super.initGui();
        this.addElement(slotsList);
        this.addElement(indicator);
    }

    @Override
    protected void updateElementInformation() {
        super.updateElementInformation();

        if (machine.isActive()) {
            indicator.setIndication(1);
        } else {
            indicator.setIndication(0);
        }
    }

    @Override
    public void textChanged(String elementName, String text, boolean typed) {

    }

    @Override
    public void ListSelectionChange(String name, int selected) {

    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        if (name != null && !name.isEmpty()) {
            String n = MCLStringHelper.translateToLocal("gui." + name + ".name");
            fontRenderer.drawString(n, 11 + xSize / 2 - (fontRenderer.getStringWidth(n) / 2), 7, Reference.COLOR_MCL.getColor());
        }

        drawElements(0, true);
    }

    public T getMachine() {
        return machine;
    }
}
