package com.lukasondrak.libraryinformationsystem.features.client;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

import static com.lukasondrak.libraryinformationsystem.common.HttpSessionManipulator.clearSessionResultAttribute;

@Controller
@AllArgsConstructor
public class ClientController {

    private ClientService clientService;

    @GetMapping("/clients")
    public String clientsPage(Model model, HttpSession session) {
        model.addAttribute("result",session.getAttribute("result"));
        model.addAttribute("clients", clientService.getAllClientsInfo());

        clearSessionResultAttribute(session);
        return "pages/client/clients";
    }

    @GetMapping("/clients/new")
    public String newClientPage(Model model, HttpSession session) {
        model.addAttribute("result",session.getAttribute("result"));
        model.addAttribute("client", new Client());

        clearSessionResultAttribute(session);
        return "pages/client/newClient";
    }

    @GetMapping("/clients/{id}/loans")
    public String clientsLoansPage(@PathVariable long id, Model model, HttpSession session) {
        model.addAttribute("result",session.getAttribute("result"));
        Optional<Client> clientToOverviewOptional = clientService.findById(id);
        if(clientToOverviewOptional.isEmpty()) {
            session.setAttribute("result", "Nepodařilo se zobrazit vypůjčky klienta. Klient se nenachází v databázi.");
            return "redirect:/clients";
        }

        model.addAttribute("client", clientToOverviewOptional.get());
        clearSessionResultAttribute(session);
        return "pages/client/clientsloans";
    }

    @DeleteMapping("/clients/{id}")
    public String deleteClient(@PathVariable long id, HttpSession session) {
        return clientService.deleteClient(id, session);
    }

    @PostMapping("/clients/new")
    public String addNewClient(@Valid @ModelAttribute Client newClient, BindingResult result,
                               HttpSession session) {
        return clientService.addNewClient(newClient, result, session);
    }

}
