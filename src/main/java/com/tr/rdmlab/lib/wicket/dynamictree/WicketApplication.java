package com.tr.rdmlab.lib.wicket.dynamictree;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.lang.Bytes;

import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.themes.markup.html.bootswatch.BootswatchTheme;
import de.agilecoders.wicket.themes.markup.html.bootswatch.BootswatchThemeProvider;

/**
 * Application object for your web application.
 * If you want to run this application without deploying, run the Start class.
 * 
 * @see com.tr.rdm.bounbudgeter.Start#main(String[])
 */
public class WicketApplication extends WebApplication
{
	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends WebPage> getHomePage()
	{
		return HomePage.class;
	}

	private DataStore dataStore;
	
	public DataStore dataStore()
	{
		return dataStore;
	}
	/**
	 * @see org.apache.wicket.Application#init()
	 */
	@Override
	public void init()
	{
		super.init();
		 dataStore = new DataStore(this);
		//getComponentInstantiationListeners().add(new JavaEEComponentInjector(this));
		
		
		// add your configuration here
		getStoreSettings().setMaxSizePerSession(Bytes.kilobytes(500));
		
		getStoreSettings().setInmemoryCacheSize(50);
		
		//getApplicationSettings().setPageExpiredErrorPage(CustomExpiredErrorPage.class);
		getApplicationSettings().setPageExpiredErrorPage(HomePage.class);
		getApplicationSettings().setAccessDeniedPage(HomePage.class);
		getApplicationSettings().setInternalErrorPage(HomePage.class);
	 
		
		
		Bootstrap.install(this);
		Bootstrap.getSettings( ).setThemeProvider(new BootswatchThemeProvider(BootswatchTheme.Cerulean));
		
		 
	}
}
