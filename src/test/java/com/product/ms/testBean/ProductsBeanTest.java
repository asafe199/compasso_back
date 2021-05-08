package com.product.ms.testBean;

import com.product.ms.bean.ProductsBean;
import com.product.ms.model.Products;

import java.util.List;

public class ProductsBeanTest {

    public static List<Products> toEntity(){
        Products products = new Products();
        products.setId(1L);
        products.setDescription("TESTE");
        products.setPrice(1000.00);
        products.setName("produtos");
        return List.of(products);
    }

    public static ProductsBean productsBean(){
        return ProductsBean.builder().description("PROD").price(1000.00).name("BEAN").build();
    }
}
