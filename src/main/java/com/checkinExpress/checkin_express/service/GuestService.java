package com.checkinExpress.checkin_express.service;

import com.checkinExpress.checkin_express.model.Guest;
import com.checkinExpress.checkin_express.repository.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GuestService {

    private final GuestRepository guestRepository;

    @Autowired
    public GuestService(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    public Optional<Guest> getGuestById(String guestId) {
        return guestRepository.findById(guestId);
    }

    public Optional<Guest> getGuestByName(String name) {
        return guestRepository.findByName(name); // Método agora está disponível
    }
}
