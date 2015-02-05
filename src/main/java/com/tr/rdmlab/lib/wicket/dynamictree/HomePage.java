/**
 * 
 */
package com.tr.rdmlab.lib.wicket.dynamictree;

import java.util.Locale;

import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.time.Duration;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;

/**
 * @author murat.ozyurt
 *
 */
public class HomePage extends WebPage {

	private static final long serialVersionUID = 1L; 
	NotificationPanel feedback = new NotificationPanel("feedback")
			.hideAfter(Duration.seconds(15)); 

	final ModalWindow modalWindow = new ModalWindow("modalWindow");

	public HomePage(final PageParameters parameters) {
		super(parameters);
		setVersioned(false); // disables back button

		Session.get().setLocale(new Locale("tr", "TR"));
		commonPanels();

	}

	void commonPanels() {

		feedback.setOutputMarkupId(true);

		add(feedback);
add(modalWindow);
		SampleRecursiveObject rootUnit = ((WicketApplication) getApplication())
				.dataStore().getRootObject();

		add(new DynamicDragDropNestedTreeSample("unitrootrepeater", rootUnit, null));
 
		feedback("Welcome", FeedbackMessage.DEBUG);

	}

	/**
	 * 
	 * @param message
	 * @param type
	 *            : e.g. //FeedbackMessage.ERROR
	 */
	public void feedback(String message, int type) {
		FeedbackMessage fm = new FeedbackMessage(this, message, type);
		feedback.getFeedbackMessages().add(fm);

	}

	public void feedback(String message, int type, AjaxRequestTarget target) {
		feedback(message, type);
		target.add(feedback);

	}

}
