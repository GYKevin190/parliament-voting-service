package hu.gyanikevin.szavazas.model;

import hu.gyanikevin.szavazas.model.enums.SzavazatTipus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "szavazat", uniqueConstraints = @UniqueConstraint(name = "uk_szavazat_kepviselo", columnNames = {"szavazas_id", "kepviselo"}))
@Getter
@Setter
@NoArgsConstructor
public class SzavazatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "szavazas_id", nullable = false)
    private SzavazasEntity szavazas;

    @Column(nullable = false, columnDefinition = "varchar")
    private String kepviselo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SzavazatTipus szavazat;
}
