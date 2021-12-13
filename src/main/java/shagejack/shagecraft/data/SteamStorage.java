package shagejack.shagecraft.data;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import shagejack.shagecraft.api.steam.ISteamHandler;

public class SteamStorage implements ISteamHandler {

    private double steam_mass;
    private double steam_temp;
    //private double steam_pressure;
    //private double steam_quality;
    private int steam_state;

    private double capacity;
    private double maxExtract;
    private double maxReceive;

    public SteamStorage(double capacity) {
        this(capacity, capacity, capacity);
    }

    public SteamStorage(double capacity, double maxTransfer) {
        this(capacity, maxTransfer, maxTransfer);
    }

    public SteamStorage(double capacity, double maxExtract, double maxReceive) {
        this.capacity = capacity;
        this.maxExtract = maxExtract;
        this.maxReceive = maxReceive;
    }

    public double getMaxExtract() {
        return maxExtract;
    }

    public void setMaxExtract(double maxExtract) {
        this.maxExtract = maxExtract;
    }

    public double getMaxReceive() {
        return maxReceive;
    }

    public void setMaxReceive(double maxReceive) {
        this.maxReceive = maxReceive;
    }

    @Override
    public double getSteamMass() {
        return steam_mass;
    }

    @Override
    public double getSteamTemp() {
        return steam_temp;
    }

    //@Override
    //public double getSteamPressure() {
    //   return steam_pressure;
    //}

    //@Override
    //public double getSteamQuality() {
    //    return steam_quality;
    //}

    /**
     *
     * @return steam pressure in MPa
     */
    public double getSteamPressure() {
        if(steam_state == 1){
            double t_r = steam_temp / 647;
            double a = 9.57;
            double b = 5.40;
            double c = -6.16;
            double d = 1.496;
            double e = 0.433;
            double lnt = Math.log(t_r);
            double lnp = a + b * lnt + c * lnt * lnt + d * Math.pow(lnt, 4) + e * Math.pow(t_r, 5);
            return Math.pow(Math.E, lnp) / 1000;
        } else if (steam_state == 2) {
            double t_r = steam_temp / 647;
            double a = 9.57;
            double b = 5.40;
            double c = -6.16;
            double d = 1.496;
            double e = 0.433;
            double lnt = Math.log(t_r);
            double lnp = a + b * lnt + c * lnt * lnt + d * Math.pow(lnt, 4) + e * Math.pow(t_r, 5);
            return 1.2 * Math.pow(Math.E, lnp) / 1000;
        }
        return 0;
    }

    @Override
    public int getSteamState() {
        return steam_state;
    }

    @Override
    public void setSteamMass(double mass) {
        this.steam_mass = mass;
    }

    @Override
    public void setSteamTemp(double temp) {
        this.steam_temp = temp;
    }

    /*
    @Override
    public void setSteamPressure(double pressure) {
        this.steam_pressure = pressure;
    }


    @Override
    public void setSteamQuality(double quality) {
        this.steam_quality = quality;
    }
     */

    @Override
    public void setSteamState(int state) {
        this.steam_state = state;
    }

    @Override
    public double getCapacity() {
        return capacity;
    }

    @Override
    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    //WIP
    @Override
    public double[] receiveSteam(double[] properties, boolean simulate) {
        return properties;
    }

