package com.lukasondrak.libraryinformationsystem.features.client;

import com.lukasondrak.libraryinformationsystem.features.author.AuthorRepository;
import com.lukasondrak.libraryinformationsystem.features.author.AuthorService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;


    @Override
    public Optional<Client> findById(long clientId) {
        return clientRepository.findById(clientId);
    }

    @Override
    public Optional<Client> findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public Client save(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public void deleteClient(Client client) {
        clientRepository.delete(client);
    }

    @Override
    public void deleteClientById(long clientId) {
        clientRepository.deleteById(clientId);
    }

    @Override
    public void deleteClientByByEmail(String email) {
        clientRepository.deleteByEmail(email);
    }
}
