package tiendasara.tiendasara.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import tiendasara.tiendasara.models.Product;
import tiendasara.tiendasara.services.CategoryRepository;
import tiendasara.tiendasara.services.MarkRepository;
import tiendasara.tiendasara.services.ProductRepository;

@Controller
public class ProductController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final MarkRepository markRepository;

    public ProductController(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            MarkRepository markRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.markRepository = markRepository;
    }

    // Página que muestra todos los productos
    @GetMapping("/products")
    public String listProducts(Model model) {
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("marks", markRepository.findAll());
        return "products/index";
    }

    // Página para crear un producto
    @GetMapping("/products/new")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        addCategoriesAndMarks(model);
        return "products/create";
    }

    // Guarda un producto enviado desde el formulario
    @PostMapping("/products")
    public String createProduct(@ModelAttribute Product product) {
        product.setId(null);
        productRepository.save(product);
        return "redirect:/products";
    }

    // Página para editar un producto
    @GetMapping("/products/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Product product = findProductOrThrow(id);
        model.addAttribute("product", product);
        addCategoriesAndMarks(model);
        return "products/edit";
    }

    // Actualiza un producto enviado desde el formulario
    @PostMapping("/products/{id}")
    public String updateProduct(
            @PathVariable Integer id,
            @ModelAttribute Product product) {
        findProductOrThrow(id);
        product.setId(id);
        productRepository.save(product);
        return "redirect:/products";
    }

    // Elimina un producto desde la página
    @GetMapping("/products/delete/{id}")
    public String deleteProductFromPage(@PathVariable Integer id) {
        findProductOrThrow(id);
        productRepository.deleteById(id);
        return "redirect:/products";
    }

    // REST: consultar todos los productos
    @ResponseBody
    @GetMapping("/api/products")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // REST: consultar un producto por ID
    @ResponseBody
    @GetMapping("/api/products/{id}")
    public Product getProductById(@PathVariable Integer id) {
        return findProductOrThrow(id);
    }

    // REST: dar de alta un producto
    @ResponseBody
    @PostMapping("/api/products")
    public ResponseEntity<Product> createProductApi(@RequestBody Product product) {
        product.setId(null);
        Product savedProduct = productRepository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    // REST: modificar un producto por ID
    @ResponseBody
    @PutMapping("/api/products/{id}")
    public Product updateProductApi(
            @PathVariable Integer id,
            @RequestBody Product product) {
        findProductOrThrow(id);
        product.setId(id);
        return productRepository.save(product);
    }

    // REST: eliminar un producto por ID
    @ResponseBody
    @DeleteMapping("/api/products/{id}")
    public ResponseEntity<Void> deleteProductApi(@PathVariable Integer id) {
        findProductOrThrow(id);
        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Product findProductOrThrow(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Producto no encontrado"));
    }

    private void addCategoriesAndMarks(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("marks", markRepository.findAll());
    }
}