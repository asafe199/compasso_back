package com.product.ms.controller;

import com.product.ms.bean.ProductsBean;
import com.product.ms.model.Products;
import com.product.ms.service.ProductsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping(value = "products", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductsController {

    private final ProductsService productService;

    @GetMapping
    public ResponseEntity<List<Products>> getAll(){
        return ResponseEntity.ok(productService.getAll());
    }

    @PostMapping
    public ResponseEntity<Products> create(@Valid @RequestBody ProductsBean productBean){
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(productBean));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Products> getOne(@PathVariable("id") Long id){
        Optional<Products> productModel = productService.findOne(id);
        return productModel.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Products> update(@PathVariable("id") Long id, @Valid @RequestBody ProductsBean bean){
        return ResponseEntity.ok(productService.update(id, bean));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id){
        productService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Products>> search(
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(value = "min_price", required = false) Double minPrice,
            @RequestParam(value = "max_price", required = false) Double maxPrice){
        return ResponseEntity.ok(this.productService.search(query, minPrice, maxPrice));
    }
}
