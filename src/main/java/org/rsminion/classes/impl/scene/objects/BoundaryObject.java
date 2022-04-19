package org.rsminion.classes.impl.scene.objects;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.rsminion.classes.RSClass;
import org.rsminion.core.gamepack.GamePack;
import org.rsminion.core.matching.Matchers;
import org.rsminion.core.matching.data.RSHook;
import org.rsminion.tools.searchers.ClassSearcher;
import org.rsminion.tools.searchers.MethodSearcher;
import org.rsminion.tools.searchers.Searcher;
import org.rsminion.tools.utils.SearchUtils;
import org.rsminion.tools.utils.Utils;

import java.lang.reflect.Modifier;

@SuppressWarnings("unchecked")
public class BoundaryObject extends RSClass {

    public BoundaryObject() {
        super("BoundaryObject", Matchers.Importance.HIGH);
    }

    @Override
    protected RSHook[] initRequiredHooks() {
        return new RSHook[] {
                high("id", "J", false),
                high("flags", "I",false),
                high("x", "I", false),
                high("y", "I", false),
                high("plane", "I", false),
                high("renderable", "#Renderable", false),
                high("renderable2", "#Renderable", false),
                high("orientation", "I", false),
                high("orientation2", "I", false)
        };
    }

    @Override
    protected boolean locateClass() {
        for(ClassNode clazz : GamePack.getClasses().values()) {
            if(SearchUtils.isPublicFinal(clazz.access) &&
            SearchUtils.isStandaloneObject(clazz) &&
                    Utils.checkIntArrayMatch(Searcher.countFieldNodes(clazz,
                            r -> !Modifier.isStatic(r.access) && r.desc.equals(Utils.formatAsClass(Matchers.
                                    getObfClass("Renderable"))),
                            i -> !Modifier.isStatic(i.access) && i.desc.equals("I")),2, 6))
                registerClass(clazz);
        }
        return isFound();
    }

    @Override
    protected void locateHooks() {
        ClassSearcher classSearcher = new ClassSearcher(clazz);
        MethodSearcher methodSearcher = new MethodSearcher();

        /* id ( J ) */
        FieldNode id = classSearcher.findField(f -> !Modifier.isStatic(f.access) &&
                f.desc.equals("J"));
        if(id != null)
            insert("id", clazz.name, id.name, id.desc);


    }

    @Override
    protected String[] initRequiredClasses() {
        return new String[] { "Renderable" };
    }

}
