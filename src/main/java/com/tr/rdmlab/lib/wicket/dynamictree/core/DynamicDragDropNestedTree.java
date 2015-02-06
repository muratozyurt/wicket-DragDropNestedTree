package com.tr.rdmlab.lib.wicket.dynamictree.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableLabel;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import com.googlecode.wicket.jquery.ui.interaction.draggable.Draggable;
import com.googlecode.wicket.jquery.ui.interaction.droppable.Droppable;
 

public abstract class DynamicDragDropNestedTree   extends Panel {
 
	private static final long serialVersionUID = 1L; 
	
	String toggleChildrenLabel = " ";
	String toggleContentLabel = open;
	String deleteLabel = delete;
	String newChildLabel = newChild;
	
	
	protected static String open = "Open";
	protected static String close = "Close";
	protected static String newChild = "New";
	protected static String newItem = "New Item";	
	protected static String delete = "Delete";
	protected static String cannotDeleteThisRecord = "You cannot delete this record";
	protected static String cannotDeleteRecordsWithSubRecords = "Cannot delete a record which has connected sub records. Try moving or deleting the children first.";
	protected static String cannotMoveToChild = "Cannot be moved to a child panel. Try moving the child to elsewhere first."; 
	
	
	protected String name ="";
 
	 
	protected List<IModel< DynamicDragDropNestedTree>> children = new ArrayList<IModel< DynamicDragDropNestedTree>>();
	
	protected RefreshingView< DynamicDragDropNestedTree> childPanels = new RefreshingView< DynamicDragDropNestedTree >("childPanel") {

		@Override
		protected   Iterator<IModel<DynamicDragDropNestedTree>> getItemModels() {
			 
			return children.iterator();
		}

		@Override
		protected   void populateItem(Item<DynamicDragDropNestedTree> item) {
			addOrReplace(item.getModelObject()); 
			
		}
		
	


		private static final long serialVersionUID = 1L;
	
		
	};  
	
	int level= 0;
			
	protected DynamicDragDropNestedTree parentPanel;
	CollapsePanelDraggable draggable; 
	CollapsePanelDroppable dropppable; 
	
	static String CONTENT_PANEL_ID = "contentPanel";
	
	WebMarkupContainer header = new WebMarkupContainer("header"); 
	protected WebMarkupContainer contentPanel = new WebMarkupContainer(CONTENT_PANEL_ID);
	
	
	// root may change due to drag and drop.  
	DynamicDragDropNestedTree getRootCollapsePanel()
	{ 	 
		DynamicDragDropNestedTree rootPanel = this;
		while(rootPanel.parentPanel != null)
		{ 			
			rootPanel = rootPanel.parentPanel;			
		}		
		return rootPanel;
	}
	
	 class CollapsePanelDroppable extends Droppable<Void> {
		 
		public CollapsePanelDroppable(String id) {
			super(id); 
		}

		private static final long serialVersionUID = 1L;

		@Override
		public void onDrop(AjaxRequestTarget target, Component component)
		{
			DynamicDragDropNestedTree rootPanel = getRootCollapsePanel();
					
			if (component != null)
			{
				Object object = component.getDefaultModelObject();

				if (component instanceof CollapsePanelDraggable)
				{
					CollapsePanelDraggable dragged = ((CollapsePanelDraggable) component);
					 
					if(dragged.panel == DynamicDragDropNestedTree.this)
					{
						// dragged to the same unit header. 
						return;
					}					
					if(dragged.panel.parentPanel == DynamicDragDropNestedTree.this)
						
					{
						// dragged to the same parent header. 
						return;
					}
					
					ArrayList<DynamicDragDropNestedTree> ancestors = getAncestors(); 
					 
					for(DynamicDragDropNestedTree ancestor: ancestors)
					{	
						if( dragged.panel == ancestor)
						{
							// cannot move to a child unit
							feedbackMessage (cannotMoveToChild , FeedbackMessage.ERROR,target);
							return;
						} 
					}					 
					
					addDraggedChildObject(dragged.panel);
					
					rootPanel.updateChildren();
					
					target.add(rootPanel); 
				}
			} 
		}
	};
	
	ArrayList<DynamicDragDropNestedTree> getAncestors()
	{
		ArrayList<DynamicDragDropNestedTree> ancestors = new ArrayList<DynamicDragDropNestedTree>();
		DynamicDragDropNestedTree root = this;
		while(root.parentPanel != null)
		{
			root = root.parentPanel;
			ancestors.add(root);
		}
		
		return ancestors;
	}
	
	abstract protected void addDraggedChildObject(DynamicDragDropNestedTree draggedChild);
	abstract protected void feedbackMessage(String message, int type, AjaxRequestTarget target);
	 
	class CollapsePanelDraggable extends Draggable<Void>{
 
		private static final long serialVersionUID = 1L;
		DynamicDragDropNestedTree panel = DynamicDragDropNestedTree.this; 
		
		public CollapsePanelDraggable(String id) {
			super(id);			
			this.setAxis(Axis.Y);			
			if(getRootCollapsePanel() == DynamicDragDropNestedTree.this)
			{
				//setAxis(Axis.X);
				// do not move root
				setContainment(this);
				nameLabel.setEnabled(false);
			}
			//setContainment(root);			
			setRevert(true);			 
		}  
	}

