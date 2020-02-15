package com.horizon.article.demo.jpa.routing;

import com.horizon.article.demo.jpa.routing.bean.Customer;
import com.horizon.article.demo.jpa.routing.repo.CustomerRepository;
import java.util.HashMap;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(
    basePackageClasses = CustomerRepository.class,
    entityManagerFactoryRef = "customerEntityManager",
    transactionManagerRef = "customerTransactionManager")
public class DataSourceConfiguration {

    @Autowired(required = false)
    private PersistenceUnitManager persistenceUnitManager;

    @Bean
    @ConfigurationProperties("app.customer.jpa")
    public JpaProperties customerJpaProperties() {
        return new JpaProperties();
    }

    @Bean
    @ConfigurationProperties("app.customer.jpa.hibernate")
    public HibernateProperties customerHibernateProperties() {
        return new HibernateProperties();
    }

    @Bean
    @ConfigurationProperties("app.customer.development.datasource")
    public DataSourceProperties developmentDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource developmentDataSource() {
        return developmentDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean
    @ConfigurationProperties("app.customer.testing.datasource")
    public DataSourceProperties testingDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource testingDataSource() {
        return testingDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean
    @ConfigurationProperties("app.customer.production.datasource")
    public DataSourceProperties productionDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource productionDataSource() {
        return testingDataSourceProperties().initializeDataSourceBuilder().build();
    }

    /**
     * Adds all available datasources to datasource map.
     *
     * @return datasource of current context
     */
    @Bean
    @Primary
    public DataSource customerDataSource() {
        DataSourceRouter router = new DataSourceRouter();

        final HashMap<Object, Object> map = new HashMap<>(3);
        map.put(DatabaseEnvironment.DEVELOPMENT, developmentDataSource());
        map.put(DatabaseEnvironment.TESTING, testingDataSource());
        map.put(DatabaseEnvironment.PRODUCTION, productionDataSource());
        router.setTargetDataSources(map);
        return router;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean customerEntityManager(
        @Qualifier("customerJpaProperties") final JpaProperties customerJpaProperties) {
        EntityManagerFactoryBuilder builder =
            createEntityManagerFactoryBuilder(customerJpaProperties);

        return builder.dataSource(customerDataSource()).packages(Customer.class)
            .properties(customerHibernateProperties().determineHibernateProperties(
                customerJpaProperties.getProperties(), new HibernateSettings()
            )).persistenceUnit("customerEntityManager").build();
    }

    @Bean
    @Primary
    public JpaTransactionManager customerTransactionManager(
        @Qualifier("customerEntityManager") final EntityManagerFactory factory) {
        return new JpaTransactionManager(factory);
    }

    private EntityManagerFactoryBuilder createEntityManagerFactoryBuilder(
        final JpaProperties customerJpaProperties) {
        JpaVendorAdapter jpaVendorAdapter =
            createJpaVendorAdapter(customerJpaProperties);
        return new EntityManagerFactoryBuilder(jpaVendorAdapter,
            customerJpaProperties.getProperties(), persistenceUnitManager);
    }

    private JpaVendorAdapter createJpaVendorAdapter(
        JpaProperties jpaProperties) {
        AbstractJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setShowSql(jpaProperties.isShowSql());
        adapter.setGenerateDdl(jpaProperties.isGenerateDdl());
        return adapter;
    }
}