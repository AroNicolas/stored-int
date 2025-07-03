package school.hei.stored.conf;

import org.springframework.test.context.DynamicPropertyRegistry;
import school.hei.stored.PojaGenerated;

@PojaGenerated
public class EmailConf {

  void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("aws.ses.source", () -> "dummy-ses-source");
  }
}
