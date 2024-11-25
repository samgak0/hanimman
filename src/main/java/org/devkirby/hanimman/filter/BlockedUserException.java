package org.devkirby.hanimman.filter;

public class BlockedUserException extends RuntimeException{
    public BlockedUserException(String message){
        super(message);
    }
}
