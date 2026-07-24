import React from 'react';
import { useParams } from 'react-router-dom';
import { useLayoutType, isDesktop, useExtensionStore, ExtensionSlot, WorkspaceContainer } from '@openmrs/esm-framework';
import { type DashboardConfig } from '../types';
import DashboardView from './dashboard-view.component';
import styles from './home-dashboard.scss';

export default function Dashboard() {
  const params = useParams();
  const extensionStore = useExtensionStore();
  const layout = useLayoutType();
  const ungroupedDashboards =
    extensionStore.slots['stock-page-dashboard-slot']?.assignedExtensions
      .map((e) => e.meta)
      .filter((e) => Object.keys(e).length) || [];
  const dashboards = ungroupedDashboards as Array<DashboardConfig>;
  const activeDashboard = dashboards.find((dashboard) => dashboard.name === params?.dashboard) || dashboards[0];

  return (
    <div className={styles.homePageWrapper}>
      <section className={isDesktop(layout) ? styles.dashboardContainer : styles.dashboardContainerTablet}>
        {isDesktop(layout) && <ExtensionSlot name="stock-sidebar-slot" key={layout} />}
        <DashboardView title={activeDashboard?.name} dashboardSlot={activeDashboard?.slot} />
      </section>
      <WorkspaceContainer overlay contextKey="stock-management" />
    </div>
  );
}
