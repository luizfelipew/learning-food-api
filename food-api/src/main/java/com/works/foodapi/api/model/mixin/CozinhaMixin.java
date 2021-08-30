package com.works.foodapi.api.model.mixin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.works.foodapi.domain.model.Restaurante;

import java.util.List;

public abstract class CozinhaMixin {

    @JsonIgnore
    private List<Restaurante> restaurantes;

}
