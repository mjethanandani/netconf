/*
 * Copyright (c) 2016 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.restconf.nb.rfc8040.references;

import java.lang.ref.SoftReference;
import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import org.opendaylight.mdsal.dom.api.DOMMountPoint;
import org.opendaylight.restconf.nb.rfc8040.Rfc8040;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.common.Revision;
import org.opendaylight.yangtools.yang.model.api.EffectiveModelContext;
import org.opendaylight.yangtools.yang.model.api.Module;
import org.opendaylight.yangtools.yang.model.api.SchemaContext;

/**
 * This class creates {@link SoftReference} of actual {@link EffectiveModelContext}
 * object and even if the {@link SchemaContext} changes, this will be sticks
 * reference to the old {@link SchemaContext} and provides work with the old
 * {@link EffectiveModelContext}.
 *
 */
public final class SchemaContextRef {

    private final SoftReference<EffectiveModelContext> schemaContextRef;

    /**
     * Create {@link SoftReference} of actual {@link EffectiveModelContext}.
     *
     * @param schemaContext
     *             actual {@link EffectiveModelContext}
     */
    public SchemaContextRef(final EffectiveModelContext schemaContext) {
        this.schemaContextRef = new SoftReference<>(schemaContext);
    }

    /**
     * Get {@link EffectiveModelContext} from reference.
     *
     * @return {@link EffectiveModelContext}
     */
    public EffectiveModelContext get() {
        return this.schemaContextRef.get();
    }

    /**
     * Get all modules like {@link Collection} of {@link Module} from {@link SchemaContext}.
     *
     * @return {@link Collection} of {@link Module}
     */
    public Collection<? extends Module> getModules() {
        return get().getModules();
    }

    /**
     * Get {@link Module} by ietf-restconf qname from
     * {@link Rfc8040.RestconfModule}.
     *
     * @return {@link Module}
     */
    public Module getRestconfModule() {
        return this.findModuleByNamespaceAndRevision(Rfc8040.RestconfModule.IETF_RESTCONF_QNAME.getNamespace(),
                Rfc8040.RestconfModule.IETF_RESTCONF_QNAME.getRevision());
    }

    /**
     * Find {@link Module} in {@link SchemaContext} by {@link URI} and
     * {@link Date}.
     *
     * @param namespace
     *             namespace of module
     * @param revision
     *             revision of module
     * @return {@link Module}
     */
    public Module findModuleByNamespaceAndRevision(final URI namespace, final Optional<Revision> revision) {
        return this.get().findModule(namespace, revision).orElse(null);
    }

    /**
     * Find {@link Module} in {@link SchemaContext} of {@link DOMMountPoint} by
     * {@link QName} of {@link Module}.
     *
     * @param mountPoint
     *             mount point
     * @param moduleQname
     *             {@link QName} of module
     * @return {@link Module}
     */
    public Module findModuleInMountPointByQName(final DOMMountPoint mountPoint, final QName moduleQname) {
        final SchemaContext schemaContext = mountPoint == null ? null : mountPoint.getSchemaContext();
        return schemaContext == null ? null
                : schemaContext.findModule(moduleQname.getLocalName(), moduleQname.getRevision()).orElse(null);
    }

    /**
     * Find {@link Module} in {@link SchemaContext} by {@link QName}.
     *
     * @param moduleQname
     *             {@link QName} of module
     * @return {@link Module}
     */
    public Module findModuleByQName(final QName moduleQname) {
        return this.findModuleByNameAndRevision(moduleQname.getLocalName(), moduleQname.getRevision());
    }

    /**
     * Find {@link Module} in {@link SchemaContext} by {@link String} localName
     * and {@link Date} revision.
     *
     * @param localName
     *             local name of module
     * @param revision
     *             revision of module
     * @return {@link Module}
     */
    public Module findModuleByNameAndRevision(final String localName, final Optional<Revision> revision) {
        return this.get().findModule(localName, revision).orElse(null);
    }
}
