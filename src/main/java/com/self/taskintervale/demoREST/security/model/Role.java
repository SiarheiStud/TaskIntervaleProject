package com.self.taskintervale.demoREST.security.model;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    ADMIN(Set.of(CustomPermission.BOOK_READ,
            CustomPermission.BOOK_WRITE,
            CustomPermission.BOOK_DELETE,
            CustomPermission.WORK_WITH_BANK)),
    USER(Set.of(CustomPermission.BOOK_READ)),
    MANAGER(Set.of(CustomPermission.BOOK_READ,
            CustomPermission.WORK_WITH_BANK));

    private final Set<CustomPermission> customPermissionSet;

    Role(Set<CustomPermission> customPermissionSet) {
        this.customPermissionSet = customPermissionSet;
    }

    public Set<CustomPermission> getCustomPermissionSet() {
        return customPermissionSet;
    }

    public Set<SimpleGrantedAuthority> getAuthority() {
        return getCustomPermissionSet().stream()
                .map(customPermission -> new SimpleGrantedAuthority(customPermission.name()))
                .collect(Collectors.toSet());
    }
}
