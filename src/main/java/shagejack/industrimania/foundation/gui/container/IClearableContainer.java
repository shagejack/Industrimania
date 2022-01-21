package shagejack.industrimania.foundation.gui.container;


import shagejack.industrimania.foundation.network.AllPackets;

public interface IClearableContainer {

	default void sendClearPacket() {
		AllPackets.CHANNEL.sendToServer(new ClearContainerPacket());
	}

	public void clearContents();

}
