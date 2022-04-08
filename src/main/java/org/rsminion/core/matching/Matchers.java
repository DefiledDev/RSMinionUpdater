package org.rsminion.core.matching;

import lombok.Getter;
import org.rsminion.classes.RSClass;
import org.rsminion.classes.impl.Node;
import org.rsminion.classes.impl.collections.*;
import org.rsminion.classes.impl.collections.Iterable;
import org.rsminion.core.matching.data.Result;
import org.rsminion.tools.utils.Condition;

import java.util.LinkedHashMap;
import java.util.Map;

//Note to self: Do what's important, don't do everything from the get go, you can do the rest later on...
public class Matchers {

    private static @Getter final Map<String, RSClass> classes = new LinkedHashMap<>();

    public static void execute() {
        execute(new Node());
        execute(new LinearHashTable());
        execute("Node",
                new CacheNode(),
                new ObjectNode(),
                new IntegerNode(),
                new HashTable(),
                new Deque(),
                new Iterable(),
                new IterableHashTable(),
                new IterableQueue());

        execute("CacheNode",
                new Queue());

        execute("IterableQueue",
                new QueueIterator());

        execute("IterableHashTable",
                new HashTableIterator());

        execute(new Cache()); //TODO: have execute()... return RSClass so i have put inside

        //Remaining Collections:
        //Buffer
        //DualNode
        //DualNodeDeque
        //EnumComposition
        //EvictingDualNodeHashTable
        //IntHashTable
        //IterableDualNodeQueue
        //IterableDualNodeQueueIterator
        //IterableNodeDeque
        //IterableNodeDequeDescendingIterator
        //IterableNodeHashTable
        //IterableNodeHashTableIterator
        //LinkDeque
        //Link
        //StructComposition

        Result.create().print(true, true, true,
                true, true);
    }

    private static void execute(String requiredClass, RSClass... matchers) {
        execute(() -> isFound(requiredClass), matchers);
    }

    private static void execute(String[] requiredClasses, RSClass... matchers) {
        execute(() -> {
            for(String className : requiredClasses) {
                if(!isFound(className))
                    return false;
            }
            return true;
        }, matchers);
    }

    private static void execute(Condition condition, RSClass... matchers) {
        for(RSClass matcher : matchers)
            classes.put(matcher.getName(), condition.verify() ? matcher.find() : matcher);
    }

    private static void execute(RSClass matcher) {
        classes.put(matcher.getName(), matcher.find());
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
