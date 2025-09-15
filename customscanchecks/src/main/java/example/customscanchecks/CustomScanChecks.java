/*
 * Copyright (c) 2023-2025. PortSwigger Ltd. All rights reserved.
 *
 * This code may be used to extend the functionality of Burp Suite Community Edition
 * and Burp Suite Professional, provided that this usage does not violate the
 * license terms for those products.
 */

package example.customscanchecks;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.scanner.scancheck.ScanCheckType;

public class CustomScanChecks implements BurpExtension
{
    @Override
    public void initialize(MontoyaApi api)
    {
        api.extension().setName("Custom Scanner checks");

        api.scanner().registerPassiveScanCheck(new MyPassiveScanCheck(), ScanCheckType.PER_REQUEST);

        api.scanner().registerActiveScanCheck(new MyActiveScanCheck(), ScanCheckType.PER_INSERTION_POINT);
    }
}
