package com.qxf.hadoop.zookeeper.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author qiuxuefu
 * @since 2019-04-11
 * @version 0.0.1
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket createRestApi() {

        ModelRef errorModel = new ModelRef("ExceptionVO");

        List<ResponseMessage> responseMessages = Arrays.asList(
                new ResponseMessageBuilder().code(409).message("Conflict")
                        .responseModel(new ModelRef("FieldExceptionVO")).build(),
                new ResponseMessageBuilder().code(403).message("Forbiddon").responseModel(null).build(),
                new ResponseMessageBuilder().code(404).message("Not Found").responseModel(errorModel).build(),
                new ResponseMessageBuilder().code(500).message("Intenal Error").responseModel(null).build());

        List<Parameter> headerParameter = new ArrayList<Parameter>();

        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.qxf.hadoop.zookeeper")).paths(PathSelectors.any())
                .build()// .groupName("外部接口")
                .globalOperationParameters(headerParameter).apiInfo(apiInfo()).useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.POST, responseMessages)
                .globalResponseMessage(RequestMethod.PUT, responseMessages)
                .globalResponseMessage(RequestMethod.GET, responseMessages)
                .globalResponseMessage(RequestMethod.DELETE, responseMessages);

    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("", "", "");
        return new ApiInfoBuilder().title("Hdfs web API  DOC").contact(contact).description("Zookeeper服务").build();
    }

    private String getAddress() {
        String hostAddress;
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            hostAddress = "127.0.0.1";
        }
        return hostAddress;
    }


}
