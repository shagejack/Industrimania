package shagejack.industrimania.content.steamAge.steam;

public interface ISteamHandler {
    enum SteamAction {
        EXECUTE, SIMULATE;

        public boolean execute() {
            return this == EXECUTE;
        }

        public boolean simulate() {
            return this == SIMULATE;
        }
    }

}