    @Override
    public double[] mergeSteam(double[] properties, double[] target, boolean simulate) {
        if (properties.length == 3 && target.length == 3) {
            if (properties[0] <= 0 || properties[1] <= 298.15) properties[2] = 0;
            if (target[0] <= 0 || target[1] <= 298.15) target[2] = 0;

            if (target[2] == 0) {
                target = properties;
            } else if (target[2] == properties[2]) {
                target[0] += properties[0];
                target[1] = ( target[0] * target[1] + properties[0] * properties[1] ) / ( target[0] + properties[0] );
            } else {
                if (properties[2] == 1) {
                    //Saturated Steam Into Overheated Steam
                    target[0] += properties[0];
                    target[1] = ( target[0] * target[1] + properties[0] * properties[1] ) / ( target[0] + properties[0] );

                    if (target[1] - properties[1] * target[0] / properties[0] > 50) {
                        if (Math.random() < 0.2 * properties[0] / target[0]) {
                            target[2] = 1;
                        }
                    }

                } else if (properties[2] == 2) {
                    //Overheated Steam Into Saturated Steam
                    target[0] += properties[0];
                    target[1] = ( target[0] * target[1] + properties[0] * properties[1] ) / target[0] + properties[0];

                    if (properties[1] * properties[0] / target[0] - target[1] > 50) {
                        if (Math.random() < 0.2 * properties[0] / target[0]) {
                            target[2] = 2;
                        }
                    }
                }
            }
        }
        return target;
    }

    public SteamStorage readFromNBT(NBTTagCompound nbt)
    {
        if (!nbt.hasKey("SteamEmpty"))
        {
            setSteamMass(nbt.getDouble("steam_mass"));
            setSteamTemp(nbt.getDouble("steam_temp"));
            setSteamState(nbt.getInteger("steam_state"));
        }
        else
        {
            setSteamMass(0);
            setSteamTemp(0);
            setSteamState(0);
        }

        return this;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        if (hasSteam())
        {
            nbt.setDouble("steam_mass", steam_mass);
            nbt.setDouble("steam_temp", steam_temp);
            nbt.setInteger("steam_state", steam_state);
        }
        else
        {
            nbt.setString("SteamEmpty", "");
        }
        return nbt;
    }

    public double getActualEnthalpy(double[] properties){
        if (properties.length == 3) {
            double mass = properties[0];
            double temp = properties[1];
            int state = (int) properties[2];
            //State:
            //0 -> Empty
            //1 -> Saturated Steam
            //2 -> Overheated Steam
            if(state != 0) {
                return mass * (2000 + 2 * temp);
            }
        }
        return 0;
    }

    @Override
    public double getActualWork(double efficiency, double mass, double enthalpy) {
        return efficiency * mass * enthalpy;
    }

    @Override
    public double getCurrentActualWork(double efficiency) {
        return getActualWork(efficiency, getSteamMass(), getCurrentEnthalpyConsume());
    }

    @Override
    public double getActualPower(double efficiency, double mass, double pressure) {
        return efficiency * mass * pressure;
    }

    @Override
    public double getCurrentActualPower(double efficiency) {
        if (getSteamState() != 0) {
            return efficiency * getSteamMass() * getSteamPressure();
        } else {
            return 0;
        }
    }

    public double getCurrentEnthalpyConsume(){
        if (getSteamState() != 0 && getSteamTemp() > 373.15) {
            return getSteamPressure();
        } else {
            return 0;
        }
    }

    public double manageSteamTempFromConsume(double consume) {
        return consume / 2;
    }

    @Override
    public double[] mergeProperties() {
        double[] properties = new double[3];
        properties[0] = getSteamMass();
        properties[1] = getSteamTemp();
        //properties[2] = getSteamPressure();
        //properties[3] = getSteamQuality();
        properties[2] = getSteamState();
        return properties;
    }

    @Override
    public void setProperties(double[] properties) {
        if (properties.length == 3) {
            setSteamMass(properties[0]);
            setSteamTemp(properties[1]);
            //setSteamPressure(properties[2]);
            //setSteamQuality(properties[3]);
            setSteamState((int) properties[2]);
        }
    }

    @Override
    public void releaseSteam() {
        setSteamMass(0);
        setSteamTemp(0);
        setSteamState(0);
    }

    @Override
    public boolean hasSteam() {
        return getSteamState() != 0;
    }

    @Override
    public boolean isExceededCapacity() {
        return hasSteam() && getSteamMass() * getSteamPressure() > getCapacity();
    }
}
