package org.openmrs.module.stockmanagement.api.dto;

import org.openmrs.module.stockmanagement.api.model.StockOperationStatus;
import org.openmrs.module.stockmanagement.api.model.StockOperationType;

import java.util.Date;
import java.util.List;

public class StockOperationDTO {
	
	private Integer id;
	
	private String uuid;
	
	private String cancelReason;
	
	private Integer cancelledBy;
	
	private String cancelledByGivenName;
	
	private String cancelledByFamilyName;
	
	private Date cancelledDate;
	
	private Integer completedBy;
	
	private String completedByGivenName;
	
	private String completedByFamilyName;
	
	private Date completedDate;
	
	private String destinationUuid;
	
	private String destinationName;
	
	private String externalReference;
	
	private Integer reasonId;
	
	private String reasonUuid;
	
	private String reasonName;
	
	private String atLocationUuid;
	
	private String atLocationName;
	
	private Date operationDate;
	
	private boolean locked;
	
	private String operationNumber;
	
	private Integer operationOrder;
	
	private String remarks;
	
	private String sourceUuid;
	
	private String sourceName;
	
	private StockOperationStatus status;
	
	private String returnReason;
	
	private String rejectionReason;
	
	private String operationTypeUuid;
	
	private String operationType;
	
	private String operationTypeName;
	
	private Integer responsiblePerson;
	
	private String responsiblePersonGivenName;
	
	private String responsiblePersonUuid;
	
	private String requisitionStockOperationUuid;
	
	private String responsiblePersonFamilyName;
	
	private String responsiblePersonOther;
	
	private Integer creator;
	
	private Date dateCreated;
	
	private String creatorGivenName;
	
	private String creatorFamilyName;
	
	private boolean voided;
	
	private Boolean approvalRequired;
	
	private Integer submittedBy;
	
	private String submittedByGivenName;
	
	private String submittedByFamilyName;
	
	private Date submittedDate;
	
	private Integer dispatchedBy;
	
	private String dispatchedByGivenName;
	
	private String dispatchedByFamilyName;
	
	private Date dispatchedDate;
	
	private Integer returnedBy;
	
	private String returnedByGivenName;
	
	private String returnedByFamilyName;
	
	private Date returnedDate;
	
	private String rejectedByGivenName;
	
	private String rejectedByFamilyName;
	
	private Integer rejectedBy;
	
	private Date rejectedDate;
	
	public Integer getSubmittedBy() {
		return submittedBy;
	}
	
	public void setSubmittedBy(Integer submittedBy) {
		this.submittedBy = submittedBy;
	}
	
	public String getSubmittedByGivenName() {
		return submittedByGivenName;
	}
	
	public void setSubmittedByGivenName(String submittedByGivenName) {
		this.submittedByGivenName = submittedByGivenName;
	}
	
	public String getSubmittedByFamilyName() {
		return submittedByFamilyName;
	}
	
	public void setSubmittedByFamilyName(String submittedByFamilyName) {
		this.submittedByFamilyName = submittedByFamilyName;
	}
	
	public Date getSubmittedDate() {
		return submittedDate;
	}
	
	public void setSubmittedDate(Date submittedDate) {
		this.submittedDate = submittedDate;
	}
	
	public Integer getReturnedBy() {
		return returnedBy;
	}
	
	public void setReturnedBy(Integer returnedBy) {
		this.returnedBy = returnedBy;
	}
	
	public String getReturnedByGivenName() {
		return returnedByGivenName;
	}
	
	public void setReturnedByGivenName(String returnedByGivenName) {
		this.returnedByGivenName = returnedByGivenName;
	}
	
	public String getReturnedByFamilyName() {
		return returnedByFamilyName;
	}
	
	public void setReturnedByFamilyName(String returnedByFamilyName) {
		this.returnedByFamilyName = returnedByFamilyName;
	}
	
	public Date getReturnedDate() {
		return returnedDate;
	}
	
	public void setReturnedDate(Date returnedDate) {
		this.returnedDate = returnedDate;
	}
	
	public String getRejectedByGivenName() {
		return rejectedByGivenName;
	}
	
	public void setRejectedByGivenName(String rejectedByGivenName) {
		this.rejectedByGivenName = rejectedByGivenName;
	}
	
	public String getRejectedByFamilyName() {
		return rejectedByFamilyName;
	}
	
	public void setRejectedByFamilyName(String rejectedByFamilyName) {
		this.rejectedByFamilyName = rejectedByFamilyName;
	}
	
	public Integer getRejectedBy() {
		return rejectedBy;
	}
	
	public void setRejectedBy(Integer rejectedBy) {
		this.rejectedBy = rejectedBy;
	}
	
	public Date getRejectedDate() {
		return rejectedDate;
	}
	
