package com.product.ms.service;

import com.product.ms.bean.ProductsBean;
import com.product.ms.model.Products;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ProductsService {
    List<Products> getAll();
    Optional<Products> findOne(Long id);
    Products create(ProductsBean bean);
    Products update(Long idProduct, ProductsBean bean);
    void delete(Long idProduct);
    List<Products> search(String param, Double minPrice, Double maxPrice);
}
