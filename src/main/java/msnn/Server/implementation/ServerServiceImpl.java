package msnn.Server.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msnn.Server.enumeration.Status;
import msnn.Server.model.Server;
import msnn.Server.repo.ServerRepo;
import msnn.Server.service.ServerService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Random;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class ServerServiceImpl implements ServerService {
    private final ServerRepo serverRepo;

    public ServerServiceImpl(ServerRepo serverRepo) {
        this.serverRepo = serverRepo;
    }

    @Override
    public Server create(Server server) {
      server.setImageUrl(setServerImageUrl());
        return serverRepo.save(server);
    }



    @Override
    public Server ping(String ipAddress) throws IOException {
        Server server=serverRepo.findByIpAddress(ipAddress);
        InetAddress address= InetAddress.getByName(ipAddress);
        server.setStatus(address.isReachable(100)? Status.SERVER_UP :Status.SERVER_DOWN);
        serverRepo.save(server);
        return server;
    }

    @Override
    public Collection<Server> list(int limit) {

        return serverRepo.findAll(PageRequest.of(0,limit)).toList();
    }

    @Override
    public Server get(Long id) {

        return serverRepo.findById(id).get();
    }

    @Override
    public Server update(Server server) {

        return serverRepo.save(server);
    }

    @Override
    public Boolean delete(Long id) {
        serverRepo.deleteById(id);
        return Boolean.TRUE;
    }
    private String setServerImageUrl() {
        String[]imageNames={"server1.png","server2.png","server3.png","server4.png"};
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/server/image"+imageNames[new Random().nextInt(1)]).toUriString();
    }
   
}
