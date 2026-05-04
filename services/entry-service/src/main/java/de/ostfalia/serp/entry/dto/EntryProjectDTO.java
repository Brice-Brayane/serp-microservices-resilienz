package de.ostfalia.serp.entry.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import de.ostfalia.serp.entry.dto.EntryConsultantDTO;
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
 * EntryProjectDTO
 */
@lombok.Builder @lombok.NoArgsConstructor @lombok.AllArgsConstructor

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-04-17T13:44:58.413945039+02:00[Europe/Berlin]")
public class EntryProjectDTO {

  private Long projectId;

  private String name;

  @Valid
  private List<@Valid EntryConsultantDTO> projectStaff;

  public EntryProjectDTO projectId(Long projectId) {
    this.projectId = projectId;
    return this;
  }

  /**
   * Get projectId
   * @return projectId
  */
  
  @Schema(name = "projectId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("projectId")
  public Long getProjectId() {
    return projectId;
  }

  public void setProjectId(Long projectId) {
    this.projectId = projectId;
  }

  public EntryProjectDTO name(String name) {
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

  public EntryProjectDTO projectStaff(List<@Valid EntryConsultantDTO> projectStaff) {
    this.projectStaff = projectStaff;
    return this;
  }

  public EntryProjectDTO addProjectStaffItem(EntryConsultantDTO projectStaffItem) {
    if (this.projectStaff == null) {
      this.projectStaff = new ArrayList<>();
    }
    this.projectStaff.add(projectStaffItem);
    return this;
  }

  /**
   * consultant IDs booked on this project
   * @return projectStaff
  */
  @Valid 
  @Schema(name = "projectStaff", description = "consultant IDs booked on this project", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("projectStaff")
  public List<@Valid EntryConsultantDTO> getProjectStaff() {
    return projectStaff;
  }

  public void setProjectStaff(List<@Valid EntryConsultantDTO> projectStaff) {
    this.projectStaff = projectStaff;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EntryProjectDTO entryProjectDTO = (EntryProjectDTO) o;
    return Objects.equals(this.projectId, entryProjectDTO.projectId) &&
        Objects.equals(this.name, entryProjectDTO.name) &&
        Objects.equals(this.projectStaff, entryProjectDTO.projectStaff);
  }

  @Override
  public int hashCode() {
    return Objects.hash(projectId, name, projectStaff);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EntryProjectDTO {\n");
    sb.append("    projectId: ").append(toIndentedString(projectId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    projectStaff: ").append(toIndentedString(projectStaff)).append("\n");
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

