package next.config;

import java.beans.PropertyVetoException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import next.support.DBInitializer;

@Configuration
@ComponentScan(basePackages = {
		"next" }, excludeFilters = @ComponentScan.Filter(value = Controller.class, type = FilterType.ANNOTATION))
@PropertySource("classpath:application.properties")
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AppConfig {
	@Value("${db.driver.class}")
	private String driverClass;

	@Value("${db.url}")
	private String url;

	@Value("${db.username}")
	private String username;

	@Value("${db.password}")
	private String password;

	@Bean
	public DataSource dataSource() throws PropertyVetoException {
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		cpds.setDriverClass(driverClass);
		cpds.setJdbcUrl(url);
		cpds.setUser(username);
		cpds.setPassword(password);
		return cpds;
	}

	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:messages");
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setCacheSeconds(30);
		return messageSource;
	}

	@Bean
	public DBInitializer dbInitializer() {
		return new DBInitializer();
	}
}
