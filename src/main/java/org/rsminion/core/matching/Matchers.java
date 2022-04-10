package org.rsminion.core.matching;

import lombok.Getter;
import org.rsminion.classes.RSClass;
import org.rsminion.classes.impl.collections.Node;
import org.rsminion.classes.impl.collections.*;
import org.rsminion.classes.impl.collections.Iterable;
import org.rsminion.classes.impl.io.*;
import org.rsminion.classes.impl.security.IsaacCipher;
import org.rsminion.core.matching.data.Result;
import org.rsminion.tools.utils.Condition;

import java.util.LinkedHashMap;
import java.util.Map;

public class Matchers {

    private static @Getter final Map<String, RSClass> classes = new LinkedHashMap<>();

    public static void execute() {

        execute(
                /* Collections */
                new Node(),
                new LinearHashTable(),
                new CacheNode(),
                new ObjectNode(),
                new IntegerNode(),
                new HashTable(),
                new Deque(),
                new Iterable(),
                new IterableHashTable(),
                new IterableQueue(),
                new Queue(),
                new QueueIterator(),
                new HashTableIterator(),
                new Cache(),

                /* IO */
                new AbstractByteBuffer(),
                new Buffer(),
                new FileOnDisk(),
                new BufferedFile(),
                new DirectByteBuffer(),
                new GameBuffer(),
                new HuffmanCodec(),
                //new SocketStream(),
                new CacheWorker(),
                new FileCache(),

                /* Security */
                new IsaacCipher());

        Result.create().print(true, true, true,
                true, true);
    }

    public static void execute(Condition condition, RSClass... matchers) {
        for(RSClass matcher : matchers)
            classes.put(matcher.getName(), condition.verify() ? matcher.find() : matcher);
    }

    private static void execute(RSClass... matchers) {
        for(RSClass matcher : matchers)
            execute(matcher);
    }

    private static void execute(RSClass matcher) {
        if(matcher.hasRequirements()) classes.put(matcher.getName(), matcher.find());
    }

    public static boolean isFound(String className) {
        RSClass clazz;
        return (clazz = classes.get(className)) != null && clazz.isFound();
    }

    public static RSClass getClass(String className) {
        return getClass(className, false);
    }

    public static RSClass getClass(String className, boolean obf) {
        if(!obf) return classes.get(className);
        for(RSClass clazz : classes.values()) {
            if(clazz.isFound() && clazz.getObfName().equals(className))
                return clazz;
        }
        return null;
    }

    public enum Importance {
        LOW, MEDIUM, HIGH
    }

}
