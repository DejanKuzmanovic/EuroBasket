package org.unibl.eurobasket.startup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.unibl.eurobasket.model.*;
import org.unibl.eurobasket.repository.*;

import java.util.*;
import java.util.stream.Collectors;

@Component
class InitialDataLoader implements ApplicationListener<ApplicationReadyEvent> {

    private static final String ADMIN_ROLE = "ROLE_ADMIN";
    private static final String MODERATOR_ROLE = "ROLE_MODERATOR";
    private static final String PREMIUM_USER_ROLE = "ROLE_PREMIUM_USER";
    private static final String USER_ROLE = "ROLE_USER";
    private static final List<String> countries = Arrays.asList("Spanija", "Litvanija", "Francuska", "Srbija", "Grcka", "Italija", "Ceska", "Rusija");

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private TeamRepository teamRepository;
    private GameRepository gameRepository;
    private NewsRepository newsRepository;
    private CommentRepository commentRepository;

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        createRoles();
        createUserWithRole("admin", "admin", Arrays.asList(USER_ROLE, PREMIUM_USER_ROLE, MODERATOR_ROLE, ADMIN_ROLE));
        createUserWithRole("moderator", "moderator", Arrays.asList(USER_ROLE, PREMIUM_USER_ROLE, MODERATOR_ROLE));
        createUserWithRole("premium", "premium", Arrays.asList(USER_ROLE, PREMIUM_USER_ROLE));
        createUserWithRole("user", "user", Collections.singletonList(USER_ROLE));
        createTeams();
        createGames();
        createNews();
    }

    private void createRoles() {
        List<String> roles = Arrays.asList(USER_ROLE, PREMIUM_USER_ROLE, MODERATOR_ROLE, ADMIN_ROLE);
        for (String roleName : roles) {
            if (roleRepository.findByName(roleName).isEmpty()) {
                Role role = new Role(roleName);
                roleRepository.save(role);
            }
        }
    }

    private void createUserWithRole(String username, String password, List<String> roleList) {
        if (userRepository.findByUsername(username).isEmpty()) {
            Set<Role> roles = roleList.stream().filter(x -> roleRepository.findByName(x).isPresent()).map(x -> roleRepository.findByName(x).get()).collect(Collectors.toSet());
            User user = new User(username, new BCryptPasswordEncoder().encode(password), roles);
            userRepository.save(user);
        }
    }

    private void createTeams() {
        for (String country : countries) {
            if (teamRepository.findByCountry(country).isEmpty()) {
                Team team = new Team(country);
                teamRepository.save(team);
            }
        }
    }

    private void createGames() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        createGame("Spanija", "Francuska", cal.getTime(), 0, 0, true);

        cal.add(Calendar.MINUTE, -10);
        createGame("Litvanija", "Srbija", cal.getTime(), 13, 10, true);

        cal.add(Calendar.MINUTE, -20);
        createGame("Grcka", "Italija", cal.getTime(), 46, 62, true);

        cal.add(Calendar.DAY_OF_MONTH, -1);
        createGame("Ceska", "Rusija", cal.getTime(), 78, 81, false);
    }

    private void createGame(String host, String guest, Date startTime, int hostPoints, int guestPoints, boolean isActive) {
        Set<Team> teams = new HashSet<>(Arrays.asList(teamRepository.findByCountry(host).orElse(null), teamRepository.findByCountry(guest).orElse(null)));
        Game game = new Game(userRepository.findByUsername("admin").orElse(null), teams, startTime, hostPoints, guestPoints, isActive);
        gameRepository.save(game);
    }

    private void createNews() {
        News news1 = new News(userRepository.findByUsername("moderator").orElse(null), "Naslov 1", "Tekst 1");
        Comment comment1 = new Comment(userRepository.findByUsername("user").orElse(null), news1, "Komentar 1");
        Comment comment2 = new Comment(userRepository.findByUsername("premium").orElse(null), news1, "Komentar 2");
        newsRepository.save(news1);
        commentRepository.save(comment1);
        commentRepository.save(comment2);
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setTeamRepository(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Autowired
    public void setGameRepository(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Autowired
    public void setNewsRepository(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @Autowired
    public void setCommentRepository(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
}