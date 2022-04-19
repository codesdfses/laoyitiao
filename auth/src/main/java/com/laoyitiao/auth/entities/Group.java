package com.laoyitiao.auth.entities;

import lombok.*;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "groups")
public class Group {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_name", nullable = false, length = 50)
    private String groupName;

    @OneToMany(mappedBy = "group",fetch = FetchType.EAGER)
    private Set<GroupAuthority> groupAuthorities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "group",fetch = FetchType.EAGER)
    private Set<GroupMember> groupMembers = new LinkedHashSet<>();

}