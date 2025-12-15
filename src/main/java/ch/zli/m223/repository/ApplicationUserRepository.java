package ch.zli.m223.repository;

import ch.zli.m223.model.ApplicationUser;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class ApplicationUserRepository implements PanacheRepository<ApplicationUser> {

    public Optional<ApplicationUser> findByUsername(String username) {
        return find("username", username).firstResultOptional();
    }
}
