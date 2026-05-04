package de.ostfalia.serp.consultant.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import de.ostfalia.serp.consultant.dto.ConsultantProjectDTO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ConsultantDTO
 */
@lombok.Builder @lombok.NoArgsConstructor @lombok.AllArgsConstructor

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-04-17T09:47:14.101996026+02:00[Europe/Berlin]")
public class ConsultantDTO {

  private Long id;

  private String name;

  @Valid
  private List<@Valid ConsultantProjectDTO> bookedProjects;

  public ConsultantDTO id(Long id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
  */
  
  @Schema(name = "id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ConsultantDTO name(String name) {
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

  public ConsultantDTO bookedProjects(List<@Valid ConsultantProjectDTO> bookedProjects) {
    this.bookedProjects = bookedProjects;
    return this;
  }

  public ConsultantDTO addBookedProjectsItem(ConsultantProjectDTO bookedProjectsItem) {
    if (this.bookedProjects == null) {
      this.bookedProjects = new ArrayList<>();
    }
    this.bookedProjects.add(bookedProjectsItem);
    return this;
  }

  /**
   * Get bookedProjects
   * @return bookedProjects
  */
  @Valid 
  @Schema(name = "bookedProjects", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bookedProjects")
  public List<@Valid ConsultantProjectDTO> getBookedProjects() {
    return bookedProjects;
  }

  public void setBookedProjects(List<@Valid ConsultantProjectDTO> bookedProjects) {
    this.bookedProjects = bookedProjects;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConsultantDTO consultantDTO = (ConsultantDTO) o;
    return Objects.equals(this.id, consultantDTO.id) &&
        Objects.equals(this.name, consultantDTO.name) &&
        Objects.equals(this.bookedProjects, consultantDTO.bookedProjects);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, bookedProjects);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConsultantDTO {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    bookedProjects: ").append(toIndentedString(bookedProjects)).append("\n");
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

