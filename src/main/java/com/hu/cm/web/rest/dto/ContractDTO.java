package com.hu.cm.web.rest.dto;


import com.hu.cm.domain.Category;
import com.hu.cm.domain.admin.User;
import com.hu.cm.domain.configuration.ContractSample;
import com.hu.cm.domain.configuration.FundSource;
import com.hu.cm.domain.configuration.Process;
import com.hu.cm.domain.enumeration.ContractStatus;
import com.hu.cm.domain.enumeration.ContractingMethod;
import com.hu.cm.domain.enumeration.Currency;
import com.hu.cm.domain.enumeration.ProcessState;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ContractDTO {
    private Long id;

    private String name;

    private String reviewIdentifier;

    private String contractIdentifier;

    private ContractingMethod contractingMethod;

    private BigDecimal amount;

    private String amountWritten;

    private String description;

    private Currency currency;

    private BigDecimal amountCurrentYear;

    private String submitDate;

    private String startDate;

    private String endDate;

    private String expireDate;

    private Boolean isMultiYear;

    private ProcessDTO status;

    private ProcessState state;

    private String approveDate;

    private String signDate;

    private String archiveDate;

    private Set<ContractPartyDTO> contractParties = new HashSet<>();

    private DepartmentDTO administrativeDepartment;

    private User administrator;

    private User author;

    private User assignee;

    private User modifiedBy;

    private CategoryDTO category;

    private FundSource fundSource;

    private ContractSample contractSample;

    private Set<DepartmentDTO> relatedDepartments = new HashSet<>();

    private Set<ProjectDTO> projects = new HashSet<>();

    private Process nextProcess;

    private Boolean isDraft;


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

    public String getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(String submitDate) {
        this.submitDate = submitDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public Boolean getIsMultiYear() {
        return isMultiYear;
    }

    public void setIsMultiYear(Boolean isMultiYear) {
        this.isMultiYear = isMultiYear;
    }

    public ProcessDTO getStatus() {
        return status;
    }

    public void setStatus(ProcessDTO status) {
        this.status = status;
    }

    public ProcessState getState() {
        return state;
    }

    public void setState(ProcessState state) {
        this.state = state;
    }

    public String getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(String approveDate) {
        this.approveDate = approveDate;
    }

    public String getSignDate() {
        return signDate;
    }

    public void setSignDate(String signDate) {
        this.signDate = signDate;
    }

    public String getArchiveDate() {
        return archiveDate;
    }

    public void setArchiveDate(String archiveDate) {
        this.archiveDate = archiveDate;
    }

    public Set<ContractPartyDTO> getContractParties() {
        return contractParties;
    }

    public void setContractParties(Set<ContractPartyDTO> contractParties) {
        this.contractParties = contractParties;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
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

    public Set<DepartmentDTO> getRelatedDepartments() {
        return relatedDepartments;
    }

    public void setRelatedDepartments(Set<DepartmentDTO> departments) {
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

    public DepartmentDTO getAdministrativeDepartment() {
        return administrativeDepartment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAdministrativeDepartment(DepartmentDTO administrativeDepartment) {
        this.administrativeDepartment = administrativeDepartment;
    }

    public Set<ProjectDTO> getProjects() {
        return projects;
    }

    public void setProjects(Set<ProjectDTO> projects) {
        this.projects = projects;
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

    public Boolean getIsDraft() {
        return isDraft;
    }

    public void setIsDraft(Boolean draft) {
        isDraft = draft;
    }

    public User getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
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
