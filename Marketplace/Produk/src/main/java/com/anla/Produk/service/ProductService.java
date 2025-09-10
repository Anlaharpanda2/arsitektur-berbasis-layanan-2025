package com.anla.Produk.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.anla.Produk.model.Product;
import com.anla.Produk.repository.ProductRepository;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public Product updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            product.setNama(productDetails.getNama());
            product.setSatuan(productDetails.getSatuan());
            product.setHarga(productDetails.getHarga());
            return productRepository.save(product);
        }
        return null;
    }
}