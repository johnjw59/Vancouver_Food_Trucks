package com.sneakyxpress.webapp.client.services.persistuser;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sneakyxpress.webapp.shared.User;

import java.util.List;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("addUser")
public interface PersistUserService extends RemoteService {
	boolean persistNewUserToDatastore(User user) throws IllegalArgumentException;
	boolean changeUserStatus(String fbId, int type);
    List<User> getAllUsers() throws IllegalArgumentException;
	boolean removeUser(String userId);
}