	public void setRejectedDate(Date rejectedDate) {
		this.rejectedDate = rejectedDate;
	}
	
	public Boolean getApprovalRequired() {
		return approvalRequired;
	}
	
	public void setApprovalRequired(Boolean approvalRequired) {
		this.approvalRequired = approvalRequired;
	}
	
	private List<StockOperationItemDTO> stockOperationItems;
	
	public boolean getVoided() {
		return voided;
	}
	
	public void setVoided(boolean voided) {
		this.voided = voided;
	}
	
	public Integer getReasonId() {
		return reasonId;
	}
	
	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public String getOperationTypeUuid() {
		return operationTypeUuid;
	}
	
	public void setOperationTypeUuid(String operationTypeUuid) {
		this.operationTypeUuid = operationTypeUuid;
	}
	
	public String getOperationTypeName() {
		return operationTypeName;
	}
	
	public void setOperationTypeName(String operationTypeName) {
		this.operationTypeName = operationTypeName;
	}
	
	public Integer getResponsiblePerson() {
		return responsiblePerson;
	}
	
	public void setResponsiblePerson(Integer responsiblePerson) {
		this.responsiblePerson = responsiblePerson;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getCancelReason() {
		return cancelReason;
	}
	
	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}
	
	public Integer getCancelledBy() {
		return cancelledBy;
	}
	
	public void setCancelledBy(Integer cancelledBy) {
		this.cancelledBy = cancelledBy;
	}
	
	public Integer getCompletedBy() {
		return completedBy;
	}
	
	public void setCompletedBy(Integer completedBy) {
		this.completedBy = completedBy;
	}
	
	public Date getCancelledDate() {
		return cancelledDate;
	}
	
	public void setCancelledDate(Date cancelledDate) {
		this.cancelledDate = cancelledDate;
	}
	
