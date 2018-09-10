package com.blade.swagger.utils;


import com.wordnik.swagger.core.*;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MvcApiMethodReader {

    private Method method;

    private String summary;
    @Getter
    private String notes;
    @Getter
    private Class<?> responseClass;
    @Getter
    private String tags;
    private String nickname;
    private boolean deprecated;

    private final static List<DocumentationParameter> parameters = new ArrayList<>();

    public MvcApiMethodReader(Method method) {
        this.method = method;
        documentOperation();
        documentParameters();
    }

    private void documentParameters() {
        ApiParam apiParam = method.getAnnotation(ApiParam.class);
        if (apiParam == null) {
            setDefaultParameters(method);
        } else {
            String name = "blade";
            String description = apiParam.value();
            if (StringUtils.isEmpty(name))
                name = apiParam.name();
            String paramType = "path";
            String dataType = method.getName();
            DocumentationParameter documentationParameter = new DocumentationParameter(name, description, apiParam.internalDescription(),
                    paramType, apiParam.defaultValue(), null, apiParam.required(), apiParam.allowMultiple());
            documentationParameter.setDataType(dataType);
            parameters.add(documentationParameter);
        }
    }

    private void setDefaultParameters(Method method) {
        String name = "blade";
        String dataType = method.getName();
        String paramType = "path";
        DocumentationParameter documentationParameter = new DocumentationParameter(name, "", "", paramType, "", null, true, false);
        documentationParameter.setDataType(dataType);
        parameters.add(documentationParameter);
    }

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


    public DocumentationOperation getOperation(Method method) {
        MvcApiMethodReader mvcApiMethodReader = new MvcApiMethodReader(method);
        DocumentationOperation operation = new DocumentationOperation(method.getName(), mvcApiMethodReader.summary, mvcApiMethodReader.notes);
        operation.setDeprecated(mvcApiMethodReader.deprecated);
        operation.setNickname(mvcApiMethodReader.nickname);
        for (DocumentationParameter parameter : parameters)
            operation.addParameter(parameter);
        setTags(operation);
        return operation;
    }

    private void setTags(DocumentationOperation operation) {
        if (tags != null)
            operation.setTags(Arrays.asList(tags.split(",")));
    }
}
