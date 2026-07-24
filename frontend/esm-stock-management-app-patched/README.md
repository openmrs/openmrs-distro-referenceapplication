# OpenMRS ESM Stock Management App

![OpenMRS CI](https://github.com/openmrs/openmrs-esm-stock-management/actions/workflows/node.js.yml/badge.svg)

The OpenMRS ESM Stock Management app is designed to help healthcare facilities manage inventory of medical supplies, pharmaceuticals, and other stockable items directly within OpenMRS. It facilitates accurate tracking of stock levels, stock movements, and supply chain operations. It uses the [stock management backend module](https://github.com/openmrs/openmrs-module-stockmanagement).

## Key Features

### Core Operations

- **Stock Management**: Comprehensive tracking of drugs, medical supplies, and commodities across multiple locations
- **Stock Operations**: Complete workflow support including:
  - Stock receipts from suppliers with batch tracking
  - Stock issues to locations/patients with dispatch management
  - Transfers between locations
  - Stock adjustments with reason codes
  - Returns processing with acknowledgments
  - Requisition management and fulfillment
  - Stock takes for physical inventory reconciliation
  - Disposal operations for expired/poor quality items

### Dashboard & Monitoring

- **Real-time Metrics**: Live dashboard showing stock levels, alerts, and key performance indicators
- **Smart Alerts**: Automated notifications for expiring stock (6-month forecasting), out-of-stock items, and disposal requirements
- **Inventory Visibility**: Multi-location stock monitoring with historical movement tracking

### Advanced Management

- **Batch Management**: Full batch number tracking with expiry dates and quality control
- **Packaging Units**: Support for multiple units of measure, pack sizes, and dispensing configurations
- **Stock Rules**: Automated reorder levels, minimum/maximum thresholds, and alert frequencies
- **Bulk Operations**: CSV import capabilities for large-scale stock item setup

### Reporting & Analytics

- **Comprehensive Reports**: 15+ report types including:
  - Stock forecasting and trend analysis
  - Movement history and transaction reports
  - Expiry forecasting and disposal tracking
  - Fulfillment analysis (full/partial/none)
  - Most/least moving items analytics
- **Print Capabilities**: Generate bin cards, stock cards, and operation vouchers

### Administration

- **User Management**: Role-based permissions with location-specific access control
- **Source Management**: Supplier/vendor setup with preferred vendor assignments
- **Location Management**: Hierarchical location setup with inheritance rules
- **Workflow Controls**: Multi-step approval processes for stock operations

### Technical Features

- **Integration**: Deep integration with OpenMRS's drug, location, and encounter models
- **Offline Support**: Operates in both online and offline modes
- **Multi-language**: Support for 40+ languages with internationalization
- **Responsive Design**: Optimized for desktop and tablet interfaces

## Use Cases

- Monitor stock availability at health centers
- Prevent stock-outs by tracking re-order levels
- Manage inbound and outbound inventory for drugs and medical commodities
- Provide real-time stock insights to health workers and supply chain managers

## Requirements

- OpenMRS Platform 2.x or higher
- Admin privileges to install and configure the app

## Installation

1. Install the [stock management backend module](https://github.com/openmrs/openmrs-module-stockmanagement) on your OpenMRS server
2. Add the frontend module to your distribution by following the [O3 configuration, setup and deployment guide](https://openmrs.atlassian.net/wiki/x/YgabAQ)
3. Configure initial settings:
   - Set up locations for stock management
   - Define stock items and sources
   - Configure user roles and permissions

## Configuration

The following configuration options are available for customization:

### Essential Configuration Options

These must be configured to match your OpenMRS concept dictionary. These UUIDs link the stock management system to the appropriate concepts in your OpenMRS installation:

- **`packingUnitsUUID`** - UUID of the concept set containing packaging unit options (e.g., "Box", "Bottle", "Strip"). Used when defining how stock items are packaged for storage and distribution.

- **`dispensingUnitsUUID`** - UUID of the concept set for dispensing units (e.g., "Tablet", "ml", "Capsule"). Used when dispensing stock items to patients or other locations.

- **`stockAdjustmentReasonUUID`** - UUID of the concept set containing reasons for stock adjustments (e.g., "Expired", "Damaged", "Lost", "Found"). Required for tracking why stock levels were manually adjusted.

- **`stockTakeReasonUUID`** - UUID of the concept set for stock take reasons (e.g., "Routine Count", "Audit", "Investigation"). Used during physical inventory reconciliation processes.

- **`stockSourceTypeUUID`** - UUID of the concept set defining types of stock sources (e.g., "Supplier", "Pharmacy", "Ward"). Used to categorize where stock comes from or goes to.

- **`stockItemCategoryUUID`** - UUID of the concept set for categorizing stock items (e.g., "Drug", "Medical Supply", "Laboratory Supply"). Used for organizing and filtering stock items.

```json
{
  "packingUnitsUUID": "bce2b1af-98b1-48a2-98a2-3e4ffb3c79c2",
  "dispensingUnitsUUID": "162402AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
  "stockAdjustmentReasonUUID": "47f0825e-8648-47c2-b847-d3197ed6bb72",
  "stockTakeReasonUUID": "47f0825e-8648-47c2-b847-d3197ed6bb72",
  "stockSourceTypeUUID": "2e1e8049-9cbe-4a2d-b1e5-8a91e5d7d97d",
  "stockItemCategoryUUID": "6d24eb6e-b42f-4706-ab2d-ae4472161f6a"
}
```

### Display & Behavior Options

- **`useItemCommonNameAsDisplay`** _(default: true)_ - When true, displays the common name of stock items in the UI. When false, displays the drug name instead. Useful for sites that prefer clinical vs. common terminology.

- **`autoPopulateResponsiblePerson`** _(default: false)_ - When enabled, automatically fills in the currently logged-in user as the responsible person for stock operations. Saves time for single-user workflows but may not be appropriate for multi-user scenarios.

- **`enablePrintButton`** _(default: true)_ - Controls whether print buttons appear in the stock management interface. Set to false if printing capabilities are not needed or if using custom printing solutions.

- **`printItemCost`** _(default: false)_ - When enabled, includes item costs on printed reports and documents. Enable only if cost information should be visible on printed materials.

- **`printBalanceOnHand`** _(default: false)_ - When enabled, includes current stock balance information on printed reports. Useful for inventory tracking but may not be needed for all report types.

```json
{
  "useItemCommonNameAsDisplay": true,
  "autoPopulateResponsiblePerson": false,
  "enablePrintButton": true,
  "printItemCost": false,
  "printBalanceOnHand": false
}
```

### Organization Branding

Customize the appearance of printed reports and system branding:

- **`logo.src`** - Path or URL to your organization's logo image. Will be displayed on printed reports and system headers.

- **`logo.alt`** _(default: "Logo")_ - Alternative text for the logo image, shown on hover and for accessibility.

- **`logo.name`** - Organization name displayed when the logo image is unavailable or not set.

```json
{
  "logo": {
    "src": "/images/my-organization-logo.png",
    "alt": "My Healthcare Organization Logo",
    "name": "My Healthcare Organization"
  }
}
```

Refer to the [OpenMRS frontend configuration guide](https://openmrs.atlassian.net/wiki/x/nAP-C) for how to apply these settings.

## Documentation

For full documentation, visit the [OpenMRS Wiki - Stock Module](https://openmrs.atlassian.net/wiki/x/zg2bAQ).

## License

This app is released under the [MPL 2.0 license with Healthcare Disclaimer](https://openmrs.org/license).
