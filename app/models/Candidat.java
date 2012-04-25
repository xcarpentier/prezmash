package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class Candidat extends Model {
    private static final long serialVersionUID = 1L;

    @Id
    public Long id;

    public Date birthdate;

    public String lastName;

    public String firstName;

    public String nameParty;

    public String imageName;

    public String colorParty;

    public String urlProfil;

    public Boolean active;

    public Candidat() {
    }

    public static Model.Finder<Long, Candidat> find = new Finder<Long, Candidat>(Long.class,
        Candidat.class);
}