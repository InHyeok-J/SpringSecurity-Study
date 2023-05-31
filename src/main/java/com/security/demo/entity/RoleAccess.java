package com.security.demo.entity;


import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "role_access", uniqueConstraints = {
    @UniqueConstraint(name = "role_access_resource", columnNames = {"role_id", "resource_id"})
})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoleAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_access_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id")
    private Resource resource;

    public RoleAccess(Role role, Resource resource) {
        this.role = role;
        this.resource = resource;
    }

    @Override
    public String toString() {
        return "RoleAccess{" +
            "id=" + id +
            ", role=" + role +
            ", resource=" + resource +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoleAccess that = (RoleAccess) o;
        if(id == null | this.id == null){ // 둘다 null이면
            return role.getId().equals(that.role.getId()) && resource.getId().equals(that.resource.getId());
        }
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = (int) (PRIME * result + this.role.getId() + this.resource.getId());
        return result;
    }
}
