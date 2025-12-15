package ch.zli.m223.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import ch.zli.m223.model.ApplicationUser;
import ch.zli.m223.repository.ApplicationUserRepository;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ApplicationScoped
public class ApplicationUserService {

    @Inject
    ApplicationUserRepository applicationUserRepository;

    /**
     * Authenticate user with username and password
     * @param username Username
     * @param password Plain password
     * @return JWT token if authentication successful, null otherwise
     */
    public String authenticate(String username, String password) {
        Optional<ApplicationUser> userOptional = applicationUserRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            return null;
        }

        ApplicationUser user = userOptional.get();

        // Verify password with BCrypt
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());

        if (!result.verified) {
            return null;
        }

        // Generate JWT token
        return generateToken(user);
    }

    /**
     * Generate JWT token for authenticated user
     */
    private String generateToken(ApplicationUser user) {
        Set<String> roles = new HashSet<>();
        roles.add(user.getRole());

        return Jwt.upn(user.getUsername())
                .issuer("https://punchclock.com/issuer")
                .groups(roles)
                .expiresIn(3600) // 1 hour
                .sign();
    }

    /**
     * Hash password using BCrypt
     */
    public String hashPassword(String plainPassword) {
        return BCrypt.withDefaults().hashToString(12, plainPassword.toCharArray());
    }

    @Transactional
    public ApplicationUser createUser(ApplicationUser user) {
        // Hash password before persisting
        user.setPassword(hashPassword(user.getPassword()));
        applicationUserRepository.persist(user);
        return user;
    }

    public List<ApplicationUser> findAll() {
        return applicationUserRepository.listAll();
    }

    public Optional<ApplicationUser> findByUsername(String username) {
        return applicationUserRepository.findByUsername(username);
    }
}
