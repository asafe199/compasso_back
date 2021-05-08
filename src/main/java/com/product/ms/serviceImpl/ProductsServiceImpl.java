package com.product.ms.serviceImpl;

import com.product.ms.bean.ProductsBean;
import com.product.ms.builder.ProductsBuilder;
import com.product.ms.exceptions.NotFoundException;
import com.product.ms.model.Products;
import com.product.ms.repository.ProductsRepository;
import com.product.ms.service.ProductsService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class ProductsServiceImpl implements ProductsService {

    @PersistenceContext
    private final EntityManager entityManager;
    private final ProductsRepository productsRepository;

    @Override
    public List<Products> getAll() {
        return this.productsRepository.findAll();
    }

    @Override
    public Optional<Products> findOne(Long id) {
        return this.productsRepository.findById(id);
    }

    @Override
    public Products create(ProductsBean bean) {
        try {
            return productsRepository.save(ProductsBuilder.beanToEntity(bean));
        } catch (Exception e){
            log.error("[ERROR CREATE PRODUCT]", e);
            throw e;
        }
    }

    @Override
    public Products update(Long idProduct, ProductsBean bean) {
        Optional<Products> productModel = productsRepository.findById(idProduct);
        throwIfProductNotExists(productModel);
        Products model = ProductsBuilder.beanToEntity(bean);
        model.setId(idProduct);
        try {
            return productsRepository.save(model);
        }catch (Exception e){
            log.error("[ERROR UPDATE PRODUCT]", e);
            throw e;
        }
    }

    private void throwIfProductNotExists(Optional<Products> productModel){
        if(productModel.isEmpty()){
            throw new NotFoundException();
        }
    }

    @Override
    public void delete(Long idProduct) {
        Optional<Products> productModel = this.productsRepository.findById(idProduct);
        throwIfProductNotExists(productModel);
        try {
            this.productsRepository.delete(productModel.get());
        } catch (Exception e){
            log.error("[ERROR DELETE PRODUCT]", e);
            throw e;
        }
    }

    @Override
    public List<Products> search(String param, Double minPrice, Double maxPrice) {
        CriteriaBuilder criteria = entityManager.getCriteriaBuilder();
        CriteriaQuery<Products> criteriaQuery = criteria.createQuery(Products.class);
        Root<Products> productsRoot = criteriaQuery.from(Products.class);
        List<Predicate> predicates = new ArrayList<>();
        if(!ObjectUtils.isEmpty(param)){
            Predicate predicateName = criteria.equal(criteria.upper(productsRoot.get("name")), param.toUpperCase());
            Predicate predicateDesc = criteria.equal(criteria.upper(productsRoot.get("description")), param.toUpperCase());
            predicates.add(criteria.or(predicateName, predicateDesc));
        }
        if(!ObjectUtils.isEmpty(minPrice) && ObjectUtils.isEmpty(maxPrice)) {
            predicates.add(criteria.greaterThanOrEqualTo(productsRoot.get("price"), minPrice));
        }
        if(ObjectUtils.isEmpty(minPrice) && !ObjectUtils.isEmpty(maxPrice)) {
            predicates.add(criteria.lessThanOrEqualTo(productsRoot.get("price"), maxPrice));
        }
        if(!ObjectUtils.isEmpty(minPrice) && !ObjectUtils.isEmpty(maxPrice)) {
            predicates.add(criteria.between(productsRoot.get("price"), minPrice, maxPrice));
        }
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
