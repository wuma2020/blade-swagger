package com.blade.swagger.utils;


import com.blade.mvc.route.Route;
import com.wordnik.swagger.core.*;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 该类是用于解析控制器中的方法上注解的的工具类
 */
public class MvcApiMethodReader {

    /**
     * 需要处理的方法
     */
    private Method method;

    private String summary;
    @Getter
    private String notes;
    @Getter
    private Class<?> responseClass;
    @Getter
    private String tags;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 是否弃用
     */
    private boolean deprecated;

    private final static List<DocumentationParameter> parameters = new ArrayList<>();

    public MvcApiMethodReader(Method method) {
        this.method = method;

        //1.根据 handlerMethod 解析 ApiOperation注解 只解析一个方法，设置一些共同的属性
        documentOperation();
        //2.根据 handlerMethod 解析  ApiParam 注解
        documentParameters();
        //3.根据handlerMethod 解析异常注解  ---  不准备添加
        // documentExceptions();

    }

    /**
     * 解析Route 方法上的 注解@ApiParam上的信息
     */
    private void documentParameters() {
        ApiParam apiParam = method.getAnnotation(ApiParam.class);
        if (apiParam == null ){
            setDefaultParameters(method);
        }else {
            String name = "blade";
            // FIXME: 2018/7/30 0030  convertToAllowableValues(apiParam.allowableValues()); 方法不懂
            // FIXME: 2018/7/30 0030 allowableValues() 属性可为不写  暂时不解析
            // val allowableValues = convertToAllowableValues(apiParam.allowableValues());
            String description = apiParam.value();
            if (StringUtils.isEmpty(name))
                name = apiParam.name();
            String paramType = "path";
            String dataType = method.getName();
            DocumentationParameter documentationParameter = new DocumentationParameter(name, description, apiParam.internalDescription(),
                    paramType,apiParam.defaultValue(), null,apiParam.required(),apiParam.allowMultiple());
            documentationParameter.setDataType(dataType);
            parameters.add(documentationParameter);
        }


    }



    /**
     * 设置默认的 @apiPram 对应的信息.即DocumentationParameter类
     * name = "blade" ;
     * @param method
     */
    private void setDefaultParameters(Method method) {
        String name = "blade";
        String dataType = method.getName();
        String paramType = "path";
        DocumentationParameter documentationParameter = new DocumentationParameter(name, "", "",	paramType,"", null, true, false);
        documentationParameter.setDataType(dataType);
        parameters.add(documentationParameter);
    }


    /**
     * 解析Route 方法上的 注解@ApiOperation上的信息
     */
    private void documentOperation() {
        ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
        if (apiOperation != null) {
            summary = apiOperation.value();
            notes = apiOperation.notes();
            tags = apiOperation.tags();
        }
        nickname = method.getName();
        deprecated = method.getAnnotation(Deprecated.class) != null;
    }


    /**
     * controller 的方法对象昂解析成
     * @param method 需要解析的方法
     * @return
     */
    public DocumentationOperation getOperation(Method method) {
        MvcApiMethodReader mvcApiMethodReader = new MvcApiMethodReader(method);
        DocumentationOperation operation = new DocumentationOperation(method.getName(),mvcApiMethodReader.summary,mvcApiMethodReader.notes);
        operation.setDeprecated(mvcApiMethodReader.deprecated);
        operation.setNickname(mvcApiMethodReader.nickname);
        for (DocumentationParameter parameter : parameters)
            operation.addParameter(parameter);
        setTags(operation);

        //错误信息暂时不支持
//        for (DocumentationError error : errors)  //
//            operation.addErrorResponse(error);//添加DocumentationError信息
        return operation;
    }

    private void setTags(DocumentationOperation operation) {
        if (tags != null)
            operation.setTags(Arrays.asList(tags.split(",")));
    }


}
