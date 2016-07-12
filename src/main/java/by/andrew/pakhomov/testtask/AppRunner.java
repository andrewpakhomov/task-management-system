/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.andrew.pakhomov.testtask;

import java.io.File;
import java.util.Properties;
import javax.sql.DataSource;
import org.hibernate.SessionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;

/**
 *
 * @author Andrew P.
 */
@EnableAutoConfiguration
@SpringBootApplication
public class AppRunner {

    private final static String DEFAULT_HSQLDB_FILE_NAME = "database"+File.separator+"database";
    
    private final static String JDBC_DRIVER_NAME = "org.hsqldb.jdbc.JDBCDriver";
    

    private static String jdbcConnectionString;

    public static void main(String[] args) {
        if (args.length > 1) {
            printHelp();
        }
        String databaseFileName = DEFAULT_HSQLDB_FILE_NAME;
        if (args.length == 1) {
            if (args[0].equals("?") || args[0].equals("help")) {
                printHelp();
            } else {
                databaseFileName = args[0];
            }
        }
        jdbcConnectionString = "jdbc:hsqldb:file:" + databaseFileName;

        ApplicationContext ctx = SpringApplication.run(AppRunner.class, args);
//        for (String current : ctx.getBeanDefinitionNames()) {
//            System.out.println(current);
//        }
    }

    public static void printHelp() {

    }

    @Bean
    @Primary
    public DataSource getApplicationDataSourceBean() {
        return DataSourceBuilder.create().driverClassName(JDBC_DRIVER_NAME).url(jdbcConnectionString).build();
    }

    @Bean
    @Primary
    public SessionFactory getHibernateSessionFactoryBean(DataSource ds) {
        LocalSessionFactoryBuilder builder = new LocalSessionFactoryBuilder(ds);
        builder.addProperties(this.getHibernateProperties());
        builder.scanPackages("by.andrew.pakhomov.testtask.web.domain");
        return builder.buildSessionFactory();
    }

    private Properties getHibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
        properties.put("hibernate.hbm2ddl.auto", "update");
        return properties;
    }

}
