package com.tr.rdmlab.lib.wicket.dynamictree;

import java.util.TreeMap;

public class DataStore {

	WicketApplication application;
	SampleRecursiveObject rootObject = new SampleRecursiveObject(getnextid(),	null, "Root");
	public TreeMap<Long, SampleRecursiveObject> objectStore = new TreeMap<Long, SampleRecursiveObject>();
	long idseq = System.currentTimeMillis();

	public SampleRecursiveObject getRootObject() {
		return rootObject;
	}

	public DataStore(WicketApplication application) {
		this.application = application;
		initBounUnits(rootObject);
	}

	public void saveObject(SampleRecursiveObject object) {
		if (object.getId() <= 0) {
			object.setId(getnextid());
		}

		objectStore.put(object.getId(), object);
	}
	
	public void deleteObject(SampleRecursiveObject object)
	{
		object.getParentObject().getChildObjects().remove(object);
		objectStore.remove(object.getId());
	}

	long getnextid() {
		return ++idseq;
	}

	private void initBounUnits(SampleRecursiveObject root) {

		objectStore.clear();
		rootObject.setPanelExpanded(true);
		objectStore.put(rootObject.getId(), rootObject);

		SampleRecursiveObject gs = new SampleRecursiveObject(getnextid(), root,
				"A Object");
		objectStore.put(gs.getId(), gs);
		{
			SampleRecursiveObject ii = new SampleRecursiveObject(getnextid(),
					gs, "AA Object");
			objectStore.put(ii.getId(), ii);
			{
				SampleRecursiveObject ii1 = new SampleRecursiveObject(
						getnextid(), ii, "AAA Object");
				objectStore.put(ii1.getId(), ii1);
				SampleRecursiveObject ii2 = new SampleRecursiveObject(
						getnextid(), ii, "AAB Object");
				objectStore.put(ii2.getId(), ii2);
			}
			SampleRecursiveObject fooAB = new SampleRecursiveObject(
					getnextid(), gs, "AB Object");
			objectStore.put(fooAB.getId(), fooAB);

		}

		SampleRecursiveObject fooB = new SampleRecursiveObject(getnextid(),
				root, "B Object");
		objectStore.put(fooB.getId(), fooB);
		{

		}

		SampleRecursiveObject fooC = new SampleRecursiveObject(getnextid(),
				root, "C Object");
		objectStore.put(fooC.getId(), fooC);
		SampleRecursiveObject fooD = new SampleRecursiveObject(getnextid(),
				root, "D Object");
		objectStore.put(fooD.getId(), fooD);

	}

}
