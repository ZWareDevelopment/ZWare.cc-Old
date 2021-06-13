package dev.zihasz.zware.utils.types;

public class Pair<K, V> {

	private K key;
	private V val;

	public Pair(K key, V val) {
		this.key = key;
		this.val = val;
	}

	public K getKey() {
		return key;
	}

	public V getVal() {
		return val;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public void setVal(V val) {
		this.val = val;
	}
}