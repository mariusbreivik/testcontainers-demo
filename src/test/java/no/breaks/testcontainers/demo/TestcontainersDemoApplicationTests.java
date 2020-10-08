package no.breaks.testcontainers.demo;

import com.sun.istack.NotNull;
import org.junit.After;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ContextConfiguration(initializers = TestcontainersDemoApplicationTests.Initializer.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
@Testcontainers
class TestcontainersDemoApplicationTests {

	@Container
	private static JdbcDatabaseContainer mssqlServerContainer = new MSSQLServerContainer("mcr.microsoft.com/mssql/server:2019-latest")
			.acceptLicense();

	public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

		@Override
		public void initialize(@NotNull ConfigurableApplicationContext configurableApplicationContext) {
			mssqlServerContainer.withConnectTimeoutSeconds(24);
			mssqlServerContainer.addExposedPort(1433);

			configurableApplicationContext.getEnvironment().getSystemProperties().put("spring.datasource.url", mssqlServerContainer.getJdbcUrl() + ":1433" );
			configurableApplicationContext.getEnvironment().getSystemProperties().put("spring.datasource.username", mssqlServerContainer.getUsername());
			configurableApplicationContext.getEnvironment().getSystemProperties().put("spring.datasource.password", mssqlServerContainer.getPassword());

			mssqlServerContainer.start();

		}
	}

	@Test
	void contextLoads() {
		mssqlServerContainer.start();
		assertTrue(true);
	}

}
