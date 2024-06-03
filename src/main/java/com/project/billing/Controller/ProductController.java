package com.project.billing.Controller;

import com.project.billing.Dto.Product;
import com.project.billing.Dto.ResponseStructure;
import com.project.billing.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/product")
public class ProductController {

    @Autowired
    public ProductService productService;

    @PostMapping(value = "/save")
    public ResponseEntity<ResponseStructure<Product>> save(@RequestBody Product product) {
        return productService.save(product);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<ResponseStructure<Product>> delete(@PathVariable long id) {
        return productService.delete(id);
    }

    @GetMapping(value = "/findById/{id}")
    public ResponseEntity<ResponseStructure<Product>> findById(@PathVariable long id) {
        return productService.findById(id);
    }

    @GetMapping(value = "/findByNameContaining")
    public ResponseEntity<ResponseStructure<List<Product>>> findByNameContaining(@RequestParam String letter) {
        return productService.findByNameContaining(letter);
    }

    @GetMapping(value = "/findAll")
    public  ResponseEntity<ResponseStructure<List <Product>>> findAll(){
        return  productService.findAll();
    }

}