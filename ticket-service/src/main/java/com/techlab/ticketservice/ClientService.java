package com.techlab.ticketservice;

import com.techlab.ticketrepository.models.Client;
import com.techlab.ticketrepository.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public Client findClientById(Integer id) {
        return clientRepository.findById(id).orElse(null);
    }

    public List<Client> findAllClients() {
        return clientRepository.findAll();
    }

    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    public void deleteClient(Integer id) {
        clientRepository.deleteById(id);
    }
}
