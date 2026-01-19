package edu.pga.psp.magiccollectionspring.models;

import jakarta.persistence.*;

@Entity
@Table(name = "cards_owned")
public class CardYouOwn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int quantity = 1;

    @Column(name = "is_foil", nullable = false)
    private boolean isFoil = false;

    @Column(name = "card_condition", length = 50)
    private String cardCondition;

    @Column(length = 50)
    private String language;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_id", nullable = false)
    private Collections collection;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_master_id", nullable = false)
    private Card cardMasterData;

}
