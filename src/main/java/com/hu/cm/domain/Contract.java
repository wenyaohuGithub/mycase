package com.hu.cm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hu.cm.domain.admin.Department;
import com.hu.cm.domain.admin.User;
import com.hu.cm.domain.configuration.Process;
import com.hu.cm.domain.configuration.ContractSample;
import com.hu.cm.domain.configuration.FundSource;
import com.hu.cm.domain.util.CustomDateTimeDeserializer;
import com.hu.cm.domain.util.CustomDateTimeSerializer;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.hu.cm.domain.enumeration.ContractingMethod;
import com.hu.cm.domain.enumeration.Currency;
import com.hu.cm.domain.enumeration.ProcessState;


/**
 * A Contract.
 */
@Entity
@Table(name = "CONTRACT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Contract implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "review_identifier")
    private String reviewIdentifier;

    @Column(name = "contract_identifier")
    private String contractIdentifier;

    @Enumerated(EnumType.STRING)
    @Column(name = "contracting_method")
    private ContractingMethod contractingMethod;

    @Column(name = "amount", precision=10, scale=2)
    private BigDecimal amount;

    @Column(name = "amount_written")
    private String amountWritten;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private Currency currency;

    @Column(name = "amount_current_year", precision=10, scale=2)
    private BigDecimal amountCurrentYear;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "submit_date")
    private DateTime submitDate;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "start_date")
    private DateTime startDate;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "end_date")
    private DateTime endDate;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "expire_date")
    private DateTime expireDate;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "modified_date")
    private DateTime modifiedDate;

    @Column(name = "is_multi_year")
    private Boolean isMultiYear;

    @ManyToOne
    @JoinColumn(name = "status")
    private Process status;

    @ManyToOne
    @JoinColumn(name = "next_process")
    private Process nextProcess;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private ProcessState state;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "approve_date")
    private DateTime approveDate;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "sign_date")
    private DateTime signDate;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "archive_date")
    private DateTime archiveDate;

    @ManyToMany
    @JoinTable(name = "CONTRACT_CONTRACT_PARTY",
        joinColumns = { @JoinColumn(name = "CONTRACT_ID") }, inverseJoinColumns = { @JoinColumn(name = "CONTRACT_PARTY_ID") })
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ContractParty> contractParties = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "administrative_department_id")
    private Department administrativeDepartment;

    @ManyToOne
    @JoinColumn(name = "administrator_id")
    private User administrator;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "modified_by")
    private User modifiedBy;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private User assignee;

    @ManyToOne
    private Category category;

    @ManyToOne
    @JoinColumn(name = "fund_source_id")
    private FundSource fundSource;

    @ManyToOne
    @JoinColumn(name = "contract_sample_id")
    private ContractSample contractSample;

    @ManyToMany
    @JoinTable(name = "CONTRACT_DEPARTMENT",
        joinColumns = { @JoinColumn(name = "CONTRACT_ID") }, inverseJoinColumns = { @JoinColumn(name = "DEPARTMENT_ID") })
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Department> relatedDepartments = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "CONTRACT_PROJECT",
        joinColumns = { @JoinColumn(name = "CONTRACT_ID") }, inverseJoinColumns = { @JoinColumn(name = "PROJECT_ID") })
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Project> projects = new HashSet<>();

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

    public String getReviewIdentifier() {
        return reviewIdentifier;
    }

    public void setReviewIdentifier(String reviewIdentifier) {
        this.reviewIdentifier = reviewIdentifier;
    }

    public String getContractIdentifier() {
        return contractIdentifier;
    }

    public void setContractIdentifier(String contractIdentifier) {
        this.contractIdentifier = contractIdentifier;
    }

    public ContractingMethod getContractingMethod() {
        return contractingMethod;
    }

    public void setContractingMethod(ContractingMethod contractingMethod) {
        this.contractingMethod = contractingMethod;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getAmountWritten() {
        return amountWritten;
    }

    public void setAmountWritten(String amountWritten) {
        this.amountWritten = amountWritten;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getAmountCurrentYear() {
        return amountCurrentYear;
    }

    public void setAmountCurrentYear(BigDecimal amountCurrentYear) {
        this.amountCurrentYear = amountCurrentYear;
    }

    public DateTime getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(DateTime submitDate) {
        this.submitDate = submitDate;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    public DateTime getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(DateTime expireDate) {
        this.expireDate = expireDate;
    }

    public Boolean getIsMultiYear() {
        return isMultiYear;
    }

    public void setIsMultiYear(Boolean isMultiYear) {
        this.isMultiYear = isMultiYear;
    }

    public Process getStatus() {
        return status;
    }

    public void setStatus(Process status) {
        this.status = status;
    }

    public ProcessState getState() {
        return state;
    }

    public void setState(ProcessState state) {
        this.state = state;
    }

    public DateTime getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(DateTime approveDate) {
        this.approveDate = approveDate;
    }

    public DateTime getSignDate() {
        return signDate;
    }

    public void setSignDate(DateTime signDate) {
        this.signDate = signDate;
    }

    public DateTime getArchiveDate() {
        return archiveDate;
    }

    public void setArchiveDate(DateTime archiveDate) {
        this.archiveDate = archiveDate;
    }

    public Set<ContractParty> getContractParties() {
        return contractParties;
    }

    public void setContractParties(Set<ContractParty> contractParties) {
        this.contractParties = contractParties;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public FundSource getFundSource() {
        return fundSource;
    }

    public void setFundSource(FundSource fundSource) {
        this.fundSource = fundSource;
    }

    public ContractSample getContractSample() {
        return contractSample;
    }

    public void setContractSample(ContractSample contractSample) {
        this.contractSample = contractSample;
    }

    public Set<Department> getRelatedDepartments() {
        return relatedDepartments;
    }

    public void setRelatedDepartments(Set<Department> departments) {
        this.relatedDepartments = departments;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public User getAdministrator() {
        return administrator;
    }

    public void setAdministrator(User administrator) {
        this.administrator = administrator;
    }

    public Department getAdministrativeDepartment() {
        return administrativeDepartment;
    }

    public void setAdministrativeDepartment(Department administrativeDepartment) {
        this.administrativeDepartment = administrativeDepartment;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(DateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public Process getNextProcess() {
        return nextProcess;
    }

    public void setNextProcess(Process nextProcess) {
        this.nextProcess = nextProcess;
    }

    public User getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Contract contract = (Contract) o;

        if ( ! Objects.equals(id, contract.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Contract{" +
                "id=" + id +
                ", name='" + name + "'" +
                ", review_identifier='" + reviewIdentifier + "'" +
                ", contract_identifier='" + contractIdentifier + "'" +
                ", contracting_method='" + contractingMethod + "'" +
                ", amount='" + amount + "'" +
                ", amount_written='" + amountWritten + "'" +
                ", currency='" + currency + "'" +
                ", amount_current_year='" + amountCurrentYear + "'" +
                ", submit_date='" + submitDate + "'" +
                ", start_date='" + startDate + "'" +
                ", end_date='" + endDate + "'" +
                ", expire_date='" + expireDate + "'" +
                ", is_multi_year='" + isMultiYear + "'" +
                ", status='" + status + "'" +
                ", state='" + state + "'" +
                ", approve_date='" + approveDate + "'" +
                ", sign_date='" + signDate + "'" +
                ", archive_date='" + archiveDate + "'" +
                '}';
    }
}
