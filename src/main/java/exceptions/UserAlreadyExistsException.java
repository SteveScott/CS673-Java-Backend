package main.java.exceptions;

public class UserAlreadyExistsException extends Exception {
	public UserAlreadyExistsException(){
		System.out.println("User already exists in the system");
	}
}
