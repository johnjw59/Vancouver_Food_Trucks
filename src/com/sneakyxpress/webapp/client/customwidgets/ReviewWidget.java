package com.sneakyxpress.webapp.client.customwidgets;

import com.github.gwtbootstrap.client.ui.Modal;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.sneakyxpress.webapp.client.Sneaky_Xpress;
import com.sneakyxpress.webapp.client.facebook.FacebookTools;
import com.sneakyxpress.webapp.client.services.vendorfeedback.VendorFeedbackService;
import com.sneakyxpress.webapp.client.services.vendorfeedback.VendorFeedbackServiceAsync;
import com.sneakyxpress.webapp.shared.FormValidator;
import com.sneakyxpress.webapp.shared.VendorFeedback;
import org.cobogw.gwt.user.client.ui.Rating;

/**
 * Created by michael on 11/19/2013.
 */
public class ReviewWidget extends Composite {
    private final VendorFeedback f;
    private final Sneaky_Xpress module;
    private final HTMLPanel quote;
    private final HTMLPanel credit;

    public ReviewWidget(Sneaky_Xpress module, VendorFeedback f) {
        this.module = module;
        this.f = f;

        quote = new HTMLPanel("blockquote", "");
        initWidget(quote);

        credit = new HTMLPanel("small", "");

        updateWidget(f.getRating(), f.getReview());
    }

    private void updateWidget(int stars, String text) {
        // Update the review
        quote.clear();
        HTMLPanel review = new HTMLPanel("p", getStars(stars) + text);
        quote.add(review);

        // Get the vendor name
        String vendorName = f.getVendorName();
        if (vendorName.isEmpty()) {
            vendorName = f.getVendorId();
        }

        // Update the credit text
        credit.clear();
        credit.add(new InlineLabel(f.getAuthorName() + " reviewing " + vendorName));
        quote.add(credit);

        // Add modify buttons to the credit text if applicable
        addModifyButtons();
    }

    private String getStars(int num) {
        String stars = "    ";
        for (int i = 0; i < f.getRating(); i++) {
            stars = "<i class=\"icon-star\"></i>" + stars;
        }
        return stars;
    }

    private void addModifyButtons() {
        final FacebookTools facebook = module.FACEBOOK_TOOLS;

        Anchor shareAnchor = new Anchor("[Share]");

        shareAnchor.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String message = f.getAuthorName() + " reviewing " + f.getVendorName() + ": "
                        + f.getReview() + " (" + f.getRating() + " Stars)";
                FacebookTools.fbShare(message);
            }
        });

        credit.add(new InlineLabel(" "));
        credit.add(shareAnchor);

        if (facebook.isLoggedIn() && f.getAuthorId().equals(facebook.getUserId())) {
            /*
             * Update functionality
             */
            Anchor editAnchor = new Anchor("[Edit]");
            editAnchor.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    HTMLPanel form = new HTMLPanel("form", "");
                    HTMLPanel fieldSet = new HTMLPanel("fieldset", "<label>Rating</label>");

                    // The star rating
                    final Rating rating = new Rating(f.getRating(), 5);
                    fieldSet.add(rating);

                    // The text review
                    fieldSet.add(new HTML("<br><br>"));
                    fieldSet.add(new HTML("<label>Review (optional)</label>"));

                    final TextArea reviewText = new TextArea();
                    reviewText.addStyleName("input-block-level");
                    reviewText.setText(f.getReview());
                    fieldSet.add(reviewText);

                    // The submit button
                    fieldSet.add(new HTML("<br>"));

                    Button submitButton = new Button("Submit Review");

                    fieldSet.add(submitButton);
                    form.add(fieldSet);

                    final FlowPanel flow = new FlowPanel();
                    flow.add(form);

                    final Modal modal = module.addModal("Edit Review", flow);

                    submitButton.addStyleName("btn btn-info");
                    submitButton.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            try {
                                // This will thrown an exception if the validation fails
                                FormValidator.validateReview(rating.getValue(), reviewText.getText());

                                f.setRating(rating.getValue());
                                f.setReview(reviewText.getText());
                                f.setAuthorName(facebook.getUserName());

                                VendorFeedbackServiceAsync vendorFeedbackService = GWT.
                                        create(VendorFeedbackService.class);
                                vendorFeedbackService.persistVendorFeedback(f, new AsyncCallback<Void>() {
                                    @Override
                                    public void onFailure(Throwable caught) {
                                        module.addMessage(true, "Updating review failed. Reason: " + caught.getMessage());
                                        modal.hide();
                                    }

                                    @Override
                                    public void onSuccess(Void result) {
                                        module.addMessage(false, "Your review was successfully updated. Thank you!");
                                        updateWidget(rating.getValue(), reviewText.getText());
                                        modal.hide();
                                    }
                                });
                            } catch (IllegalArgumentException e) {
                                flow.insert(getMessageWidget(true, e.getMessage()), 0);
                            }
                        }
                    });
                }
            });

            /*
             * Delete functionality
             */
            Anchor deleteAnchor = new Anchor("[Delete]");
            deleteAnchor.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    HTMLPanel areYouSure = new HTMLPanel("<p class=\"lead pagination-centered\">"
                            + "Are you sure?</p>");
                    Button confirm = new Button("Confirm Deletion");
                    confirm.addStyleName("btn btn-danger btn-large btn-block");

                    areYouSure.add(confirm);
                    final Modal modal2 = module.addModal("Confirm Deletion", areYouSure);

                    confirm.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            VendorFeedbackServiceAsync vendorFeedbackService = GWT.
                                    create(VendorFeedbackService.class);
                            vendorFeedbackService.deleteVendorFeedback(f.getId(), new AsyncCallback<Void>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                    module.addMessage(true, "Deleting review failed. Reason: " + caught.getMessage());
                                    modal2.hide();
                                }

                                @Override
                                public void onSuccess(Void result) {
                                    module.addMessage(false, "Deletion of review was successful.");
                                    ReviewWidget.this.removeFromParent();
                                    modal2.hide();
                                }
                            });
                        }
                    });
                }
            });

            /*
             * Put it all together
             */
            credit.add(new InlineLabel(" "));
            credit.add(editAnchor);
            credit.add(new InlineLabel(" "));
            credit.add(deleteAnchor);
        }
    }

    private HTML getMessageWidget(boolean error, String info) {
        String type = error ? "alert-danger" : "alert-info";
        HTML alert = new HTML("<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-hidden=\"true\">"
                + "&times;</button>" + info);
        alert.addStyleName(type + " alert fade in");
        return alert;
    }
}
