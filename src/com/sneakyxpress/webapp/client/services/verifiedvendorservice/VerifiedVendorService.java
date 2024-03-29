package com.sneakyxpress.webapp.client.services.verifiedvendorservice;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sneakyxpress.webapp.shared.VerifiedVendor;

import java.util.List;

/**
 * The client side stub for retrieving VendorFeedback
 * Created by michael on 11/14/2013.
 */
@RemoteServiceRelativePath("verifiedVendor")
public interface VerifiedVendorService extends RemoteService {
    boolean removeVerifiedVendor(String verifiedVendorId);
    List<VerifiedVendor> getVerifiedVendors(String userId) throws IllegalArgumentException;
    VerifiedVendor getVerifiedVendor(String vendorId) throws IllegalArgumentException;
    // For truck claims
    boolean addVerifiedVendor(VerifiedVendor v);
}
