package pl.betoncraft.betonquest.conditions;

import pl.betoncraft.betonquest.Instruction;
import pl.betoncraft.betonquest.VariableNumber;
import pl.betoncraft.betonquest.api.Condition;
import pl.betoncraft.betonquest.exceptions.InstructionParseException;
import pl.betoncraft.betonquest.exceptions.QuestRuntimeException;

import java.util.Random;

/**
 * The condition that is met randomly
 */
public class RandomCondition extends Condition {

    private final VariableNumber valueMax;
    private final VariableNumber rangeOfRandom;

    public RandomCondition(final Instruction instruction) throws InstructionParseException {
        super(instruction, false);
        staticness = true;
        persistent = true;
        final String[] values = instruction.next().split("-");
        if (values.length != 2) {
            throw new InstructionParseException("Wrong randomness format");
        }
        final String packName = instruction.getPackage().getName();
        try {
            valueMax = new VariableNumber(packName, values[0]);
            rangeOfRandom = new VariableNumber(packName, values[1]);
        } catch (InstructionParseException e) {
            throw new InstructionParseException("Cannot parse randomness values", e);
        }
    }

    @Override
    protected Boolean execute(final String playerID) throws QuestRuntimeException {
        final Random generator = new Random();
        final int temp = generator.nextInt(rangeOfRandom.getInt(playerID)) + 1;
        return temp <= valueMax.getInt(playerID);
    }

}
