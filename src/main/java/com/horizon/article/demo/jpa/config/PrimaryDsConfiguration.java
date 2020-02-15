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
    entityManagerFactoryRef = "primaryEntityManager",
    transactionManagerRef = "primaryTransactionManager",
    basePackageClasses = {com.horizon.article.demo.jpa.primary.repo.Package.class})
public class PrimaryDsConfiguration {

  @Bean("primaryDataSourceProperties")
  @ConfigurationProperties(prefix = "spring.datasource.primary")
  public DataSourceProperties primaryDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean("primaryDataSource")
  public DataSource primaryDataSource() {
    return primaryDataSourceProperties().initializeDataSourceBuilder().build();
  }

  @Bean("primaryJpaProperties")
  @ConfigurationProperties(prefix = "spring.datasource.primary.jpa")
  public JpaProperties primaryJpaProperties() {
    return new JpaProperties();
  }

  @Bean("primaryHibernateProperties")
  @ConfigurationProperties(prefix = "spring.datasource.primary.jpa.hibernate")
  public HibernateProperties primaryHibernateProperties() {
    return new HibernateProperties();
  }

  @Bean("primaryEntityManager")
  public LocalContainerEntityManagerFactoryBean primaryEntityManager() {
    final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setPackagesToScan(com.horizon.article.demo.jpa.primary.bean.Package.class.getPackage().getName()
    );
    em.setDataSource(primaryDataSource());
    em.setJpaVendorAdapter(primaryVendorAdapter());
    Map<String, Object> properties = primaryHibernateProperties()
        .determineHibernateProperties(
            primaryJpaProperties().getProperties(), new HibernateSettings()
        );
    em.setJpaPropertyMap(properties);
    em.setPersistenceUnitName("primary");
    return em;
  }

  @Bean("primaryVendorAdapter")
  public HibernateJpaVendorAdapter primaryVendorAdapter() {
    final HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
    adapter.setGenerateDdl(false);
    return adapter;
  }

  @Bean("primaryTransactionManager")
  public PlatformTransactionManager primaryTransactionManager() {
    final JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(primaryEntityManager().getObject());
    return transactionManager;
  }

  @Bean
  @Qualifier("primaryJdbcTemplate")
  public JdbcTemplate primaryJdbcTemplate() {
    return new JdbcTemplate(primaryDataSource());
  }
}