package edu.inlab.models;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;

/**
 * Created by inlab-dell on 2016/5/27.
 */
@Entity
@Table(name = "headline")
public class Headline {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "type", nullable = false)
    @Max(32767)
    private Integer type;

    @Column(name = "link", nullable = false)
    @Length(max = 45)
    private String link;

    @Column(name = "title", nullable = false)
    @Length(max = 45)
    private String title;

    @Column(name = "image", nullable = false)
    @Length(max = 128)
    private String image;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
