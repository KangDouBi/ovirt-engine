package org.ovirt.engine.core.utils.serialization.json;

import java.util.Collection;

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.codehaus.jackson.map.annotate.JsonTypeResolver;

@SuppressWarnings("serial")
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY)
@JsonTypeResolver(JsonCustomTypeResolverBuilder.class)
public abstract class JsonCollectionMixIn implements Collection {
}
