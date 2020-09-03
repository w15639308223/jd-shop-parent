package com.baidu.global;

import com.alibaba.fastjson.JSONObject;
import com.baidu.base.Result;
import com.baidu.status.HttpStatus;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/8/29 11:54
 */

//全局异常处理类
@RestControllerAdvice
@Slf4j
public class GlobalException {

    @ExceptionHandler(RuntimeException.class)
    public Result<JSONObject> test(HttpServletRequest req, Exception e){

        Result<JSONObject> result = new Result();
        result.setCode(HttpStatus.ERROR);
        result.setMessage(e.getMessage());

        log.debug(e.getMessage());

        return result;
    }

    @ExceptionHandler(value=MethodArgumentNotValidException.class)
    public List<Result<JsonObject>> MethodArgumentNotValidHandler(HttpServletRequest request,
                                                                  MethodArgumentNotValidException exception) throws Exception
    {
        List<Result<JsonObject>> objects = new ArrayList<>();

        //按需重新封装需要返回的错误信息

        for (FieldError error : exception.getBindingResult().getFieldErrors()) {

            Result<JsonObject> jsonObjectResult = new Result<>();

            jsonObjectResult.setCode(HttpStatus.PARAMS_VALIDATE_ERROR);

            jsonObjectResult.setMessage("Field --> " + error.getField() + " : " + error.getDefaultMessage());

            log.debug("Field --> " + error.getField() + " : " + error.getDefaultMessage());
            objects.add(jsonObjectResult);
        }

        return objects;
    }
}
