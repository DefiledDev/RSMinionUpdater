package org.rsminion.tools.searchers;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.rsminion.tools.searchers.data.Pattern;
import org.objectweb.asm.tree.*;
import org.rsminion.tools.utils.Filter;
import org.rsminion.tools.utils.Function;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class MethodSearcher {

    private @Getter MethodNode method;

    private @Getter AbstractInsnNode[] instructions;

    public MethodSearcher(MethodNode method) {
        this.method = method;
        this.instructions = method.instructions.toArray();
    }

    /* --------------- Linear --------------- */

    public Pattern linearSearch(Filter<Pattern> patternCondition,
                                int startLine, int endLine, int patternInstance, int... pattern) {
        instructions = method.instructions.toArray();
        int instancesFound = 0;
        outerLoop:
        for(int i = startLine; i < endLine; i++) {
            int instSetIndex = i;
            int patIndex = 0;
            AbstractInsnNode instruction;

            while((instruction = instructions[instSetIndex]) != null &&
                    verifyPatternOpcode(instruction, pattern[patIndex])) {

                if(instSetIndex == (instructions.length - 1))
                    break outerLoop;

                if(patIndex == (pattern.length - 1)) {
                    Pattern result = Pattern.createLinear(method, i, pattern);
                    if(patternCondition == null || patternCondition.verify(result)) {

                        if (instancesFound == patternInstance)
                            return result;

                        instancesFound++;
                        break;
                    }
                }

                instSetIndex++;
                patIndex++;

                if(instructions[instSetIndex].getOpcode() == -1)
                    instSetIndex++;

            }
        }
        return Pattern.EMPTY_PATTERN;
    }

    public Pattern linearSearch(int patternInstance, int... pattern) {
        return linearSearch(null,0, method.instructions.size(), patternInstance, pattern);
    }

    public Pattern linearMultiSearch(Filter<Pattern>[] patternConditions, int startLine, int endLine,
                                     int[] patternsInstances, int[][] patterns) {
        for(int i = 0; i < patterns.length; i++) {
            Pattern result = linearSearch(patternConditions[i],startLine,endLine,
                    patternsInstances[i],patterns[i]);
            if(!result.isEmpty())
                return result;
        }
        return Pattern.EMPTY_PATTERN;
    }

    public Pattern linearMultiSearch(int startLine, int endLine,
                                     int[] patternsInstances, int[][] patterns) {
        return linearMultiSearch(null,  startLine, endLine, patternsInstances, patterns);
    }

    public Pattern linearMultiSearch(int[] patternsInstances, int[][] patterns) {
        return linearMultiSearch(0, method.instructions.size(), patternsInstances, patterns);
    }

    public Pattern linearMultiSearch(int patternsInstance, int[][] patterns) {
        for (int[] pattern : patterns) {
            Pattern result = linearSearch(patternsInstance, pattern);
            if (!result.isEmpty())
                return result;
        }
        return Pattern.EMPTY_PATTERN;
    }

    /* --------------- Jumps/Branches --------------- */

    public Pattern jumpSearch(Filter<Pattern> patternCondition, int jumpOpcode, int startLine,
                              int maxLinesToSearch, int patternInstance, int... pattern) {
        instructions = method.instructions.toArray();
        int instance = 0;
        for(int i = startLine, linesSearched = 0; linesSearched <= maxLinesToSearch; i++) {
            if(i >= instructions.length)
                break;

            linesSearched++;
            AbstractInsnNode ain;
            if((ain = instructions[i]).getOpcode() == jumpOpcode)
                i = method.instructions.indexOf(((JumpInsnNode)ain).label);

            Pattern result;
            if ((result = linearSearch(patternCondition,i,i + (pattern.length + 1),
                    0, pattern)).isFound()) {
                if (instance == patternInstance)
                    return result;
                instance++;
            }
        }
        return Pattern.EMPTY_PATTERN;
    }

    public Pattern jumpSearch(int jumpOpcode, int maxLinesToSearch, int patternInstance, int... patterns) {
        return jumpSearch(null, jumpOpcode, 0, maxLinesToSearch, patternInstance,
                patterns);
    }

    public Pattern singularJumpSearch(Filter<AbstractInsnNode> patternCondition, int jumpOpcode,
                                      int startLine, int maxLinesToSearch,
                                      int patternInstance, int pattern) {
        instructions = method.instructions.toArray();
        int instance = 0;
        for(int i = startLine, linesSearched = 0; linesSearched <= maxLinesToSearch; i++) {
            if(i >= instructions.length)
                break;

            linesSearched++;
            AbstractInsnNode ain;
            if((ain = instructions[i]).getOpcode() == jumpOpcode)
                i = method.instructions.indexOf(((JumpInsnNode)ain).label);

            if (ain.getOpcode() == pattern && (patternCondition == null || patternCondition.verify(ain))) {
                if (instance == patternInstance)
                    return Pattern.createSingular(method, ain.getOpcode(), i);
                instance++;
            }
        }
        return Pattern.EMPTY_PATTERN;
    }

    public Pattern singularJumpSearch(int jumpOpcode, int maxLinesToSearch, int patternInstance, int pattern) {
        return singularJumpSearch(null, jumpOpcode, 0, maxLinesToSearch, patternInstance, pattern);
    }

    /* --------------- Singular Linear --------------- */

    public Pattern singularSearch(int startLine, int endLine, int patternInstance, int opcode) {
        instructions = method.instructions.toArray();
        int instance = 0;
        for(int i = startLine; i < endLine; i++) {
            if(instructions[i].getOpcode() == opcode) {
                if(instance == patternInstance)
                    return Pattern.createSingular(method, opcode, i);
                instance++;
            }
        }
        return Pattern.EMPTY_PATTERN;
    }

    public Pattern singularSearch(int patternInstance, int opcode) {
        return singularSearch(0, instructions.length, patternInstance, opcode);
    }

    public Pattern singularSearch(Filter<AbstractInsnNode> condition, int startLine, int endLine,
                                  int patternInstance, int opcode) {
        instructions = method.instructions.toArray();
        int instance = 0;
        for(int i = startLine; i < endLine; i++) {
            AbstractInsnNode ain;
            if((ain = instructions[i]).getOpcode() == opcode && condition.verify(ain)) {
                if(instance == patternInstance)
                    return Pattern.createSingular(method, opcode, i);
                instance++;
            }
        }
        return Pattern.EMPTY_PATTERN;
    }

    public Pattern singularSearch(Filter<AbstractInsnNode> condition, int patternInstance, int opcode) {
        return singularSearch(condition, 0, instructions.length, patternInstance, opcode);
    }

    public Pattern singularLdcSearch(Number value, int startLine, int endLine, int patternInstance, int opcode) {
        instructions = method.instructions.toArray();
        int instance = 0;
        for(int i = startLine; i < endLine; i++) {
            AbstractInsnNode ain;
            if((ain = instructions[i]).getOpcode() == opcode && ((LdcInsnNode)ain).cst.equals(value)) {
                if(instance == patternInstance)
                    return Pattern.createSingular(method, opcode, i);
                instance++;
            }
        }
        return Pattern.EMPTY_PATTERN;
    }

    public Pattern singularLdcSearch(Number value, int patternInstance, int opcode) {
        return singularLdcSearch(value, 0, instructions.length, patternInstance, opcode);
    }

    public Pattern singularIntSearch(int value, int startLine, int endLine, int patternInstance, int opcode) {
        instructions = method.instructions.toArray();
        int instance = 0;
        for(int i = startLine; i < endLine; i++) {
            AbstractInsnNode ain;
            if((ain = instructions[i]).getOpcode() == opcode && ((IntInsnNode)ain).operand == value) {
                if(instance == patternInstance)
                    return Pattern.createSingular(method, opcode, i);
                instance++;
            }
        }
        return Pattern.EMPTY_PATTERN;
    }

    public Pattern singularIntSearch(int value, int patternInstance, int opcode) {
        return singularIntSearch(value, 0, method.instructions.size(), patternInstance, opcode);
    }

    public Pattern cycleInstances(Function<Pattern, Integer> searchFunction,
                                  Filter<Pattern> returnCondition,
                                  int maxLoops) {
        int instance = 0;
        int loops = 0;
        Pattern pattern;
        while((pattern = searchFunction.execute(instance)).isFound()) {
            if(loops > maxLoops) break;
            if(returnCondition.verify(pattern))
                return pattern;
            instance++;
            loops++;
        }
        return Pattern.EMPTY_PATTERN;
    }

    public Pattern[] cycleInstancesAll(Function<Pattern, Integer> searchFunction,
                                  Filter<Pattern> returnCondition,
                                  int maxLoops) {
        List<Pattern> patterns = new ArrayList<>();
        int instance = 0;
        int loops = 0;
        Pattern pattern;
        while((pattern = searchFunction.execute(instance)).isFound()) {
            if(loops > maxLoops) break;
            if(returnCondition.verify(pattern))
                patterns.add(pattern);
            instance++;
            loops++;
        }
        return patterns.toArray(new Pattern[0]);
    }

    public int[] getInstructionsFrequency(AbstractInsnNode... opcodes) {
        instructions = method.instructions.toArray();
        int[] counts = new int[opcodes.length];
        for(AbstractInsnNode ain : instructions) {
            for(int i = 0; i < opcodes.length; i++) {
                if(opcodes[i] instanceof FieldInsnNode) { //support for Fields only right now.
                    if(ain instanceof FieldInsnNode) {
                        FieldInsnNode fin = (FieldInsnNode) ain;
                        FieldInsnNode opcodes_fin = (FieldInsnNode) opcodes[i];
                        if(fin.owner.equals(opcodes_fin.owner) && fin.name.equals(opcodes_fin.name)
                        && fin.desc.equals(opcodes_fin.desc)) counts[i]++;
                    }
                } //TODO: add support for Ldc, etc... here
            }
        }
        return counts;
    }

    public int compareInstructionFrequency(AbstractInsnNode ain1, AbstractInsnNode ain2) {
        int[] result = getInstructionsFrequency(ain1, ain2);
        return result[0] - result[1];
    }

    /* --------------- Misc ----------------------- */

    public List<AbstractInsnNode> getInstructions(Filter<AbstractInsnNode> condition) {
        instructions = method.instructions.toArray();
        List<AbstractInsnNode> nodes = new ArrayList<>();
        for(AbstractInsnNode abstractInsnNode : instructions) {
            if(condition.verify(abstractInsnNode))
                nodes.add(abstractInsnNode);

        }
        return nodes;
    }

    /* --------------- Dependencies --------------- */

    private boolean verifyPatternOpcode(AbstractInsnNode instruction, int currPatternOpcode) {
        int instOpcode = instruction.getOpcode();
        return  //Verify Pattern Instruction Opcode OR is it a Wildcard?
                (instOpcode == currPatternOpcode || currPatternOpcode == Pattern.SKIP_WILDCARD) ||
                        //Check if it's a General Branch Wildcard
                        (currPatternOpcode == Pattern.BRANCH_WILDCARD && (instOpcode >= 159 && instOpcode <= 166)) ||
                        //Check if it's a Constant Wildcard
                        (currPatternOpcode == Pattern.CONST_WILDCARD && (instOpcode >= 1 && instOpcode <= 17)) ||
                        //Check if it's a IF-Branch Wildcard
                        (currPatternOpcode == Pattern.IF_WILDCARD && (instOpcode >= 153 && instOpcode <= 158)) ||
                        //Check if it's a MUL Wildcard
                        (currPatternOpcode == Pattern.MUL_WILDCARD && (instOpcode >= 104 && instOpcode <= 107)) ||
                        //Check if it's a GET Wildcard
                        (currPatternOpcode == Pattern.GET_WILDCARD && (instOpcode == 178 || instOpcode == 180)) ||
                        //Check if it's a PUT Wildcard
                        (currPatternOpcode == Pattern.PUT_WILDCARD && (instOpcode >= 179 && instOpcode <= 181));
    }
    
    public void setMethod(MethodNode method) {
        this.method = method;
        this.instructions = method.instructions.toArray();
    }

}
