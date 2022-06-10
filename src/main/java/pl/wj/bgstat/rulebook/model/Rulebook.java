package pl.wj.bgstat.rulebook.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name="rulebooks")
public class Rulebook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long boardGameId;
    private String languageIso;
    private String path;
}
