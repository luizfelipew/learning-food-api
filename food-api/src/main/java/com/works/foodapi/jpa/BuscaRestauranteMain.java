package com.works.foodapi.jpa;

import com.works.foodapi.FoodApiApplication;
import com.works.foodapi.domain.model.Cozinha;
import com.works.foodapi.domain.model.Restaurante;
import com.works.foodapi.domain.repository.CozinhaRepository;
import com.works.foodapi.domain.repository.RestauranteRepository;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

public class BuscaRestauranteMain {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new SpringApplicationBuilder(FoodApiApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);

        RestauranteRepository restauranteRepository = applicationContext.getBean(RestauranteRepository.class);

        Restaurante restaurante = restauranteRepository.buscar(1L);

        System.out.println(restaurante.getNome());
    }
}
