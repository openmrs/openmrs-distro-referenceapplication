package org.openmrs.module.stockmanagement.api.model;

import org.openmrs.BaseOpenmrsObject;
import org.openmrs.User;

import javax.persistence.*;
import java.util.Date;

/**
 * The persistent class for the stockmgmt_batch_job_owner database table.
 */
@Entity(name = "stockmanagement.BatchJobOwner")
@Table(name = "stockmgmt_batch_job_owner")
public class BatchJobOwner extends BaseOpenmrsObject {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "batch_job_owner_id")
	private Integer id;
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "batch_job_id", nullable = false)
	private BatchJob batchJob;
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "owner", nullable = false)
	private User owner;
	
	@Column(name = "date_created", nullable = false)
	private Date dateCreated;
	
	@Override
	public Integer getId() {
		return id;
	}
	
	@Override
	public void setId(Integer id) {
		this.id = id;
	}
	
	public BatchJob getBatchJob() {
		return batchJob;
	}
	
	public void setBatchJob(BatchJob batchJob) {
		this.batchJob = batchJob;
	}
	
	public User getOwner() {
		return owner;
	}
	
	public void setOwner(User owner) {
		this.owner = owner;
	}
	
	public Date getDateCreated() {
		return dateCreated;
	}
	
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
}
