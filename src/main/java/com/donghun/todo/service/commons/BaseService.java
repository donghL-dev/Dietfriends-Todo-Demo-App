package com.donghun.todo.service.commons;

import com.donghun.todo.web.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;

public class BaseService {

    public ResponseEntity<?> validation(BindingResult bindingResult) {
        List<ObjectError> list = bindingResult.getAllErrors();
        StringBuilder msg = new StringBuilder();

        for (int i=0; i<list.size(); i++) {
            ObjectError error = list.get(i);
            msg.append(error.getDefaultMessage());

            if (i != list.size() -1)
                msg.append(", ");
        }

        ErrorResponseDTO response = new ErrorResponseDTO("400",
                "Bad Request", msg.toString());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
