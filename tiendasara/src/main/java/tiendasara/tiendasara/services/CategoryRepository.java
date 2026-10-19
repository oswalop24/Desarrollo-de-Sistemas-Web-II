package tiendasara.tiendasara.services;

import org.springframework.data.jpa.repository.JpaRepository;
import tiendasara.tiendasara.models.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}