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
package io.prestosql.sql.tree;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.MoreObjects.toStringHelper;
import static java.util.Objects.requireNonNull;

public class GrantRoles
        extends Statement
{
    private final Set<Identifier> roles;
    private final Set<PrincipalSpecification> grantees;
    private final boolean withAdminOption;
    private final Optional<GrantorSpecification> grantor;
    private final Optional<Identifier> catalog;

    public GrantRoles(
            NodeLocation location,
            Set<Identifier> roles,
            Set<PrincipalSpecification> grantees,
            boolean withAdminOption,
            Optional<GrantorSpecification> grantor,
            Optional<Identifier> catalog)
    {
        this(Optional.of(location), roles, grantees, withAdminOption, grantor, catalog);
    }

    public GrantRoles(
            Set<Identifier> roles,
            Set<PrincipalSpecification> grantees,
            boolean withAdminOption,
            Optional<GrantorSpecification> grantor,
            Optional<Identifier> catalog)
    {
        this(Optional.empty(), roles, grantees, withAdminOption, grantor, catalog);
    }

    private GrantRoles(
            Optional<NodeLocation> location,
            Set<Identifier> roles,
            Set<PrincipalSpecification> grantees,
            boolean withAdminOption,
            Optional<GrantorSpecification> grantor,
            Optional<Identifier> catalog)
    {
        super(location);
        this.roles = ImmutableSet.copyOf(requireNonNull(roles, "roles is null"));
        this.grantees = ImmutableSet.copyOf(requireNonNull(grantees, "grantees is null"));
        this.withAdminOption = withAdminOption;
        this.grantor = requireNonNull(grantor, "grantor is null");
        this.catalog = requireNonNull(catalog, "catalog is null");
    }

    public Set<Identifier> getRoles()
    {
        return roles;
    }

    public Set<PrincipalSpecification> getGrantees()
    {
        return grantees;
    }

    public boolean isWithAdminOption()
    {
        return withAdminOption;
    }

    public Optional<GrantorSpecification> getGrantor()
    {
        return grantor;
    }

    public Optional<Identifier> getCatalog()
    {
        return catalog;
    }

    @Override
    public List<? extends Node> getChildren()
    {
        return ImmutableList.of();
    }

    @Override
    public <R, C> R accept(AstVisitor<R, C> visitor, C context)
    {
        return visitor.visitGrantRoles(this, context);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GrantRoles grantRoles = (GrantRoles) o;
        return withAdminOption == grantRoles.withAdminOption &&
                Objects.equals(roles, grantRoles.roles) &&
                Objects.equals(grantees, grantRoles.grantees) &&
                Objects.equals(grantor, grantRoles.grantor) &&
                Objects.equals(catalog, grantRoles.catalog);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(roles, grantees, withAdminOption, grantor, catalog);
    }

    @Override
    public String toString()
    {
        return toStringHelper(this)
                .add("roles", roles)
                .add("grantees", grantees)
                .add("withAdminOption", withAdminOption)
                .add("grantor", grantor)
                .add("catalog", catalog)
                .toString();
    }
}
