package com.checkinExpress.checkin_express.repository;

import com.checkinExpress.checkin_express.model.Expense;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class ExpenseRepositoryTest {

    @Autowired
    private ExpenseRepository expenseRepository;

    private Expense expense1;
    private Expense expense2;

    @BeforeEach
    void setUp() {
        // Limpa a base de dados antes de cada teste
        expenseRepository.deleteAll();

        // Criando objetos de teste
        expense1 = new Expense("Food", 100.0);
        expense2 = new Expense("Transport", 50.0);
        expense1.setBookingId("1");
        expense2.setBookingId("1");

        // Salvando as despesas no repositório
        expenseRepository.save(expense1);
        expenseRepository.save(expense2);
    }

    @Test
    void testFindByBookingId() {
        // Verifica se a consulta por bookingId retorna a lista correta de despesas
        List<Expense> expenses = expenseRepository.findByBookingId("1");

        assertNotNull(expenses);
        assertEquals(2, expenses.size());
        assertTrue(expenses.contains(expense1));
        assertTrue(expenses.contains(expense2));
    }

    @Test
    void testFindByBookingIdNoResults() {
        // Verifica se retorna uma lista vazia para um bookingId inexistente
        List<Expense> expenses = expenseRepository.findByBookingId("non-existent");

        assertNotNull(expenses);
        assertTrue(expenses.isEmpty());
    }
}