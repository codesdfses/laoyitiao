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
@Table(name = "blog")
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String title;

    private String coverImage;

    private String contentPreview;

    private String ContentPath;

    private String author;

    private Timestamp dateCreated;

    private Long numberOfLikes;
    private Long numberOfComments;
    private Long numberOfForwarded;
    private Long numberOfCollect;

}
