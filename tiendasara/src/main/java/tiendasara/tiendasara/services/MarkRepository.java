package tiendasara.tiendasara.services;

import org.springframework.data.jpa.repository.JpaRepository;
import tiendasara.tiendasara.models.Mark;

public interface MarkRepository extends JpaRepository<Mark, Integer> {
}