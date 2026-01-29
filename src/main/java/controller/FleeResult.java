package controller;
//A Value Object is a class whose identity is based on its value, not its reference/memory location.
public record FleeResult(boolean success, String logMessage) {
}