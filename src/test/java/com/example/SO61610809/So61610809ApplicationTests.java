package com.example.SO61610809;

import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@SpringBootTest
class So61610809ApplicationTests {

	@Test
	void sendPackets() throws Exception {
		List<Path> files = getRecursiveSortedPacketFiles(this.getClass().getResource("/data").getPath());
		try (Socket socket = new Socket("localhost", 12345)) {
			try (OutputStream outputStream = socket.getOutputStream()) {
				for (int i = 0; i < 10; i++) {
					Path path = files.get(i);
					byte[] bytes = Files.readAllBytes(path);
					outputStream.write(bytes);
					outputStream.flush();
					Thread.sleep(200);
				}
			}
		}
		// Sleep to ensure application stays running after the connection is closed
		TimeUnit.MILLISECONDS.sleep(1);
	}

	public static List<Path> getRecursiveSortedPacketFiles(String rootDir) {
		try {
			Path packetsDirPath = Paths.get(rootDir);
			return Files.walk(packetsDirPath)
					.filter(Files::isRegularFile)
					.filter(path -> path.toFile().getAbsolutePath().endsWith(".packet"))
					.sorted()
					.collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

}
