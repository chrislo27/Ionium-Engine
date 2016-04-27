package ionium.benchmarking;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entries;
import com.badlogic.gdx.utils.ObjectMap.Entry;

import ionium.templates.Main;

public class TickBenchmark {

	private static TickBenchmark instance;

	private TickBenchmark() {
	}

	public static TickBenchmark instance() {
		if (instance == null) {
			instance = new TickBenchmark();
			instance.loadResources();
		}
		return instance;
	}

	private boolean isStarted = false;
	private long startTime = 0;
	private long lastTime = 0;

	private ObjectMap<String, Long> benchmarkTimes = new ObjectMap<>();
	private ObjectMap<String, Long> actualTimes = new ObjectMap<>();

	private void loadResources() {

	}

	public void startBenchmarking() {
		if (!isStarted) {
			isStarted = true;
			startTime = System.nanoTime();
			benchmarkTimes.clear();
		}
	}

	public void stopBenchmarking() {
		if (isStarted) {
			isStarted = false;
			lastTime = System.nanoTime() - startTime;

			actualTimes.put("misc", 0L);

			long addedUpTime = 0;
			for (Entry<String, Long> entry : actualTimes.entries()) {
				addedUpTime += entry.value;
			}

			actualTimes.put("misc", lastTime - addedUpTime);
		}
	}

	public void start(String id) {
		if (isStarted) {
			benchmarkTimes.put(id, System.nanoTime());
			actualTimes.put(id, 0L);
		}
	}

	public void stop(String id) {
		if (isStarted && benchmarkTimes.get(id) != null) {
			actualTimes.put(id,
					actualTimes.get(id, 0L) + (System.nanoTime() - benchmarkTimes.get(id)));

			benchmarkTimes.remove(id);
		}
	}

	public long getTime(String id) {
		return actualTimes.get(id, 0L);
	}

	public long getTotalTime() {
		return lastTime;
	}

	public Entries<String, Long> getAllEntries() {
		return actualTimes.entries();
	}

}
