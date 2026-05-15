package me.mklv.handshaker.common.utils;

import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Locale;

/**
 * Utility to generate a unique hardware fingerprint for the client.
 * Uses system properties and network interface MAC addresses.
 */
public final class HardwareFingerprint {
    private HardwareFingerprint() {
    }

    /**
     * Generates a SHA-256 hash of the system hardware profile.
     * @return A 64-character hex string representing the hardware fingerprint.
     */
    public static String generate() {
        try {
            StringBuilder sb = new StringBuilder();
            // Basic system info
            sb.append(System.getProperty("os.name", "unknown")).append("|");
            sb.append(System.getProperty("os.arch", "unknown")).append("|");
            sb.append(System.getProperty("os.version", "unknown")).append("|");
            
            // Processor/Computer info (non-OSHI fallback)
            sb.append(System.getenv("PROCESSOR_IDENTIFIER")).append("|");
            sb.append(System.getenv("COMPUTERNAME")).append("|");

            // Hardware-tied identifiers: MAC addresses
            try {
                Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                if (interfaces != null) {
                    while (interfaces.hasMoreElements()) {
                        NetworkInterface ni = interfaces.nextElement();
                        if (ni.isLoopback() || ni.isVirtual()) {
                            continue;
                        }
                        byte[] mac = ni.getHardwareAddress();
                        if (mac != null && mac.length > 0) {
                            for (int i = 0; i < mac.length; i++) {
                                sb.append(String.format("%02X", mac[i]));
                            }
                            sb.append(",");
                        }
                    }
                }
            } catch (Exception ignored) {
                // Network interface access might be restricted
            }

            return HashUtils.sha256Hex(sb.toString().toLowerCase(Locale.ROOT));
        } catch (Exception e) {
            // Extreme fallback: minimal uniqueness
            return HashUtils.sha256Hex(
                System.getProperty("os.name", "generic") +
                System.getProperty("os.arch", "generic") +
                System.getProperty("user.name", "user")
            );
        }
    }
}
