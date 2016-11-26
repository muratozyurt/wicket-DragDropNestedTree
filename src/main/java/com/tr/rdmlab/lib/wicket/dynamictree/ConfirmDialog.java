package com.tr.rdmlab.lib.wicket.dynamictree;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;

public abstract  class ConfirmDialog extends ModalWindow {
	   
	HomePage page ; 
	 
	private static final long serialVersionUID = 1L;
		public ConfirmDialog(String title, String message, final HomePage page  ) {
	         super( "modalWindow");
	          setAutoSize(true);
	          setMinimalWidth(250);
	         this.page = page;
	         WebMarkupContainer content = new WebMarkupContainer("content");
	         setContent(content);
	         content.add(new Label("title", title));
	         content.add(new Label("message", message)); 
	         content.add(new AjaxLink<String>("confirm") {
	          
			private static final long serialVersionUID = 1L;

			@Override
	          public void onClick(AjaxRequestTarget target) {
				 close(target);
				  
				onConfirm(target);  
	          }
	        });
	     
	         content.add(new AjaxLink<String>("cancel") {
	        
			private static final long serialVersionUID = 1L;

			@Override
	          public void onClick(AjaxRequestTarget target) {
				close(target);
				onCancel();
	            
	          }
	        });  
	     }
	    @Override
	    public void show(AjaxRequestTarget target) {
	    	page.replace(this);
	    	super.show(target);
	    	
	    };
		 
	     protected abstract void onCancel();
	     protected abstract void onConfirm(AjaxRequestTarget target);
	}
 
