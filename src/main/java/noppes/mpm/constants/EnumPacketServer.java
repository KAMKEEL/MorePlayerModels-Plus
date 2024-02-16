package noppes.mpm.constants;

import kamkeel.MorePlayerModelsPermissions;

public enum EnumPacketServer {

	CLIENT_PING,
	REQUEST_PLAYER_DATA,
	PERMISSION_SEND,
	UPDATE_PLAYER_DATA,
	ANIMATION;

	public MorePlayerModelsPermissions.Permission permission = null;

	EnumPacketServer() {}

	EnumPacketServer(MorePlayerModelsPermissions.Permission permission) {
		this.permission = permission;
	}
	public boolean hasPermission() {
		return permission != null;
	}
}
