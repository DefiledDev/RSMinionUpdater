package org.rsminion.tools.deobfuscators.deobfuscators;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.rsminion.core.gamepack.GamePack;
import org.rsminion.tools.searchers.MethodSearcher;
import org.rsminion.tools.searchers.data.Pattern;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Created by ZakariaP
//TODO: Write a custom one later on, this is ugly as hell******
public class ArithmeticOperations extends Deobfuscator {

    public int reorderOperations() {
        int Patterns[][] = new int[][]{
                //{Opcodes.ICONST_0, Opcodes.GETSTATIC, Opcodes.LDC},
                {Opcodes.BIPUSH, Opcodes.GETSTATIC, Opcodes.LDC},
                {Opcodes.BIPUSH, Opcodes.ALOAD, Opcodes.GETFIELD, Opcodes.LDC},
        };

        int Fixed = 0;
        for (ClassNode Class : GamePack.getClasses().values()) {
            List<MethodNode> methodList = Class.methods;
            for (MethodNode Method : methodList) {
                MethodSearcher Search = new MethodSearcher(Method);
                ArrayList<AbstractInsnNode> Instructions = new ArrayList(Arrays.asList(Method.instructions.toArray()));
                for (int[] Pattern : Patterns) {
                    int L = Search.linearSearch(0, Pattern).getFirstLine();
                    int Count = 0;
                    while (L != -1) {
                        int afterMul;
                        afterMul = Search.singularSearch(L, L + 5, 0, Opcodes.IMUL).getFirstLine();
                        if (afterMul == -1)
                            afterMul = Search.singularSearch(L, L + 5, 0, Opcodes.LMUL).getFirstLine();
                        if (afterMul == -1)
                            break;
                        Instructions.add(afterMul + 1, Instructions.get(L));
                        Instructions.remove(L);
                        ++Count;
                        ++Fixed;
                        L = Search.linearSearch(Count, Pattern).getFirstLine();
                    }
                }
                Method.instructions.clear();
                for (AbstractInsnNode n : Instructions)
                    Method.instructions.add(n);
            }
        }
        Patterns = new int[][]{
                {Opcodes.BIPUSH, Opcodes.ALOAD, Opcodes.GETFIELD, Opcodes.LDC}
        };
        for (ClassNode Class : GamePack.getClasses().values()) {
            List<MethodNode> methodList = Class.methods;
            for (MethodNode Method : methodList) {
                MethodSearcher Search = new MethodSearcher(Method);
                ArrayList<AbstractInsnNode> Instructions = new ArrayList(Arrays.asList(Method.instructions.toArray()));

                for (int[] Pattern : Patterns) {
                    int L = Search.linearSearch(0, Pattern).getFirstLine();
                    int Count = 0;
                    while (L != -1) {
                        AbstractInsnNode[] instructions = Method.instructions.toArray();
                        int after5 = 0;
                        for (int I = 0; I < Pattern.length; ++I) {
                            if (instructions[L + I] instanceof FieldInsnNode) {
                                after5 = L + I + 5;
                                break;
                            }
                        }
                        Instructions.add(after5, Instructions.get(L));
                        Instructions.remove(L);
                        ++Count;
                        ++Fixed;
                        L = Search.linearSearch(Count, Pattern).getFirstLine();
                    }
                }
                Method.instructions.clear();
                for (AbstractInsnNode n : Instructions) {
                    Method.instructions.add(n);
                }
            }
        }
        Patterns = new int[][]{
                {Opcodes.BIPUSH, Opcodes.ALOAD, Opcodes.GETFIELD, Opcodes.LDC}
        };
        for (ClassNode Class : GamePack.getClasses().values()) {
            List<MethodNode> methodList = Class.methods;
            for (MethodNode Method : methodList) {
                MethodSearcher Search = new MethodSearcher(Method);
                ArrayList<AbstractInsnNode> Instructions = new ArrayList(Arrays.asList(Method.instructions.toArray()));

                for (int[] Pattern : Patterns) {
                    int L = Search.linearSearch(0, Pattern).getFirstLine();
                    int Count = 0;
                    while (L != -1) {
                        AbstractInsnNode[] instructions = Method.instructions.toArray();
                        int after5 = 0;
                        out:
                        for (int I = 0; I < Pattern.length; ++I) {
                            int Op = instructions[L + I].getOpcode();
                            if (Op >= Opcodes.IF_ICMPEQ && Op <= Opcodes.IF_ACMPNE) {
                                after5 = L + I + 5;
                                break out;
                            }
                        }
                        Instructions.add(after5, Instructions.get(L));
                        Instructions.remove(L);
                        ++Count;
                        ++Fixed;
                        L = Search.linearSearch(Count, Pattern).getFirstLine();
                    }
                }
                Method.instructions.clear();
                for (AbstractInsnNode n : Instructions) {
                    Method.instructions.add(n);
                }
            }
        }
        Patterns = new int[][]{
                {Opcodes.BIPUSH, Opcodes.ALOAD, Opcodes.GETFIELD, Opcodes.GETFIELD, Opcodes.LDC},
                {Opcodes.BIPUSH, Opcodes.GETSTATIC, Opcodes.GETSTATIC, Opcodes.LDC}
        };
        for (ClassNode Class : GamePack.getClasses().values()) {
            List<MethodNode> methodList = Class.methods;
            for (MethodNode Method : methodList) {
                MethodSearcher Search = new MethodSearcher(Method);
                ArrayList<AbstractInsnNode> Instructions = new ArrayList(Arrays.asList(Method.instructions.toArray()));

                for (int[] Pattern : Patterns) {
                    int L = Search.linearSearch(0, Pattern).getFirstLine();
                    int Count = 0;
                    while (L != -1) {
                        AbstractInsnNode[] instructions = Method.instructions.toArray();
                        int after4 = 0;
                        for (int I = 0; I < Pattern.length; ++I) {
                            if (instructions[L + I] instanceof FieldInsnNode) {
                                after4 = L + I + 4;
                                break;
                            }
                        }

                        Instructions.add(after4, Instructions.get(L));
                        Instructions.remove(L);
                        ++Count;
                        ++Fixed;
                        L = Search.linearSearch(Count, Pattern).getFirstLine();
                    }
                }
                Method.instructions.clear();
                for (AbstractInsnNode n : Instructions) {
                    if(n != null) Method.instructions.add(n);
                }
            }
        }
        Patterns = new int[][]{
                {Opcodes.GETSTATIC, Opcodes.LDC, Opcodes.IMUL, Opcodes.ILOAD, Pattern.BRANCH_WILDCARD},
                {Opcodes.GETSTATIC, Opcodes.LDC, Opcodes.LMUL, Opcodes.ILOAD, Pattern.BRANCH_WILDCARD},
        };
        for (ClassNode Class : GamePack.getClasses().values()) {
            List<MethodNode> methodList = Class.methods;
            for (MethodNode Method : methodList) {
                MethodSearcher Search = new MethodSearcher(Method);
                ArrayList<AbstractInsnNode> Instructions = new ArrayList(Arrays.asList(Method.instructions.toArray()));
                for (int[] Pattern : Patterns) {
                    int L = Search.linearSearch(0, Pattern).getFirstLine();
                    int Count = 0;
                    while (L != -1) {
                        Instructions.add(L, Instructions.get(L + 3));
                        Instructions.remove(L + 4);
                        ++Count;
                        ++Fixed;
                        L = Search.linearSearch(Count, Pattern).getFirstLine();
                    }
                }
                Method.instructions.clear();
                for (AbstractInsnNode n : Instructions) {
                    Method.instructions.add(n);
                }
            }
        }
        Patterns = new int[][]{
                {Pattern.CONST_WILDCARD, Opcodes.ILOAD, Pattern.BRANCH_WILDCARD},
                {Pattern.CONST_WILDCARD, Opcodes.ALOAD, Pattern.BRANCH_WILDCARD},
        };
        for (ClassNode Class : GamePack.getClasses().values()) {
            List<MethodNode> methodList = Class.methods;
            for (MethodNode Method : methodList) {
                MethodSearcher Search = new MethodSearcher(Method);
                ArrayList<AbstractInsnNode> Instructions = new ArrayList(Arrays.asList(Method.instructions.toArray()));
                for (int[] Pattern : Patterns) {
                    int L = Search.linearSearch(0, Pattern).getFirstLine();
                    int Count = 0;
                    while (L != -1) {
                        Instructions.add(L, Instructions.get(L + 1));
                        Instructions.remove(L + 2);
                        ++Count;
                        ++Fixed;
                        L = Search.linearSearch(Count, Pattern).getFirstLine();
                    }
                }
                Method.instructions.clear();
                for (AbstractInsnNode n : Instructions) {
                    Method.instructions.add(n);
                }
            }
        }

        Patterns = new int[][]{
                {Opcodes.ACONST_NULL, Opcodes.ALOAD, Opcodes.GETFIELD},
        };
        for (ClassNode Class : GamePack.getClasses().values()) {
            List<MethodNode> methodList = Class.methods;
            for (MethodNode Method : methodList) {
                MethodSearcher Search = new MethodSearcher(Method);
                ArrayList<AbstractInsnNode> Instructions = new ArrayList(Arrays.asList(Method.instructions.toArray()));
                for (int[] Pattern : Patterns) {
                    int L = Search.linearSearch(0, Pattern).getFirstLine();
                    int Count = 0;
                    while (L != -1) {
                        Instructions.add(L + 3, Instructions.get(L));
                        Instructions.remove(L);
                        ++Count;
                        ++Fixed;
                        L = Search.linearSearch(Count, Pattern).getFirstLine();
                    }
                }
                Method.instructions.clear();
                for (AbstractInsnNode n : Instructions) {
                    Method.instructions.add(n);
                }
            }
        }

        Patterns = new int[][]{
                {Opcodes.LDC, Opcodes.ILOAD, Opcodes.IMUL, Opcodes.PUTSTATIC},
                {Opcodes.LDC, Opcodes.ILOAD, Opcodes.IMUL, Opcodes.PUTFIELD},
        };
        for (ClassNode Class : GamePack.getClasses().values()) {
            List<MethodNode> methodList = Class.methods;
            for (MethodNode Method : methodList) {
                MethodSearcher Search = new MethodSearcher(Method);
                ArrayList<AbstractInsnNode> Instructions = new ArrayList(Arrays.asList(Method.instructions.toArray()));
                for (int[] Pattern : Patterns) {
                    int L = Search.linearSearch(0, Pattern).getFirstLine();
                    int Count = 0;
                    while (L != -1) {
                        Instructions.add(L, Instructions.get(L + 1));
                        Instructions.remove(L + 2);
                        ++Count;
                        ++Fixed;
                        L = Search.linearSearch(Count, Pattern).getFirstLine();
                    }
                }
                Method.instructions.clear();
                for (AbstractInsnNode n : Instructions) {
                    Method.instructions.add(n);
                }
            }
        }

        Patterns = new int[][]{
                {Opcodes.LDC, Opcodes.GETSTATIC, Opcodes.GETFIELD, Opcodes.IMUL},
        };
        for (ClassNode Class : GamePack.getClasses().values()) {
            List<MethodNode> methodList = Class.methods;
            for (MethodNode Method : methodList) {
                MethodSearcher Search = new MethodSearcher(Method);
                ArrayList<AbstractInsnNode> Instructions = new ArrayList(Arrays.asList(Method.instructions.toArray()));
                for (int[] Pattern : Patterns) {
                    int L = Search.linearSearch(0, Pattern).getFirstLine();
                    int Count = 0;
                    while (L != -1) {
                        Instructions.add(L + 3, Instructions.get(L));
                        Instructions.remove(L);
                        ++Count;
                        ++Fixed;
                        L = Search.linearSearch(Count, Pattern).getFirstLine();
                    }
                }
                Method.instructions.clear();
                for (AbstractInsnNode n : Instructions) {
                    Method.instructions.add(n);
                }
            }
        }

        Patterns = new int[][]{
                {Opcodes.ALOAD, Opcodes.LDC, Opcodes.ALOAD, Opcodes.GETFIELD, Opcodes.GETFIELD, Opcodes.IMUL, Opcodes.PUTFIELD},
        };
        for (ClassNode Class : GamePack.getClasses().values()) {
            List<MethodNode> methodList = Class.methods;
            for (MethodNode Method : methodList) {
                MethodSearcher Search = new MethodSearcher(Method);
                ArrayList<AbstractInsnNode> Instructions = new ArrayList(Arrays.asList(Method.instructions.toArray()));
                for (int[] Pattern : Patterns) {
                    int L = Search.linearSearch(0, Pattern).getFirstLine();
                    int Count = 0;
                    while (L != -1) {
                        Instructions.add(L + 5, Instructions.get(L + 1));
                        Instructions.remove(L + 1);
                        ++Count;
                        ++Fixed;
                        L = Search.linearSearch(Count, Pattern).getFirstLine();
                    }
                }
                Method.instructions.clear();
                for (AbstractInsnNode n : Instructions) {
                    Method.instructions.add(n);
                }
            }
        }

        Patterns = new int[][]{
                {Pattern.CONST_WILDCARD, Opcodes.GETSTATIC, Pattern.BRANCH_WILDCARD},
        };
        for (ClassNode Class : GamePack.getClasses().values()) {
            List<MethodNode> methodList = Class.methods;
            for (MethodNode Method : methodList) {
                MethodSearcher Search = new MethodSearcher(Method);
                ArrayList<AbstractInsnNode> Instructions = new ArrayList(Arrays.asList(Method.instructions.toArray()));
                for (int[] Pattern : Patterns) {
                    int L = Search.linearSearch(0, Pattern).getFirstLine();
                    int Count = 0;
                    while (L != -1) {
                        Instructions.add(L, Instructions.get(L + 1));
                        Instructions.remove(L + 2);
                        ++Count;
                        ++Fixed;
                        L = Search.linearSearch(Count, Pattern).getFirstLine();
                    }
                }
                Method.instructions.clear();
                for (AbstractInsnNode n : Instructions) {
                    Method.instructions.add(n);
                }
            }
        }

        Patterns = new int[][]{
                {Pattern.CONST_WILDCARD, Opcodes.GETSTATIC, Opcodes.LDC, Opcodes.IMUL, Pattern.BRANCH_WILDCARD},
                {Pattern.CONST_WILDCARD, Opcodes.GETSTATIC, Opcodes.LDC, Opcodes.LMUL, Pattern.BRANCH_WILDCARD},
        };
        for (ClassNode Class : GamePack.getClasses().values()) {
            List<MethodNode> methodList = Class.methods;
            for (MethodNode Method : methodList) {
                MethodSearcher Search = new MethodSearcher(Method);
                ArrayList<AbstractInsnNode> Instructions = new ArrayList(Arrays.asList(Method.instructions.toArray()));
                for (int[] Pattern : Patterns) {
                    int L = Search.linearSearch(0, Pattern).getFirstLine();
                    int Count = 0;
                    while (L != -1) {
                        Instructions.add(L + 3, Instructions.get(L));
                        Instructions.remove(L);
                        ++Count;
                        ++Fixed;
                        L = Search.linearSearch(Count, Pattern).getFirstLine();
                    }
                }
                Method.instructions.clear();
                for (AbstractInsnNode n : Instructions) {
                    Method.instructions.add(n);
                }
            }
        }

        Patterns = new int[][]{
                {Opcodes.LDC, Opcodes.ALOAD, Opcodes.GETFIELD, Opcodes.LDC, Opcodes.IMUL, Pattern.BRANCH_WILDCARD},
                {Pattern.CONST_WILDCARD, Opcodes.ALOAD, Opcodes.GETFIELD, Opcodes.LDC, Opcodes.IMUL, Pattern.BRANCH_WILDCARD},

        };
        for (ClassNode Class : GamePack.getClasses().values()) {
            List<MethodNode> methodList = Class.methods;
            for (MethodNode Method : methodList) {
                MethodSearcher Search = new MethodSearcher(Method);
                ArrayList<AbstractInsnNode> Instructions = new ArrayList(Arrays.asList(Method.instructions.toArray()));
                for (int[] Pattern : Patterns) {
                    int L = Search.linearSearch(0, Pattern).getFirstLine();
                    int Count = 0;
                    while (L != -1) {
                        Instructions.add(L + 5, Instructions.get(L));
                        Instructions.remove(L);
                        ++Count;
                        ++Fixed;
                        L = Search.linearSearch(Count, Pattern).getFirstLine();
                    }
                }
                Method.instructions.clear();
                for (AbstractInsnNode n : Instructions) {
                    Method.instructions.add(n);
                }
            }
        }

        Patterns = new int[][]{
                {Opcodes.GETSTATIC, Opcodes.LDC, Pattern.CONST_WILDCARD, Opcodes.IMUL},
        };
        for (ClassNode Class : GamePack.getClasses().values()) {
            List<MethodNode> methodList = Class.methods;
            for (MethodNode Method : methodList) {
                MethodSearcher Search = new MethodSearcher(Method);
                ArrayList<AbstractInsnNode> Instructions = new ArrayList(Arrays.asList(Method.instructions.toArray()));
                for (int[] Pattern : Patterns) {
                    int L = Search.linearSearch(0, Pattern).getFirstLine();
                    int Count = 0;
                    while (L != -1) {
                        Instructions.add(L + 2, Instructions.get(L + 3));
                        Instructions.remove(L + 4);
                        ++Count;
                        ++Fixed;
                        L = Search.linearSearch(Count, Pattern).getFirstLine();
                    }
                }
                Method.instructions.clear();
                for (AbstractInsnNode n : Instructions) {
                    Method.instructions.add(n);
                }
            }
        }

        Patterns = new int[][]{
                {Pattern.CONST_WILDCARD, Opcodes.ALOAD, Opcodes.GETFIELD, Opcodes.ALOAD, Opcodes.GETFIELD, Opcodes.LDC},

        };
        for (ClassNode Class : GamePack.getClasses().values()) {
            List<MethodNode> methodList = Class.methods;
            for (MethodNode Method : methodList) {
                MethodSearcher Search = new MethodSearcher(Method);
                ArrayList<AbstractInsnNode> Instructions = new ArrayList(Arrays.asList(Method.instructions.toArray()));
                for (int[] Pattern : Patterns) {
                    int L = Search.linearSearch(0, Pattern).getFirstLine();
                    int Count = 0;
                    while (L != -1) {
                        Instructions.add(L + 10, Instructions.get(L));
                        Instructions.remove(L);
                        ++Count;
                        ++Fixed;
                        L = Search.linearSearch(Count, Pattern).getFirstLine();
                    }
                }
                Method.instructions.clear();
                for (AbstractInsnNode n : Instructions) {
                    Method.instructions.add(n);
                }
            }
        }

        Patterns = new int[][]{
                {Opcodes.ALOAD, Opcodes.GETFIELD, Opcodes.LDC, Opcodes.IMUL, Opcodes.ILOAD, Opcodes.IMUL},

        };
        for (ClassNode Class : GamePack.getClasses().values()) {
            List<MethodNode> methodList = Class.methods;
            for (MethodNode Method : methodList) {
                MethodSearcher Search = new MethodSearcher(Method);
                ArrayList<AbstractInsnNode> Instructions = new ArrayList(Arrays.asList(Method.instructions.toArray()));
                for (int[] Pattern : Patterns) {
                    int L = Search.linearSearch(0, Pattern).getFirstLine();
                    int Count = 0;
                    while (L != -1) {
                        Instructions.add(L, Instructions.get(L + 4));
                        Instructions.remove(L + 5);
                        ++Count;
                        ++Fixed;
                        L = Search.linearSearch(Count, Pattern).getFirstLine();
                    }
                }
                Method.instructions.clear();
                for (AbstractInsnNode n : Instructions)
                    Method.instructions.add(n);
            }
        }

        Patterns = new int[][]{
                {Opcodes.GETSTATIC, Opcodes.GETFIELD, Opcodes.LDC, Opcodes.IMUL, Opcodes.BIPUSH, Opcodes.ISHR, Opcodes.GETSTATIC, Opcodes.LDC, Opcodes.IMUL, Opcodes.IADD},
                {Opcodes.GETSTATIC, Opcodes.GETFIELD, Opcodes.LDC, Opcodes.IMUL, Opcodes.BIPUSH, Opcodes.ISHR, Opcodes.GETSTATIC, Opcodes.LDC, Opcodes.IMUL, Pattern.BRANCH_WILDCARD},
                {Opcodes.GETSTATIC, Opcodes.GETFIELD, Opcodes.LDC, Opcodes.IMUL, Opcodes.BIPUSH, Opcodes.ISHL, Opcodes.GETSTATIC, Opcodes.LDC, Opcodes.IMUL, Opcodes.IADD},
                {Opcodes.GETSTATIC, Opcodes.GETFIELD, Opcodes.LDC, Opcodes.IMUL, Opcodes.BIPUSH, Opcodes.ISHL, Opcodes.GETSTATIC, Opcodes.LDC, Opcodes.IMUL, Pattern.BRANCH_WILDCARD},

        };
        for (ClassNode Class : GamePack.getClasses().values()) {
            List<MethodNode> methodList = Class.methods;
            for (MethodNode Method : methodList) {
                MethodSearcher Search = new MethodSearcher(Method);
                ArrayList<AbstractInsnNode> Instructions = new ArrayList(Arrays.asList(Method.instructions.toArray()));
                for (int[] Pattern : Patterns) {
                    int L = Search.linearSearch(0, Pattern).getFirstLine();
                    int Count = 0;
                    while (L != -1) {
                        Instructions.add(L, Instructions.get(L + 6));
                        Instructions.remove(L + 7);
                        Instructions.add(L + 1, Instructions.get(L + 7));
                        Instructions.remove(L + 8);
                        Instructions.add(L + 2, Instructions.get(L + 8));
                        Instructions.remove(L + 9);
                        ++Count;
                        ++Fixed;
                        L = Search.linearSearch(Count, Pattern).getFirstLine();
                    }
                }
                Method.instructions.clear();
                for (AbstractInsnNode n : Instructions)
                    Method.instructions.add(n);
            }
        }

        Patterns = new int[][]{
                {Opcodes.GETSTATIC, Opcodes.ILOAD, Opcodes.IINC, Opcodes.ILOAD, Opcodes.ILOAD, Opcodes.BIPUSH, Opcodes.ISHL, Opcodes.ILOAD, Opcodes.BIPUSH, Opcodes.ISHL},

        };
        for (ClassNode Class : GamePack.getClasses().values()) {
            List<MethodNode> methodList = Class.methods;
            for (MethodNode Method : methodList) {
                MethodSearcher Search = new MethodSearcher(Method);
                ArrayList<AbstractInsnNode> Instructions = new ArrayList(Arrays.asList(Method.instructions.toArray()));
                for (int[] Pattern : Patterns) {
                    int L = Search.linearSearch(0, Pattern).getFirstLine();
                    int Count = 0;
                    while (L != -1) {
                        Instructions.add(L + 3, Instructions.get(L + 7));
                        Instructions.remove(L + 8);
                        Instructions.add(L + 4, Instructions.get(L + 8));
                        Instructions.remove(L + 9);
                        Instructions.add(L + 5, Instructions.get(L + 9));
                        Instructions.remove(L + 10);
                        ++Count;
                        ++Fixed;
                        L = Search.linearSearch(Count, Pattern).getFirstLine();
                    }
                }
                Method.instructions.clear();
                for (AbstractInsnNode n : Instructions)
                    Method.instructions.add(n);
            }
        }

        Patterns = new int[][]{
                {Opcodes.GETSTATIC, Opcodes.ILOAD, Opcodes.IINC, Opcodes.ILOAD, Opcodes.BIPUSH, Opcodes.ISHL, Opcodes.ILOAD, Opcodes.ILOAD, Opcodes.BIPUSH},

        };
        for (ClassNode Class : GamePack.getClasses().values()) {
            List<MethodNode> methodList = Class.methods;
            for (MethodNode Method : methodList) {
                MethodSearcher Search = new MethodSearcher(Method);
                ArrayList<AbstractInsnNode> Instructions = new ArrayList(Arrays.asList(Method.instructions.toArray()));
                for (int[] Pattern : Patterns) {
                    int L = Search.linearSearch(0, Pattern).getFirstLine();
                    int Count = 0;
                    while (L != -1) {
                        Instructions.add(L + 11, Instructions.get(L + 6));
                        Instructions.remove(L + 6);
                        ++Count;
                        ++Fixed;
                        L = Search.linearSearch(Count, Pattern).getFirstLine();
                    }
                }
                Method.instructions.clear();
                for (AbstractInsnNode n : Instructions)
                    Method.instructions.add(n);
            }
        }

        for (ClassNode Class : GamePack.getClasses().values()) {
            List<MethodNode> methodList = Class.methods;
            for (MethodNode Method : methodList) {
                MethodSearcher Search = new MethodSearcher(Method);
                ArrayList<AbstractInsnNode> Instructions = new ArrayList(Arrays.asList(Method.instructions.toArray()));
                int L = Search.linearSearch(0, new int[]{Opcodes.ALOAD, Opcodes.ACONST_NULL, Opcodes.IF_ACMPNE}).getFirstLine();
                while (L != -1) {
                    LabelNode label = ((JumpInsnNode) Instructions.get(L + 2)).label;
                    Instructions.remove(L + 1);
                    Instructions.remove(L + 1);
                    Instructions.add(L + 1, new JumpInsnNode(Opcodes.IFNONNULL, label));
                    Method.instructions.clear();
                    for (AbstractInsnNode n : Instructions)
                        Method.instructions.add(n);
                    L = Search.linearSearch(0, new int[]{Opcodes.ALOAD, Opcodes.ACONST_NULL, Opcodes.IF_ACMPNE}).getFirstLine();
                    ++Fixed;
                }
            }
        }

        Patterns = new int[][]{
                {Opcodes.ALOAD, Opcodes.GETFIELD, Opcodes.ALOAD, Opcodes.GETFIELD, Opcodes.LDC, Opcodes.IMUL,
                        Opcodes.ICONST_1, Opcodes.ISUB, Opcodes.IALOAD, Opcodes.SIPUSH, Opcodes.IMUL, Opcodes.ALOAD,
                        Opcodes.GETFIELD, Opcodes.LDC, Opcodes.IMUL, Opcodes.IADD, Opcodes.ISTORE
                },

        };
        boolean bool = false;
        for (ClassNode Class : GamePack.getClasses().values()) {
            List<MethodNode> methodList = Class.methods;
            for (MethodNode Method : methodList) {
                MethodSearcher Search = new MethodSearcher(Method);
                ArrayList<AbstractInsnNode> Instructions = new ArrayList(Arrays.asList(Method.instructions.toArray()));
                for (int[] Pattern : Patterns) {
                    int L = Search.linearSearch(0, Pattern).getFirstLine();
                    int Count = 0;
                    while (L != -1) {
                        bool = true;
                        Instructions.add(L, Instructions.get(L + 11));
                        Instructions.remove(L + 12);
                        Instructions.add(L + 1, Instructions.get(L + 12));
                        Instructions.remove(L + 13);
                        Instructions.add(L + 2, Instructions.get(L + 13));
                        Instructions.remove(L + 14);
                        Instructions.add(L + 3, Instructions.get(L + 14));
                        Instructions.remove(L + 15);
                        ++Count;
                        ++Fixed;
                        L = Search.linearSearch(Count, Pattern).getFirstLine();
                    }
                }
                Method.instructions.clear();
                for (AbstractInsnNode n : Instructions)
                    Method.instructions.add(n);
                if (bool) {
                    bool = false;
                }
            }
        }

        return Fixed;
    }



    @Override
    public int execute() {
        int totalReordered = 0;
        int reordered = -1;
        while (reordered != 0) {
            reordered = reorderOperations();
            totalReordered += reordered;
        }
        return totalReordered;
    }

    @Override
    public String getName() {
        return "Operations Reorder-er";
    }
}
