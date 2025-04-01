package com.rbbozkurt.ethmonitor.util;

public class AddressUtils {

    public static boolean isValidAddress(String address) {
        return address != null &&
                address.startsWith("0x") &&
                address.length() == 42 &&
                address.matches("^0x[0-9a-fA-F]{40}$");
    }
}
