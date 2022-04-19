package org.rsminion.classes.impl.scene.item;

import jdk.internal.org.objectweb.asm.Opcodes;
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
public class ItemLayer extends RSClass {

    private String renderableDesc;
    private MethodSearcher methodSearcher;

    public ItemLayer() {
        super("ItemLayer", Matchers.Importance.HIGH);
    }

    @Override
    protected RSHook[] initRequiredHooks() {
        return new RSHook[] {
                high("top", "#Renderable", false),
                high("middle", "#Renderable", false),
                high("bottom", "#Renderable", false),
                high("x", "I", false),
                high("y", "I", false),
                high("hash", "J", false),//a.k.a tag
                high("flags", "I", false),//a.k.a tileHeight
                high("height", "I", false),
        };
    }

    @Override
    protected boolean locateClass() {
        renderableDesc = Utils.formatAsClass(
                Matchers.getClass("Renderable").getObfName());
        for(ClassNode clazz : GamePack.getClasses().values()) {
            if(SearchUtils.isPublicFinal(clazz.access) &&
                    SearchUtils.isStandaloneObject(clazz) &&
                    Searcher.countFieldNodes(clazz,
                            r -> !Modifier.isStatic(r.access) && r.desc.equals(renderableDesc))[0] == 3)
                registerClass(clazz);
        }
        return isFound();
    }

    @Override
    protected void locateHooks() {
        ClassSearcher classSearcher = new ClassSearcher(clazz);
        methodSearcher = new MethodSearcher();

        /* hash ( I ) */
        FieldNode hash = classSearcher.findField(f -> !Modifier.isStatic(f.access) &&
                f.desc.equals("J"));
        if(hash != null) {

            insert("hash", clazz.name, hash.name, hash.desc);

            /* addItemPile */
            MethodNode addItemPile = Searcher.deepFindMethod(m -> !Modifier.isStatic(m.access) &&
                    SearchUtils.isReturnType(m, "V") && SearchUtils.countParam(m, Utils.
                    formatAsClass(Matchers.getObfClass("Renderable"))) == 3 &&
                    SearchUtils.containsParams(m, "I"));

            if (addItemPile != null) {
                //TODO: add ability to add methods to other classes later on, not important
                methodSearcher.setMethod(addItemPile);

                Pattern current = methodSearcher.searchForKnown(clazz.name, hash.name);

                if(current.isFound()) {

                    Pattern flags = methodSearcher.singularSearch(f -> {
                        FieldInsnNode fin = (FieldInsnNode) f;
                        return fin.owner.equals(clazz.name) && fin.desc.equals("I");
                    }, current.getFirstLine() - 10, current.getFirstLine() - 1,
                            0, Opcodes.PUTFIELD);
                    /* flags ( I ) */
                    if(flags.isFound())
                        insert("flags", flags.getFirstFieldNode());

                    current = methodSearcher.singularSearch(f -> {
                                FieldInsnNode fin = (FieldInsnNode) f;
                                return fin.owner.equals(clazz.name) && fin.desc.equals(renderableDesc);
                            }, current.getFirstLine(), current.getFirstLine() + 20,
                            0, Opcodes.PUTFIELD);

                }

                /* middle ( #Renderable ) */
                if(current.isFound()) {
                    insert("middle", current.getFirstFieldNode());

                    current = putJump(renderableDesc, current.getFirstLine(), 0);
                }

                /* top ( #Renderable ) */
                if(current.isFound()) {
                    insert("top", current.getFirstFieldNode());

                    current = methodSearcher.singularSearch(f -> {
                        FieldInsnNode fin = (FieldInsnNode) f;
                        return fin.owner.equals(clazz.name) && fin.desc.equals(renderableDesc)
                                && !isHookFound(fin.name, true);
                    }, 0, Opcodes.PUTFIELD);
                }

                /* bottom ( #Renderable ) */
                if(current.isFound()) {
                    insert("bottom", current.getFirstFieldNode());

                    current = putJump("I", current.getFirstLine(), 0);
                }

                /* x ( I ) */
                if(current.isFound()) {
                    insert("x", current.getFirstFieldNode());

                    current = putJump("I", current.getFirstLine(), 0);
                }

                /* y ( I ) */
                if(current.isFound())
                    insert("y", current.getFirstFieldNode());
            }

            /* height ( I ) */
            FieldNode height = classSearcher.findField(f -> !Modifier.isStatic(f.access) &&
                    f.desc.equals("I") && !isHookFound(f.name, true));
            if(height != null)
                insert("height", clazz.name, height.name, height.desc);

        }

    }

    private Pattern putJump(String desc, int startLine, int instance) {
        Pattern jump = methodSearcher.singularSearch(startLine, startLine + 50,
                0, Opcodes.GOTO);
        if(jump.isFound()) {
            return methodSearcher.jumpSearch(p -> {
                        FieldInsnNode fin = p.getFirstFieldNode();
                        return fin.owner.equals(clazz.name) && fin.desc.equals(desc);
                    }, Opcodes.GOTO, jump.getFirstLine(),
                    100, instance, Opcodes.PUTFIELD);
        }
        return Pattern.EMPTY_PATTERN;
    }

    @Override
    protected String[] initRequiredClasses() {
        return new String[] { "Renderable" };
    }
}
