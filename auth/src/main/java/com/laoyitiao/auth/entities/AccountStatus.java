package com.laoyitiao.auth.entities;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "account_status")
public class AccountStatus {
    @Id
    @Column(name = "username", nullable = false, length = 16)
    private String id;

    @Column(name = "account_is_expired")
    private Instant accountIsExpired;

    @Column(name = "account_is_locked")
    private Instant accountIsLocked;

    @Column(name = "credentials_is_expired")
    private Instant credentialsIsExpired;

    @Column(name = "action_notes", length = 1000)
    private String actionNotes;

}