package hu.gyanikevin.szavazas.model;

import hu.gyanikevin.szavazas.model.enums.EljarasTipus;
import hu.gyanikevin.szavazas.model.enums.SzavazasTipus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "szavazas", uniqueConstraints = @UniqueConstraint(name = "uk_szavazas_idopont", columnNames = "idopont"))
@Getter
@Setter
@NoArgsConstructor
public class SzavazasEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private Instant idopont;

    @Column(nullable = false, columnDefinition = "varchar")
    private String targy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SzavazasTipus tipus;

    @Enumerated(EnumType.STRING)
    private EljarasTipus eljaras;

    @Column(nullable = false, columnDefinition = "varchar")
    private String elnok;

    @OneToMany(mappedBy = "szavazas", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<SzavazatEntity> szavazatok = new ArrayList<>();
}
