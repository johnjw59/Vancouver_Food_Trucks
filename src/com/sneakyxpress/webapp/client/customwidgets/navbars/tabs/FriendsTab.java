package com.sneakyxpress.webapp.client.customwidgets.navbars.tabs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.sneakyxpress.webapp.client.Sneaky_Xpress;
import com.sneakyxpress.webapp.client.customwidgets.simpletable.SimpleTable;
import com.sneakyxpress.webapp.client.pages.PageClickHandler;
import com.sneakyxpress.webapp.client.services.friendsservice.FriendsService;
import com.sneakyxpress.webapp.client.services.friendsservice.FriendsServiceAsync;
import com.sneakyxpress.webapp.shared.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 11/14/2013.
 */
public class FriendsTab extends AbstractNavbarTab {
    private final Sneaky_Xpress module;
    private final User user;

    public FriendsTab(Sneaky_Xpress module, User user) {
        this.module = module;
        this.user = user;
    }

    @Override
    public String getTitle() {
        return "Friends";
    }

    @Override
    public FlowPanel getContent() {
        final FlowPanel friends = new FlowPanel();

        List<String> friendsIds = new ArrayList<String>(module.FACEBOOK_TOOLS.getUserFriends().values());

        FriendsServiceAsync friendsService = GWT.
                create(FriendsService.class);
        friendsService.getUserFriends(user.getId(), friendsIds,
                new AsyncCallback<List<User>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        module.addMessage(true, "Loading friends! Reason: " +
                                caught.getMessage());
                    }

                    @Override
                    public void onSuccess(List<User> result) {
                        if (result.isEmpty()) {
                            HTMLPanel response = new HTMLPanel("p", "No friends could be found :(");
                            response.addStyleName("lead pagination-centered");
                            friends.add(response);
                            friends.addStyleName("well");
                        } else {
                            HTMLPanel header = new HTMLPanel("p", "You have " + result.size() + " friends!");
                            header.addStyleName("lead");
                            friends.add(header);

                            SimpleTable friendsTable = new SimpleTable("table-bordered", "Friends Name");
                            for (User u : result) {
                                friendsTable.addRow(new PageClickHandler(module.PROFILE_PAGE, u.getId()),
                                        u.getName());
                            }
                            friendsTable.sortRows(0, false);
                            friends.add(friendsTable);
                        }
                    }
                });

        return friends;
    }
}
