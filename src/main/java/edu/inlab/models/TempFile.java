package edu.inlab.models;

import javax.persistence.*;

/**
 * Created by inlab-dell on 2016/5/20.
 */
@Entity
@Table(name = "tempfile")
public class TempFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "usermicrotask_id", nullable = false)
    private Integer usermicrotaskId;

    @Column(name = "filename", nullable = false)
    private String filename;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUsermicrotaskId() {
        return usermicrotaskId;
    }

    public void setUsermicrotaskId(Integer usermicrotaskId) {
        this.usermicrotaskId = usermicrotaskId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
