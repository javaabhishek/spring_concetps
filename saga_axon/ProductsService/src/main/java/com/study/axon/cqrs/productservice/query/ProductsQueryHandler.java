package com.study.axon.cqrs.productservice.query;


import com.study.axon.cqrs.productservice.core.data.ProductEntity;
import com.study.axon.cqrs.productservice.core.data.ProductsRepository;
import com.study.axon.cqrs.productservice.query.rest.ProductRestModel;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductsQueryHandler {

    private final ProductsRepository productsRepository;

    public ProductsQueryHandler(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    @QueryHandler
    public List<ProductRestModel> findProducts(FindProductsQuery findProductsQuery){
        List<ProductRestModel> products=new ArrayList<>();
        List<ProductEntity> storedProducts=productsRepository.findAll();
        for(ProductEntity productEntity: storedProducts) {
            ProductRestModel productRestModel = new ProductRestModel();
            BeanUtils.copyProperties(productEntity, productRestModel);
            products.add(productRestModel);
        }
        return products;
    }

}
