package tiendasara.tiendasara.services;

import org.springframework.data.jpa.repository.JpaRepository;
import tiendasara.tiendasara.models.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}