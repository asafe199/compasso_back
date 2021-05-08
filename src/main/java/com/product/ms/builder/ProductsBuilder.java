package com.product.ms.builder;

import com.product.ms.bean.ProductsBean;
import com.product.ms.model.Products;

public class ProductsBuilder {

    public static Products beanToEntity(ProductsBean bean){
        Products products = new Products();
        products.setId(bean.getId());
        products.setPrice(bean.getPrice());
        products.setName(bean.getName());
        products.setDescription(bean.getDescription());
        return products;
    }
}
