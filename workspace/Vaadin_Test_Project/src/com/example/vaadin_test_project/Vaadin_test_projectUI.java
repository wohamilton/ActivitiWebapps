package com.example.vaadin_test_project;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.woodlodge.javaclasses.StringUpdater;

@SuppressWarnings("serial")
@Theme("vaadin_test_project")
public class Vaadin_test_projectUI extends UI {
	
	String enteredValue;

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = Vaadin_test_projectUI.class)
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		setContent(layout);
		
		
		// Create a text field
		TextField tf = new TextField("Please enter lower case value");
		        
		// Handle changes in the value
		tf.addValueChangeListener(new Property.ValueChangeListener() {
		    public void valueChange(ValueChangeEvent event) {
		        // Assuming that the value type is a String
		        enteredValue = (String) event.getProperty().getValue();
		        
		    }

		});
		
		
		Button button = new Button("Convert to upper");
		button.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {

		        StringUpdater stringUpdater = new StringUpdater();
		        String upperVal = stringUpdater.setToUppercase(enteredValue);
		        
		        Notification.show("Upper Case Value: " + upperVal);
			}
		});


		// Fire value changes immediately when the field loses focus
		tf.setImmediate(true);
		
		
		layout.addComponent(tf);
		layout.addComponent(button);
		
	}

}