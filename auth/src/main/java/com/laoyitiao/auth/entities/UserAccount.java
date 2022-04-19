package com.laoyitiao.auth.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "user_account")
public class UserAccount{
    @Id
    @Column(name = "username", nullable = false)
    private String id;

    @Column(name = "account_nickname", nullable = false)
    private String accountNickname;

    @Column(name = "account_avatar", nullable = false)
    private String accountAvatar;

    @Column(name = "account_level", nullable = false)
    private String accountLevel;

    @Column(name = "account_experience", nullable = false)
    private Long accountExperience;

    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "phone_number", length = 11)
    private String phoneNumber;

    @Column(name = "account_type", nullable = false)
    private String accountType;

    @Column(name = "create_date")
    private Instant createDate;

}