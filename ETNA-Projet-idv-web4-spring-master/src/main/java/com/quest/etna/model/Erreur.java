package com.quest.etna.model;

public class Erreur {
    String error;

    public Erreur(String errorMessage) {
        this.error = errorMessage;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
