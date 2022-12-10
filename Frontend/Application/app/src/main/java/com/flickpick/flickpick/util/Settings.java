package com.flickpick.flickpick.util;

public class Settings {
	public static final Setting<Integer> NUM_RECENT_SEARCHES = new Setting<>(8, "num_recent_searches");
	public static final Setting<Boolean> NETFLIX = new Setting<>(false, "netflix");
	public static final Setting<Boolean> HULU = new Setting<>(false, "hulu");
	public static final Setting<Boolean> PRIME = new Setting<>(false, "prime");
	public static final Setting<Boolean> DISNEY = new Setting<>(false, "disney");
	public static final Setting<Boolean> HBO = new Setting<>(false, "hbo");
	
	public static class Setting<T> {
		T value;
		final String key;
		
		protected Setting(T initial, String key) {
			value = initial;
			this.key = key;
		}
		
		public void set(T value) {
			this.value = value;
		}
		
		public T get() {
			return value;
		}
	}
}

