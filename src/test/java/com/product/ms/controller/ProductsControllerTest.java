package com.product.ms.controller;

import com.product.ms.bean.ProductsBean;
import com.product.ms.model.Products;
import com.product.ms.service.ProductsService;
import com.product.ms.testBean.ProductsBeanTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ProductsControllerTest {

    @InjectMocks
    private ProductsController productsController;
    @Mock
    private ProductsService productsService;

    @BeforeEach
    void setUp(){
        List<Products> productsBeans = ProductsBeanTest.toEntity();

        BDDMockito.when(productsService.getAll())
                .thenReturn(productsBeans);

        BDDMockito.when(productsService.create(ArgumentMatchers.any(ProductsBean.class)))
                .thenReturn(productsBeans.get(0));

        BDDMockito.when(productsService.findOne(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(productsBeans.get(0)));

        BDDMockito.doNothing().when(productsService)
                .delete(ArgumentMatchers.anyLong());

        BDDMockito.when(productsService.update(ArgumentMatchers.anyLong(),
                ArgumentMatchers.any(ProductsBean.class))).thenReturn(productsBeans.get(0));

        BDDMockito.when(productsService.search(ArgumentMatchers.anyString(), ArgumentMatchers.anyDouble(), ArgumentMatchers.anyDouble()))
                .thenReturn(productsBeans);
    }

    @Test
    @DisplayName("listAll returns list of products when successful")
    void listAllProducts() {
        ProductsBean productsBean = ProductsBeanTest.productsBean();
        Products products = productsController.create(productsBean).getBody();
        Assertions.assertThat(products).isNotNull();
        List<Products> list = productsController.getAll().getBody();
        Assertions.assertThat(list).isNotNull().isNotEmpty();
    }

    @Test
    @DisplayName("get Product by id and return a product when successful")
    void getOne() {
        ResponseEntity<Products> one = productsController.getOne(10L);
        Assertions.assertThat(one.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(one).isNotNull();
        Assertions.assertThat(one.getBody()).isEqualTo(ProductsBeanTest.toEntity().get(0));
    }

    @Test
    @DisplayName("create a product and return a product when successful")
    void createProduct() {
        ProductsBean productsBean = ProductsBeanTest.productsBean();
        ResponseEntity<Products> responseEntity = productsController.create(productsBean);
        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getBody()).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(responseEntity.getBody().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("update a product and return a update product when successful")
    void updateProduct() {
        ProductsBean bean = ProductsBeanTest.productsBean();
        Assertions.assertThatCode(() -> productsController.update(1L, bean)).doesNotThrowAnyException();
        bean.setDescription("produtos");
        ResponseEntity<Products> update = productsController.update(2L, bean);
        Assertions.assertThat(update).isNotNull();
        Assertions.assertThat(update.getBody()).isNotNull();
        Assertions.assertThat(update.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(update.getBody().getName()).isEqualTo("produtos");
    }

    @Test
    @DisplayName("search with Params [q, min_price, max_price] then returns a List of Products")
    void deleteProduct(){
        ResponseEntity<List<Products>> teste = productsController.search("TESTE", 10.0, 15.00);
        Assertions.assertThat(teste).isNotNull();
        Assertions.assertThat(teste.getBody()).isNotNull();
        Assertions.assertThat(teste.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(teste.getBody()).isEqualTo(ProductsBeanTest.toEntity());
        Assertions.assertThat(teste.getBody()).hasSize(1);
    }
}
