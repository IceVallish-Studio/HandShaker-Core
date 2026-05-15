package me.mklv.handshaker.common.utils;

import java.util.Set;

public record ClientInfo(
	boolean hasHandshakeClient,
	Set<String> mods,
	boolean signatureVerified,
	boolean veltonVerified,
	String modListNonce,
	String integrityNonce,
	String veltonNonce,
	boolean handshakeChecked,
	String hwid
) {

	public ClientInfo(Set<String> mods,
					  boolean signatureVerified,
					  boolean veltonVerified,
					  String modListNonce,
					  String integrityNonce,
					  String veltonNonce) {
		this(false, mods, signatureVerified, veltonVerified, modListNonce, integrityNonce, veltonNonce, false, null);
	}

	public ClientInfo(Set<String> mods,
					  boolean signatureVerified,
					  boolean veltonVerified,
					  String modListNonce,
					  String integrityNonce,
					  String veltonNonce,
					  String hwid) {
		this(false, mods, signatureVerified, veltonVerified, modListNonce, integrityNonce, veltonNonce, false, hwid);
	}

	public ClientInfo withHandshakeChecked(boolean handshakeChecked) {
		return new ClientInfo(hasHandshakeClient, mods, signatureVerified, veltonVerified,
			modListNonce, integrityNonce, veltonNonce, handshakeChecked, hwid);
	}

	@Deprecated
	public ClientInfo withChecked(boolean checked) {
		return withHandshakeChecked(checked);
	}

	public ClientInfo withMods(Set<String> mods) {
		return new ClientInfo(hasHandshakeClient, mods, signatureVerified, veltonVerified,
			modListNonce, integrityNonce, veltonNonce, handshakeChecked, hwid);
	}

	public ClientInfo withHasHandshakeClient(boolean hasHandshakeClient) {
		return new ClientInfo(hasHandshakeClient, mods, signatureVerified, veltonVerified,
			modListNonce, integrityNonce, veltonNonce, handshakeChecked, hwid);
	}

	@Deprecated
	public ClientInfo withFabric(boolean fabric) {
		return withHasHandshakeClient(fabric);
	}

	public ClientInfo withSignatureVerified(boolean signatureVerified) {
		return new ClientInfo(hasHandshakeClient, mods, signatureVerified, veltonVerified,
			modListNonce, integrityNonce, veltonNonce, handshakeChecked, hwid);
	}

	public ClientInfo withVeltonVerified(boolean veltonVerified) {
		return new ClientInfo(hasHandshakeClient, mods, signatureVerified, veltonVerified,
			modListNonce, integrityNonce, veltonNonce, handshakeChecked, hwid);
	}

	public ClientInfo withHwid(String hwid) {
		return new ClientInfo(hasHandshakeClient, mods, signatureVerified, veltonVerified,
			modListNonce, integrityNonce, veltonNonce, handshakeChecked, hwid);
	}
}
