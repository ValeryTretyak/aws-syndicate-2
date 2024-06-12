package com.task02.dto;

public class HelloMessage {
  private final int statusCode;
  private final String message;

  public HelloMessage(String message) {
    this(200, message);
  }

  public HelloMessage(int statusCode, String message) {
    this.statusCode = statusCode;
    this.message = message;
  }
}
