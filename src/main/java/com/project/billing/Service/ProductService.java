package com.project.billing.Service;

import com.project.billing.Dao.ProductDao;
import com.project.billing.Dto.Product;
import com.project.billing.Dto.ResponseStructure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductDao productDao;

    public ResponseEntity<ResponseStructure<Product>> save(Product product) {
        ResponseStructure<Product> responseStructure = new ResponseStructure<>();
        Product product1 = productDao.findById(product.getId());
        if (product1 != null && product1.getName().equalsIgnoreCase(product.getName())) {
            responseStructure.setStatus(HttpStatus.CONFLICT.value());
            responseStructure.setMessage("Product Already exits" + product.getName());
            responseStructure.setData(null);
            return new ResponseEntity<>(responseStructure, HttpStatus.CONFLICT);
        } else {
            responseStructure.setStatus(HttpStatus.OK.value());
            responseStructure.setMessage("Product Saved Succesfully");
            responseStructure.setData(productDao.save(product));
            return new ResponseEntity<>(responseStructure, HttpStatus.OK);
        }
    }

    public ResponseEntity<ResponseStructure<Product>> delete(long id) {
        ResponseStructure<Product> responseStructure = new ResponseStructure<>();
        Product product1 = productDao.findById(id);
        if (product1 != null) {
            productDao.delete(product1);
            responseStructure.setStatus(HttpStatus.OK.value());
            responseStructure.setMessage("Product Already exits" + id);
            responseStructure.setData(product1);
            return new ResponseEntity<>(responseStructure, HttpStatus.OK);
        } else {
            responseStructure.setStatus(HttpStatus.NOT_FOUND.value());
            responseStructure.setMessage("Product  Not Found to Delete");
            responseStructure.setData(null);
            return new ResponseEntity<>(responseStructure, HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<ResponseStructure<Product>> findById(long id) {
        ResponseStructure<Product> responseStructure = new ResponseStructure<>();
        Product product1 = productDao.findById(id);
        if (product1 != null) {
            responseStructure.setStatus(HttpStatus.FOUND.value());
            responseStructure.setMessage("Product Found Successfully" + id);
            responseStructure.setData(product1);
            return new ResponseEntity<>(responseStructure, HttpStatus.FOUND);
        } else {
            responseStructure.setStatus(HttpStatus.NOT_FOUND.value());
            responseStructure.setMessage("Product Not Found By id ");
            responseStructure.setData(null);
            return new ResponseEntity<>(responseStructure, HttpStatus.NOT_FOUND);

        }
    }


    public ResponseEntity<ResponseStructure<List<Product>>> findByNameContaining(String letter) {
        ResponseStructure<List<Product>> responseStructure = new ResponseStructure<>();
        List<Product> products = productDao.findByNameContaining(letter);

        if (products.isEmpty()) {
            responseStructure.setStatus(HttpStatus.NOT_FOUND.value());
            responseStructure.setMessage("No Product found with name containing letter " + letter);
            responseStructure.setData(null);
            return new ResponseEntity<>(responseStructure, HttpStatus.NOT_FOUND);
        } else {
            responseStructure.setStatus(HttpStatus.OK.value());
            responseStructure.setMessage("Product with name containing letter " + letter);
            responseStructure.setData(products);
            return new ResponseEntity<>(responseStructure, HttpStatus.OK);
        }
    }

    public ResponseEntity<ResponseStructure<List<Product>>> findAll() {
        ResponseStructure<List<Product>> responseStructure = new ResponseStructure<>();
        List<Product> product1 = productDao.findAll();
        if (product1 != null) {
            responseStructure.setStatus(HttpStatus.FOUND.value());
            responseStructure.setMessage("Product  Found Sucessfully  ");
            responseStructure.setData(product1);
            return new ResponseEntity<>(responseStructure, HttpStatus.FOUND);
        } else {
            responseStructure.setStatus(HttpStatus.NOT_FOUND.value());
            responseStructure.setMessage("Product  Not Found  ");
            responseStructure.setData(null);
            return new ResponseEntity<>(responseStructure, HttpStatus.NOT_FOUND);
        }
    }

}