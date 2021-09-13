package com.lukasondrak.libraryinformationsystem.features.client;

import com.lukasondrak.libraryinformationsystem.dto.ClientInfoWithoutLoansDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public String deleteClient(long clientId, HttpSession session) {
        List<ClientInfoWithoutLoansDto> allClients = getAllClientsInfo();
        session.setAttribute("clients", allClients);

        Optional<Client> clientToDeleteOptional = findById(clientId);
        if(clientToDeleteOptional.isEmpty()) {
            session.setAttribute("result", "Nepodařilo se smazat klienta. Klient se nenachází v databázi.");
        } else {
            clientRepository.deleteById(clientId);
            Client deletedClient = clientToDeleteOptional.get();
            session.setAttribute("result", "Klient " + deletedClient.getName() + " " + deletedClient.getSurname() + " byl úspěšně vymazán.");
        }
        return "redirect:/clients";
    }


    @Override
    public Optional<Client> findById(long clientId) {
        return clientRepository.findById(clientId);
    }

    @Override
    public List<ClientInfoWithoutLoansDto> getAllClientsInfo() {
        return clientRepository.findAll()
                .stream()
                .map(client -> new ClientInfoWithoutLoansDto(client.getIdClient(), client.getName(), client.getSurname(), client.getEmail()))
                .collect(Collectors.toList());
    }

    @Override
    public String addNewClient(Client newClient, BindingResult result, HttpSession session) {
        if(result.hasErrors()) {
            return "pages/client/newClient";
        }

        if(clientRepository.findByEmail(newClient.getEmail()).isPresent()) {
            session.setAttribute("result", "Nepodařilo se vytvořit uživatele. Uživatel s tímto emailem se již nachází v databázi.");
            return "redirect:/clients/new";
        }

        Client savedClient = clientRepository.save(new Client(newClient.getName(), newClient.getSurname(), newClient.getEmail()));

        session.setAttribute("result", "Uživatel " + savedClient.getName() + " " + savedClient.getSurname() + " byl úspěšně vytvořen.");
        return "redirect:/clients";
    }

}
