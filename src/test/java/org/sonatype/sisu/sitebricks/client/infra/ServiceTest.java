package org.sonatype.sisu.sitebricks.client.infra;

import java.io.File;
import java.util.Properties;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.sonatype.guice.bean.containers.InjectedTest;
import org.sonatype.sisu.sitebricks.SisuConfigurationModule;
import org.sonatype.sisu.sitebricks.SisuSitebricksModule;
import org.sonatype.sisu.sitebricks.client.server.ServicePackage;

import com.google.inject.Binder;
import com.google.inject.Injector;

public abstract class ServiceTest
    extends InjectedTest
{

    private File webappBaseDirectory;

    private Jetty server;

    @Override
    public void configure( final Properties properties )
    {
        super.configure( properties );
    }

    @Override
    @Before
    public void setUp()
    {
        if ( webappBaseDirectory == null )
        {
            webappBaseDirectory = new File( getBasedir(), "src/test/webapp" );
        }

        System.setProperty( "appMode", "dev" );
        super.setUp();
    }

    @Inject
    void start( final Injector injector )
    {
        //
        // Take the ClassLoader that is being used by the TestCase and make sure Jetty uses this for its WebAppContext.
        // We also pass in the
        // injector we have created so that we can stuff it into the ServletContext's attributes. While using the
        // SisuServletContextListener, the injector
        // will be taken from the attributes if it exists to provide some consistency during the testing.
        //
        server = new Jetty( webappBaseDirectory, getClass().getClassLoader(), injector );
        server.start();
    }

    @Override
    @After
    public void tearDown()
    {
        server.stop();
        super.tearDown();
    }

    @Override
    public void configure( final Binder binder )
    {
        binder.install( new SisuConfigurationModule( webappBaseDirectory, "test" ) );
        binder.install( new SisuSitebricksModule( ServicePackage.class.getPackage() ) );
    }

    protected String baseUrl()
    {
        return "http://localhost:" + server.getPort() + "/test";
    }

}
