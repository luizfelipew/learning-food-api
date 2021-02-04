package com.works.foodapi.jpa;

import com.works.foodapi.FoodApiApplication;
import com.works.foodapi.domain.model.Cozinha;
import com.works.foodapi.domain.repository.CozinhaRepository;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

public class ExclusaoCozinhaMain {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new SpringApplicationBuilder(FoodApiApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);

        CozinhaRepository cozinhas = applicationContext.getBean(CozinhaRepository.class);

//        Cozinha cozinha = new Cozinha();
//        cozinha.setId(1L);

        cozinhas.remover(1L);

    }
}
