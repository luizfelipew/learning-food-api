package com.works.foodapi.jpa;

import com.works.foodapi.FoodApiApplication;
import com.works.foodapi.domain.model.Cozinha;
import com.works.foodapi.domain.model.Restaurante;
import com.works.foodapi.domain.repository.CozinhaRepository;
import com.works.foodapi.domain.repository.RestauranteRepository;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import java.util.List;

public class ConsultaRestauranteMain {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new SpringApplicationBuilder(FoodApiApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);

        RestauranteRepository restaurantes = applicationContext.getBean(RestauranteRepository.class);

        List<Restaurante> todosRestaurantes = restaurantes.findAll();

        todosRestaurantes.forEach(restaurante ->
                System.out.printf("%s - %f - %s\n", restaurante.getNome(),
                        restaurante.getTaxaFrete(), restaurante.getCozinha().getNome()));
    }
}
