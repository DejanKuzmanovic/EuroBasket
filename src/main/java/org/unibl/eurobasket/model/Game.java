package org.unibl.eurobasket.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User creator;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "game_teams",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    private Set<Team> teams;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    private Integer hostPoints;

    private Integer guestPoints;

    private boolean isActive;

    public Game() {
    }

    public Game(User creator, Set<Team> teams, Date startTime, Integer hostPoints, Integer guestPoints, boolean isActive) {
        this.creator = creator;
        this.teams = teams;
        this.startTime = startTime;
        this.hostPoints = hostPoints;
        this.guestPoints = guestPoints;
        this.isActive = isActive;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Set<Team> getTeams() {
        return teams;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Integer getHostPoints() {
        return hostPoints;
    }

    public void setHostPoints(Integer hostPoints) {
        this.hostPoints = hostPoints;
    }

    public Integer getGuestPoints() {
        return guestPoints;
    }

    public void setGuestPoints(Integer guestPoints) {
        this.guestPoints = guestPoints;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
