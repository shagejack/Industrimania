package shagejack.industrimania.content.steamAge.steam;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

public class SteamStack {

    public static final SteamStack EMPTY = new SteamStack(0, 273.15, SteamState.EMPTY);

    private SteamState state;
    private double mass;
    private double temperature;

    public SteamStack(double mass, double temperature, SteamState state)
    {
        if (mass == 0 || temperature <= 273.15 || state == SteamState.EMPTY)
        {
            mass = 0;
            temperature = 273.15;
            state = SteamState.EMPTY;
        }

        this.mass = mass;
        this.temperature = temperature;
        this.state = state;

        updateEmpty();
    }

    public SteamStack(double mass, double temperature, int state)
    {
        if (mass == 0 || temperature <= 273.15 || state == 0)
        {
            mass = 0;
            temperature = 273.15;
            state = 0;
        }

        this.mass = mass;
        this.temperature = temperature;
        this.state = SteamState.getState(state);

        updateEmpty();
    }

    public static SteamStack saturatedSteam(double mass, double temperature) {
        return new SteamStack(mass, temperature, 1);
    }

    public static SteamStack superheatedSteam(double mass, double temperature) {
        return new SteamStack(mass, temperature, 2);
    }

    public static SteamStack loadSteamStackFromNBT(CompoundTag nbt)
    {
        if (nbt == null)
        {
            return EMPTY;
        }
        if (!nbt.contains("SteamMass", Tag.TAG_DOUBLE))
        {
            return EMPTY;
        }
        if (!nbt.contains("SteamTemperature", Tag.TAG_DOUBLE))
        {
            return EMPTY;
        }
        if (!nbt.contains("SteamState", Tag.TAG_INT))
        {
            return EMPTY;
        }

        double mass = nbt.getDouble("SteamMass");
        double temperature = nbt.getDouble("SteamTemperature");
        int state = nbt.getInt("SteamState");

        SteamStack stack = new SteamStack(mass, temperature, SteamState.getState(state));

        if (stack.isEmpty()) return EMPTY;

        return stack;
    }

    public CompoundTag writeToNBT(CompoundTag nbt)
    {
        nbt.putDouble("SteamMass", mass);
        nbt.putDouble("SteamTemperature", temperature);
        nbt.putInt("SteamState", state.getCode());

        return nbt;
    }

    public void writeToPacket(FriendlyByteBuf buf) {
        buf.writeDouble(getMass());
        buf.writeDouble(getTemperature());
        buf.writeVarInt(getState().getCode());
    }

    public static SteamStack readFromPacket(FriendlyByteBuf buf)
    {
        double mass = buf.readDouble();
        double temperature = buf.readDouble();
        SteamState state = SteamState.getState(buf.readVarInt());
        if (mass == 0 || temperature <= 273.15 || state == SteamState.EMPTY) return EMPTY;
        return new SteamStack(mass, temperature, state);
    }

    public double getMass()
    {
        return isEmpty() ? 0 : mass;
    }

    public double getTemperature()
    {
        return isEmpty() ? 273.15 : temperature;
    }

    public SteamState getState()
    {
        return isEmpty() ? SteamState.EMPTY : state;
    }

    public SteamStack setMass(double mass)
    {
        this.mass = mass;
        return this;
    }

    public void setTemperature(double temperature)
    {
        this.temperature = Math.max(temperature, 273.15);
    }

    public void setState(SteamState state)
    {
        this.state = state;
    }

    public void setState(int state)
    {
        this.state = SteamState.getState(state);
    }

    protected void updateEmpty() {
        if (isEmpty())
        {
            this.mass = 0;
            this.temperature = 273.15;
            this.state = SteamState.EMPTY;
        }
    }

    public boolean isEmpty() {
        return this.mass <= 0 || this.temperature <= 273.15 || this.state == SteamState.EMPTY;
    }

    public boolean isSaturated() {
        return !isEmpty() && state == SteamState.SATURATED;
    }

    public boolean isSuperheated() {
        return !isEmpty() && state == SteamState.SUPERHEATED;
    }

    public SteamStack copy()
    {
        return new SteamStack(getMass(), getTemperature(), getState());
    }

    public SteamStack getDivided(int count)
    {
        return new SteamStack(getMass() / (double) count, getTemperature(), getState());
    }

    @Override
    public final int hashCode()
    {
        int code = 1;
        code = 31 * code + Double.valueOf(mass).hashCode();
        code = 31 * code + Double.valueOf(temperature).hashCode();
        code = 31 * code + state.hashCode();
        return code;
    }

