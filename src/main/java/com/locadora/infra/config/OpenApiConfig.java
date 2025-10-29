
package com.locadora.infra.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.media.StringSchema;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class OpenApiConfig implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof OpenAPI openApi) {
            Parameter empresaHeader = new Parameter()
                    .in("header")
                    .schema(new StringSchema())
                    .name("X-Empresa-Id")
                    .description("ID da empresa (opcional — JWT tem prioridade)")
                    .required(false);

            if (openApi.getPaths() != null) {
                for (PathItem pathItem : openApi.getPaths().values()) {
                    for (Operation op : pathItem.readOperations()) {
                        boolean exists = op.getParameters() != null && op.getParameters().stream()
                                .anyMatch(p -> "X-Empresa-Id".equalsIgnoreCase(p.getName()));
                        if (!exists) {
                            op.addParametersItem(empresaHeader);
                        }
                    }
                }
            }
        }
        return bean;
    }
}
