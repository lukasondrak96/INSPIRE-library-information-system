package com.lukasondrak.libraryinformationsystem.repositoryTests;

import com.lukasondrak.libraryinformationsystem.features.client.Client;
import com.lukasondrak.libraryinformationsystem.features.client.ClientRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@EnableTransactionManagement
public class ClientRepositoryTest {

    public static final long ID_NOT_IN_DB = 123456L;
    public static final String MOCK_NAME = "MockName";
    public static final String MOCK_SURNAME = "MockSurname";
    public static final String MOCK_EMAIL = "mock@mock.com";

    private static Client client;

    @Autowired
    private ClientRepository clientRepository;


    @BeforeEach
    public void beforeEach() {
        clientRepository.deleteAll();
        client = new Client(MOCK_NAME, MOCK_SURNAME, MOCK_EMAIL);
    }

    @AfterEach
    public void afterEach() {
        clientRepository.deleteAll();
    }

    @Test
    public void saveClientAndFindByIdTest() {
        Client savedClient = clientRepository.save(client);

        Optional<Client> foundClient = clientRepository.findById(savedClient.getIdClient());

        assertTrue(foundClient.isPresent(), "We should find some client");
        assertEquals(savedClient, foundClient.get(), "We should get the same client as the client we saved");
        assertEquals(MOCK_NAME, foundClient.get().getName(), "We should get the same client name as the clients name we saved");
        assertEquals(MOCK_SURNAME, foundClient.get().getSurname(), "We should get the same client surname as the clients name we saved");
        assertEquals(MOCK_EMAIL, foundClient.get().getEmail(), "We should get the same client email as the clients email we saved");
    }

    @Test
    public void saveClientsWithSameEmailTest() {
        Client client2 = new Client("MockName2", "MockSurname2", MOCK_EMAIL);

        Client savedClient = clientRepository.save(client);
        assertThrows(DataIntegrityViolationException.class, () -> {
            Client savedClient2 = clientRepository.save(client2);
        });

        List<Client> allClients = clientRepository.findAll();

        assertFalse(allClients.isEmpty(), "There should be some client");
        assertEquals(1, allClients.size(), "There should be only one clients");
        assertEquals(savedClient, allClients.get(0), "Saved client should be the first client we saved");
    }

    @Test
    public void findAllTest() {
        Client client2 = new Client("MockName2", "MockSurname2", "another@mock.com");
        List<Client> savedClientList = new ArrayList<>();
        savedClientList.add(clientRepository.save(client));
        savedClientList.add(clientRepository.save(client2));

        List<Client> allClients = clientRepository.findAll();

        assertFalse(allClients.isEmpty(), "There should be some client");
        assertEquals(2, allClients.size(), "There should be only two clients");
        assertEquals(allClients, savedClientList, "Saved clients should be the same as the clients we saved");
    }

    @Test
    public void findByNotExistentIdTest() {
        Optional<Client> foundClient = clientRepository.findById(ID_NOT_IN_DB);

        assertTrue(foundClient.isEmpty(), "There should not be any client with this ID");
    }

    @Test
    public void deleteTest() {
        Client savedClient = clientRepository.save(client);
        clientRepository.delete(savedClient);

        Optional<Client> foundClient = clientRepository.findById(savedClient.getIdClient());

        assertTrue(foundClient.isEmpty(), "There should not be any client with this ID");
    }

    @Test
    public void deleteNotExistentClientTest() {
        clientRepository.delete(client);

        List<Client> allClients = clientRepository.findAll();

        assertTrue(allClients.isEmpty(), "There should not be any client");
    }

}
