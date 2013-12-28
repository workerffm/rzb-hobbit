package com.omic.kj;

public class GameOverException extends Exception {

	public GameOverException(String errormessage) {
		super(errormessage);
	}

	public GameOverException(String errormessage, Throwable cause) {
 		super (errormessage,cause);
 	}
 		
}
