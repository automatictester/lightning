package uk.co.automatictester.lightning.lambda;

public class LightningRequestException extends RuntimeException {
    public LightningRequestException(String message) {
        super(message);
    }
}
