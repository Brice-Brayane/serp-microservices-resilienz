package de.ostfalia.serp.entry.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import de.ostfalia.serp.entry.dto.EntryConsultantDTO;
import de.ostfalia.serp.entry.dto.EntryProjectDTO;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * time booking entry
 */
@lombok.Builder @lombok.NoArgsConstructor @lombok.AllArgsConstructor

@Schema(name = "EntryDTO", description = "time booking entry")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-04-17T13:44:58.413945039+02:00[Europe/Berlin]")
public class EntryDTO {

  private Long entryId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime date;

  private Integer hours;

  private EntryProjectDTO project;

  private EntryConsultantDTO consultant;

  public EntryDTO entryId(Long entryId) {
    this.entryId = entryId;
    return this;
  }

  /**
   * Get entryId
   * @return entryId
  */
  
  @Schema(name = "entryId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("entryId")
  public Long getEntryId() {
    return entryId;
  }

  public void setEntryId(Long entryId) {
    this.entryId = entryId;
  }

  public EntryDTO date(OffsetDateTime date) {
    this.date = date;
    return this;
  }

  /**
   * Get date
   * @return date
  */
  @Valid 
  @Schema(name = "date", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("date")
  public OffsetDateTime getDate() {
    return date;
  }

  public void setDate(OffsetDateTime date) {
    this.date = date;
  }

  public EntryDTO hours(Integer hours) {
    this.hours = hours;
    return this;
  }

  /**
   * Get hours
   * @return hours
  */
  
  @Schema(name = "hours", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hours")
  public Integer getHours() {
    return hours;
  }

  public void setHours(Integer hours) {
    this.hours = hours;
  }

  public EntryDTO project(EntryProjectDTO project) {
    this.project = project;
    return this;
  }

  /**
   * Get project
   * @return project
  */
  @Valid 
  @Schema(name = "project", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("project")
  public EntryProjectDTO getProject() {
    return project;
  }

  public void setProject(EntryProjectDTO project) {
    this.project = project;
  }

  public EntryDTO consultant(EntryConsultantDTO consultant) {
    this.consultant = consultant;
    return this;
  }

  /**
   * Get consultant
   * @return consultant
  */
  @Valid 
  @Schema(name = "consultant", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("consultant")
  public EntryConsultantDTO getConsultant() {
    return consultant;
  }

  public void setConsultant(EntryConsultantDTO consultant) {
    this.consultant = consultant;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EntryDTO entryDTO = (EntryDTO) o;
    return Objects.equals(this.entryId, entryDTO.entryId) &&
        Objects.equals(this.date, entryDTO.date) &&
        Objects.equals(this.hours, entryDTO.hours) &&
        Objects.equals(this.project, entryDTO.project) &&
        Objects.equals(this.consultant, entryDTO.consultant);
  }

  @Override
  public int hashCode() {
    return Objects.hash(entryId, date, hours, project, consultant);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EntryDTO {\n");
    sb.append("    entryId: ").append(toIndentedString(entryId)).append("\n");
    sb.append("    date: ").append(toIndentedString(date)).append("\n");
    sb.append("    hours: ").append(toIndentedString(hours)).append("\n");
    sb.append("    project: ").append(toIndentedString(project)).append("\n");
    sb.append("    consultant: ").append(toIndentedString(consultant)).append("\n");
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

