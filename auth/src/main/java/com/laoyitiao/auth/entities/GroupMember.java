package com.laoyitiao.auth.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Table(name = "group_members")
public class GroupMember implements Serializable {

    @Id
    @Column(name = "username", nullable = false,length = 50)
    private String id;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "group_id")
    private Group group;

}