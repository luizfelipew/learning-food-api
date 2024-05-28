package com.works.foodapi.core.squiggly;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.web.RequestSquigglyContextProvider;
import com.github.bohnman.squiggly.web.SquigglyRequestFilter;

import java.util.ArrayList;
import java.util.Arrays;


@Slf4j
@Configuration
public class SquigglyConfig {

    @Bean
    public FilterRegistrationBean<SquigglyRequestFilter> squigglyRequestFilter(ObjectMapper objectMapper) {
        Squiggly.init(objectMapper, new RequestSquigglyContextProvider("campos", null));

        var urlPatterns = Arrays.asList("/pedidos/*", "/restaurantes/*");

        var filterRegistration = new FilterRegistrationBean<SquigglyRequestFilter>();
        filterRegistration.setFilter(new SquigglyRequestFilter());
        filterRegistration.setOrder(1);
        filterRegistration.setUrlPatterns(urlPatterns);

        return filterRegistration;
    }
}
