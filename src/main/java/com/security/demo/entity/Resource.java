package com.security.demo.entity;


import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "resources")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resource_id")
    private Long id;

    @Column(nullable = false)
    private String resourceName;


    @Column(nullable = false)
    private String httpMethod;


    @Column(nullable = false)
    private int orderNum;


    @Column(nullable = false)
    private String resourceType;

    @OneToMany(mappedBy = "resource", cascade = {CascadeType.ALL}, orphanRemoval = true)
    public Set<RoleAccess> canAccessRoles = new HashSet<>();

    @Builder
    public Resource(String resourceName, String httpMethod, int orderNum, String resourceType) {
        this.resourceName = resourceName;
        this.httpMethod = httpMethod;
        this.orderNum = orderNum;
        this.resourceType = resourceType;
    }

    @Override
    public String toString() {
        return "Resource{" +
            "id=" + id +
            ", resourceName='" + resourceName + '\'' +
            ", httpMethod='" + httpMethod + '\'' +
            ", orderNum=" + orderNum +
            ", resourceType='" + resourceType + '\'' +
            '}';
    }
}
