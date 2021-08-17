package learn.lodging.domain;

import learn.lodging.data.HostRepository;
import learn.lodging.models.Host;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class HostService {
    private final HostRepository repository;

    public HostService(HostRepository repository) {
        this.repository = repository;
    }

    public List<Host> findByLastName(String prefix) {return repository.findByLastName(prefix);}

}
