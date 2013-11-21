package com.sneakyxpress.webapp.server.services;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sneakyxpress.webapp.client.services.persistuser.PersistUserService;
import com.sneakyxpress.webapp.server.PMF;
import com.sneakyxpress.webapp.shared.User;

/**
 * Updates the Food Vendor data from DataVancouver
 */
public class PersistUserServiceImpl extends RemoteServiceServlet implements
		PersistUserService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static final Logger logger = Logger.getLogger("");

	@Override
	public boolean persistNewUserToDatastore(User user)
			throws IllegalArgumentException
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Query q = pm.newQuery("SELECT UNIQUE FROM " + User.class.getName()
				+ " WHERE id == \"" + user.getId() + "\"");
		User result = (User) q.execute();

		boolean existingMember = result != null;
		if (existingMember)
		{
			// Update the existing user's information
			result.setName(user.getName());
			result.setEmail(user.getEmail());
		}
		else
		{
			// Create the new user
			pm.makePersistent(user);
		}

		pm.close();
		return existingMember;
	}

	/**
	 * 
	 * @param fbId
	 * @param type
	 *            1-Admin, 2-Owner, 3-User, 4-Admin&Owner as defined in User
	 *            class
	 * @return
	 */
	public boolean changeUserStatus(String fbId, int type)
	{
		try
		{
			PersistenceManager pm = PMF.get().getPersistenceManager();
			User user = pm.getObjectById(User.class, fbId);
			user.setType(type);
			pm.close();
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	@Override
	public List<User> getAllUsers() throws IllegalArgumentException
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery();
		q.setClass(User.class);

		@SuppressWarnings("unchecked")
		List<User> results = (List<User>) q.execute();

		pm.close();

		return new ArrayList<User>(results);
	}

	@Override
	public boolean removeUser(String userId)
	{
		try
		{
			PersistenceManager pm = PMF.get().getPersistenceManager();
			User u = (User) pm.getObjectById(userId);
			
			if(u == null)
			{
				return false;
			}
			
			pm.deletePersistent(u);
			return true;
		}
		catch (Exception e)
		{
			logger.log(Level.SEVERE, "removeUser: " + e.getMessage());
		}

		return false;

	}

}
