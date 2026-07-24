import React from 'react';
import { type TabItem } from './types';
import { Tab, TabList, TabPanel, TabPanels, Tabs } from '@carbon/react';
import styles from './vertical-tabs.scss';

interface VerticalTabsProps {
  tabs: TabItem[];
  title?: string;
  hasContainer?: boolean;
  selectedIndex?: number;
  onChange?: (index: number) => void;
}

const VerticalTabs: React.FC<VerticalTabsProps> = ({ tabs, title, hasContainer, selectedIndex, onChange }) => {
  return (
    <div
      className={`
        ${hasContainer ? styles.tabContainer : ''}
        ${styles.cohortBuilder}
      `}
    >
      {title && <p className={styles.heading}>{title}</p>}
      <div className={`${styles.tab} ${styles.verticalTabs}`}>
        <Tabs selectedIndex={selectedIndex} onChange={({ selectedIndex }) => onChange?.(selectedIndex)}>
          <TabList aria-label="navigation">
            {tabs.map((tab: TabItem, index: number) => (
              <Tab key={index} disabled={tab.disabled}>
                {tab.name}
              </Tab>
            ))}
          </TabList>
          <TabPanels>
            {tabs.map((tab: TabItem, index: number) => (
              <TabPanel key={index}>{tab.component}</TabPanel>
            ))}
          </TabPanels>
        </Tabs>
      </div>
    </div>
  );
};

export default VerticalTabs;
