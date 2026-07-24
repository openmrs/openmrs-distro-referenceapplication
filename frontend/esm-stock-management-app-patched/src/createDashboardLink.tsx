import React, { useMemo } from 'react';
import { ConfigurableLink } from '@openmrs/esm-framework';
import { BrowserRouter, useLocation } from 'react-router-dom';

export interface DashboardLinkConfig {
  name: string;
  title: string;
}

function DashboardExtension({ dashboardLinkConfig }: { dashboardLinkConfig: DashboardLinkConfig }) {
  const { name, title } = dashboardLinkConfig;
  const location = useLocation();
  const spaBasePath = `${window.spaBase}/stock-management`;

  const navLink = useMemo(() => {
    const pathArray = location.pathname.split('/');
    const lastElement = pathArray[pathArray.length - 1];
    return decodeURIComponent(lastElement);
  }, [location.pathname]);

  return (
    <ConfigurableLink
      to={`${spaBasePath}/${name}`}
      className={`cds--side-nav__link ${navLink.match(name) && 'active-left-nav-link'}`}
    >
      {title}
    </ConfigurableLink>
  );
}

export const createDashboardLink = (dashboardLinkConfig: DashboardLinkConfig) => () =>
  (
    <BrowserRouter>
      <DashboardExtension dashboardLinkConfig={dashboardLinkConfig} />
    </BrowserRouter>
  );
