package com.pvzh.simulator.engine;

/**
 * Exception thrown when a deck fails validation rules.
 */
public class DeckValidationException extends Exception {
    public DeckValidationException(String message) {
        super(message);
    }
}
