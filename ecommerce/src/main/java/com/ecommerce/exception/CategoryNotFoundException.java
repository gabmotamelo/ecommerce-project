package com.ecommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CategoryNotFoundException extends Exception {

    public CategoryNotFoundException(String name){
        super(String.format("Category with name %s not found.", name));
    }

    public CategoryNotFoundException(Long id){
        super(String.format("Category with id %s not found.", id));
    }
}
