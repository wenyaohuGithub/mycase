package com.hu.cm.domain;

import com.hu.cm.domain.admin.Role;
import com.hu.cm.domain.configuration.Route;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A Route_condition.
 */
@Entity
@Table(name = "ROUTE_CONDITION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Route_condition implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "previous_step_result")
    private Boolean previous_step_result;

    @ManyToOne
    private Route route;

    @ManyToOne
    private Role submitter_role;

    @ManyToOne
    private Category contract_category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getPrevious_step_result() {
        return previous_step_result;
    }

    public void setPrevious_step_result(Boolean previous_step_result) {
        this.previous_step_result = previous_step_result;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Role getSubmitter_role() {
        return submitter_role;
    }

    public void setSubmitter_role(Role role) {
        this.submitter_role = role;
    }

    public Category getContract_category() {
        return contract_category;
    }

    public void setContract_category(Category category) {
        this.contract_category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Route_condition route_condition = (Route_condition) o;

        if ( ! Objects.equals(id, route_condition.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Route_condition{" +
                "id=" + id +
                ", previous_step_result='" + previous_step_result + "'" +
                '}';
    }
}
