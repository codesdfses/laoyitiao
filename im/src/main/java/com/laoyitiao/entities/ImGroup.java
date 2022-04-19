package com.laoyitiao.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class ImGroup {

    @Id
    private String title;

    private String coverImage;

    private String briefIntroduction;

    private String groupOwner;

    @CreatedDate
    private Timestamp dateCreated;

}
