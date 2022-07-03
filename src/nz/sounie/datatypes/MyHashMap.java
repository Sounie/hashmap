package nz.sounie.datatypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.Math.abs;

/**
 * Playing with implementing a simple hash map.  Not attempting to fully implement Java's Map interface (yet).
 *
 * In Java's HashMap the hash of the key is a vital implementation detail for being able to store and retrieve the key
 * value pair, but at the same time it is the specific way that the hash is used should be considered as a hidden
 * implementation detail.  Out of curiosity, here we could offer to expose some statistics about how the mappings are
 * working, to provide some insight into whether the hash is balancing evenly or not.
 *
 * Type parameters:
 * @param <K> the type of keys held in this map
 * @param <V> the type of values held in this map
 */
public class MyHashMap<K, V> {
    private static final int DEFAULT_KEY_RANGE_SIZE = 16;

    private final int hashKeyRange;

    private int size = 0;

    private final List<KeyValuePair<K, V>>[] dataStore;

    public MyHashMap() {
        this(DEFAULT_KEY_RANGE_SIZE);
    }
    public MyHashMap(int hashKeyRange) {
        this.hashKeyRange = hashKeyRange;

        dataStore = new List[this.hashKeyRange];
        for (int i = 0; i < this.hashKeyRange; i++) {
            dataStore[i] = new ArrayList<>();
        }
    }

    public void add(K key, V value) {
        int hashKeyIndex = determineHashKey(key);
        boolean replaced = false;
        // Check for existing entry with matching key
        for (int matchListIndex = 0; matchListIndex < dataStore[hashKeyIndex].size();  matchListIndex++)
        {
            if (dataStore[hashKeyIndex].get(matchListIndex).key.equals(key)) {
                dataStore[hashKeyIndex].set(matchListIndex, new KeyValuePair<>(key, value));
                replaced = true;
            }
        }
        if (!replaced) {
            dataStore[hashKeyIndex].add(new KeyValuePair<>(key, value));
            size++;
        }
    }

    /**
     * If a map entry exists with the corresponding key, then return the value, else null.
     *
     * @param key - the key by which as value may have been stored into this map.
     *
     * @return value if found, otherwise null
     */
    public V get(K key) {
       int hashKeyIndex = determineHashKey(key);
       Optional<KeyValuePair<K, V>> potentialMatch = dataStore[hashKeyIndex]
               .stream()
               .filter(kv -> kv.key.equals(key))
               .findFirst();

       return potentialMatch.map(kvKeyValuePair -> kvKeyValuePair.value).orElse(null);
    }

    public int size() {
        return size;
    }

    public int[] distribution() {
        int[] distribution = new int[dataStore.length];
        for (int i = 0; i < dataStore.length; i++) {
            distribution[i] = dataStore[i].size();
        }

        return distribution;
    }

    private int determineHashKey(K key) {
        int hash = key.hashCode();
        // Catering for edge case where hashCode might not be representable from absolute value.
        return hash == Integer.MIN_VALUE ? 0 : abs(hash) % hashKeyRange;
    }

    private static class KeyValuePair<K, V> {
        K key;
        V value;

        KeyValuePair(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
