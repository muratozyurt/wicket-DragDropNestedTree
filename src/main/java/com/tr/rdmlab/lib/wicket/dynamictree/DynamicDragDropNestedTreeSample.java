package com.tr.rdmlab.lib.wicket.dynamictree;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import com.tr.rdmlab.lib.wicket.dynamictree.core.DynamicDragDropNestedTree;

public class DynamicDragDropNestedTreeSample extends DynamicDragDropNestedTree  {

	
	
	
 SampleRecursiveObject sampleRecursiveObject;

	public DynamicDragDropNestedTreeSample(String id, SampleRecursiveObject sampleRecursiveObject, DynamicDragDropNestedTreeSample parentPanel) {
		
		super(id, parentPanel);
		this.sampleRecursiveObject = sampleRecursiveObject;
		getNameFromObject();
		updateChildren();
		 
		initContentPanel();
		 
	}

	private void initContentPanel() {
		
		toggleContentPanel(sampleRecursiveObject.isContentExpanded());
		contentPanel.add(new Label("childMarkupLabel","This is in the child markup."));
		
	}


	static {
		/**
		 * You can add your own language version here
		 */
		// TURKISH: 
//		newItem = "Yeni Kayıt";
//		open = "Aç";
//		close = "Kapat";
//		newChild = "Yeni";
//		delete = "Sil";
//		cannotDeleteThisRecord = "Bu kaydı silemezsiniz..";
//		cannotDeleteRecordsWithSubRecords = "Alt birimleri olan üst birimleri silemezsiniz. Öncelikle alt birimleri silmeniz gerekir."; 
//		cannotMoveToChild = "Alt birime taşıyamazsınız. Öncelikle alt birimi başka bir konuma taşımanız gerekir.";
//		
		
		
	} 

	@Override
	protected void updateChildList() {
		for (SampleRecursiveObject bu : sampleRecursiveObject.getChildObjects()) {
			 
			children.add(Model.of((DynamicDragDropNestedTree) new DynamicDragDropNestedTreeSample(childPanels.newChildId(), bu, this)));
		}
		
		toggleChildren(sampleRecursiveObject.isPanelExpanded());
		
	}

	@Override
	protected void toggleChildrenExtension(boolean childrenVisible) {
		sampleRecursiveObject.setPanelExpanded(childrenVisible);
		
	}
	
	@Override
	protected void toggleContentExtension(boolean contentVisible) {
		sampleRecursiveObject.setContentExpanded(contentVisible);
		
	}
	

	@Override
	protected void newChild() {
		SampleRecursiveObject bu = new SampleRecursiveObject(sampleRecursiveObject, newItem); 
		 ((WicketApplication)getApplication()).dataStore().saveObject(bu);
		 
		
	}

	@Override
	protected void setObjectName(String name) {
		sampleRecursiveObject.setName(name);
		
	}

	@Override
	protected void getNameFromObject() {
		name = sampleRecursiveObject.getName();
		
	}

	@Override
	protected boolean canDeleteObject() {
		 

		return sampleRecursiveObject.getParentObject() != null && 
		sampleRecursiveObject.getParentObject() != sampleRecursiveObject && 
		sampleRecursiveObject.getParentObject().getId() != sampleRecursiveObject.getId();
	}

	@Override
	protected void addDraggedChildObject(DynamicDragDropNestedTree draggedChild) {
		SampleRecursiveObject draggedsampleRecursiveObject = ((DynamicDragDropNestedTreeSample)draggedChild).sampleRecursiveObject;
		draggedsampleRecursiveObject.getParentObject().getChildObjects().remove(draggedsampleRecursiveObject);
		draggedsampleRecursiveObject.setParentObject(this.sampleRecursiveObject);
		
		this.sampleRecursiveObject.getChildObjects().add(draggedsampleRecursiveObject);
		this.sampleRecursiveObject.setPanelExpanded(true);
		
		((WicketApplication)getApplication()).dataStore().saveObject(draggedsampleRecursiveObject);
		
	}

	@Override
	public void feedbackMessage(String message, int type, AjaxRequestTarget target) {
		((HomePage)getPage()).feedback(message, type, target);
		
	}

	@Override
	protected  void deleteObject(AjaxRequestTarget target) {

		final ConfirmDialog confirm = 
				new ConfirmDialog("Do you want to delete this record?", name , (HomePage)getPage() ) { 
				 
					private static final long serialVersionUID = 1L;

					@Override
					protected void onConfirm(AjaxRequestTarget target) { 
						
						
						((WicketApplication) getApplication()).dataStore().deleteObject(DynamicDragDropNestedTreeSample.this.sampleRecursiveObject);
						parentPanel.updateChildren();
						parentPanel.remove(DynamicDragDropNestedTreeSample.this);
						
						target.add(parentPanel);
						  
					}
					
					@Override
					protected void onCancel() {
						// TODO Auto-generated method stub
						 
					}
				}; 
				confirm.show(target);  
		
	}

	
	
	

}
