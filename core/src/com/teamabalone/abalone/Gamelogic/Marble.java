package com.teamabalone.abalone.Gamelogic;

/**
 * Class which represents a marble token.
 * <P></P>
 * This is used to check which player this marble belongs to.
 */
public class Marble {
	private Team team;

	/**
	 * Constructor for {@code Marble}.
	 *
	 * @param team the player this marble belongs to
	 */
	public Marble(Team team) {
		this.team = team;
	}

	/**
	 * Gets the Team.
	 *
	 * @return the player this marble belongs to
	 */
	public Team getTeam() {
		return team;
	}

	/**
	 * Sets the Team.
	 *
	 * @param team the player this marble should belong to
	 */
	public void setTeam(Team team) {
		this.team = team;
	}
}
