package com.sneakyxpress.webapp.client.services.vendorfeedback;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sneakyxpress.webapp.shared.VendorFeedback;

import java.util.List;

/**
 * The client side stub for retrieving VendorFeedback
 * Created by michael on 11/14/2013.
 */
@RemoteServiceRelativePath("getVendorFeedback")
public interface VendorFeedbackService extends RemoteService {
    List<VendorFeedback> getVendorFeedback(String userId) throws IllegalArgumentException;
}