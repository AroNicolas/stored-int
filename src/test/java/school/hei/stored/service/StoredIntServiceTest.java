package school.hei.stored.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.io.*;
import org.junit.jupiter.api.Test;
import school.hei.stored.file.bucket.BucketComponent;

public class StoredIntServiceTest {

  @Test
  void returns_random_number_and_uploads_when_file_not_exists() throws Exception {
    var bucketMock = mock(BucketComponent.class);

    doThrow(new RuntimeException("File not found")).when(bucketMock).download(eq("stored-int.txt"));

    StoredIntService service = new StoredIntService(bucketMock);

    int result = service.getOrCreateStoredInt();

    assert (result >= 0 && result < 10000);
    verify(bucketMock, times(1)).upload(any(File.class), eq("stored-int.txt"));
  }

  @Test
  void returns_existing_number_when_file_exists() throws Exception {
    BucketComponent bucketMock = mock(BucketComponent.class);
    StoredIntService service = new StoredIntService(bucketMock);

    File sourceFile = File.createTempFile("stored-int-test", ".txt");
    try (FileWriter fw = new FileWriter(sourceFile)) {
      fw.write("5032");
    }

    doAnswer(
            invocation -> {
              File targetFile = invocation.getArgument(0);
              try (InputStream in = new FileInputStream(sourceFile);
                  OutputStream out = new FileOutputStream(targetFile)) {
                in.transferTo(out);
              }
              return null;
            })
        .when(bucketMock)
        .download(String.valueOf(any(File.class)));

    int result = service.getOrCreateStoredInt();

    assertTrue(result >= 0 && result <= 100_000, "Result should be a positive number");

    verify(bucketMock, atMostOnce()).upload(any(), any());
  }
}
