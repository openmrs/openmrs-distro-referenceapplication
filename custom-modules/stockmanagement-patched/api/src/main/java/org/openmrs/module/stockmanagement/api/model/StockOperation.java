package org.openmrs.module.stockmanagement.api.model;

import org.openmrs.*;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * The persistent class for the stockmgmt_stock_operation database table.
 */
@Entity(name = "stockmanagement.StockOperation")
@Table(name = "stockmgmt_stock_operation")
public class StockOperation extends BaseChangeableOpenmrsData implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "stock_operation_id")
	private Integer id;
	
	@Column(name = "cancel_reason", length = 500)
	private String cancelReason;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cancelled_by")
	private User cancelledBy;
	
	@Column(name = "cancelled_date")
	private Date cancelledDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "completed_by")
	private User completedBy;
	
	@Column(name = "completed_date")
	private Date completedDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "submitted_by")
	private User submittedBy;
	
	@Column(name = "submitted_date")
	private Date submittedDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dispatched_by")
	private User dispatchedBy;
	
	@Column(name = "dispatched_date")
	private Date dispatchedDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "returned_by")
	private User returnedBy;
	
	@Column(name = "returned_date")
	private Date returnedDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rejected_by")
	private User rejectedBy;
	
	@Column(name = "rejected_date")
	private Date rejectedDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "destination_id")
	private Party destination;
	
	@Column(name = "external_reference", length = 50)
	private String externalReference;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "at_location_id")
	private Location atLocation;
	
	@Column(name = "operation_date")
	private Date operationDate;
	
	@Column(name = "locked")
	private boolean locked;
	
	@Column(name = "operation_number", length = 255)
	private String operationNumber;
	
	@Column(name = "operation_order")
	private Integer operationOrder;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reason_id")
	private Concept reason;
	
	@Column(name = "remarks", length = 255)
	private String remarks;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "source_id")
	private Party source;
	
	@Column(name = "status", length = 50)
	@Enumerated(EnumType.STRING)
	private StockOperationStatus status;
	
	@Column(name = "return_reason", length = 500)
	private String returnReason;
	
	@Column(name = "reject_reason", length = 500)
	private String rejectionReason;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "responsible_person")
	private User responsiblePerson;
	
	@Column(name = "responsible_person_other", length = 150)
	private String responsiblePersonOther;
	
	//bi-directional many-to-one association to StockItemTransaction
	@OneToMany(mappedBy = "stockOperation", cascade = CascadeType.ALL)
	private Set<StockItemTransaction> stockItemTransactions;
	
	//bi-directional many-to-one association to StockItemTransaction
	@OneToMany(mappedBy = "stockOperation", cascade = CascadeType.ALL)
	private Set<ReservedTransaction> reservedTransactions;
	
	//bi-directional many-to-one association to StockOperationType
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "operation_type_id")
	private StockOperationType stockOperationType;
	
	//bi-directional many-to-one association to StockOperationItem
	@OneToMany(mappedBy = "stockOperation", cascade = CascadeType.ALL)
	private Set<StockOperationItem> stockOperationItems;
	
	//bi-directional many-to-one association to StockOperationItem
	@OneToMany(mappedBy = "child")
	private Set<StockOperationLink> parentStockOperationLinks;
	
	//bi-directional many-to-one association to StockOperationItem
	@OneToMany(mappedBy = "parent")
	private Set<StockOperationLink> childStockOperationLinks;
	
	@Column(name = "approval_required", nullable = true)
	private Boolean approvalRequired;
	
	public User getSubmittedBy() {
		return submittedBy;
	}
	
	public void setSubmittedBy(User submittedBy) {
		this.submittedBy = submittedBy;
	}
	
	public Date getSubmittedDate() {
		return submittedDate;
	}
	
	public void setSubmittedDate(Date submittedDate) {
		this.submittedDate = submittedDate;
	}
	
	public User getReturnedBy() {
		return returnedBy;
	}
	
	public void setReturnedBy(User returnedBy) {
		this.returnedBy = returnedBy;
	}
	
	public Date getReturnedDate() {
		return returnedDate;
	}
	
	public void setReturnedDate(Date returnedDate) {
		this.returnedDate = returnedDate;
	}
	
	public User getRejectedBy() {
		return rejectedBy;
	}
	
	public void setRejectedBy(User rejectedBy) {
		this.rejectedBy = rejectedBy;
	}
	
	public Date getRejectedDate() {
		return rejectedDate;
	}
	
	public void setRejectedDate(Date rejectedDate) {
		this.rejectedDate = rejectedDate;
	}
	
	public Concept getReason() {
		return reason;
	}
	
	public void setReason(Concept reason) {
		this.reason = reason;
	}
	
	public StockOperation() {
	}
	
	public Boolean getApprovalRequired() {
		return approvalRequired;
	}
	
	public void setApprovalRequired(Boolean approvalRequired) {
		this.approvalRequired = approvalRequired;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setId(Integer Id) {
		this.id = Id;
	}
	
	public String getCancelReason() {
		return this.cancelReason;
	}
	
	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}
	
	public User getCancelledBy() {
		return this.cancelledBy;
	}
	
	public void setCancelledBy(User cancelledBy) {
		this.cancelledBy = cancelledBy;
	}
	
	public Date getCancelledDate() {
		return this.cancelledDate;
	}
	
	public void setCancelledDate(Date cancelledDate) {
		this.cancelledDate = cancelledDate;
	}
	
	public User getCompletedBy() {
		return this.completedBy;
	}
	
	public void setCompletedBy(User completedBy) {
		this.completedBy = completedBy;
	}
	
	public Date getCompletedDate() {
		return this.completedDate;
	}
	
	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}
	
	public Party getDestination() {
		return destination;
	}
	
	public String getExternalReference() {
		return this.externalReference;
	}
	
	public void setExternalReference(String externalReference) {
		this.externalReference = externalReference;
	}
	
	public Date getOperationDate() {
		return this.operationDate;
	}
	
	public void setOperationDate(Date operationDate) {
		this.operationDate = operationDate;
	}
	
	public String getOperationNumber() {
		return this.operationNumber;
	}
	
	public void setOperationNumber(String operationNumber) {
		this.operationNumber = operationNumber;
	}
	
	public Integer getOperationOrder() {
		return this.operationOrder;
	}
	
	public void setOperationOrder(Integer operationOrder) {
		this.operationOrder = operationOrder;
	}
	
	public String getRemarks() {
		return this.remarks;
	}
	
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public StockOperationStatus getStatus() {
		return this.status;
	}
	
	public void setStatus(StockOperationStatus status) {
		this.status = status;
	}
	
	public boolean getLocked() {
		return locked;
	}
	
	public void setLocked(boolean locked) {
		this.locked = locked;
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
	
	public Set<StockItemTransaction> getStockItemTransactions() {
		return this.stockItemTransactions;
	}
	
	public void setStockItemTransactions(Set<StockItemTransaction> stockItemTransactions) {
		this.stockItemTransactions = stockItemTransactions;
	}
	
	public StockItemTransaction addStockItemTransaction(StockItemTransaction stockItemTransaction) {
		getStockItemTransactions().add(stockItemTransaction);
		stockItemTransaction.setStockOperation(this);
		
		return stockItemTransaction;
	}
	
	public StockItemTransaction removeStockItemTransaction(StockItemTransaction stockItemTransaction) {
		getStockItemTransactions().remove(stockItemTransaction);
		stockItemTransaction.setStockOperation(null);
		
		return stockItemTransaction;
	}
	
	public StockOperationType getStockOperationType() {
		return this.stockOperationType;
	}
	
	public void setStockOperationType(StockOperationType stockOperationType) {
		this.stockOperationType = stockOperationType;
	}
	
	public Set<StockOperationItem> getStockOperationItems() {
		return this.stockOperationItems;
	}
	
	public void setStockOperationItems(Set<StockOperationItem> stockOperationItems) {
		this.stockOperationItems = stockOperationItems;
	}
	
	public StockOperationItem addStockOperationItem(StockOperationItem stockOperationItem) {
		getStockOperationItems().add(stockOperationItem);
		stockOperationItem.setStockOperation(this);
		
		return stockOperationItem;
	}
	
	public StockOperationItem removeStockOperationItem(StockOperationItem stockOperationItem) {
		getStockOperationItems().remove(stockOperationItem);
		stockOperationItem.setStockOperation(null);
		
		return stockOperationItem;
	}
	
	public Set<ReservedTransaction> getReservedTransactions() {
		return reservedTransactions;
	}
	
	public void setReservedTransactions(Set<ReservedTransaction> reservedTransactions) {
		this.reservedTransactions = reservedTransactions;
	}
	
	public ReservedTransaction addReservedTransaction(ReservedTransaction reservedTransaction) {
		getReservedTransactions().add(reservedTransaction);
		reservedTransaction.setStockOperation(this);
		reservedTransaction.setIsAvailable(getStockOperationType().getAvailableWhenReserved() != null
		        && getStockOperationType().getAvailableWhenReserved());
		return reservedTransaction;
	}
	
	public ReservedTransaction removeReservedTransaction(ReservedTransaction reservedTransaction) {
		getReservedTransactions().remove(reservedTransaction);
		reservedTransaction.setStockOperation(null);
		
		return reservedTransaction;
	}
	
	public void setDestination(Party destination) {
		this.destination = destination;
	}
	
	public Location getAtLocation() {
		return atLocation;
	}
	
	public void setAtLocation(Location atLocation) {
		this.atLocation = atLocation;
	}
	
	public Party getSource() {
		return source;
	}
	
	public void setSource(Party source) {
		this.source = source;
	}
	
	public User getResponsiblePerson() {
		return responsiblePerson;
	}
	
	public void setResponsiblePerson(User responsiblePerson) {
		this.responsiblePerson = responsiblePerson;
	}
	
	public String getResponsiblePersonOther() {
		return responsiblePersonOther;
	}
	
	public void setResponsiblePersonOther(String responsiblePersonOther) {
		this.responsiblePersonOther = responsiblePersonOther;
	}
	
	public User getDispatchedBy() {
		return dispatchedBy;
	}
	
	public void setDispatchedBy(User dispatchedBy) {
		this.dispatchedBy = dispatchedBy;
	}
	
	public Date getDispatchedDate() {
		return dispatchedDate;
	}
	
	public void setDispatchedDate(Date dispatchedDate) {
		this.dispatchedDate = dispatchedDate;
	}
	
	public boolean isUpdateable() {
		return !getLocked() && !getVoided() && StockOperationStatus.IsUpdateable(getStatus());
	}
	
	public boolean canReceiveItems() {
		return !isUpdateable() && StockOperationStatus.canReceiveItems(getStatus());
	}
	
	public boolean canDisplayReceivedItems() {
		return canReceiveItems() || (StockOperationStatus.canDisplayReceivedItems(getStatus(), this.getDispatchedDate()));
	}
	
	public Set<StockOperationLink> getParentStockOperationLinks() {
		return parentStockOperationLinks;
	}
	
	public void setParentStockOperationLinks(Set<StockOperationLink> parentStockOperationLinks) {
		this.parentStockOperationLinks = parentStockOperationLinks;
	}
	
	public Set<StockOperationLink> getChildStockOperationLinks() {
		return childStockOperationLinks;
	}
	
	public void setChildStockOperationLinks(Set<StockOperationLink> childStockOperationLinks) {
		this.childStockOperationLinks = childStockOperationLinks;
	}
}
