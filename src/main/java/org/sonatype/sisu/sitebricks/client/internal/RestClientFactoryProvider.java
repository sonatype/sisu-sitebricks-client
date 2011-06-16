/*******************************************************************************
 * Copyright (c) 2011 Sonatype, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 * The Eclipse Public License is available at
 *   http://www.eclipse.org/legal/epl-v10.html
 * The Apache License v2.0 is available at
 *   http://www.apache.org/licenses/LICENSE-2.0.html
 * You may elect to redistribute this code under either of these licenses.
 *******************************************************************************/
package org.sonatype.sisu.sitebricks.client.internal;

import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.spi.Toolable;
import org.sonatype.sisu.sitebricks.client.RestClientFactory;

import javax.inject.Inject;
import java.util.Map;

public class RestClientFactoryProvider<S>
    implements Provider<RestClientFactory<S>>
{

    private final Class<S> serviceInterface;

    private Injector injector;

    private final Map<String, String> bindings;

    public RestClientFactoryProvider( final Class<S> serviceInterface, final Map<String, String> bindings )
    {
        this.serviceInterface = serviceInterface;
        this.bindings = bindings;
    }

    @Override
    public RestClientFactory<S> get()
    {
        final DefaultRestClientFactory<S> restClientFactory =
            new DefaultRestClientFactory<S>( serviceInterface, bindings );
        injector.injectMembers( restClientFactory );
        return restClientFactory;
    }

    @Inject
    @Toolable
    void initialize( final Injector injector )
    {
        this.injector = injector;
    }

}