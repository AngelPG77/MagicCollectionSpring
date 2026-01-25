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

    @Enumerated(EnumType.STRING)
    @Column(name = "card_condition", length = 20, nullable = false)
    private String cardCondition;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private String language;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_id", nullable = false)
    private Collections collection;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_master_id", nullable = false)
    private Card cardMasterData;

    // Constructores

    public CardYouOwn() {
    }

    public CardYouOwn(Long id, int quantity, boolean isFoil, String cardCondition, String language, Collections collection, Card cardMasterData) {
        this.id = id;
        this.quantity = quantity;
        this.isFoil = isFoil;
        this.cardCondition = cardCondition;
        this.language = language;
        this.collection = collection;
        this.cardMasterData = cardMasterData;
    }

    // Getters y Setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isFoil() {
        return isFoil;
    }

    public void setFoil(boolean foil) {
        isFoil = foil;
    }

    public String getCardCondition() {
        return cardCondition;
    }

    public void setCardCondition(String cardCondition) {
        this.cardCondition = cardCondition;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Collections getCollection() {
        return collection;
    }

    public void setCollection(Collections collection) {
        this.collection = collection;
    }

    public Card getCardMasterData() {
        return cardMasterData;
    }

    public void setCardMasterData(Card cardMasterData) {
        this.cardMasterData = cardMasterData;
    }
}
