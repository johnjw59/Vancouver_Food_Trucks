package com.sneakyxpress.webapp.client.pages;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.sneakyxpress.webapp.client.pages.Content;

/**
 * Handles actions invoked by clicking links in the navigation bar
 */
public class PageClickHandler implements ClickHandler {
    private Content page;
    private String input;

    public PageClickHandler(Content page, String input) {
        super();
        this.page = page;
        this.input = input;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (input.equals("")) {
            History.newItem(page.getPageStub());
        } else {
            History.newItem(page.getPageStub() + "?" + input);
        }
    }
}
