package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class User extends Model {
    private static final long serialVersionUID = 1L;

    @Id
    public Long id;

    public Date createdDate;

    public String cookie;

    public String ip;

    public String userAgent;

    public String email;

    public User() {
    }

    public static Model.Finder<Long, User> find = new Finder<Long, User>(Long.class,
            User.class);

}