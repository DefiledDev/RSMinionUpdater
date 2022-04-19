package org.rsminion.classes.impl.scene.objects;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
import org.rsminion.classes.RSClass;
import org.rsminion.core.gamepack.GamePack;
import org.rsminion.core.matching.Matchers;
import org.rsminion.core.matching.data.RSHook;
import org.rsminion.tools.searchers.ClassSearcher;
import org.rsminion.tools.searchers.MethodSearcher;
import org.rsminion.tools.searchers.Searcher;
import org.rsminion.tools.searchers.data.Pattern;
import org.rsminion.tools.utils.SearchUtils;
import org.rsminion.tools.utils.Utils;

import java.lang.reflect.Modifier;

@SuppressWarnings("unchecked")
public class GameObject extends RSClass {

    private MethodSearcher methodSearcher;

    public GameObject() {
        super("GameObject", Matchers.Importance.HIGH);
    }

    @Override
    protected RSHook[] initRequiredHooks() {
        return new RSHook[] {
                high("id", "J", false),
                high("flags", "I", false),
                high("plane", "I", false),
                high("x", "I", false),
                high("y", "I", false),
                high("height", "I", false),
                high("renderable", "#Renderable", false),
                high("orientation", "I", false),
                high("relativeX", "I", false),
                high("relativeY", "I", false),
                high("offsetX", "I", false),
                high("offsetY", "I", false),
                //Do these later (After launch), found in draw(LTile;Z) in Region
                //high("drawPriority", "I", false),
                //high("cycle", "I", false)
        };
    }

    @Override
    protected boolean locateClass() {
        for(ClassNode clazz : GamePack.getClasses().values()) {
            if(SearchUtils.isPublicFinal(clazz.access) &&
            SearchUtils.isStandaloneObject(clazz) &&
            clazz.interfaces.size() <= 0 &&
            Utils.isBetween(SearchUtils.countObjectFields(clazz),12, 20) &&
            SearchUtils.countObjectMethods(clazz) <= 1 &&
            Utils.checkIntArrayMatch(Searcher.countFieldNodes(clazz,
                    r -> !Modifier.isStatic(r.access) && r.desc.equals(Utils.
                            formatAsClass(Matchers.
                                    getObfClass("Renderable"))),
                    i -> !Modifier.isStatic(i.access) && i.desc.equals("I")),1,12))
                registerClass(clazz);
        }
        return isFound();
    }

    @Override
    protected void locateHooks() {
        ClassSearcher classSearcher = new ClassSearcher(clazz);
        methodSearcher = new MethodSearcher();

        /* id ( J ) */
        FieldNode id = classSearcher.findField(f -> !Modifier.isStatic(f.access) &&
                f.desc.equals("J"));
        if(id != null)
            insert("id", clazz.name, id.name, id.desc);

        /* renderable ( #Renderable ) */
        FieldNode renderable = classSearcher.findField(f -> !Modifier.isStatic(f.access) &&
                f.desc.equals(Utils.formatAsClass(Matchers.getObfClass("Renderable"))));
        if(renderable != null)
            insert("renderable", clazz.name, renderable.name, renderable.desc);

        MethodNode addEntityMarker = Searcher.deepFindMethod(f -> !Modifier.isStatic(f.access) &&
                f.desc.contains("(IIIIIIIIL" + Matchers.getObfClass("Renderable") + ";IZ"));
        if(addEntityMarker != null) {
            methodSearcher.setMethod(addEntityMarker);

            if(isHookFound("id")) {

                assert id != null;
                Pattern current = methodSearcher.searchForKnown(clazz.name, id.name);
                //hg.c L20 -> L21
                //hg.j L21 -> L24
                //hg.v, hg.h L24 -> L25
                //hg.g L25 -> L28
                //hg.o, hg.l, hg.n L28 -> L29
                //hg.d L29 -> L22
                //hg.u L22 -> L23
                //hg.f L23 -> L26
                //hg.r L26 -> L27
                if(current.isFound())
                    current = putJump(Opcodes.PUTFIELD, "I",
                            current.getFirstLine(), 0);

                /* flags ( I ) */
                if(current.isFound()) {
                    insert("flags", current.getFirstFieldNode());

                    current = putJump(Opcodes.PUTFIELD, "I",
                            current.getFirstLine(), 0);
                }

                /* plane ( I ) */
                if(current.isFound()) {
                    insert("plane", current.getFirstFieldNode());

                    current = methodSearcher.singularSearch(f -> {
                        FieldInsnNode fin = (FieldInsnNode) f;
                        return fin.owner.equals(clazz.name) && fin.desc.equals("I");
                    }, current.getFirstLine() + 1,current.getFirstLine() + 20,
                            0, Opcodes.PUTFIELD);
                }

                /* x ( I ) */
                if(current.isFound()) {
                    insert("x", current.getFirstFieldNode());

                    current = putJump(Opcodes.PUTFIELD, "I",
                            current.getFirstLine(), 0);
                }

                /* y ( I ) */
                if(current.isFound()) {
                    insert("y", current.getFirstFieldNode());

                    current = putJump(Opcodes.PUTFIELD, "I",
                            current.getFirstLine(), 0);
                }

                /* height ( I ) */
                if(current.isFound()) {
                    insert("height", current.getFirstFieldNode());

                    current = methodSearcher.singularSearch(f -> {
                                FieldInsnNode fin = (FieldInsnNode) f;
                                return fin.owner.equals(clazz.name) && fin.desc.equals("I");
                            }, current.getFirstLine() + 1,current.getFirstLine() + 20,
                            0, Opcodes.PUTFIELD);
                }

                /* orientation ( I ) */
                if(current.isFound()) {
                    insert("orientation", current.getFirstFieldNode());

                    current = putJump(Opcodes.PUTFIELD, "I",
                            current.getFirstLine(), 0);
                }

                /* relativeX ( I ) */
                if(current.isFound()) {
                    insert("relativeX", current.getFirstFieldNode());

                    current = putJump(Opcodes.PUTFIELD, "I",
                            current.getFirstLine(), 0);
                }

                /* relativeY ( I ) */
                if(current.isFound()) {
                    insert("relativeY", current.getFirstFieldNode());

                    current = putJump(Opcodes.PUTFIELD, "I",
                            current.getFirstLine(), 0);
                }

                /* offsetX ( I ) */
                if(current.isFound()) {
                    insert("offsetX", current.getFirstFieldNode());

                    current = putJump(Opcodes.PUTFIELD, "I",
                            current.getFirstLine(), 0);
                }

                /* offsetY ( I ) */
                if(current.isFound())
                    insert("offsetY", current.getFirstFieldNode());
            }

        }
    }

    private Pattern putJump(int opcode, String desc, int startLine, int instance) {
        Pattern jump = methodSearcher.singularSearch(startLine, startLine + 50,
                0, Opcodes.GOTO);
        if(jump.isFound()) {
            return methodSearcher.jumpSearch(p -> {
                        FieldInsnNode fin = p.getFirstFieldNode();
                        return fin.owner.equals(clazz.name) && fin.desc.equals(desc);
                    }, Opcodes.GOTO, jump.getFirstLine(),
                    100, instance, opcode);
        }
        return Pattern.EMPTY_PATTERN;
    }

    @Override
    protected String[] initRequiredClasses() {
        return new String[] { "Renderable" };
    }
}
