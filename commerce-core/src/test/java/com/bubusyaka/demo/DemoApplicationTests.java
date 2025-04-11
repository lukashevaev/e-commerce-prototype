package com.bubusyaka.demo;

import lombok.AllArgsConstructor;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bubusyaka.demo.repository.jpa.NewOrderRepository;
import com.bubusyaka.demo.repository.jpa.OrderRepository;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("tc")
@Component
@ContextConfiguration(initializers = {DemoApplicationTests.Initializer.class})
class DemoApplicationTests {

	@Container
	private static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
			new PostgreSQLContainer<>("postgres:11")
					.withDatabaseName("test-db")
					.withUsername("buba")
					.withPassword("zuyba")// Создать контейнер из образа postgres:11
					.withInitScript("db_orders.sql");// Выполнить db.sql после запуска

	@Autowired // используем бин из спринга
	private NewOrderRepository newOrderRepository;

	@Autowired
	private OrderRepository oldOrderRepository;

	@Test
	void findAllFrameworks_ReturnsFrameworksList() {
		var oldOrders = oldOrderRepository.count();
		// when
		//var ordersSize = repository.count();
		var transferedOrders = newOrderRepository.transferCompletedOrders();
		var sizeOfTransferesOrders = oldOrderRepository.count() - oldOrders;
		// then
		assertEquals(sizeOfTransferesOrders, transferedOrders.size());
		//assertEquals(1, 1);
	}

	static class Initializer
			implements ApplicationContextInitializer<ConfigurableApplicationContext> {
		public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
			TestPropertyValues.of(
					"spring.datasource.url=" + POSTGRESQL_CONTAINER.getJdbcUrl(),
					"spring.datasource.username=" + POSTGRESQL_CONTAINER.getUsername(),
					"spring.datasource.password=" + POSTGRESQL_CONTAINER.getPassword()
			).applyTo(configurableApplicationContext.getEnvironment());
		}
	}

}