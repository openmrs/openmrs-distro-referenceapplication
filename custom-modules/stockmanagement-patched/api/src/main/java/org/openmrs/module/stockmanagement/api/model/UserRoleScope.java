package org.openmrs.module.stockmanagement.api.model;

import org.openmrs.Location;
import org.openmrs.Role;
import org.openmrs.User;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

/**
 * The persistent class for the stockmgmt_user_role_scope database table.
 */
@Entity(name = "stockmanagement.UserRoleScope")
@Table(name = "stockmgmt_user_role_scope")
public class UserRoleScope extends org.openmrs.BaseChangeableOpenmrsData implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_role_scope_id")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role")
	private Role role;
	
	@Column(name = "is_permanent")
	private boolean permanent;
	
	@Column(name = "active_from")
	private Date activeFrom;
	
	@Column(name = "active_to")
	private Date activeTo;
	
	@Column(name = "enabled")
	private boolean enabled;
	
	@OneToMany(mappedBy = "userRoleScope")
	private Set<UserRoleScopeLocation> userRoleScopeLocations;
	
	@OneToMany(mappedBy = "userRoleScope")
	private Set<UserRoleScopeOperationType> userRoleScopeOperationTypes;
	
	public UserRoleScope() {
		permanent = true;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public Role getRole() {
		return role;
	}
	
	public void setRole(Role role) {
		this.role = role;
	}
	
	public boolean getPermanent() {
		return permanent;
	}
	
	public void setPermanent(Boolean permanent) {
		this.permanent = permanent;
	}
	
	public Date getActiveFrom() {
		return activeFrom;
	}
	
	public void setActiveFrom(Date activeFrom) {
		this.activeFrom = activeFrom;
	}
	
	public Date getActiveTo() {
		return activeTo;
	}
	
	public void setActiveTo(Date activeTo) {
		this.activeTo = activeTo;
	}
	
	public boolean getEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public Set<UserRoleScopeLocation> getUserRoleScopeLocations() {
		return this.userRoleScopeLocations;
	}
	
	public void setUserRoleScopeLocations(Set<UserRoleScopeLocation> userRoleScopeLocations) {
		this.userRoleScopeLocations = userRoleScopeLocations;
	}
	
	public UserRoleScopeLocation addUserRoleScopeLocation(UserRoleScopeLocation userRoleScopeLocation) {
		getUserRoleScopeLocations().add(userRoleScopeLocation);
		userRoleScopeLocation.setUserRoleScope(this);
		
		return userRoleScopeLocation;
	}
	
	public UserRoleScopeLocation removeUserRoleScopeLocation(UserRoleScopeLocation userRoleScopeLocation) {
		getUserRoleScopeLocations().remove(userRoleScopeLocation);
		userRoleScopeLocation.setUserRoleScope(null);
		
		return userRoleScopeLocation;
	}
	
	public Set<UserRoleScopeOperationType> getUserRoleScopeOperationTypes() {
		return this.userRoleScopeOperationTypes;
	}
	
	public void setUserRoleScopeOperationTypes(Set<UserRoleScopeOperationType> userRoleScopeOperationTypes) {
		this.userRoleScopeOperationTypes = userRoleScopeOperationTypes;
	}
	
	public UserRoleScopeOperationType addUserRoleScopeOperationType(UserRoleScopeOperationType userRoleScopeOperationType) {
		getUserRoleScopeOperationTypes().add(userRoleScopeOperationType);
		userRoleScopeOperationType.setUserRoleScope(this);
		
		return userRoleScopeOperationType;
	}
	
	public UserRoleScopeOperationType removeUserRoleScopeOperationType(UserRoleScopeOperationType userRoleScopeOperationType) {
		getUserRoleScopeOperationTypes().remove(userRoleScopeOperationType);
		userRoleScopeOperationType.setUserRoleScope(null);
		
		return userRoleScopeOperationType;
	}
}
