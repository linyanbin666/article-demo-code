package com.horizon.article.demo.jpa.routing.aop;

import com.horizon.article.demo.jpa.routing.ApplyDataSource;
import com.horizon.article.demo.jpa.routing.DatabaseContextHolder;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 *
 * Created by linyanbin on 2020/2/14
 */
@Aspect
@Component
public class DataSourceDetermineAspect {

  @Before("@annotation(applyDataSource)")
  public void before(ApplyDataSource applyDataSource) {
    DatabaseContextHolder.set(applyDataSource.value());
  }

  @After("@annotation(applyDataSource)")
  public void after(ApplyDataSource applyDataSource) {
    DatabaseContextHolder.clear();
  }

}
