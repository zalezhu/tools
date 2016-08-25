package com.zale.shortlink.web.controller;

import com.cardsmart.inf.entity.RespEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import javax.naming.NoPermissionException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by young on 6/11/15.
 */
@ControllerAdvice
public class ExceptionHandleController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandleController.class);

    private final static String NOT_FOUND = "not_found";

    private final static String HANDLER_METHOD_NOT_FOUND = "handler_method_not_found";

    private final static String HTTP_METHOD_NOT_SUPPORTED = "http_method_not_supported";

    private final static String MEDIA_TYPE_NOT_SUPPORTED = "media_type_not_supported";

    private final static String MEDIA_TYPE_NOT_ACCEPTABLE = "media_type_not_acceptable";

    private final static String REQUEST_PARAM_REQUIRED = "request_param_required";

    private final static String REQUEST_PART_REQUIRED = "request_part_required";

    private final static String REQUEST_HEADER_REQUIRED = "request_header_required";

    private final static String HTTP_MESSAGE_NOT_READABLE = "http_message_not_readable";

    private final static String CONVERSION_NOT_SUPPORTED = "conversion_not_supported";

    private final static String METHOD_ARGUMENT_NOT_VALID = "method_argument_not_valid";

    private final static String INTERNAL_SERVER_ERROR = "internal_server_error";

    private final static String FORBIDDEN = "permission_invalid";
    /**
     * 无效的 URL 请求
     * 找不到处理该 URL 的 Controller
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = NoHandlerFoundException.class)
    @ResponseBody
    public ResponseEntity<RespEntity> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        RespEntity entity = new RespEntity(NOT_FOUND,
                "没有找到 [" + ex.getHttpMethod().toUpperCase() + " " + ex.getRequestURL() + "] 相应的处理器.");
        return getJSONResp(entity, HttpStatus.NOT_FOUND);
    }

    /**
     * 无效的 URL 请求
     * 找不到 Controller 中对应的方法
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = NoSuchRequestHandlingMethodException.class)
    @ResponseBody
    public ResponseEntity<RespEntity> handleNoSuchRequestHandlingMethodException(NoSuchRequestHandlingMethodException ex) {
        RespEntity entity = new RespEntity(HANDLER_METHOD_NOT_FOUND,
                "没有找到处理该请求的方法 [" + ex.getMethodName() + "].");
        return getJSONResp(entity, HttpStatus.NOT_FOUND);
    }

    /**
     * 不支持的 Http Method
     * @param ex
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public ResponseEntity<RespEntity> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        StringBuffer supportedMethods = new StringBuffer();
        if(ex.getSupportedMethods() != null && ex.getSupportedMethods().length > 0) {
            for(int i=0; i<ex.getSupportedMethods().length; i++) {
                if(i != 0) {
                    supportedMethods.append(" | ");
                }
                supportedMethods.append(ex.getSupportedMethods()[i]);
            }
        }
        RespEntity entity = new RespEntity(HTTP_METHOD_NOT_SUPPORTED,
                "不支持的Http方法 [" + ex.getMethod().toUpperCase() + "], 请尝试 [" + supportedMethods.toString() + "].");
        return getJSONResp(entity, HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * 不支持的 Http media type
     * @param ex
     * @return
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseBody
    public ResponseEntity<RespEntity> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        RespEntity entity = new RespEntity(MEDIA_TYPE_NOT_SUPPORTED,
                "不支持的Http媒体类型 [" + ex.getContentType().toString() + "].");
        return getJSONResp(entity, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    @ResponseBody
    public ResponseEntity<RespEntity> handleHttpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException ex) {
        StringBuffer supportsList = new StringBuffer();
        if(ex.getSupportedMediaTypes() != null && ex.getSupportedMediaTypes().size() > 0) {
            for(int i=0; i<ex.getSupportedMediaTypes().size(); i++) {
                if(i != 0) {
                    supportsList.append(" | ");
                }
                supportsList.append(ex.getSupportedMediaTypes().get(i).toString());
            }
        }
        RespEntity entity = new RespEntity(MEDIA_TYPE_NOT_ACCEPTABLE,
                "不可接受的Http媒体类型, 仅支持 [" + supportsList.toString() + "].");
        return getJSONResp(entity, HttpStatus.NOT_ACCEPTABLE);
    }

    /**
     * URL 缺少必要的请求参数
     * @param ex
     * @return
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public ResponseEntity<RespEntity> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        RespEntity entity = new RespEntity(REQUEST_PARAM_REQUIRED,
                "URL参数 [" + ex.getParameterName() + "] 不能为空.");
        return getJSONResp(entity, HttpStatus.BAD_REQUEST);
    }

    /**
     * Header 缺少必要的参数
     * @param ex
     * @return
     */
    @ExceptionHandler(ServletRequestBindingException.class)
    @ResponseBody
    public ResponseEntity<RespEntity> handleServletRequestBindingException(ServletRequestBindingException ex) {
        RespEntity entity = new RespEntity(REQUEST_HEADER_REQUIRED, "HEADER参数不能为空.");
        return getJSONResp(entity, HttpStatus.BAD_REQUEST);
    }

    /**
     * 缺少 Request Part
     * @param ex
     * @return
     */
    @ExceptionHandler(MissingServletRequestPartException.class)
    @ResponseBody
    public ResponseEntity<RespEntity> handleMissingServletRequestPartException(MissingServletRequestPartException ex) {
        RespEntity entity = new RespEntity(REQUEST_PART_REQUIRED,
                "Request part [" + ex.getRequestPartName() + "] 不能为空.");
        return getJSONResp(entity, HttpStatus.BAD_REQUEST);
    }

    /**
     * 请求体(Request Body)不可读, 或转换出错
     * @param ex
     * @return
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseEntity<RespEntity> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        RespEntity entity = new RespEntity(HTTP_MESSAGE_NOT_READABLE, ex.getMessage());
        return getJSONResp(entity, HttpStatus.BAD_REQUEST);
    }

    /**
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(ConversionNotSupportedException.class)
    @ResponseBody
    public ResponseEntity<RespEntity> handleConversionNotSupportedException(ConversionNotSupportedException ex) {
        RespEntity entity = new RespEntity(CONVERSION_NOT_SUPPORTED,
                "不支持的类型转换, 属性 [" + ex.getPropertyName() + "].");
        return getJSONResp(entity, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<RespEntity> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, Object> results = new HashMap<String,Object>();
        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if(fieldErrors != null) {
            for(int i=0; i<fieldErrors.size(); i++) {
                FieldError fieldError = fieldErrors.get(i);
                String value = ((fieldError.getRejectedValue() == null)? "null" : fieldError.getRejectedValue().toString());
                String reason = ((fieldError.getDefaultMessage() == null)? "" : fieldError.getDefaultMessage());
                String errorFieldMessage =
                        "被拒绝的值 [" + value + "], 原因 [" + reason + "].";
                results.put(fieldError.getField(), errorFieldMessage);
            }
        }
        RespEntity entity = new RespEntity(METHOD_ARGUMENT_NOT_VALID, "请求数据格式有误.");
        entity.setExt(results);
        return getJSONResp(entity, HttpStatus.BAD_REQUEST);
    }

   

    @ExceptionHandler(value = { NoPermissionException.class } )
    @ResponseBody
    public ResponseEntity<RespEntity> handleNoPermissionException(NoPermissionException ex) {
        RespEntity entity = new RespEntity(FORBIDDEN, ex.getMessage());
        return getJSONResp(entity, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = { RuntimeException.class, Exception.class, Throwable.class,  })
    @ResponseBody
    public ResponseEntity<RespEntity> handleException(Throwable th) {
        logger.error("internal server error", th);
        RespEntity entity = new RespEntity(INTERNAL_SERVER_ERROR, th.getMessage());
        return getJSONResp(entity, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}