	AjaxLink<String> toggleChildPanel = new AjaxLink<String>("toggleChildPanel" ) { 
		
		private static final long serialVersionUID = 1L; 
		
		@Override
		public void onClick(AjaxRequestTarget target) {
			toggleChildren(!childPanels.isVisible());
			target.add(DynamicDragDropNestedTree.this);
			 
		}
	};
	

	
	protected void toggleChildren(boolean childrenVisible)
	{
		if(children.size()<= 0)
		{
			return;
		}
		
		childPanels.setVisible(childrenVisible);
		//addOrReplace(childrenVisible ? toggleTextLabelVisible : toggleTextLabelHidden);
		toggleChildrenLabel = childrenVisible ? "-" : "+";
		
		toggleChildrenExtension(childrenVisible);
		
	}
	
	abstract protected void toggleChildrenExtension(boolean childrenVisible);
	
	AjaxLink<String> add=new AjaxLink<String>("add"){
 
		private static final long serialVersionUID = 1L;

		@Override
		public void onClick(AjaxRequestTarget target) {
			 newChild(); 
			 updateChildren();
			 toggleChildren(true);
			 target.add(DynamicDragDropNestedTree.this); 
		} 
	};
	 
	abstract protected void newChild() ;
	 
	AjaxLink<String> openContent=new AjaxLink<String>("open"){
 
		private static final long serialVersionUID = 1L;

		 
		@Override
		public void onClick(AjaxRequestTarget target) {

			contentPanel.setVisible(!contentPanel.isVisible());
			toggleContentLabel = contentPanel.isVisible() ? close : open;
			target.add(contentPanel); 
			target.add(this);
		} 
		
	}; 
	 
	
	AjaxLink<String> deleteObject=new AjaxLink<String>("delete"){
	 
		private static final long serialVersionUID = 1L;
		

		@Override
		public void onClick(AjaxRequestTarget target) {
			 if(children.size()>0)
			 {  feedbackMessage(cannotDeleteRecordsWithSubRecords , FeedbackMessage.ERROR, target);
				 return;
			 }
			 
			 
				if(!(canDelete()))
				{// cannot remove the root entity. Should not be visible at all. Just in case.   
					feedbackMessage(cannotDeleteThisRecord , FeedbackMessage.ERROR, target); 
					return;
				} 
			 
				deleteObject(target);
		}
		
	};
	
	
	abstract protected void deleteObject(AjaxRequestTarget target);
 
	AjaxEditableLabel<String> nameLabel = new AjaxEditableLabel<String>("nameField",new PropertyModel<String>(this, "name")) {
		 
		private static final long serialVersionUID = 1L;

		@Override
		protected void onSubmit(AjaxRequestTarget target) {
			super.onSubmit(target);
			setObjectName(name);
				
		} 
		};  
		 
		
	abstract protected void setObjectName(String name);
	abstract protected void getNameFromObject();
	
	public DynamicDragDropNestedTree(String id,  DynamicDragDropNestedTree parentPanel) {
		super(id);
		
		
		this.parentPanel = parentPanel;
		if(parentPanel == null)
		{	
			// root panel
			// add tree to the root panel for moving boununits to a selected unit.
			//((BounUnitTreeProvider)tree.getProvider()).addRoot(this.bounUnit);  
		}
		
		else
		{
			level = parentPanel.level+1;
			//tree.setVisible(false);
			 
		}
		
		
		draggable = new CollapsePanelDraggable("dragarea") ; 
		dropppable = new CollapsePanelDroppable("droparea"); 
		
		 
		
		header.add(nameLabel); 
		toggleChildPanel.add(new Label("toggleChildrenLabel", new PropertyModel<String>(this, "toggleChildrenLabel")));
		openContent.add(new Label("toggleContentLabel", new PropertyModel<String>(this, "toggleContentLabel")));
		deleteObject.add(new Label("deleteLabel", new PropertyModel<String>(this, "deleteLabel")));
		add.add(new Label("newChildLabel", new PropertyModel<String>(this, "newChildLabel")));
		
		header.add(toggleChildPanel);
		
		header.add(deleteObject);
		header.add(add); 
		
		header.add(openContent);
		draggable.add(header);
		openContent.setVisible(level>0);
		
		dropppable.add(draggable);
		
		add(dropppable);
		//add(tree);
	 
		
		
		
		setOutputMarkupId(true); 
		contentPanel.setVisible(false);
		//contentPanel.setOutputMarkupId(true);
		contentPanel.setOutputMarkupPlaceholderTag(true);
		add(contentPanel);
		
		add(childPanels); 
	} 
	 
	public void updateChildren(){
		
		children.clear();
		updateChildList();
		
		
		toggleChildPanel.setEnabled(children.size()>0); 
		//toggleChildPanel.addOrReplace(children.size()<=0 ? toggleTextLabelNoChild : childPanels.isVisible() ? toggleTextLabelVisible : toggleTextLabelHidden);
	 		
		toggleChildrenLabel = children.size()<=0 ? " " : childPanels.isVisible() ? "-" : "+";
		deleteObject.setVisible(canDelete());
		
	}
	
	abstract protected void updateChildList();
	 
	boolean canDelete()
	{
		return parentPanel != null && 
				children.size()<=0 && canDeleteObject();
	}
	
	abstract protected boolean canDeleteObject();
	

}
