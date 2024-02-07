package org.poo.cb;


public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}

class UserAlreadyFriendException extends Exception {
    public UserAlreadyFriendException(String message) {
        super(message);
    }
}

class UserDoestExistException extends Exception {
    public UserDoestExistException(String message) {
        super(message);
    }
}

class InsufficientAmountExchangeException extends Exception {
    public InsufficientAmountExchangeException(String message) {
        super(message);
    }
}

class InsufficientAmountTransferException extends Exception {
    public InsufficientAmountTransferException(String message) {
        super(message);
    }
}

class InsufficientAmountStocks extends Exception {
    public InsufficientAmountStocks(String message) {
        super(message);
    }
}
