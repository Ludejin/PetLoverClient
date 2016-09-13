/*
 * The GPL License (GPL)
 *
 * Copyright (c) 2016 MarkZhai (http://zhaiyifan.cn)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.moduth.ext.utils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**
 * Multiple value (multiple values for one key) hash map.
 *
 * @author markzhai on 16/3/5
 */
public class MultiHashMap<K, V> implements Serializable {

    private static final long serialVersionUID = 2309170850883325510L;

    private final HashMap<K, HashMap<V, V>> backedMap = new HashMap<K, HashMap<V, V>>();

    /**
     * Returns the mapped value for key-value pair.
     *
     * @return The mapped value for key-value pair.
     */
    public V get(K key, V value) {
        HashMap<V, V> map = backedMap.get(key);
        return map == null ? null : map.get(value);
    }

    /**
     * Returns the value collection with corresponding key.
     *
     * @return the value collection with corresponding key.
     */
    public Collection<V> get(K key) {
        HashMap<V, V> map = backedMap.get(key);
        return map == null ? null : map.values();
    }

    /**
     * Put a key-value pair into this map.
     *
     * @return the value of any previous mapping with the specified key-value pair or
     * {@code null} if there was no such mapping.
     */
    public V put(K key, V value) {
        HashMap<V, V> map = backedMap.get(key);
        if (map == null) {
            map = new HashMap<>();
            backedMap.put(key, map);
        }
        return map.put(value, value);
    }

    /**
     * Put a key-value collection into this map.
     */
    public void put(K key, Collection<V> values) {
        if (values == null) {
            return;
        }
        HashMap<V, V> map = backedMap.get(key);
        if (map == null) {
            map = new HashMap<>();
            backedMap.put(key, map);
        }
        for (V value : values) {
            map.put(value, value);
        }
    }

    /**
     * Remove a key-value pair from this map.
     *
     * @return The removed mapped value.
     */
    public V remove(K key, V value) {
        HashMap<V, V> map = backedMap.get(key);
        return map == null ? null : map.remove(value);
    }

    /**
     * Remove collection with corresponding key.
     *
     * @return removed collection with corresponding key.
     */
    public Collection<V> remove(K key) {
        HashMap<V, V> map = backedMap.remove(key);
        return map == null ? null : map.values();
    }

    /**
     * Check whether corresponding key-value pair is contained in this map.
     *
     * @return Whether corresponding key-value pair is contained in this map.
     */
    public boolean contains(K key, V value) {
        HashMap<V, V> map = backedMap.get(key);
        return map != null && map.containsKey(value);
    }

    /**
     * Check whether corresponding key is contained in this map.
     *
     * @return Whether corresponding key is contained in this map.
     */
    public boolean contains(K key) {
        return backedMap.containsKey(key);
    }

    /**
     * Clear this map.
     */
    public void clear() {
        backedMap.clear();
    }

    /**
     * Returns whether this map is empty.
     *
     * @return Whether this map is empty.
     */
    public boolean isEmpty() {
        return backedMap.isEmpty();
    }

    /**
     * Returns the value number of corresponding key.
     *
     * @return The value number of corresponding key.
     */
    public int sizeOf(K key) {
        HashMap<V, V> map = backedMap.get(key);
        return map == null ? 0 : map.size();
    }

    /**
     * Returns the total size of this map.
     *
     * @return The total size of this map.
     */
    public int size() {
        int size = 0;
        for (HashMap<V, V> map : backedMap.values()) {
            size += map.size();
        }
        return size;
    }

    /**
     * Returns a set of the keys contained in this map. The set is backed by
     * this map so changes to one are reflected by the other. The set does not
     * support adding.
     *
     * @return a set of the keys.
     */
    public Set<K> keySet() {
        return backedMap.keySet();
    }
}
