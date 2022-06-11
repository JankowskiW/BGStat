package pl.wj.bgstat.rulebook.model;

import lombok.Getter;
import lombok.Setter;
import pl.wj.bgstat.rulebook.enumeration.LanguageISO;

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
    private LanguageISO languageIso;
    private String path;
}
