package learn.lodging.domain;

import learn.lodging.data.GuestRepository;
import learn.lodging.models.Guest;
import learn.lodging.models.Host;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class GuestService {
    private final GuestRepository repository;

    public GuestService(GuestRepository repository) {
        this.repository = repository;
    }

    public List<Guest> findByLastName(String prefix) {
        return repository.findByLastName(prefix);
    }

}
