package de.ostfalia.serp.project.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import de.ostfalia.serp.project.dto.ProjectConsultantDTO;
import de.ostfalia.serp.project.dto.ProjectCustomerDTO;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ProjectDTO
 */
@lombok.Builder @lombok.NoArgsConstructor @lombok.AllArgsConstructor

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-04-22T13:15:13.010093795+02:00[Europe/Berlin]")
public class ProjectDTO {

  private Long id;

  private String name;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime start;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime end;

  private String status;

  private ProjectCustomerDTO customer;

  @Valid
  private List<@Valid ProjectConsultantDTO> projectStaff;

  public ProjectDTO id(Long id) {
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

  public ProjectDTO name(String name) {
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

  public ProjectDTO start(OffsetDateTime start) {
    this.start = start;
    return this;
  }

  /**
   * Get start
   * @return start
  */
  @Valid 
  @Schema(name = "start", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("start")
  public OffsetDateTime getStart() {
    return start;
  }

  public void setStart(OffsetDateTime start) {
    this.start = start;
  }

  public ProjectDTO end(OffsetDateTime end) {
    this.end = end;
    return this;
  }

  /**
   * Get end
   * @return end
  */
  @Valid 
  @Schema(name = "end", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("end")
  public OffsetDateTime getEnd() {
    return end;
  }

  public void setEnd(OffsetDateTime end) {
    this.end = end;
  }

  public ProjectDTO status(String status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
  */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public ProjectDTO customer(ProjectCustomerDTO customer) {
    this.customer = customer;
    return this;
  }

  /**
   * Get customer
   * @return customer
  */
  @Valid 
  @Schema(name = "customer", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("customer")
  public ProjectCustomerDTO getCustomer() {
    return customer;
  }

  public void setCustomer(ProjectCustomerDTO customer) {
    this.customer = customer;
  }

  public ProjectDTO projectStaff(List<@Valid ProjectConsultantDTO> projectStaff) {
    this.projectStaff = projectStaff;
    return this;
  }

  public ProjectDTO addProjectStaffItem(ProjectConsultantDTO projectStaffItem) {
    if (this.projectStaff == null) {
      this.projectStaff = new ArrayList<>();
    }
    this.projectStaff.add(projectStaffItem);
    return this;
  }

  /**
   * Get projectStaff
   * @return projectStaff
  */
  @Valid 
  @Schema(name = "projectStaff", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("projectStaff")
  public List<@Valid ProjectConsultantDTO> getProjectStaff() {
    return projectStaff;
  }

  public void setProjectStaff(List<@Valid ProjectConsultantDTO> projectStaff) {
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
    ProjectDTO projectDTO = (ProjectDTO) o;
    return Objects.equals(this.id, projectDTO.id) &&
        Objects.equals(this.name, projectDTO.name) &&
        Objects.equals(this.start, projectDTO.start) &&
        Objects.equals(this.end, projectDTO.end) &&
        Objects.equals(this.status, projectDTO.status) &&
        Objects.equals(this.customer, projectDTO.customer) &&
        Objects.equals(this.projectStaff, projectDTO.projectStaff);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, start, end, status, customer, projectStaff);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProjectDTO {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    start: ").append(toIndentedString(start)).append("\n");
    sb.append("    end: ").append(toIndentedString(end)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    customer: ").append(toIndentedString(customer)).append("\n");
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

