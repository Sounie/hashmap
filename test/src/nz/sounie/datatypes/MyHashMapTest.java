package nz.sounie.datatypes;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class MyHashMapTest {
    @Test
    void makeSomething() {
        MyHashMap<String, Integer> myHashMap = new MyHashMap<>(12);

        myHashMap.add("Hello", 123);

        assertEquals(123, myHashMap.get("Hello"));
        assertEquals(1, myHashMap.size());
        assertEquals(12, myHashMap.distribution().length);
        assertEquals(1, Arrays.stream(myHashMap.distribution()).filter(i -> i == 1).count());
        assertEquals(11, Arrays.stream(myHashMap.distribution()).filter(i -> i == 0).count());
    }

    @Test
    void overwritingWithNewValue() {
        MyHashMap<String, Integer> myHashMap = new MyHashMap<>(12);

        myHashMap.add("Hello", 123);
        // Replacing value for existing key
        myHashMap.add("Hello", 456);

        assertEquals(456, myHashMap.get("Hello"));
        assertEquals(1, myHashMap.size());
        assertEquals(1, Arrays.stream(myHashMap.distribution()).filter(i -> i == 1).count());
        assertEquals(11, Arrays.stream(myHashMap.distribution()).filter(i -> i == 0).count());
    }

    @Test
    void multipleDistinctEntries() {
        MyHashMap<String, Integer> myHashMap = new MyHashMap<>(12);

        myHashMap.add("Hello", 123);
        myHashMap.add("Goodbye", 543);

        assertEquals(123, myHashMap.get("Hello"));
        assertEquals(543, myHashMap.get("Goodbye"));
        assertEquals(2, myHashMap.size());
        // `Here we don't expect to know the hash distribution of java.lang.String
    }

    @Test
    void recordAsKey() {
        MyHashMap<SomeRecord, String> myMapWithRecordsAsKey = new MyHashMap<>(16);

        myMapWithRecordsAsKey.add(new SomeRecord(10, "Green bottles"), "Hanging on the wall");

        assertEquals(1, myMapWithRecordsAsKey.size());
        assertEquals("Hanging on the wall", myMapWithRecordsAsKey.get(new SomeRecord(10, "Green bottles")));
    }

    @Test
    void recordAsValue() {
        MyHashMap<String, SomeRecord> myMapWithRecordsAsKey = new MyHashMap<>(16);

        myMapWithRecordsAsKey.add("Hanging on the wall", new SomeRecord(10, "Green bottles"));
        assertEquals(1, myMapWithRecordsAsKey.size());
        assertEquals(new SomeRecord(10, "Green bottles"), myMapWithRecordsAsKey.get("Hanging on the wall"));

        // Replacing value for existing key
        myMapWithRecordsAsKey.add("Hanging on the wall", new SomeRecord(9, "Green bottles"));
        assertEquals(new SomeRecord(9, "Green bottles"), myMapWithRecordsAsKey.get("Hanging on the wall"));
    }

    record SomeRecord (int count, String name) {}
}