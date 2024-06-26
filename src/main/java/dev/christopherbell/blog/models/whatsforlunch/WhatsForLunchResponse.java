package dev.christopherbell.blog.models.whatsforlunch;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class WhatsForLunchResponse {

  private List<Restaurant> restaurants;
}