package in.rohit.custom.hasmap;

import java.util.ArrayList;

public class CustomHashMap<K, V> {
	private static final int DEFAULT_SIZE = 16;
//	private final float loadFactor = 0.65f;
	private int capacity;
	private int entriesFilled;
	private ArrayList<Entry<K, V>> entries;

	public CustomHashMap() {
		this.capacity = DEFAULT_SIZE;
		entries = new ArrayList<Entry<K,V>>(this.capacity);
		initializeArray();
	}

	public CustomHashMap(int capacity) {
		int i = 2;
		while (i < capacity) {
			i *= 2;
		}
		this.capacity = i;
		entries = new ArrayList<Entry<K,V>>(this.capacity);
		initializeArray();
	}

	private void initializeArray() {
		for(int i = 0; i < capacity; i++) {
			entries.add(null);
		}
	}

	public void put(K key, V value) {
		int keyHashCode = key.hashCode();
		int bucketIndex = keyHashCode % this.capacity;

		Entry<K, V> entry = this.entries.get(bucketIndex);
		if (entry == null) {
			entry = new Entry<K, V>(key, value);
			this.entriesFilled++;
			this.entries.set(bucketIndex, entry);
			/*if (((float) entriesFilled / (float) capacity) > loadFactor) {
				increaseHashMapCapacity();
			}*/
		} else {
			if(entry.key.equals(key)) {
				entry.value = value;
				return;
			}
			while (entry.next != null) {
				if(entry.next.key.equals(key)) {
					entry.next.value = value;
					return;
				}
				entry = entry.next;
			}
			entry.next = new Entry<K, V>(key, value);
		}
	}

	public V get(K key) {
		int keyHashCode = key.hashCode();
		int bucketIndex = keyHashCode % this.capacity;

		Entry<K, V> entry = this.entries.get(bucketIndex);

		if (entry == null) {
			return null;
		}

		while (entry != null) {
			if (entry.key.equals(key)) {
				return entry.value;
			} else {
				entry = entry.next;
			}
		}

		return null;
	}

	public boolean remove(K key) {
		int keyHashCode = key.hashCode();
		int bucketIndex = keyHashCode % this.capacity;

		Entry<K, V> entry = this.entries.get(bucketIndex);

		if (entry == null) {
			return false;
		}

		if(entry.key.equals(key)) {
			this.entries.set(bucketIndex, null);
			return true;
		}

		while(entry.next != null) {
			if(entry.next.key.equals(key)) {
				entry.next = entry.next.next;
				return true;
			}
		}

		return false;
	}

	private void increaseHashMapCapacity() {
		capacity *= 2;
		// TODO copy data
	}

	private class Entry<K, V> {
		K key;
		V value;
		Entry<K, V> next;

		public Entry(K key, V value) {
			this.key = key;
			this.value = value;
			this.next = null;
		}

	}
}
