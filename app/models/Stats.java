package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Stats extends Model {
    private static final long serialVersionUID = 1L;

    @Id
    public Long id;

    @OneToOne
    public User user;

    @OneToOne
    public Candidat candidat;

    public Stats() {
    }

    public static Finder<Long, Stats> find = new Finder<Long, Stats>(Long.class,
            Stats.class);
}