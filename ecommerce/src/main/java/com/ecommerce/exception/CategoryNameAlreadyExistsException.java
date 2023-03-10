package com.ecommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class CategoryNameAlreadyExistsException extends Exception {

    public CategoryNameAlreadyExistsException(String categoryName){
        super(String.format("Category %s already exists in the system.", categoryName));
    }
}
