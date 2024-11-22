package com.checkinExpress.checkin_express;

import com.checkinExpress.checkin_express.controller.BookingController;
import com.checkinExpress.checkin_express.service.ExpenseSummaryService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CheckinExpressApplicationTests {

	@Autowired
	private ApplicationContext applicationCoxntext;

	@Test
	void contextLoads() {
		// Verifica se o contexto da aplicação foi carregado corretamente
		assertNotNull(applicationCoxntext);
	}

	// Caso queira testar algum componente específico
	@Test
	void testServiceBean() {
		// Verifica se o bean de um serviço específico está sendo carregado corretamente
		Object service = applicationCoxntext.getBean(ExpenseSummaryService.class);
		assertNotNull(service, "ExpenseSummaryService should not be null");
	}

	@Test
	void testControllerBean() {
		// Verifica se o bean de um controlador está sendo carregado corretamente
		Object controller = applicationCoxntext.getBean(BookingController.class);
		assertNotNull(controller, "BookingController should not be null");
	}
}