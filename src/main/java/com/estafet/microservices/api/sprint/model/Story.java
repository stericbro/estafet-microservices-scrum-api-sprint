package com.estafet.microservices.api.sprint.model;

import java.io.IOException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "STORY")
public class Story {

    @Id
    @Column(name = "STORY_ID")
    private Integer id;

    @Column(name = "STATUS", nullable = false)
    private String status;

    @Transient
    private Integer sprintId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "SPRINT_ID", nullable = false, referencedColumnName = "SPRINT_ID", foreignKey = @ForeignKey(name = "STORY_TO_SPRINT_FK"))
    private Sprint storySprint;

    public Story update(Story newStory) {
        status = newStory.getStatus() != null ? newStory.getStatus() : status;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public Sprint getStorySprint() {
        return storySprint;
    }

    public Integer getSprintId() {
        return sprintId;
    }

    void setStorySprint(Sprint storySprint) {
        this.storySprint = storySprint;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Story other = (Story) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public static Story fromJSON(String message) {
        try {
            return new ObjectMapper().readValue(message, Story.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
