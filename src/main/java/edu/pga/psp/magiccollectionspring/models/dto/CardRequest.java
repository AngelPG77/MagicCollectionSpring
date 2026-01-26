package edu.pga.psp.magiccollectionspring.models.dto;

public class CardRequest {

    private Long collectionId;
    private String cardName;
    private int quantity;
    private String condition;
    private boolean foil;
    private String language;

    // Constructores

    public CardRequest() {
    }

    public CardRequest(Long collectionId, String cardName, int quantity, String condition, boolean foil, String language) {
        this.collectionId = collectionId;
        this.cardName = cardName;
        this.quantity = quantity;
        this.condition = condition;
        this.foil = foil;
        this.language = language;
    }

    // Getters y setters


    public Long getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(Long collectionId) {
        this.collectionId = collectionId;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public boolean isFoil() {
        return foil;
    }

    public void setFoil(boolean foil) {
        this.foil = foil;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
