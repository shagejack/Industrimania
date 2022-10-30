package shagejack.industrimania.foundation.data;

import net.minecraftforge.event.AddReloadListenerEvent;

public class ReloadListenerEventHandle {
    public static void onReload(final AddReloadListenerEvent event) {
        event.addListener(IMDataReloadListener.INSTANCE);
    }
}
