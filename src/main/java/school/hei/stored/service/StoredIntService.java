package school.hei.stored.service;

import java.io.*;
import java.nio.file.Files;
import java.util.Random;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import school.hei.stored.file.bucket.BucketComponent;

@Service
@AllArgsConstructor
public class StoredIntService {
  private final BucketComponent bucketComponent;
  private static final String BUCKET_KEY = "stored-int.txt";

  @SneakyThrows
  public int getOrCreateStoredInt() {
    File file = Files.createTempFile("stored-int", ".txt").toFile();

    try {
      bucketComponent.download(BUCKET_KEY);
      try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
        return Integer.parseInt(reader.readLine().trim());
      }
    } catch (Exception e) {
      int randomInt = new Random().nextInt(10000);
      try (FileWriter writer = new FileWriter(file)) {
        writer.write(String.valueOf(randomInt));
      }
      bucketComponent.upload(file, BUCKET_KEY);
      return randomInt;
    }
  }
}
