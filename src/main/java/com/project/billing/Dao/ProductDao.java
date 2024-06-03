package com.project.billing.Dao;

import com.project.billing.Dto.Product;
import com.project.billing.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductDao {

    @Autowired
    public ProductRepository productRepository;

    public Product save(Product product){
        return productRepository.save(product);
    }
    public void delete(Product product){
        productRepository.delete(product);
    }

    public  Product findById(long id){
        Optional<Product> optional=productRepository.findById(id);
        return  optional.orElse(null);
    }

    public List <Product> findAll(){
        return productRepository.findAll();
    }

    public List<Product> findByNameContaining(String  letter){
        return productRepository.findByNameContaining(letter);
    }

}
