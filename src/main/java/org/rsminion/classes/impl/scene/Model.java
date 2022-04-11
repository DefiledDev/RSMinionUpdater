package org.rsminion.classes.impl.scene;

import org.objectweb.asm.tree.ClassNode;
import org.rsminion.classes.RSClass;
import org.rsminion.core.gamepack.GamePack;
import org.rsminion.core.matching.Matchers;
import org.rsminion.core.matching.data.RSHook;
import org.rsminion.tools.searchers.Searcher;
import org.rsminion.tools.utils.SearchUtils;
import org.rsminion.tools.utils.Utils;

import java.lang.reflect.Modifier;

@SuppressWarnings("unchecked")
public class Model extends RSClass {

    public Model() {
        super("Model", Matchers.Importance.HIGH);
    }

    @Override
    protected RSHook[] initRequiredHooks() {
        return new RSHook[] {
                high("uidCount", "I", true),
                high("onCursorUIDs", "[J", true),

                high("indicesLength", "I", false),
                high("verticesLength", "I", false),
                high("verticesX", "[I", false),
                high("verticesY", "[I", false),
                high("verticesZ", "[I", false),
                high("vectorSkin", "[[I", false),
                high("indicesX", "[I", false),
                high("indicesY", "[I", false),
                high("indicesZ", "[I", false),
                high("diameter", "I", false),
                high("radius", "I", false),
                high("singleTile", "Z", false),
                high("boundsType", "I", false),
                high("centerX", "I", false),
                high("centerY", "I", false),
                high("centerZ", "I", false),
                high("vertexGroups", "[[I", false)
                //The rest is un-necessary for now
        };
    }

    @Override
    protected boolean locateClass() {
        for(ClassNode clazz : GamePack.getClasses().values()) {
            if(SearchUtils.isParent(clazz, "Renderable") &&
            SearchUtils.countObjectFields(clazz) >= 20 &&
            Utils.checkIntArrayMatch(Searcher.countFieldNodes(clazz,
                    i -> !Modifier.isStatic(i.access) && i.desc.equals("I"),
                    ia -> !Modifier.isStatic(ia.access) && ia.desc.equals("[I")),
                    14, 12))
                registerClass(clazz);
        }
        return isFound();
    }

    @Override
    protected void locateHooks() {
        //TODO
    }

    @Override
    protected String[] initRequiredClasses() {
        return new String[] { "Renderable" };
    }

}
