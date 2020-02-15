package com.horizon.article.demo.jpa.routing.repo;

import com.horizon.article.demo.jpa.routing.bean.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by linyanbin on 2020/2/14
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
