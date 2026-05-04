package de.ostfalia.serp.consultant.dto;

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
 * UserDTO
 */
@lombok.Builder @lombok.NoArgsConstructor @lombok.AllArgsConstructor

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-04-17T09:47:14.101996026+02:00[Europe/Berlin]")
public class UserDTO {

  private Long id;

  private String name;

  private Long consultantID;

  private String externalID;

  public UserDTO id(Long id) {
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

  public UserDTO name(String name) {
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

  public UserDTO consultantID(Long consultantID) {
    this.consultantID = consultantID;
    return this;
  }

  /**
   * Get consultantID
   * @return consultantID
  */
  
  @Schema(name = "consultantID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("consultantID")
  public Long getConsultantID() {
    return consultantID;
  }

  public void setConsultantID(Long consultantID) {
    this.consultantID = consultantID;
  }

  public UserDTO externalID(String externalID) {
    this.externalID = externalID;
    return this;
  }

  /**
   * Get externalID
   * @return externalID
  */
  
  @Schema(name = "externalID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("externalID")
  public String getExternalID() {
    return externalID;
  }

  public void setExternalID(String externalID) {
    this.externalID = externalID;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserDTO userDTO = (UserDTO) o;
    return Objects.equals(this.id, userDTO.id) &&
        Objects.equals(this.name, userDTO.name) &&
        Objects.equals(this.consultantID, userDTO.consultantID) &&
        Objects.equals(this.externalID, userDTO.externalID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, consultantID, externalID);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserDTO {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    consultantID: ").append(toIndentedString(consultantID)).append("\n");
    sb.append("    externalID: ").append(toIndentedString(externalID)).append("\n");
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

