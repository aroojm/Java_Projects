package learn.lodging.data;

import learn.lodging.models.Host;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HostRepositoryDouble implements HostRepository{

    private final ArrayList<Host> hosts = new ArrayList<>();

    public final static Host HOST1 = new Host("hhhh-1111-aaaa-9999","HostFirst","test1@sfgate.com", "(806) 1783815",
            "3 Nova Trail","Amarillo","TX","79182",new BigDecimal(100),new BigDecimal(200)) ;

    public final static Host HOST2 = new Host("hhhh-2222-bbbb-8888","HostSecond","test2@posterous.com","(478) 7475991",
            "7262 Morning Avenue","Macon","GA","31296",new BigDecimal(200),new BigDecimal(300));

    public final static Host HOST3 = new Host("hhhh-3333-cccc-7777","HostThird","test3@apple.com","(954) 7895760",
            "1 Maple Wood Terrace","Orlando","FL","32825",new BigDecimal(300),new BigDecimal(400));

    public HostRepositoryDouble() {
        hosts.add(HOST1);
        hosts.add(HOST2);
        hosts.add(HOST3);
    }

    @Override
    public List<Host> findAll() {
        return new ArrayList<>(hosts);
    }

    @Override
    public Host findById(String id) {
        return hosts.stream()
                .filter(h -> h.getHostId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Host> findByLastName(String prefix) {
        return new ArrayList<>(hosts);
    }
}