    public double getSteamPressure() {
        if (isEmpty())
            return 0;

        double t_r = temperature / 647;
        double a = 9.57;
        double b = 5.40;
        double c = -6.16;
        double d = 1.496;
        double e = 0.433;
        double lnt = Math.log(t_r);
        double lnp = a + b * lnt + c * lnt * lnt + d * Math.pow(lnt, 4) + e * Math.pow(t_r, 5);
        if (lnp > 11) lnp = 11;

        if(isSaturated()){
            return Math.pow(Math.E, lnp) / 1000;
        } else if (isSuperheated()) {
            return 1.2 * Math.pow(Math.E, lnp) / 1000;
        }

        return 0;
    }

    public double getDensity() {
        if (isEmpty())
            return 0;

        double a = 0.16946;
        double b = 3.8232;
        double c = 1.1946;
        double p = getSteamPressure();
        return a * p * p + b * p + c;
    }

    public double getVolume() {
        if (isEmpty())
            return 0;

        return getMass() / getDensity();
    }

    public double getStress() {
        if (isEmpty())
            return 0;

        return getVolume() * getSteamPressure();
    }

    public double getMoistureContent() {
        return isEmpty() || isSuperheated() ? 0 : 220 * getSteamPressure() * getMass() / (getTemperature() * getDensity());
    }

    public double getLatentHeat() {
        if (isEmpty())
            return 0;
        if (getTemperature() >= 647.1)
            return 0;

        double t_c = -10 + getTemperature() / 100;
        double mu = -209.5;
        double nu = 39015;
        double theta_5 = -14.682;
        double theta_4 = -395.8;
        double theta_3 = -4398.8;
        double theta_2 = -24286.5;
        double theta_1 = -70322.5;
        double theta_0 = -347946;
        return Math.exp(mu + getTemperature() / 3) + nu * Math.log(getTemperature()) + theta_5 * Math.pow(t_c, 5) + theta_4 * Math.pow(t_c, 4) + theta_3 * Math.pow(t_c, 3) + theta_2 * Math.pow(t_c, 2) + theta_1 * t_c + theta_0;
    }

    public double getEnthalpy() {
        if (isEmpty()) return 0;

        if (isSaturated()) return 2000 + 2 * getTemperature();

        if (isSuperheated()) return 2000 + getLatentHeat() * getMoistureContent() / getMass() + 2 * getTemperature();

        return 0;
    }

    public static boolean isStateDifferent(SteamStack stack0, SteamStack stack1) {
        return stack0.getState() == stack1.getState();
    }

    public static SteamStack mergeSteamStack(SteamStack stack0, SteamStack stack1) {
        if (stack0.isEmpty() && stack1.isEmpty())
            return EMPTY;
        if (stack0.isEmpty())
            return stack1;
        if (stack1.isEmpty())
            return stack0;

        double nTemp = ( stack0.getMass() * stack0.getTemperature() + stack1.getMass() * stack1.getTemperature() ) / (stack0.getMass() + stack1.getMass());

        if (isStateDifferent(stack0, stack1)) {

            double nLatentHeat = (stack0.getMass() * stack0.getLatentHeat() + stack1.getMass() * stack1.getLatentHeat()) / (stack0.getMass() + stack1.getMass());

            SteamStack nStack = new SteamStack(stack0.getMass() + stack1.getMass(), nTemp, SteamState.SUPERHEATED);

            if (nStack.isEmpty()) return EMPTY;

            if (nLatentHeat < nStack.getLatentHeat()) nStack.setState(SteamState.SATURATED);

            return nStack;

        } else {

            SteamStack nStack = new SteamStack(stack0.getMass() + stack1.getMass(), nTemp, stack0.getState());

            return nStack;

        }

    }

    public void doWork() {
        this.manageSteamFromConsume(getEnthalpyConsume());
    }

    public double getPower(double efficiency) {
        return efficiency * getMass() * getSteamPressure();
    }

    public double getWork(double efficiency) {
        return efficiency * getMass() * getEnthalpyConsume();
    }

    public double getEnthalpyConsume() {
        if (isEmpty())
            return 0;

        return getSteamPressure() * getTemperature() / 40;
    }

    public SteamStack manageSteamFromConsume(double consume) {
        this.temperature -= consume / 2;
        return this;
    }



}
