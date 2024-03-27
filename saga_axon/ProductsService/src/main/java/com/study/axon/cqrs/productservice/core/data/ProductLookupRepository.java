package com.study.axon.cqrs.productservice.core.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductLookupRepository extends JpaRepository<ProductLookupEntity, String> {
	ProductLookupEntity findByProductIdOrTitle(String productId, String title);
}