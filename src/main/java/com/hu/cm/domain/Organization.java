package com.hu.cm.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A Organization.
 */
@Entity
@Table(name = "ORGANIZATION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Organization implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "session_timeout")
    private Integer session_timeout;

    @Column(name = "smtp_enabled")
    private Boolean smtp_enabled;

    @Column(name = "smtp_server_address")
    private String smtp_server_address;

    @Column(name = "smtp_username")
    private String smtp_username;

    @Column(name = "smtp_password")
    private String smtp_password;

    @Column(name = "smtp_port")
    private Integer smtp_port;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getSession_timeout() {
        return session_timeout;
    }

    public void setSession_timeout(Integer session_timeout) {
        this.session_timeout = session_timeout;
    }

    public Boolean getSmtp_enabled() {
        return smtp_enabled;
    }

    public void setSmtp_enabled(Boolean smtp_enabled) {
        this.smtp_enabled = smtp_enabled;
    }

    public String getSmtp_server_address() {
        return smtp_server_address;
    }

    public void setSmtp_server_address(String smtp_server_address) {
        this.smtp_server_address = smtp_server_address;
    }

    public String getSmtp_username() {
        return smtp_username;
    }

    public void setSmtp_username(String smtp_username) {
        this.smtp_username = smtp_username;
    }

    public String getSmtp_password() {
        return smtp_password;
    }

    public void setSmtp_password(String smtp_password) {
        this.smtp_password = smtp_password;
    }

    public Integer getSmtp_port() {
        return smtp_port;
    }

    public void setSmtp_port(Integer smtp_port) {
        this.smtp_port = smtp_port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Organization organization = (Organization) o;

        if ( ! Objects.equals(id, organization.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Organization{" +
                "id=" + id +
                ", name='" + name + "'" +
                ", description='" + description + "'" +
                ", active='" + active + "'" +
                ", session_timeout='" + session_timeout + "'" +
                ", smtp_enabled='" + smtp_enabled + "'" +
                ", smtp_server_address='" + smtp_server_address + "'" +
                ", smtp_username='" + smtp_username + "'" +
                ", smtp_password='" + smtp_password + "'" +
                ", smtp_port='" + smtp_port + "'" +
                '}';
    }
}
