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

    /**
     * Redirects from root to clients
     * @param model model
     * @param session http session
     * @return clients page
     */
    @GetMapping("/")
    public String homePage(Model model, HttpSession session) {
        return "redirect:/clients";
    }

    /**
     * Returns clients page with set model
     * @param model model
     * @param session http session
     * @return clients page
     */
    @GetMapping("/clients")
    public String clientsPage(Model model, HttpSession session) {
        model.addAttribute("result",session.getAttribute("result"));
        model.addAttribute("clients", clientService.getAllClientsInfo());

        clearSessionResultAttribute(session);
        return "pages/client/clients";
    }

    /**
     * Returns add new client page with model
     * @param model model
     * @param session http session
     * @return add new client page
     */
    @GetMapping("/clients/new")
    public String newClientPage(Model model, HttpSession session) {
        model.addAttribute("result",session.getAttribute("result"));
        model.addAttribute("client", new Client());

        clearSessionResultAttribute(session);
        return "pages/client/newClient";
    }

    /**
     * Returns client's loans page
     * @param id client's id
     * @param model model
     * @param session http session
     * @return
     */
    @GetMapping("/client/{id}/loans")
    public String clientsLoansPage(@PathVariable long id, Model model, HttpSession session) {
        model.addAttribute("result",session.getAttribute("result"));
        Optional<Client> clientToOverviewOptional = clientService.findById(id);
        if(clientToOverviewOptional.isEmpty()) {
            session.setAttribute("result", "Nepodařilo se zobrazit výpůjčky klienta. Klient se nenachází v databázi.");
            return "redirect:/clients";
        }

        model.addAttribute("client", clientToOverviewOptional.get());
        clearSessionResultAttribute(session);
        return "pages/client/clientsloans";
    }

    /**
     * Delete client endpoint
     * @param id clients id
     * @param session http session
     * @return clients page
     */
    @DeleteMapping("/client/{id}")
    public String deleteClient(@PathVariable long id, HttpSession session) {
        return clientService.deleteClient(id, session);
    }

    /**
     * Create new client endpoint
     * @param newClient new client info
     * @param result binding result of form
     * @param session http session
     * @return clients page
     */
    @PostMapping("/clients/new")
    public String addNewClient(@Valid @ModelAttribute Client newClient, BindingResult result,
                               HttpSession session) {
        return clientService.addNewClient(newClient, result, session);
    }

}
