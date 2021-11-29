package shagejack.minecraftology.gui.pages;

import shagejack.minecraftology.Reference;
import shagejack.minecraftology.gui.MCLGuiMachine;
import shagejack.minecraftology.gui.element.*;
import shagejack.minecraftology.machines.components.ComponentConfigs;
import shagejack.minecraftology.machines.configs.*;
import shagejack.minecraftology.util.MCLStringHelper;

public class AutoConfigPage extends ElementBaseGroup {
    final MCLGuiMachine machineGui;
    private final ComponentConfigs componentConfigs;

    public AutoConfigPage(MCLGuiMachine gui, int posX, int posY, int width, int height) {
        super(gui, posX, posY, width, height);
        this.componentConfigs = gui.getMachine().getConfigs();
        this.machineGui = gui;
    }

    @Override
    public void init() {
        super.init();
        int yPos = 0;
        for (IConfigProperty configProperty : componentConfigs.getValues().values()) {
            if (configProperty instanceof ConfigPropertyBoolean) {
                ElementCheckbox elementCheckbox = new ElementCheckbox(gui, this, 0, yPos, configProperty.getKey(), (boolean) configProperty.getValue());
                elementCheckbox.setCheckboxLabel(MCLStringHelper.translateToLocal(configProperty.getUnlocalizedName()));
                addElement(elementCheckbox);
                yPos += elementCheckbox.getHeight() + 4;
            } else if (configProperty instanceof ConfigPropertyInteger) {
                if (configProperty instanceof ConfigPropertyStringList) {
                    ElementStates elementStates = new ElementStates(gui, this, 0, yPos, configProperty.getKey(), ((ConfigPropertyStringList) configProperty).getList());
                    elementStates.setSelectedState((Integer) configProperty.getValue());
                    elementStates.setLabel(MCLStringHelper.translateToLocal(configProperty.getUnlocalizedName()));
                    elementStates.setTextColor(Reference.COLOR_HOLO.getColor());
                    addElement(elementStates);
                    yPos += elementStates.getHeight() + 4;
                } else {
                    ElementIntegerField elementIntegerField = new ElementIntegerField(gui, this, 0, yPos, 20, ((ConfigPropertyInteger) configProperty).getMin(), ((ConfigPropertyInteger) configProperty).getMax());
                    elementIntegerField.setNumber((Integer) configProperty.getValue());
                    elementIntegerField.setName(configProperty.getKey());
                    elementIntegerField.setLabel(MCLStringHelper.translateToLocal(configProperty.getUnlocalizedName()));
                    elementIntegerField.setLabelColor(Reference.COLOR_HOLO.getColor());
                    elementIntegerField.init();
                    addElement(elementIntegerField);
                    yPos += elementIntegerField.getHeight() + 4;
                }
            } else if (configProperty instanceof ConfigPropertyString) {
                MCLElementTextField textField = new MCLElementTextField(gui, this, 8, yPos + 2, sizeX, 20);
                textField.setMaxLength(((ConfigPropertyString) configProperty).getMaxLength());
                textField.setName(configProperty.getKey());
                textField.setText(configProperty.getValue().toString());
                textField.setBackground(MCLElementButtonScaled.HOVER_TEXTURE_DARK);
                textField.setTextOffset(8, 4);
                if (((ConfigPropertyString) configProperty).getPattern() != null) {
                    textField.setFilter(((ConfigPropertyString) configProperty).getPattern(), false);
                }
                addElement(textField);
                yPos += textField.getHeight() + 4;
            }
        }
    }

    @Override
    public void handleElementButtonClick(MCLElementBase element, String name, int mouseButton) {
        for (IConfigProperty configProperty : componentConfigs.getValues().values()) {
            if (configProperty instanceof ConfigPropertyBoolean) {
                if (name.equals(configProperty.getKey()) && element instanceof ElementCheckbox) {
                    configProperty.setValue(((ElementCheckbox) element).getState());
                    componentConfigs.getMachine().sendConfigsToServer(true);
                }
            } else if (configProperty instanceof ConfigPropertyInteger) {
                if (name.equals(configProperty.getKey())) {
                    int state = 0;
                    if (element instanceof ElementStates) {
                        state = mouseButton;
                    } else if (element instanceof ElementIntegerField) {
                        state = ((ElementIntegerField) element).getNumber();
                    }
                    configProperty.setValue(state);
                    componentConfigs.getMachine().sendConfigsToServer(true);
                }
            }
        }
    }

    @Override
    public void textChanged(String elementName, String text, boolean typed) {
        for (IConfigProperty configProperty : componentConfigs.getValues().values()) {
            if (configProperty instanceof ConfigPropertyString) {
                if (elementName.equals(configProperty.getKey())) {
                    configProperty.setValue(text);
                    componentConfigs.getMachine().sendConfigsToServer(true);
                }
            }
        }
    }
}
