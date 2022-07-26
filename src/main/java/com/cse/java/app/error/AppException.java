package com.cse.java.app.error;

/** Custom Exception to address linter feedback. */
public class AppException extends Exception {
  private static final long serialVersionUID = -1905031427519507137L;
  
  public AppException() {
  }

  public AppException(String message) {
    super(message);
  }
}
