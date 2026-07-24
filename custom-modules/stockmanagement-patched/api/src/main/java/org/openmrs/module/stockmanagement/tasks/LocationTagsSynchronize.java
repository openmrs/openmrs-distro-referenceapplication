package org.openmrs.module.stockmanagement.tasks;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.LocationTag;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.stockmanagement.StockLocationTags;
import org.openmrs.module.stockmanagement.api.StockManagementService;
import org.openmrs.module.stockmanagement.api.model.Party;

import java.util.*;
import java.util.stream.Collectors;

public class LocationTagsSynchronize implements StartupTask {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	private LocationTag ensureMainStore(LocationService locationService) {
		LocationTag mainStore = locationService.getLocationTagByName(StockLocationTags.MAIN_STORE_LOCATION_TAG);
		if (mainStore == null) {
			log.debug("Created main store tag");
			mainStore = new LocationTag();
			mainStore.setUuid("e539ebca-2899-11ed-bdcb-507b9dea1806");
			mainStore.setName(StockLocationTags.MAIN_STORE_LOCATION_TAG);
			mainStore.setDescription("A location which is the main facility stock holding area");
			mainStore.setDateCreated(new Date());
			mainStore.setCreator(Context.getAuthenticatedUser());
			mainStore = locationService.saveLocationTag(mainStore);
			
		}
		return mainStore;
	}
	
	private LocationTag ensureMainPharmacy(LocationService locationService) {
		LocationTag mainPharmacy = locationService.getLocationTagByName(StockLocationTags.MAIN_PHARMACY_LOCATION_TAG);
		if (mainPharmacy == null) {
			log.debug("Created main pharmacy tag");
			mainPharmacy = new LocationTag();
			mainPharmacy.setUuid("89a80c4d-2899-11ed-bdcb-507b9dea1806");
			mainPharmacy.setName(StockLocationTags.MAIN_PHARMACY_LOCATION_TAG);
			mainPharmacy.setDescription("Main pharmacy location.");
			mainPharmacy.setDateCreated(new Date());
			mainPharmacy.setCreator(Context.getAuthenticatedUser());
			mainPharmacy = locationService.saveLocationTag(mainPharmacy);
		}
		return mainPharmacy;
	}
	
	private LocationTag ensureDispensary(LocationService locationService) {
		LocationTag dispensary = locationService.getLocationTagByName(StockLocationTags.DISPENSARY_LOCATION_TAG);
		if (dispensary == null) {
			log.debug("Created dispensary tag");
			dispensary = new LocationTag();
			dispensary.setUuid("fe7c970f-2aba-11ed-ba4a-507b9dea1806");
			dispensary.setName(StockLocationTags.DISPENSARY_LOCATION_TAG);
			dispensary.setDescription("A dispensary.");
			dispensary.setDateCreated(new Date());
			dispensary.setCreator(Context.getAuthenticatedUser());
			dispensary = locationService.saveLocationTag(dispensary);
		}
		return dispensary;
	}
	
	private Location getSuitableParentLocation(List<Location> locations){
        Map<Optional<Location>, List<Location>> parents = locations.stream().collect(Collectors.groupingBy(p -> Optional.ofNullable(p.getParentLocation())));
        if(parents.isEmpty()) return null;
        ArrayList<List<Location>> locationGroups = new ArrayList<>(parents.values());
        locationGroups.sort((x, y) -> Integer.compare(y.size(),x.size()));
        return locationGroups.get(0).get(0).getParentLocation();
    }
	
	@Override
    public void execute() {
        log.debug("Checking the synchronization status of the location tags for main store, main pharmacy, dispensary");
        try {
            LocationService locationService = Context.getLocationService();
            LocationTag mainStoreTag = ensureMainStore(locationService);
            LocationTag mainPharmacyTag = ensureMainPharmacy(locationService);
            ensureDispensary(locationService);

            Location pharmacy = null;
            Location mainStore = null;

            List<Location> locationsWithTag = locationService.getLocationsHavingAllTags(Arrays.asList(mainStoreTag));
            if(!locationsWithTag.isEmpty()){
                Optional<Location> tempLocation = locationsWithTag.stream().filter(p->p.getRetired() == null || !p.getRetired()).findFirst();
                if(tempLocation.isPresent()){
                    mainStore = tempLocation.get();
                }else{
                    mainStore = locationsWithTag.get(0);
                }
            }

            locationsWithTag = locationService.getLocationsHavingAllTags(Arrays.asList(mainPharmacyTag));
            if(!locationsWithTag.isEmpty()){
                Optional<Location> tempLocation = locationsWithTag.stream().filter(p->p.getRetired() == null || !p.getRetired()).findFirst();
                if(tempLocation.isPresent()){
                    pharmacy = tempLocation.get();
                }else{
                    pharmacy = locationsWithTag.get(0);
                }
            }

            List<Location> locations = locationService.getAllLocations(true);
            for(Location location : locations){
                if(pharmacy == null){
                    if(location.getName().toLowerCase().indexOf("pharmacy") >= 0){
                        pharmacy = location;
                    }
                }

                if(mainStore == null){
                    String locationName =location.getName().toLowerCase();
                    if(locationName.indexOf("main") >= 0 && locationName.indexOf("store") >= 0){
                        mainStore = location;
                    }
                }

                if(pharmacy != null && mainStore != null){
                    break;
                }
            }

            if(mainStore == null){
                mainStore = locationService.getLocationByUuid("19dbe2c0-289d-11ed-bdcb-507b9dea1806");
            }

            if(pharmacy == null){
                pharmacy = locationService.getLocationByUuid("7f65d926-57d6-4402-ae10-a5b3bcbf7986");
            }

            Location parentLocation = null;
            if(mainStore == null){
                log.debug("Creating main store location");
                parentLocation = getSuitableParentLocation(locations);
                mainStore=new Location();
                mainStore.setName("Main Store");
                mainStore.setCreator(Context.getAuthenticatedUser());
                mainStore.setDateCreated(new Date());
                mainStore.setParentLocation(parentLocation);
                mainStore.setUuid("19dbe2c0-289d-11ed-bdcb-507b9dea1806");
                mainStore = locationService.saveLocation(mainStore);
            }

            if(pharmacy == null){
                log.debug("Creating main pharmacy location");
                if(parentLocation == null){
                    parentLocation = getSuitableParentLocation(locations);
                }
                pharmacy=new Location();
                pharmacy.setName("Main Pharmacy");
                pharmacy.setCreator(Context.getAuthenticatedUser());
                pharmacy.setDateCreated(new Date());
                pharmacy.setParentLocation(parentLocation);
                pharmacy.setUuid("7f65d926-57d6-4402-ae10-a5b3bcbf7986");
                pharmacy = locationService.saveLocation(pharmacy);
            }

            Set<LocationTag> locationTags = mainStore.getTags();
            if(locationTags == null || !locationTags.contains(mainStoreTag)){
                log.debug("Taging main store location");
                mainStore.addTag(mainStoreTag);
                locationService.saveLocation(mainStore);
            }

            locationTags = pharmacy.getTags();
            if(locationTags == null || !locationTags.contains(mainStoreTag)){
                log.debug("Taging main pharmacy location");
                pharmacy.addTag(mainPharmacyTag);
                locationService.saveLocation(pharmacy);
            }

        }catch (Exception exception){
            log.error("Error while synchronizing location and tags", exception);
        }
    }
	
	@Override
	public int getPriority() {
		return 90;
	}
}
