package com.works.foodapi.domain.repository;

import com.works.foodapi.domain.model.Usuario;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends CustomJpaRepository<Usuario, Long> {

}
