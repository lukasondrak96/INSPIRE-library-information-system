package com.lukasondrak.libraryinformationsystem.features.client;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    Optional<Client> findById(long clientId);

    Optional<Client> findByEmail(String email);

    List<Client> getAllClients();


    Client save(Client client);

    void deleteClient(Client client);

    void deleteClientById(long clientId);

    void deleteClientByByEmail(String email);

}