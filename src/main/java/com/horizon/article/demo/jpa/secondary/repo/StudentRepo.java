package com.horizon.article.demo.jpa.secondary.repo;

import com.horizon.article.demo.jpa.secondary.bean.Student;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by linyanbin on 2020/2/14
 */
public interface StudentRepo extends JpaRepository<Student, Long> {

}
