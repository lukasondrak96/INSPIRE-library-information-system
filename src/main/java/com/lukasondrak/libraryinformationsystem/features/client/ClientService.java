package com.lukasondrak.libraryinformationsystem.features.client;

import com.lukasondrak.libraryinformationsystem.dto.ClientInfoWithoutLoansDto;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

public interface ClientService {

    String deleteClient(long clientId, HttpSession session);

    Optional<Client> findById(long clientId);

    List<ClientInfoWithoutLoansDto> getAllClientsInfo();

    String addNewClient(Client newClient, BindingResult result, HttpSession session);
}