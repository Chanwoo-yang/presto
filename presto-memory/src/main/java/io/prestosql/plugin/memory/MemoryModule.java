/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.prestosql.plugin.memory;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;
import io.prestosql.spi.NodeManager;
import io.prestosql.spi.type.Type;
import io.prestosql.spi.type.TypeManager;

import javax.inject.Inject;

import static io.airlift.configuration.ConfigBinder.configBinder;
import static io.prestosql.spi.type.TypeSignature.parseTypeSignature;
import static java.util.Objects.requireNonNull;

public class MemoryModule
        implements Module
{
    private final TypeManager typeManager;
    private final NodeManager nodeManager;

    public MemoryModule(TypeManager typeManager, NodeManager nodeManager)
    {
        this.typeManager = requireNonNull(typeManager, "typeManager is null");
        this.nodeManager = requireNonNull(nodeManager, "nodeManager is null");
    }

    @Override
    public void configure(Binder binder)
    {
        binder.bind(TypeManager.class).toInstance(typeManager);
        binder.bind(NodeManager.class).toInstance(nodeManager);

        binder.bind(MemoryConnector.class).in(Scopes.SINGLETON);
        binder.bind(MemoryMetadata.class).in(Scopes.SINGLETON);
        binder.bind(MemorySplitManager.class).in(Scopes.SINGLETON);
        binder.bind(MemoryPagesStore.class).in(Scopes.SINGLETON);
        binder.bind(MemoryPageSourceProvider.class).in(Scopes.SINGLETON);
        binder.bind(MemoryPageSinkProvider.class).in(Scopes.SINGLETON);
        configBinder(binder).bindConfig(MemoryConfig.class);
    }

    public static final class TypeDeserializer
            extends FromStringDeserializer<Type>
    {
        private final TypeManager typeManager;

        @Inject
        public TypeDeserializer(TypeManager typeManager)
        {
            super(Type.class);
            this.typeManager = requireNonNull(typeManager, "typeManager is null");
        }

        @Override
        protected Type _deserialize(String value, DeserializationContext context)
        {
            return typeManager.getType(parseTypeSignature(value));
        }
    }
}
