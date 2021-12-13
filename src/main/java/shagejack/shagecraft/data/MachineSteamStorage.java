package shagejack.shagecraft.data;

import net.minecraft.block.state.IBlockState;
import shagejack.shagecraft.tile.ShageTileEntityMachineSteam;

public class MachineSteamStorage<T extends ShageTileEntityMachineSteam> extends SteamStorage {
    protected final T machine;

    public MachineSteamStorage(T machine, int capacity) {
        this(machine, capacity, capacity, capacity);
    }

    public MachineSteamStorage(T machine, int capacity, int maxExtract) {
        this(machine, capacity, maxExtract, maxExtract);
    }

    public MachineSteamStorage(T machine, int capacity, int maxExtract, int maxReceive) {
        super(capacity, maxExtract, maxReceive);
        this.machine = machine;
    }

    @Override
    public void setSteamMass(double mass) {
        double last_mass = super.getSteamMass();
        super.setSteamMass(mass);
        if (last_mass != mass) {
            machine.updateClientSteam();
        }
    }

    @Override
    public void setSteamTemp(double temp) {
        double last_temp = super.getSteamTemp();
        super.setSteamTemp(temp);
        if (last_temp != temp) {
            machine.updateClientSteam();
        }
    }

    /*

    @Override
    public void setSteamPressure(double pressure) {
        double last_pressure = super.getSteamPressure();
        super.setSteamPressure(pressure);
        if (last_pressure != pressure) {
            machine.updateClientSteam();
        }
    }

    @Override
    public void setSteamQuality(double quality) {
        double last_quality = super.getSteamQuality();
        super.setSteamQuality(quality);
        if (last_quality != quality) {
            machine.updateClientSteam();
        }
    }
     */

    //WIP
    @Override
    public void setSteamState(int state) {
        int last_state = super.getSteamState();
        super.setSteamState(state);
        if (last_state != state){
            machine.updateClientSteam();
        }
    }
}

