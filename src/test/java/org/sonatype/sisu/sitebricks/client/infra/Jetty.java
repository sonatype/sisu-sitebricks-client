package org.sonatype.sisu.sitebricks.client.infra;

import java.io.File;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;
import org.sonatype.sisu.sitebricks.SisuServletContextListener;

import com.google.inject.Injector;

public class Jetty
{
    private static final String APP_NAME = "/test";

    private static final int PORT = 0;

    private final Server server;

    public Jetty( final File path, final ClassLoader classLoader, final Injector injector )
    {
        final WebAppContext webAppContext = new WebAppContext( path.getAbsolutePath(), APP_NAME );
        webAppContext.setClassLoader( classLoader );
        webAppContext.getServletContext().setAttribute( SisuServletContextListener.INJECTOR_KEY, injector );
        server = new Server( PORT );
        server.addHandler( webAppContext );
    }

    public int getPort()
    {
        return server.getConnectors()[0].getLocalPort();
    }

    public void start()
    {
        try
        {
            server.start();
        }
        catch ( final Exception e )
        {
            throw new RuntimeException( e );
        }
    }

    public void join()
    {
        try
        {
            server.join();
        }
        catch ( final Exception e )
        {
            throw new RuntimeException( e );
        }
    }

    public void stop()
    {
        try
        {
            server.stop();
        }
        catch ( final Exception e )
        {
            throw new RuntimeException( e );
        }
    }
}
