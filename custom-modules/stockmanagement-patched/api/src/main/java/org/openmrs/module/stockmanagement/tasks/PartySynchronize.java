package org.openmrs.module.stockmanagement.tasks;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.stockmanagement.api.StockManagementService;
import org.openmrs.module.stockmanagement.api.model.LocationTree;
import org.openmrs.module.stockmanagement.api.model.Party;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PartySynchronize implements StartupTask {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	@Override
    public void execute() {
        log.debug("Checking the synchronization status of the party table");
        try {
            StockManagementService stockManagementService = Context.getService(StockManagementService.class);
            List<Party> partyList = stockManagementService.findParty(true, false);
            List<Location> locations = Context.getLocationService().getAllLocations();
            int countLocations = 0;
            for(Location location : locations){
                if(!partyList.stream().anyMatch(p -> p.getLocation().getId().equals(location.getLocationId()))){
                    Party party=new Party();
                    party.setLocation(location);
                    party.setDateCreated(new Date());
                    party.setCreator(Context.getAuthenticatedUser());
                    stockManagementService.saveParty(party);
                    countLocations++;
                }
            }

            if (countLocations > 0) {
                log.debug("Created missing parties: " + Integer.toString(countLocations));
            }
        }catch (Exception exception){
            log.error("Error while synchronizing party table", exception);
        }
    }
	
	@Override
	public int getPriority() {
		return 99;
	}
}
