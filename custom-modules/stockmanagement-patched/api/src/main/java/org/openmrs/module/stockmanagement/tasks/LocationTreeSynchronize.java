package org.openmrs.module.stockmanagement.tasks;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.stockmanagement.api.StockManagementService;
import org.openmrs.module.stockmanagement.api.model.LocationTree;

import java.util.ArrayList;
import java.util.List;

public class LocationTreeSynchronize implements StartupTask {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	@Override
    public void execute() {
        log.debug("Checking the synchronization status of the location tree table");
        try {
            StockManagementService stockManagementService = Context.getService(StockManagementService.class);
            List<LocationTree> completeLocationTree = stockManagementService.getCompleteLocationTree();
            List<Location> allLocations = Context.getLocationService().getAllLocations();
            List<LocationTree> expectedTree = generateLocationTree(allLocations);
            List<LocationTree> toInsertNodes = new ArrayList<>();
            for (LocationTree locationTree : expectedTree) {
                LocationTree found = null;
                for (LocationTree dbLocationTree : completeLocationTree) {
                    if (dbLocationTree.getChildLocationId().equals(locationTree.getChildLocationId()) && dbLocationTree.getParentLocationId().equals(locationTree.getParentLocationId()) && locationTree.getDepth() == dbLocationTree.getDepth()) {
                        found = dbLocationTree;
                        break;
                    }
                }
                if (found != null) {
                    completeLocationTree.remove(found);
                } else {
                    toInsertNodes.add(locationTree);
                }
            }

            if (completeLocationTree.size() > 0) {
                log.debug("Deleting invalid nodes");
                stockManagementService.deleteLocationTreeNodes(completeLocationTree);
            }

            if (toInsertNodes.size() > 0) {
                log.debug("Creating missing nodes");
                stockManagementService.saveLocationTreeNodes(toInsertNodes);
            }
        }catch (Exception exception){
            log.error("Error while synchronizing location tree", exception);
        }
    }
	
	private List<LocationTree> generateLocationTree(List<Location> locations){
        List<LocationTree> locationTree = new ArrayList<>();
        for (Location location : locations) {
            LocationTree node = new LocationTree();
            node.setParentLocationId(location.getLocationId());
            node.setChildLocationId(location.getLocationId());
            node.setDepth(0);
            locationTree.add(node);
            addChildNodes(location, location, locations, locationTree, node.getDepth(), 1000);
        }
        return locationTree;
    }
	
	private void addChildNodes(Location currentNode, Location rootNode, List<Location> allLocations, List<LocationTree> tree, int currentDepth, int maxDepth){
        allLocations.stream().filter(p -> p.getParentLocation() != null && p.getParentLocation().getLocationId().equals(currentNode.getLocationId()))
                .forEach(childNode ->{
                    LocationTree node = new LocationTree();
                    node.setParentLocationId(rootNode.getLocationId());
                    node.setChildLocationId(childNode.getLocationId());
                    node.setDepth(currentDepth + 1);
                    tree.add(node);
                    if(node.getDepth() == maxDepth) return;
                    addChildNodes(childNode, rootNode, allLocations, tree, node.getDepth(), maxDepth);
                });
    }
	
	@Override
	public int getPriority() {
		return 100;
	}
}
