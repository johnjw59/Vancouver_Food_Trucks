package com.sneakyxpress.webapp.client.customwidgets.navbars.tabs;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.sneakyxpress.webapp.client.Sneaky_Xpress;

/**
 * Created by michael on 11/14/2013.
 */
public class LogoutTab extends AbstractNavbarTab {
    private final Sneaky_Xpress module;

    public LogoutTab(Sneaky_Xpress module) {
        this.module = module;
    }

    @Override
    public String getTitle() {
        return "Logout";
    }

    @Override
    public FlowPanel getContent() {
        FlowPanel logout = new FlowPanel();
        logout.addStyleName("well");
		HTMLPanel info = new HTMLPanel(
				"<p class=\"lead pagination-centered\">Click to logout. See you soon!</p>");
		logout.add(info);
        
        logout.add(getButtonWidget(module.FACEBOOK_TOOLS.getLogoutButton()));
        return logout;
    }
}
