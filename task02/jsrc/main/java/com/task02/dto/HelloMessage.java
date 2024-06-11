package com.task02.dto;

public class HelloMessage {
  private final int statusCode = 200;
  private final String message;

  public HelloMessage(String message) {
    this.message = message;
  }
}
