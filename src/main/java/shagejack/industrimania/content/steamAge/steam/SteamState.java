package shagejack.industrimania.content.steamAge.steam;

public enum SteamState {
    EMPTY(0),
    SATURATED(1),
    SUPERHEATED(2)

    ;

    private final int code;

    SteamState(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static SteamState getState(int code) {
        return switch (code) {
            case 1 -> SATURATED;
            case 2 -> SUPERHEATED;
            default -> EMPTY;
        };
    }

}
