import type React from 'react';

export interface TabItem {
  name: string;
  component: React.ReactElement;
  disabled?: boolean;
}
