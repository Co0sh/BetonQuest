/*
 * BetonQuest - advanced quests for Bukkit
 * Copyright (C) 2016  Jakub "Co0sh" Sapalski
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package pl.betoncraft.betonquest.compatibility.jobsreborn;

import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.container.Job;
import com.gamingmesh.jobs.container.JobsPlayer;
import com.gamingmesh.jobs.container.JobProgression;
import org.bukkit.entity.Player;
import pl.betoncraft.betonquest.Instruction;
import pl.betoncraft.betonquest.api.QuestEvent;
import pl.betoncraft.betonquest.exceptions.InstructionParseException;
import pl.betoncraft.betonquest.utils.PlayerConverter;

import java.util.List;

public class Event_AddLevel extends QuestEvent {
    private String sJobName;
    private Integer nAddLevel;

    public Event_AddLevel(Instruction instruction) throws InstructionParseException {
        super(instruction, true);

        if (instruction.size() < 3) {
            throw new InstructionParseException("Not enough arguments");
        }

        for (Job job : Jobs.getJobs()) {
            if (job.getName().equalsIgnoreCase(instruction.getPart(1))) {
                sJobName = job.getName();

                try {
                    this.nAddLevel = Integer.parseInt(instruction.getPart(2));
                } catch (Exception e) {
                    throw new InstructionParseException("NUJobs_AddLevel: Unable to parse the level amount", e);
                }

                return;
            }
        }

        throw new InstructionParseException("Jobs Reborn job " + instruction.getPart(1) + " does not exist");
    }

    @Override
    protected Void execute(String playerID) {
        Player oPlayer = PlayerConverter.getPlayer(playerID);
		JobsPlayer jPlayer = Jobs.getPlayerManager().getJobsPlayer(oPlayer);
		if (jPlayer == null)
			return null;

        List<JobProgression> oJobs = jPlayer.getJobProgression();
        for (JobProgression oJob : oJobs) {
            if (oJob.getJob().getName().equalsIgnoreCase(sJobName)) {
                oJob.setLevel(this.nAddLevel + oJob.getLevel());
				break;
            }
        }

        return null;
    }
}
