package ionium.benchmarking;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entries;
import com.badlogic.gdx.utils.ObjectMap.Entry;

import ionium.templates.Main;

public class TickBenchmark extends Benchmark {

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

	private void loadResources() {

	}

}
