package org.rsminion.classes.impl.io;

import org.rsminion.classes.RSClass;
import org.rsminion.core.matching.Matchers;
import org.rsminion.core.matching.data.RSHook;

/*
TODO: DO oldschool.task.Task, oldschool.task.TaskQueue before doing this.
 */
public class SocketStream extends RSClass {

    public SocketStream(String name, Matchers.Importance importance) {
        super(name, importance);
    }

    @Override
    protected RSHook[] initRequiredHooks() {
        return new RSHook[] {
                high("inputStream", "Ljava/io/InputStream;", false),
                high("outBuffer", "[B", false),
        };
    }

    @Override
    protected boolean locateClass() {
        return false;
    }

    @Override
    protected void locateHooks() {

    }

    @Override
    protected String[] initRequiredClasses() {
        return new String[] { "Task", "TaskQueue" };
    }

}
