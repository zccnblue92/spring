package com;


import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.plugin.EnableSwagger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebMvc   //启用Spring MVC组件
@ComponentScan("com.api")  //启用组件扫描
@EnableSwagger
public class SwaggerConfig {  
  
    private SpringSwaggerConfig springSwaggerConfig;
  
    /** 
     * Required to autowire SpringSwaggerConfig 
     */  
    @Autowired
    public void setSpringSwaggerConfig(SpringSwaggerConfig springSwaggerConfig)  
    {  
        this.springSwaggerConfig = springSwaggerConfig;  
    }  

    @Bean
    public Docket createRestApi()
    {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenPar.name("authorization").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(tokenPar.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.xe.demo.api"))
                .paths(PathSelectors.any())
                .build().globalOperationParameters(pars);
    }  
  
    private ApiInfo apiInfo()
    {
        return new ApiInfoBuilder()
                .title("汉艺网微信小程序后台接口文档与测试")
                .description("这是一个给app端人员调用server端接口的测试文档与平台")
                .termsOfServiceUrl("http://www.zallhy.com")
                .version("1.0")
                .build();
    }  
}  