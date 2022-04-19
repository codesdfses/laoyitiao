package com.laoyitiao.auth.entities;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "group_authorities")
public class GroupAuthority implements Serializable {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @Column(name = "authority", nullable = false, length = 50)
    private String authority;

    @Lob
    @Column(name = "remark")
    private String remark;

}