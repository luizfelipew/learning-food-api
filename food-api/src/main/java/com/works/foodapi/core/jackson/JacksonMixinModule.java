package com.works.foodapi.core.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.works.foodapi.api.model.mixin.CidadeMixin;
import com.works.foodapi.api.model.mixin.CozinhaMixin;
import com.works.foodapi.api.model.mixin.RestauranteMixin;
import com.works.foodapi.domain.model.Cidade;
import com.works.foodapi.domain.model.Cozinha;
import com.works.foodapi.domain.model.Restaurante;
import org.springframework.stereotype.Component;

@Component
public class JacksonMixinModule extends SimpleModule {

    public JacksonMixinModule() {
        setMixInAnnotation(Restaurante.class, RestauranteMixin.class);
        setMixInAnnotation(Cidade.class, CidadeMixin.class);
        setMixInAnnotation(Cozinha.class, CozinhaMixin.class);
    }
}
