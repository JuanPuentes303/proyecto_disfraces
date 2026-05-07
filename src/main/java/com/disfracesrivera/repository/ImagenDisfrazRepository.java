package com.disfracesrivera.repository;

import com.disfracesrivera.model.ImagenDisfraz;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ImagenDisfrazRepository extends JpaRepository<ImagenDisfraz, Long> {

    List<ImagenDisfraz> findByDisfrazId(Long disfrazId);

    Optional<ImagenDisfraz> findByDisfrazIdAndPrincipalTrue(Long disfrazId);
}