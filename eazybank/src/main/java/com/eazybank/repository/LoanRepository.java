package com.eazybank.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import com.eazybank.model.Loans;

@Repository
public interface LoanRepository extends CrudRepository<Loans, Long> {

	@PreAuthorize("hasRole('ROOT')")
	List<Loans> findByCustomerIdOrderByStartDtDesc(int customerId);

}
