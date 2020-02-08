package com.donghun.todo.web.error;

import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class GlobalErrorWebExceptionHandler extends AbstractErrorController {

    public GlobalErrorWebExceptionHandler(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @RequestMapping(value = "/error", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> handleError(HttpServletRequest request) {
        Map<String, Object> errorAttributes = getErrorAttributes(request, true);

        errorAttributes.remove("timestamp");
        errorAttributes.remove("path");
        errorAttributes.remove("message");
        errorAttributes.remove("trace");

        return errorAttributes;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
