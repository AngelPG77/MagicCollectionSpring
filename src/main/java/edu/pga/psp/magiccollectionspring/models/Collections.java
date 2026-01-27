package edu.pga.psp.magiccollectionspring.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "collections")
public class Collections {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Users owner;

    @OneToMany(mappedBy = "collection", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CardYouOwn> cards = new ArrayList<>();

    // Constructores

    public Collections() {
    }

    public Collections(Long id, String name, Users owner, List<CardYouOwn> cards) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.cards = cards;
    }

    // Getters y Setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Users getOwner() {
        return owner;
    }

    public void setOwner(Users owner) {
        this.owner = owner;
    }

    public List<CardYouOwn> getCards() {
        return cards;
    }

    public void setCards(List<CardYouOwn> cards) {
        this.cards = cards;
    }

}
