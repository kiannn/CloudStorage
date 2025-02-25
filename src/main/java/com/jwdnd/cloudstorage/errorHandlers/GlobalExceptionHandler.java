package com.jwdnd.cloudstorage.errorHandlers;

import java.nio.file.AccessDeniedException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
 
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({NoResourceFoundException.class,
        NoHandlerFoundException.class,
        NullPointerException.class,
        HttpRequestMethodNotSupportedException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handle404(Model model, Exception Exception) {

        String simpleName = Exception.getClass().getSimpleName();

        switch (simpleName) {
            case "HttpRequestMethodNotSupportedException" ->
                model.addAttribute("msg", "Request method is not supported");
            default ->
                model.addAttribute("msg", "The page does not exist or user is not authenticated");
        }

        return "errorPage";

    }

    @ExceptionHandler({AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FOUND)
    public String handle(Exception Exception) {

        return "redirect:/home";

    }

}
