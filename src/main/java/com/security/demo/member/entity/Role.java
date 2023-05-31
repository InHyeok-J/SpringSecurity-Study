package com.security.demo.member.entity;

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
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String roleName;


    @Column(nullable = false)
    private String roleDesc;

    @OneToMany(mappedBy = "role", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private Set<RoleAccess> roleAccessResources = new HashSet<>();

    @OneToMany(mappedBy = "role", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private Set<MemberRole> memberRoles = new HashSet<>();

    @Builder
    public Role(String roleName, String roleDesc) {
        this.roleName = roleName;
        this.roleDesc = roleDesc;
    }
}
