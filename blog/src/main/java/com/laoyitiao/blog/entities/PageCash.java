package com.laoyitiao.blog.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "page_cash")
public class PageCash {

    @Id
    @Column(name = "page_name",nullable = false)
    private String pageName;

    @Column(name = "data",nullable = false)
    private String data;

    @Column(name = "last_update",nullable = false)
    private Timestamp lastUpdate;
}
