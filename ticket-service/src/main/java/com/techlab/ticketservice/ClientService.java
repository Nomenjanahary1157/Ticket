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

    public Client findById(Integer id) {
        return clientRepository.findById(id).orElse(null);
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public Client save(Client client) {
        return clientRepository.save(client);
    }

    public void delete(Integer id) {
        clientRepository.deleteById(id);
    }
}
