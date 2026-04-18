package me.mklv.handshaker.common.loader;

public final class CommonClientHandshakeOrchestrator {
    public interface Logger {
        void info(String format, Object... args);

        void warn(String message);
    }

    public interface Sender {
        void sendModList(String transportPayload, String modListHash, String nonce);

        void sendIntegrity(byte[] signature, String jarHash, String nonce);
    }

    public interface Availability {
        boolean isConnectionReady();
    }

    public interface PayloadProvider {
        CommonClientHashPayloadService.ModListData getModListData();

        CommonClientHashPayloadService.IntegrityData getIntegrityData();
    }

    private String lastChallenge = null;

    public void onJoin(Availability availability, PayloadProvider payloads, Sender sender, Logger logger) {
        if (!availability.isConnectionReady()) {
            return;
        }

        // Always clear stale challenge on a fresh join event to prevent re-using old session data
        if (lastChallenge != null) {
            logger.info("Fresh join detected, clearing previous session challenge.");
            lastChallenge = null;
        }

        logger.info("Joined server, waiting for handshake challenge...");
    }

    public void onChallenge(String challenge, Availability availability, PayloadProvider payloads, Sender sender, Logger logger) {
        logger.info("Received handshake challenge: {}. Checking connection readiness...", challenge);
        lastChallenge = challenge;
        
        if (!availability.isConnectionReady()) {
            logger.warn("Received challenge but connection is not ready yet. Storing for later.");
            return;
        }

        sendHandshake(challenge, payloads, sender, logger);
    }

    private void sendHandshake(String challenge, PayloadProvider payloads, Sender sender, Logger logger) {
        CommonClientHashPayloadService.ModListData modList = payloads.getModListData();
        sender.sendModList(modList.transportPayload(), modList.modListHash(), challenge);
        logger.info(
            "Sent mod list ({} chars, hash: {}) with challenge: {}",
            modList.transportPayload().length(),
            abbreviateHash(modList.modListHash()),
            challenge
        );

        CommonClientHashPayloadService.IntegrityData integrity = payloads.getIntegrityData();
        
        // Cryptographic binding: prove possession of the static signature for THIS session challenge
        byte[] staticSignature = integrity.signature();
        if (staticSignature.length > 0) {
            byte[] bindingProof = me.mklv.handshaker.common.utils.HashUtils.sha256(
                combine(staticSignature, challenge.getBytes(java.nio.charset.StandardCharsets.UTF_8))
            );
            
            // Re-purpose the signature field: [32 bytes binding proof][rest is static signature]
            byte[] boundPayload = combine(bindingProof, staticSignature);
            
            sender.sendIntegrity(boundPayload, integrity.jarHash(), challenge);
            logger.info(
                "Sent detached integrity proof ({} bytes + binding) with content hash {} and challenge: {}",
                staticSignature.length,
                abbreviateHash(integrity.jarHash()),
                challenge
            );
        } else {
            sender.sendIntegrity(new byte[0], integrity.jarHash(), challenge);
            logger.warn("Could not build runtime integrity proof. Sending empty payload.");
        }
    }

    private byte[] combine(byte[] a, byte[] b) {
        byte[] res = new byte[a.length + b.length];
        System.arraycopy(a, 0, res, 0, a.length);
        System.arraycopy(b, 0, res, a.length, b.length);
        return res;
    }

    private String abbreviateHash(String hash) {
        if (hash == null || hash.length() < 8) {
            return hash == null ? "" : hash;
        }
        return hash.substring(0, 8);
    }
}