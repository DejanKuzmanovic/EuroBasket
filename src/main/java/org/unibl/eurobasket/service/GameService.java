package org.unibl.eurobasket.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unibl.eurobasket.model.Game;
import org.unibl.eurobasket.model.Team;
import org.unibl.eurobasket.model.User;
import org.unibl.eurobasket.repository.GameRepository;
import org.unibl.eurobasket.repository.TeamRepository;

import java.util.*;

@Service
public class GameService {

    private GameRepository gameRepository;
    private TeamRepository teamRepository;

    public JSONObject getAllActive() {
        return getAll(true);
    }

    public JSONObject getAllFinished() {
        return getAll(false);
    }

    public JSONObject getScore(int id) {
        return gameRepository.findById(id).map(this::createJsonWithTeams).orElse(null);
    }

    public JSONObject createGame(String host, String guest, Date startTime, int hostPoints, int guestPoints, boolean isActive, User creator) {
        Optional<Team> team1 = teamRepository.findByCountry(host);
        Optional<Team> team2 = teamRepository.findByCountry(guest);
        if (team1.isPresent() && team2.isPresent()) {
            Set<Team> teams = new HashSet<>(Arrays.asList(team1.get(), team2.get()));
            Game game = new Game(creator, teams, startTime, hostPoints, guestPoints, isActive);
            gameRepository.save(game);
            return createGameJsonResponse(game);
        } else {
            return null;
        }
    }

    public JSONObject changeScore(int id, int hostPoints, int guestPoints) {
        Optional<Game> optionalGame = gameRepository.findById(id);
        if (optionalGame.isPresent()) {
            Game game = optionalGame.get();
            game.setHostPoints(hostPoints);
            game.setGuestPoints(guestPoints);
            gameRepository.save(game);
            return createGameJsonResponse(game);
        } else {
            return null;
        }
    }

    public JSONObject activateGame(int id) {
        return setGameActive(id, true);
    }

    public JSONObject finishGame(int id) {
        return setGameActive(id, false);
    }

    public JSONObject setGameActive(int id, boolean active) {
        Optional<Game> optionalGame = gameRepository.findById(id);
        if (optionalGame.isPresent() && optionalGame.get().isActive() != active) {
            Game game = optionalGame.get();
            game.setActive(active);
            gameRepository.save(game);
            return createGameJsonResponse(game);
        } else {
            return null;
        }
    }

    private JSONObject getAll(boolean isActive) {
        JSONObject result = new JSONObject();
        JSONArray games = new JSONArray();
        gameRepository.findAll().stream().filter(game -> game.isActive() == isActive).forEach(game -> games.put(createGameJsonResponse(game)));
        result.put("games", games);
        return result;
    }

    private JSONObject createGameJsonResponse(Game game) {
        JSONObject response = createJsonWithTeams(game);
        response.put("id", game.getId());
        response.put("creator", game.getCreator().getUsername());
        response.put("startTime", game.getStartTime());
        response.put("isActive", game.isActive());
        return response;
    }

    private JSONObject createJsonWithTeams(Game game) {
        JSONObject response = new JSONObject();
        List<Team> teams = new ArrayList<>(game.getTeams());
        response.put("host", teams.get(0).getCountry());
        response.put("hostPoints", game.getHostPoints());
        response.put("guest", teams.get(1).getCountry());
        response.put("guestPoints", game.getGuestPoints());
        return response;
    }

    @Autowired
    public void setGameRepository(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Autowired
    public void setTeamRepository(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }
}
