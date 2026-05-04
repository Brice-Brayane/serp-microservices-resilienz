package de.ostfalia.serp.entry.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * EntryConsultantDTO
 */
@lombok.Builder @lombok.NoArgsConstructor @lombok.AllArgsConstructor

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-04-17T13:44:58.413945039+02:00[Europe/Berlin]")
public class EntryConsultantDTO {

  private Long consultantId;

  private String name;

  public EntryConsultantDTO consultantId(Long consultantId) {
    this.consultantId = consultantId;
    return this;
  }

  /**
   * Get consultantId
   * @return consultantId
  */
  
  @Schema(name = "consultantId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("consultantId")
  public Long getConsultantId() {
    return consultantId;
  }

  public void setConsultantId(Long consultantId) {
    this.consultantId = consultantId;
  }

  public EntryConsultantDTO name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
  */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EntryConsultantDTO entryConsultantDTO = (EntryConsultantDTO) o;
    return Objects.equals(this.consultantId, entryConsultantDTO.consultantId) &&
        Objects.equals(this.name, entryConsultantDTO.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(consultantId, name);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EntryConsultantDTO {\n");
    sb.append("    consultantId: ").append(toIndentedString(consultantId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

