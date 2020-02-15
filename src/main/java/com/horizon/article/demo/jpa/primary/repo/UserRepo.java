package com.horizon.article.demo.jpa.primary.repo;

import com.horizon.article.demo.jpa.primary.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by linyanbin on 2020/2/14
 */
public interface UserRepo extends JpaRepository<User, Long> {

}
