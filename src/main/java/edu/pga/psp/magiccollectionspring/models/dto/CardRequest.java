package edu.pga.psp.magiccollectionspring.models.dto;

public class CardRequest {

    private Long collectionId;
    private String cardName;
    private Integer quantity;
    private String condition;
    private Boolean isFoil;

    private String language;

    // Constructores

    public CardRequest() {
    }

    public CardRequest(Long collectionId, String cardName, Integer quantity, String condition, Boolean foil, String language) {
        this.collectionId = collectionId;
        this.cardName = cardName;
        this.quantity = quantity;
        this.condition = condition;
        this.isFoil = foil;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Boolean getIsFoil() {
        return isFoil;
    }

    public void setIsFoil(Boolean isFoil) {
        this.isFoil = isFoil;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
