package org.openmrs.module.stockmanagement.api.model;

import org.openmrs.BaseChangeableOpenmrsData;
import org.openmrs.Location;
import org.openmrs.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * The persistent class for the stockmgmt_batch_job database table.
 */
@Entity(name = "stockmanagement.BatchJob")
@Table(name = "stockmgmt_batch_job")
public class BatchJob extends BaseChangeableOpenmrsData implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "batch_job_id")
	private Integer id;
	
	@Column(name = "batch_job_type", length = 50)
	@Enumerated(EnumType.STRING)
	private BatchJobType batchJobType;
	
	@Column(name = "status", length = 50)
	@Enumerated(EnumType.STRING)
	private BatchJobStatus status;
	
	@Column(name = "description", length = 255)
	private String description;
	
	@Column(name = "start_time")
	private Date startTime;
	
	@Column(name = "end_time")
	private Date endTime;
	
	@Column(name = "expiration")
	private Date expiration;
	
	@Column(name = "parameters", length = 5000)
	private String parameters;
	
	@Column(name = "privilege_scope", length = 255)
	private String privilegeScope;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "location_scope")
	private Location locationScope;
	
	@Column(name = "execution_state", length = 5000)
	private String executionState;
	
	@Column(name = "cancel_reason", length = 500)
	private String cancelReason;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cancelled_by")
	private User cancelledBy;
	
	@Column(name = "cancelled_date")
	private Date cancelledDate;
	
	@Column(name = "exit_message", length = 2500)
	private String exitMessage;
	
	@Column(name = "completed_date")
	private Date completedDate;
	
	@Column(name = "output_artifact_size")
	private Long outputArtifactSize;
	
	@Column(name = "output_artifact_file_ext", length = 10)
	private String outputArtifactFileExt;
	
	@Column(name = "output_artifact_viewable")
	private Boolean outputArtifactViewable;
	
	//bi-directional many-to-one association to StockItemTransaction
	@OneToMany(mappedBy = "batchJob", cascade = CascadeType.ALL)
	private Set<BatchJobOwner> batchJobOwners;
	
	public BatchJob() {
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public BatchJobType getBatchJobType() {
		return batchJobType;
	}
	
	public void setBatchJobType(BatchJobType batchJobType) {
		this.batchJobType = batchJobType;
	}
	
	public BatchJobStatus getStatus() {
		return status;
	}
	
	public void setStatus(BatchJobStatus status) {
		this.status = status;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Date getStartTime() {
		return startTime;
	}
	
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	public Date getEndTime() {
		return endTime;
	}
	
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public Date getExpiration() {
		return expiration;
	}
	
	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}
	
	public String getParameters() {
		return parameters;
	}
	
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
	
	public String getCancelReason() {
		return cancelReason;
	}
	
	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}
	
	public User getCancelledBy() {
		return cancelledBy;
	}
	
	public void setCancelledBy(User cancelledBy) {
		this.cancelledBy = cancelledBy;
	}
	
	public Date getCancelledDate() {
		return cancelledDate;
	}
	
	public void setCancelledDate(Date cancelledDate) {
		this.cancelledDate = cancelledDate;
	}
	
	public String getExitMessage() {
		return exitMessage;
	}
	
	public void setExitMessage(String exitMessage) {
		this.exitMessage = exitMessage;
	}
	
	public Date getCompletedDate() {
		return completedDate;
	}
	
	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}
	
	public String getExecutionState() {
		return executionState;
	}
	
	public void setExecutionState(String executionState) {
		this.executionState = executionState;
	}
	
	public Long getOutputArtifactSize() {
		return outputArtifactSize;
	}
	
	public void setOutputArtifactSize(Long outputArtifactSize) {
		this.outputArtifactSize = outputArtifactSize;
	}
	
	public Set<BatchJobOwner> getBatchJobOwners() {
		return batchJobOwners;
	}
	
	public void setBatchJobOwners(Set<BatchJobOwner> batchJobOwners) {
		this.batchJobOwners = batchJobOwners;
	}
	
	public BatchJobOwner addBatchJobOwner(BatchJobOwner batchJobOwner) {
        Set<BatchJobOwner> owners = getBatchJobOwners();
        if(owners == null){
            owners= new HashSet<>();
            setBatchJobOwners(owners);
        }
        owners.add(batchJobOwner);
        batchJobOwner.setBatchJob(this);

        return batchJobOwner;
    }
	
	public BatchJobOwner removeBatchJobOwner(BatchJobOwner batchJobOwner) {
		getBatchJobOwners().remove(batchJobOwner);
		batchJobOwner.setBatchJob(null);
		return batchJobOwner;
	}
	
	public String getPrivilegeScope() {
		return privilegeScope;
	}
	
	public void setPrivilegeScope(String privilegeScope) {
		this.privilegeScope = privilegeScope;
	}
	
	public Location getLocationScope() {
		return locationScope;
	}
	
	public void setLocationScope(Location locationScope) {
		this.locationScope = locationScope;
	}
	
	public String getOutputArtifactFileExt() {
		return outputArtifactFileExt;
	}
	
	public void setOutputArtifactFileExt(String outputArtifactFileExt) {
		this.outputArtifactFileExt = outputArtifactFileExt;
	}
	
	public Boolean getOutputArtifactViewable() {
		return outputArtifactViewable;
	}
	
	public void setOutputArtifactViewable(Boolean outputArtifactViewable) {
		this.outputArtifactViewable = outputArtifactViewable;
	}
}
