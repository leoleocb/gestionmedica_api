package com.cibertec.gestionmedica.repository;

import com.cibertec.gestionmedica.model.RecetaItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecetaItemRepository extends JpaRepository<RecetaItem, Long> {
}
