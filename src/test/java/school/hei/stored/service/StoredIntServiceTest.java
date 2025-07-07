package school.hei.stored.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.io.*;
import java.nio.file.Files;
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

    File tempFile = Files.createTempFile("stored-int-test", ".txt").toFile();
    try (FileWriter fw = new FileWriter(tempFile)) {
      fw.write("4242");
    }

    doAnswer(
            invocation -> {
              File targetFile = invocation.getArgument(1);
              try (InputStream in = new FileInputStream(tempFile);
                  OutputStream out = new FileOutputStream(targetFile)) {
                in.transferTo(out);
              }
              return null;
            })
        .when(bucketMock)
        .download(eq("stored-int.txt"));

    int result = service.getOrCreateStoredInt();

    assertEquals(4242, result);
    verify(bucketMock, times(0)).upload(any(), any());
  }
}
