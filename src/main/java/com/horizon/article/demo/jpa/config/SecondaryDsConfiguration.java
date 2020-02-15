package com.horizon.article.demo.jpa.config;

import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Created by linyanbin on 2020/2/13
 */
@Configuration
@EnableJpaRepositories(
    entityManagerFactoryRef = "secondaryEntityManager",
    transactionManagerRef = "secondaryTransactionManager",
    basePackageClasses = {com.horizon.article.demo.jpa.secondary.repo.Package.class})
public class SecondaryDsConfiguration {

  @Bean("secondaryDataSourceProperties")
  @ConfigurationProperties(prefix = "spring.datasource.secondary")
  public DataSourceProperties secondaryDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean("secondaryDataSource")
  public DataSource secondaryDataSource() {
    return secondaryDataSourceProperties().initializeDataSourceBuilder().build();
  }

  @Bean("secondaryJpaProperties")
  @ConfigurationProperties(prefix = "spring.datasource.secondary.jpa")
  public JpaProperties secondaryJpaProperties() {
    return new JpaProperties();
  }

  @Bean("secondaryHibernateProperties")
  @ConfigurationProperties(prefix = "spring.datasource.secondary.jpa.hibernate")
  public HibernateProperties secondaryHibernateProperties() {
    return new HibernateProperties();
  }

  @Bean("secondaryEntityManager")
  public LocalContainerEntityManagerFactoryBean secondaryEntityManager() {
    final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setPackagesToScan(com.horizon.article.demo.jpa.secondary.bean.Package.class.getPackage().getName()
    );
    em.setDataSource(secondaryDataSource());
    em.setJpaVendorAdapter(secondaryVendorAdapter());
    Map<String, Object> properties = secondaryHibernateProperties()
        .determineHibernateProperties(
            secondaryJpaProperties().getProperties(), new HibernateSettings()
        );
    em.setJpaPropertyMap(properties);
    em.setPersistenceUnitName("secondary");
    return em;
  }

  @Bean("secondaryVendorAdapter")
  public HibernateJpaVendorAdapter secondaryVendorAdapter() {
    final HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
    adapter.setGenerateDdl(false);
    return adapter;
  }

  @Bean("secondaryTransactionManager")
  public PlatformTransactionManager secondaryTransactionManager() {
    final JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(secondaryEntityManager().getObject());
    return transactionManager;
  }

  @Bean
  @Qualifier("secondaryJdbcTemplate")
  public JdbcTemplate secondaryJdbcTemplate() {
    return new JdbcTemplate(secondaryDataSource());
  }
}