package com.ap.homebanking.services;

import com.ap.homebanking.dtos.ClientDTO;
import com.ap.homebanking.models.Client;

import java.util.List;

public interface ClientService {
    List<ClientDTO> getClients();
    ClientDTO getClient(Long id);
    Client findById(Long id);
    void save(Client client);
    Client findByEmail(String email);

}
