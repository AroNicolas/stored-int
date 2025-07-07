package school.hei.stored.endpoint;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import school.hei.stored.service.StoredIntService;

@RestController
@AllArgsConstructor
public class StoredIntController {
  private final StoredIntService service;

  @GetMapping("/stored-int")
  public int getStoredInt() {
    return service.getOrCreateStoredInt();
  }
}
