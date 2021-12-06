package shagejack.shagecraft.gui;

import shagejack.shagecraft.Reference;
import shagejack.shagecraft.container.ContainerMachine;
import shagejack.shagecraft.container.ShageBaseContainer;
import shagejack.shagecraft.data.inventory.Slot;
import shagejack.shagecraft.gui.element.ElementBaseGroup;
import shagejack.shagecraft.gui.element.ElementIndicator;
import shagejack.shagecraft.gui.element.ElementSlotsList;
import shagejack.shagecraft.gui.pages.AutoConfigPage;
import shagejack.shagecraft.machines.ShageTileEntityMachine;
import shagejack.shagecraft.proxy.ClientProxy;
import shagejack.shagecraft.util.ShageStringHelper;

public class ShageGuiMachine<T extends ShageTileEntityMachine> extends ShageGuiBase {
    T machine;
    ElementSlotsList slotsList;
    ElementIndicator indicator;

    public ShageGuiMachine(ContainerMachine<T> container, T machine) {
        this(container, machine, 225, 186);
    }

    public ShageGuiMachine(ContainerMachine<T> container, T machine, int width, int height) {
        super(container, width, height);
        this.machine = machine;

        indicator = new ElementIndicator(this, 6, ySize - 18);

        slotsList = new ElementSlotsList(this, 5, 52, 80, 200, machine.getInventoryContainer(), 0);
        slotsList.setMargin(5);

        registerPages(container, machine);
    }

    public void registerPages(ShageBaseContainer container, T machine) {
        ElementBaseGroup homePage = new ElementBaseGroup(this, 0, 0, xSize, ySize);
        homePage.setName("Home");
        AutoConfigPage configPage = new AutoConfigPage(this, 48, 32, xSize - 76, ySize);
        configPage.setName("Configurations");

        AddPage(homePage, ClientProxy.holoIcons.getIcon("page_icon_home"), ShageStringHelper.translateToLocal("gui.tooltip.page.home")).setIconColor(Reference.COLOR_SHAGECRAFT);
        AddPage(configPage, ClientProxy.holoIcons.getIcon("page_icon_config"), ShageStringHelper.translateToLocal("gui.tooltip.page.configurations"));

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
            String n = ShageStringHelper.translateToLocal("gui." + name + ".name");
            fontRenderer.drawString(n, 11 + xSize / 2 - (fontRenderer.getStringWidth(n) / 2), 7, Reference.COLOR_SHAGECRAFT.getColor());
        }

        drawElements(0, true);
    }

    public T getMachine() {
        return machine;
    }
}
