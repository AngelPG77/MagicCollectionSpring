package edu.pga.psp.magiccollectionspring.models;

import jakarta.persistence.*;

@Entity
@Table(name = "master_cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "set_code", nullable = false, length = 10)
    private String setCode;

    @Column(name = "scryfall_id", unique = true, nullable = false)
    private String scryfallId;

}
