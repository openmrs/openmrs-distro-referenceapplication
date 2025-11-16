# Augen Auf - OpenMRS 3 Deployment

Custom OpenMRS 3 distribution for the Augen Auf volunteer project, providing eye care management for doctors in Guatemala.

## Overview

This deployment is based on the OpenMRS 3 Reference Application but has been customized with:
- Removal of demo data and content
- Custom configuration for ophthalmology workflows using the Initializer module
- Custom forms for registration, triage, eye examination, refraction, therapy, pre-surgery, and surgery
- Custom queues, locations, and encounter types specific to eye care

## Quick Start

### Prerequisites
- Podman (or Docker) installed
- Podman Compose plugin

### Build and Start

Use the provided script to build and start the entire stack:

```bash
./build-and-start.sh
```

This will:
- Build the custom backend with your Initializer configuration
- Build the frontend
- Start all services (gateway, frontend, backend, database)

The application will be available at: **http://localhost**

### Clean Up

To completely remove containers, volumes, and images for a fresh start:

```bash
./cleanup.sh
```

**Warning:** This removes all data including the database. Use with caution.

## Custom Configuration

It builds upon the minimal [Reference application configuration](https://github.com/openmrs/openmrs-distro-referenceapplication).

All custom configuration is managed through the **Initializer module** and located in `distro/configuration/`.
This includes custom forms for eye care workflows (registration, triage, eye examination, refraction, therapy, pre-surgery, surgery), concepts, queues, locations, and encounter types.

For details on configuration structure and available options, see the [Initializer documentation](https://github.com/mekomsolutions/openmrs-module-initializer/blob/master/README.md) and [demo content](https://github.com/openmrs/openmrs-content-referenceapplication-demo).

The list of included modules can be found in `distro/distro.properties`.
Note that the demo data module (`referencedemodata`) has been disabled.

## Making Changes

### Modifying Configuration

1. Edit files in `distro/configuration/` directory
2. Rebuild and restart using `./cleanup.sh && ./build-and-start.sh`
3. The Initializer module will automatically load your changes on startup

### Adding New Forms

1. Create a new JSON file in `distro/configuration/ampathforms/`
2. Follow the structure of existing forms
3. Reference concepts defined in `distro/configuration/concepts/`
4. Rebuild and restart

### Adding Modules

1. Edit `distro/distro.properties`
2. Add the module with format: `omod.modulename=${modulename.version}`
3. Define the version in `distro/pom.xml`
4. Rebuild and restart

## Architecture

The deployment consists of four services:

- **Gateway** (Nginx) - Routes requests to frontend and backend
- **Frontend** - OpenMRS 3.x SPA (Single Page Application)
- **Backend** - OpenMRS server with custom modules and configuration
- **Database** - MariaDB 10.11.7 with UTF-8 support

## Docker Compose Files

- **docker-compose.build.yml** - Development deployment that builds images locally

## Environment Variables

You can customize the deployment using these environment variables:

- `TAG` - Image tag to use (default: `qa`)
- `OMRS_DB_USER` - Database username (default: `openmrs`)
- `OMRS_DB_PASSWORD` - Database password (default: `openmrs`)
- `MYSQL_ROOT_PASSWORD` - MySQL root password (default: `openmrs`)

## Development Workflow

1. Make changes to configuration files in `distro/configuration/`
2. Run `./cleanup.sh` to remove existing containers and data
3. Run `./build-and-start.sh` to rebuild and start with new configuration
4. Test your changes at http://localhost
5. Commit changes to the `augen-auf` branch

## Troubleshooting

### View logs
```bash
podman compose logs -f [service-name]
```

### Access backend container
```bash
podman exec -it openmrs-distro-referenceapplication-backend-1 /bin/bash
```

### Check database
```bash
podman exec -it openmrs-distro-referenceapplication-db-1 mysql -u openmrs -popenmrs openmrs
```

### Initializer not loading configuration
- Check backend logs for errors: `podman compose logs backend`
- Verify file formats (CSV files must have proper headers, JSON must be valid)
- Ensure file names follow Initializer conventions

## Resources

### OpenMRS 3 Reference Application
- Repository: https://github.com/openmrs/openmrs-distro-referenceapplication
- Documentation: https://wiki.openmrs.org/display/projects/3.x+Distro

### Initializer Module
- Repository: https://github.com/mekomsolutions/openmrs-module-initializer
- Documentation: https://github.com/mekomsolutions/openmrs-module-initializer/blob/master/README.md

### OpenMRS Documentation
- OpenMRS Wiki: https://wiki.openmrs.org
- REST API Documentation: https://rest.openmrs.org
- Talk Forum: https://talk.openmrs.org

## License

This distribution is based on OpenMRS, which is licensed under the Mozilla Public License 2.0.

## Support

For questions or issues specific to Augen Auf configuration, contact the team.

For OpenMRS platform issues, visit https://talk.openmrs.org