	public Date getCompletedDate() {
		return completedDate;
	}
	
	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}
	
	public String getDestinationUuid() {
		return destinationUuid;
	}
	
	public void setDestinationUuid(String destinationUuid) {
		this.destinationUuid = destinationUuid;
	}
	
	public String getDestinationName() {
		return destinationName;
	}
	
	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}
	
	public String getExternalReference() {
		return externalReference;
	}
	
	public void setExternalReference(String externalReference) {
		this.externalReference = externalReference;
	}
	
	public String getAtLocationUuid() {
		return atLocationUuid;
	}
	
	public void setAtLocationUuid(String atLocationUuid) {
		this.atLocationUuid = atLocationUuid;
	}
	
	public String getAtLocationName() {
		return atLocationName;
	}
	
	public void setAtLocationName(String atLocationName) {
		this.atLocationName = atLocationName;
	}
	
	public Date getOperationDate() {
		return operationDate;
	}
	
	public void setOperationDate(Date operationDate) {
		this.operationDate = operationDate;
	}
	
	public boolean getLocked() {
		return locked;
	}
	
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
	public String getOperationNumber() {
		return operationNumber;
	}
	
	public void setOperationNumber(String operationNumber) {
		this.operationNumber = operationNumber;
	}
	
	public Integer getOperationOrder() {
		return operationOrder;
	}
	
	public void setOperationOrder(Integer operationOrder) {
		this.operationOrder = operationOrder;
	}
	
	public String getReasonUuid() {
		return reasonUuid;
	}
	
	public void setReasonUuid(String reasonUuid) {
		this.reasonUuid = reasonUuid;
	}
	
	public String getReasonName() {
		return reasonName;
	}
	
	public void setReasonName(String reasonName) {
		this.reasonName = reasonName;
	}
	
	public String getRemarks() {
		return remarks;
	}
	
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public String getSourceUuid() {
		return sourceUuid;
	}
	
	public void setSourceUuid(String sourceUuid) {
		this.sourceUuid = sourceUuid;
	}
	
	public String getSourceName() {
		return sourceName;
	}
	
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	
	public StockOperationStatus getStatus() {
		return status;
	}
	
	public void setStatus(StockOperationStatus status) {
		this.status = status;
	}
	
	public String getReturnReason() {
		return returnReason;
	}
	
	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}
	
	public String getRejectionReason() {
		return rejectionReason;
	}
	
	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}
	
	public String getOperationType() {
		return operationType;
	}
	
	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
	
	public String getResponsiblePersonGivenName() {
		return responsiblePersonGivenName;
	}
	
	public void setResponsiblePersonGivenName(String responsiblePersonGivenName) {
		this.responsiblePersonGivenName = responsiblePersonGivenName;
	}
	
	public String getResponsiblePersonFamilyName() {
		return responsiblePersonFamilyName;
	}
	
	public void setResponsiblePersonFamilyName(String responsiblePersonFamilyName) {
		this.responsiblePersonFamilyName = responsiblePersonFamilyName;
	}
	
	public String getResponsiblePersonOther() {
		return responsiblePersonOther;
	}
	
	public void setResponsiblePersonOther(String responsiblePersonOther) {
		this.responsiblePersonOther = responsiblePersonOther;
	}
	
	public Integer getCreator() {
		return creator;
	}
	
	public void setCreator(Integer creator) {
		this.creator = creator;
	}
	
	public Date getDateCreated() {
		return dateCreated;
	}
	
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	public String getCancelledByGivenName() {
		return cancelledByGivenName;
	}
	
	public void setCancelledByGivenName(String cancelledByGivenName) {
		this.cancelledByGivenName = cancelledByGivenName;
	}
	
	public String getCancelledByFamilyName() {
		return cancelledByFamilyName;
	}
	
	public void setCancelledByFamilyName(String cancelledByFamilyName) {
		this.cancelledByFamilyName = cancelledByFamilyName;
	}
	
	public String getCompletedByGivenName() {
		return completedByGivenName;
	}
	
	public void setCompletedByGivenName(String completedByGivenName) {
		this.completedByGivenName = completedByGivenName;
	}
	
	public String getCompletedByFamilyName() {
		return completedByFamilyName;
	}
	
	public void setCompletedByFamilyName(String completedByFamilyName) {
		this.completedByFamilyName = completedByFamilyName;
	}
	
	public String getCreatorGivenName() {
		return creatorGivenName;
	}
	
	public void setCreatorGivenName(String creatorGivenName) {
		this.creatorGivenName = creatorGivenName;
	}
	
	public String getCreatorFamilyName() {
		return creatorFamilyName;
	}
	
	public void setCreatorFamilyName(String creatorFamilyName) {
		this.creatorFamilyName = creatorFamilyName;
	}
	
	public List<StockOperationItemDTO> getStockOperationItems() {
		return stockOperationItems;
	}
	
	public void setStockOperationItems(List<StockOperationItemDTO> stockOperationItems) {
		this.stockOperationItems = stockOperationItems;
	}
	
	public String getResponsiblePersonUuid() {
		return responsiblePersonUuid;
	}
	
	public void setResponsiblePersonUuid(String responsiblePersonUuid) {
		this.responsiblePersonUuid = responsiblePersonUuid;
	}
	
	public String getRequisitionStockOperationUuid() {
		return requisitionStockOperationUuid;
	}
	
	public void setRequisitionStockOperationUuid(String requisitionStockOperationUuid) {
		this.requisitionStockOperationUuid = requisitionStockOperationUuid;
	}
	
	public boolean isUpdateable() {
		return !getLocked() && !getVoided() && StockOperationStatus.IsUpdateable(getStatus());
	}
	
	public boolean isApproveable() {
		return getLocked() && !getVoided() && StockOperationStatus.IsApproveable(getStatus());
	}
	
	public boolean canReceiveItems() {
		return !isUpdateable() && StockOperationStatus.canReceiveItems(getStatus());
	}
	
	public boolean canDisplayReceivedItems() {
		return canReceiveItems() || (StockOperationStatus.canDisplayReceivedItems(getStatus(), this.getDispatchedDate()));
	}
	
	public boolean isRequisitionAndCanIssueStock() {
		return !isUpdateable() && StockOperationType.REQUISITION.equals(getOperationType())
		        && StockOperationStatus.isCompleted(getStatus());
	}
	
	public boolean canUpdateBatchInformation(StockOperationType stockOperationType) {
		return (!getVoided()) && StockOperationStatus.canUpdateBatchInformation(getStatus())
		        && stockOperationType.getOperationType().equals(getOperationType())
		        && stockOperationType.getAllowBatchInfoUpdate();
	}
	
	public Date getDispatchedDate() {
		return dispatchedDate;
	}
	
	public void setDispatchedDate(Date dispatchedDate) {
		this.dispatchedDate = dispatchedDate;
	}
	
	public String getDispatchedByFamilyName() {
		return dispatchedByFamilyName;
	}
	
	public void setDispatchedByFamilyName(String dispatchedByFamilyName) {
		this.dispatchedByFamilyName = dispatchedByFamilyName;
	}
	
	public String getDispatchedByGivenName() {
		return dispatchedByGivenName;
	}
	
	public void setDispatchedByGivenName(String dispatchedByGivenName) {
		this.dispatchedByGivenName = dispatchedByGivenName;
	}
	
	public Integer getDispatchedBy() {
		return dispatchedBy;
	}
	
	public void setDispatchedBy(Integer dispatchedBy) {
		this.dispatchedBy = dispatchedBy;
	}
}
