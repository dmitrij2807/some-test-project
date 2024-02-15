package dev.mesh.moneytransfer.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public class PageData<T> {

  @Schema(
      description = "контент страницы"
  )
  @JsonProperty("page")
  private List<T> page;
  @Schema(
      description = "размер запрошенной "
  )
  @JsonProperty("size")
  private Integer size;

  public static <T> PageData<T> of(List<T> page, Integer size) {
    return new PageData(page, size);
  }

  private PageData(final List<T> page, final Integer size) {
    this.page = page;
    this.size = size;
  }

}
