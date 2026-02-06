package com.employee.security.entity;

import com.employee.security.enums.Authority;
import com.employee.security.enums.Permission;
import com.employee.security.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_tbl",schema = "employee")
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name")
    private String username;

    @Column(name = "password",unique = true)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role",schema = "employee",joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    Set<Role> roles=new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_permission",schema = "employee",joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "permission")
    Set<Permission> permissions=new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_authority",schema = "employee",joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "authority")
    Set<Authority> authority=new HashSet<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @PrePersist
    protected void create(){
    createdAt=LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> grantedAuthorities=new HashSet<>();
        grantedAuthorities.addAll(roles.stream().map(role->new SimpleGrantedAuthority("ROLE_"+role.name())).collect(Collectors.toSet()));
        grantedAuthorities.addAll(permissions.stream().map(perm->new SimpleGrantedAuthority(perm.name())).collect(Collectors.toSet()));
        grantedAuthorities.addAll(authority.stream().map(auth->new SimpleGrantedAuthority(auth.name())).collect(Collectors.toSet()));
        return grantedAuthorities;
    }

}
