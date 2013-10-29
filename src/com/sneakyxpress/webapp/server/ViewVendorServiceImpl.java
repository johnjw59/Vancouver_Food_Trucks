package com.sneakyxpress.webapp.server;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sneakyxpress.webapp.client.ViewVendorService;
import com.sneakyxpress.webapp.shared.FoodVendor;

import java.util.List;

/**
 * The server side implementation of the RPC service.
 */
public class ViewVendorServiceImpl extends RemoteServiceServlet implements
        ViewVendorService {

    @Override
    public FoodVendor viewVendorServer(String id) throws IllegalArgumentException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query q = pm.newQuery("SELECT UNIQUE FROM " + FoodVendor.class.getName()
                + " WHERE vendorId == \"" + id + "\"");
        FoodVendor result = (FoodVendor) q.execute();

        if (result == null) { // Not sure if this works
            throw new IllegalArgumentException("No vendor with an ID of " + id + " could be found!");
        } else {
            return result;
        }
    }
}
