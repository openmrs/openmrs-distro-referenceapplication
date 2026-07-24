package org.openmrs.module.stockmanagement.api.model;

import org.openmrs.BaseChangeableOpenmrsData;
import org.openmrs.BaseOpenmrsData;
import org.openmrs.Location;
import org.openmrs.User;
import org.openmrs.module.stockmanagement.api.StockOperationTypeProcessor;
import org.openmrs.module.stockmanagement.api.impl.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;
import javax.persistence.*;

/**
 * The persistent class for the stockmgmt_stock_operation_type database table.
 */
@Entity(name = "stockmanagement.StockOperationType")
@Table(name = "stockmgmt_stock_operation_type")
public class StockOperationType extends BaseChangeableOpenmrsData implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "stock_operation_type_id")
	private Integer id;
	
	@Column(name = "name", length = 255)
	private String name;
	
	@Column(name = "description", length = 1024)
	private String description;
	
	@Column(name = "operation_type", length = 255)
	private String operationType;
	
	@Column(name = "has_source")
	private Boolean hasSource;
	
	@Column(name = "source_type", nullable = true, length = 50)
	@Enumerated(EnumType.STRING)
	private LocationType sourceType;
	
	@Column(name = "has_destination")
	private Boolean hasDestination;
	
	@Column(name = "destination_type", nullable = true, length = 50)
	@Enumerated(EnumType.STRING)
	private LocationType destinationType;
	
	@Column(name = "available_when_reserved")
	private Boolean availableWhenReserved;
	
	@Column(name = "acronym")
	private String acronym;
	
	@Column(name = "notify_submitted")
	private Boolean notifySubmitted;
	
	@Column(name = "notify_approved")
	private Boolean notifyApproved;
	
	@Column(name = "notify_completed")
	private Boolean notifyCompleted;
	
	@Column(name = "notify_cancelled")
	private Boolean notifyCancelled;
	
	@Column(name = "notify_rejected")
	private Boolean notifyRejected;
	
	@Column(name = "notify_returned")
	private Boolean notifyReturned;
	
	@Column(name = "notify_dispatched")
	private Boolean notifyDispatched;
	
	@Column(name = "allow_expired_batch_numbers")
	private Boolean allowExpiredBatchNumbers;
	
	@Column(name = "allow_batch_info_update")
	private Boolean allowBatchInfoUpdate;
	
	//bi-directional many-to-one association to StockOperationTypeLocationScope
	@OneToMany(mappedBy = "stockOperationType")
	private Set<StockOperationTypeLocationScope> stockOperationTypeLocationScopes;
	
	@Transient
	private StockOperationTypeProcessor stockOperationTypeProcessor;
	
	public static final String ADJUSTMENT = "adjustment";
	
	public static final String DISPOSED = "disposed";
	
	public static final String TRANSFER_OUT = "transferout";
	
	public static final String INITIAL = "initial";
	
	public static final String RECEIPT = "receipt";
	
	public static final String RETURN = "return";
	
	public static final String STOCK_ISSUE = "stockissue";
	
	public static final String STOCKTAKE = "stocktake";
	
	public static final String REQUISITION = "requisition";
	
	public static final String EXTERNAL_REQUISITION = "erequisition";

	public StockOperationType() {
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setId(Integer stockOperationTypeId) {
		this.id = stockOperationTypeId;
	}
	
	public Set<StockOperationTypeLocationScope> getStockOperationTypeLocationScopes() {
		return stockOperationTypeLocationScopes;
	}
	
	public void setStockOperationTypeLocationScopes(Set<StockOperationTypeLocationScope> stockOperationTypeLocationScopes) {
		this.stockOperationTypeLocationScopes = stockOperationTypeLocationScopes;
	}
	
	public StockOperationTypeLocationScope addStockOperationTypeLocationScopes(
	        StockOperationTypeLocationScope stockOperationTypeLocationScope) {
		getStockOperationTypeLocationScopes().add(stockOperationTypeLocationScope);
		stockOperationTypeLocationScope.setStockOperationType(this);
		
		return stockOperationTypeLocationScope;
	}
	
	public StockOperationTypeLocationScope removeStockOperationTypeLocationScopes(
	        StockOperationTypeLocationScope stockOperationTypeLocationScope) {
		getStockOperationTypeLocationScopes().remove(stockOperationTypeLocationScope);
		stockOperationTypeLocationScope.setStockOperationType(null);
		
		return stockOperationTypeLocationScope;
	}
	
	public Boolean getAvailableWhenReserved() {
		return this.availableWhenReserved;
	}
	
	public void setAvailableWhenReserved(Boolean availableWhenReserved) {
		this.availableWhenReserved = availableWhenReserved;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Boolean getHasDestination() {
		return this.hasDestination;
	}
	
	public void setHasDestination(Boolean hasDestination) {
		this.hasDestination = hasDestination;
	}
	
	public Boolean getHasSource() {
		return this.hasSource;
	}
	
	public void setHasSource(Boolean hasSource) {
		this.hasSource = hasSource;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getOperationType() {
		return this.operationType;
	}
	
	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
	
	public LocationType getSourceType() {
		return sourceType;
	}
	
	public void setSourceType(LocationType sourceType) {
		this.sourceType = sourceType;
	}
	
	public LocationType getDestinationType() {
		return destinationType;
	}
	
	public void setDestinationType(LocationType destinationType) {
		this.destinationType = destinationType;
	}
	
	public String getAcronym() {
		return acronym;
	}
	
	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}
	
	protected StockOperationTypeProcessor getStockOperationTypeProcessor() {
		if (stockOperationTypeProcessor == null) {
			switch (operationType) {
				case ADJUSTMENT:
					stockOperationTypeProcessor = new AdjustmentOperationTypeProcessor(this);
					break;
				case DISPOSED:
					stockOperationTypeProcessor = new DisposedOperationTypeProcessor(this);
					break;
				case TRANSFER_OUT:
					stockOperationTypeProcessor = new TransferOutOperationTypeProcessor(this);
					break;
				case INITIAL:
					stockOperationTypeProcessor = new InitialOperationTypeProcessorProcessor(this);
					break;
				case RECEIPT:
					stockOperationTypeProcessor = new ReceiptOperationTypeProcessor(this);
					break;
				case RETURN:
					stockOperationTypeProcessor = new ReturnOperationTypeProcessorProcessor(this);
					break;
				case STOCK_ISSUE:
					stockOperationTypeProcessor = new StockIssueOperationTypeProcessor(this);
					break;
				case EXTERNAL_REQUISITION:
					stockOperationTypeProcessor = new RequistionOperationTypeProcessor(this);
					break;
				case REQUISITION:
					stockOperationTypeProcessor = new RequistionOperationTypeProcessor(this);
					break;
				case STOCKTAKE:
					stockOperationTypeProcessor = new StockTakeOperationTypeProcessor(this);
					break;
				default:
					throw new RuntimeException("operation type " + operationType + " not supported");
			}
		}
		return stockOperationTypeProcessor;
	}
	
	public boolean userCanProcess(User user, Location location) {
		return getStockOperationTypeProcessor().userCanProcess(user, location);
	}
	
	public boolean userCanProcess(User user, Location location, String privilege) {
		return getStockOperationTypeProcessor().userCanProcess(user, location, privilege);
	}
	
	public boolean requiresReason() {
		return getStockOperationTypeProcessor().requiresReason();
	}
	
	public boolean requiresBatchUuid() {
		return getStockOperationTypeProcessor().requiresBatchUuid();
	}
	
	public boolean requiresActualBatchInformation() {
		return getStockOperationTypeProcessor().requiresActualBatchInformation();
	}
	
	public boolean requiresDispatchAcknowledgement() {
		return getStockOperationTypeProcessor().requiresDispatchAcknowledgement();
	}
	
	public boolean isQuantityOptional() {
		return getStockOperationTypeProcessor().isQuantityOptional();
	}
	
	public boolean canCapturePurchasePrice() {
		return getStockOperationTypeProcessor().canCapturePurchasePrice();
	}
	
	public boolean canBeRelatedToRequisition() {
		return getStockOperationTypeProcessor().canBeRelatedToRequisition();
	}
	
	public boolean shouldVerifyNegativeStockAmountsAtSource() {
		return getStockOperationTypeProcessor().shouldVerifyNegativeStockAmountsAtSource();
	}
	
	public BigDecimal getQuantityToApplyAtSource(BigDecimal quantity) {
		return getStockOperationTypeProcessor().getQuantityToApplyAtSource(quantity);
	}
	
	/**
	 * Called when the {@link StockOperation} status is initially created and the status is
	 * StockOperationStatus.PENDING.
	 * 
	 * @param operation The associated stock operation.
	 */
	public void onPending(StockOperation operation) {
		getStockOperationTypeProcessor().onPending(operation);
	}
	
	/**
	 * Called when the {@link StockOperation} status is changed to StockOperationStatus.CANCELLED.
	 * 
	 * @param operation The associated stock operation.
	 */
	public void onCancelled(StockOperation operation) {
		getStockOperationTypeProcessor().onCancelled(operation);
	}
	
	/**
	 * Called when the {@link StockOperation} status is changed to StockOperationStatus.COMPLETED.
	 * 
	 * @param operation The associated stock operation.
	 */
	public void onCompleted(StockOperation operation) {
		getStockOperationTypeProcessor().onCompleted(operation);
	}
	
	/**
	 * Determines weather or not negative quantities for items are allowed
	 * 
	 * @return true if negative quantities are allowed, else false
	 */
	public boolean isNegativeItemQuantityAllowed() {
		return getStockOperationTypeProcessor().isNegativeItemQuantityAllowed();
	}
	
	public Boolean getNotifySubmitted() {
		return notifySubmitted;
	}
	
	public void setNotifySubmitted(Boolean notifySubmitted) {
		this.notifySubmitted = notifySubmitted;
	}
	
	public Boolean getNotifyApproved() {
		return notifyApproved;
	}
	
	public void setNotifyApproved(Boolean notifyApproved) {
		this.notifyApproved = notifyApproved;
	}
	
	public Boolean getNotifyCompleted() {
		return notifyCompleted;
	}
	
	public void setNotifyCompleted(Boolean notifyCompleted) {
		this.notifyCompleted = notifyCompleted;
	}
	
	public Boolean getNotifyCancelled() {
		return notifyCancelled;
	}
	
	public void setNotifyCancelled(Boolean notifyCancelled) {
		this.notifyCancelled = notifyCancelled;
	}
	
	public Boolean getNotifyRejected() {
		return notifyRejected;
	}
	
	public void setNotifyRejected(Boolean notifyRejected) {
		this.notifyRejected = notifyRejected;
	}
	
	public Boolean getNotifyReturned() {
		return notifyReturned;
	}
	
	public void setNotifyReturned(Boolean notifyReturned) {
		this.notifyReturned = notifyReturned;
	}
	
	public Boolean getNotifyDispatched() {
		return notifyDispatched;
	}
	
	public void setNotifyDispatched(Boolean notifyDispatched) {
		this.notifyDispatched = notifyDispatched;
	}
	
	public Boolean getAllowExpiredBatchNumbers() {
		return allowExpiredBatchNumbers;
	}
	
	public void setAllowExpiredBatchNumbers(Boolean allowExpiredBatchNumbers) {
		this.allowExpiredBatchNumbers = allowExpiredBatchNumbers;
	}
	
	public Boolean getAllowBatchInfoUpdate() {
		return allowBatchInfoUpdate;
	}
	
	public void setAllowBatchInfoUpdate(Boolean allowBatchInfoUpdate) {
		this.allowBatchInfoUpdate = allowBatchInfoUpdate;
	}
}
