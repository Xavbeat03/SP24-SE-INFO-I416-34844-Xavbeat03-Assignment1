package server.file;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileHandlerTest {

	private static final String TEST_KEY = "testKey";
	private static final String TEST_VALUE = "testValue";

	private static final String LONG_TEST_KEY = "testKeytestKeytestKeytestKeytestKeytestKeytestKeytestKeytestValuetestValue";
	private static final String LONG_TEST_VALUE = "testValuetestValuetestValuetestValuetestValuetestValuetestValuetestValue";

	private Path tempFilePath;

	@BeforeEach
	void setUp() throws IOException {
		// Create a temporary file
		tempFilePath = Files.createTempFile("test", ".txt");

		// Update the FILE_PATH in FileHandler to point to the temporary file
		FileHandler.setFilePath(tempFilePath.toString());
	}

	@AfterEach
	void tearDown() throws IOException {
		// Delete the temporary file
		Files.delete(tempFilePath);
	}

	@Test
	void storeValueWritesToFileWhenKeyNotFound() throws IOException {
		FileHandler.storeValue(TEST_VALUE, TEST_KEY);

		String[] result = FileHandler.getValue(TEST_KEY);

		assertArrayEquals(new String[]{TEST_KEY, TEST_VALUE}, result);
	}

	@Test
	void storeValueUpdatesValueWhenKeyFound() throws IOException {
		FileHandler.storeValue(TEST_VALUE, TEST_KEY);
		FileHandler.storeValue("newValue", TEST_KEY);

		String[] result = FileHandler.getValue(TEST_KEY);

		assertArrayEquals(new String[]{TEST_KEY, "newValue"}, result);
	}

	@Test
	void getValueReturnsEmptyArrayWhenKeyNotFound() throws IOException {
		String[] result = FileHandler.getValue("nonExistentKey");

		assertArrayEquals(new String[]{}, result);
	}

	@Test
	void storeValueWritesMultipleValues() throws IOException {
		FileHandler.storeValue(TEST_VALUE, TEST_KEY);
		FileHandler.storeValue("anotherValue", "anotherKey");

		String[] result1 = FileHandler.getValue(TEST_KEY);
		String[] result2 = FileHandler.getValue("anotherKey");

		assertArrayEquals(new String[]{TEST_KEY, TEST_VALUE}, result1);
		assertArrayEquals(new String[]{"anotherKey", "anotherValue"}, result2);
	}

	@Test
	void storeValueHandlesConcurrentWrites() throws InterruptedException {
		int threadCount = 100;
		ExecutorService service = Executors.newFixedThreadPool(threadCount);
		CountDownLatch latch = new CountDownLatch(threadCount);
		FileHandler.setTestingMode(true);

		for (int i = 0; i < threadCount; i++) {
			int finalI = i;
			service.submit(() -> {
				FileHandler.storeValue("value" + finalI, "key" + finalI);
				latch.countDown();
			});
		}

		latch.await();  // Wait for all threads to finish

		for (int i = 0; i < threadCount; i++) {
			String[] result = FileHandler.getValue("key" + i);
			assertArrayEquals(new String[]{"key" + i, "value" + i}, result);
		}
	}

	@Test
	void storeValueThrowsExceptionForEmptyKeyOrValue() {
		assertThrows(IllegalArgumentException.class, () -> FileHandler.storeValue("", "key"));
		assertThrows(IllegalArgumentException.class, () -> FileHandler.storeValue("value", ""));
	}

	@Test
	void storeLongValueWritesToFileWhenLongKeyNotFound() throws IOException {
		FileHandler.storeValue(LONG_TEST_VALUE, LONG_TEST_KEY);

		String[] result = FileHandler.getValue(LONG_TEST_KEY);

		assertArrayEquals(new String[]{LONG_TEST_KEY, LONG_TEST_VALUE}, result);
	}

	@Test
	void storeLongValueUpdatesValueWhenLongKeyFound() throws IOException {
		FileHandler.storeValue(LONG_TEST_VALUE, LONG_TEST_KEY);
		FileHandler.storeValue("newValue", LONG_TEST_KEY);

		String[] result = FileHandler.getValue(LONG_TEST_KEY);

		assertArrayEquals(new String[]{LONG_TEST_KEY, "newValue"}, result);
	}




}
