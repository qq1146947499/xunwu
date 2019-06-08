package com.imooc.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Web页面全局错误处理
 */
@Controller
public class AppErrorController implements ErrorController {
    private ErrorAttributes errorAttributes;

    private static final String ERROR_PATH = "/error";

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }


    @Autowired
    public AppErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    /**
     * Web页面错误处理
     */
    @RequestMapping(value = ERROR_PATH, produces = "text/html")
    public String errorPageHandler(HttpServletRequest request, HttpServletResponse response) {
        int status = response.getStatus ( );
        switch (status) {
            case 403:
                return "403";
            case 404:
                return "404";
            case 500:
                return "500";
        }
        return "index";
    }

    /**
     * 除web页面的错误处理,json/xml等
     */
    @RequestMapping(value = ERROR_PATH)
    @ResponseBody
    public ApiResponse errorApliHandler(HttpServletRequest request) {
        //错误的RequestAttribute requestAttribute = (RequestAttribute) new ServletRequestAttributes (request);
        RequestAttributes requestAttributes = new ServletRequestAttributes (request);
        Map<String, Object> attr = this.errorAttributes.getErrorAttributes (requestAttributes, false);
        int status = getStatus (request);
        return ApiResponse.ofMessage (status, String.valueOf (attr.getOrDefault ("message", "error")));
    }

    private int getStatus(HttpServletRequest request) {
        Integer status = (Integer) request.getAttribute ("javax.servlet.error.status_code");
        if (status != null) {
            return status;
        }
        return 500;
    }

}
