import React from 'react';
import StockHomeDetailCards from './stock-home-detail-cards.component';
import StockManagementMetrics from './stock-home-metrics.component';

export default function StockHomeLandingPage() {
  return (
    <div style={{ backgroundColor: 'white' }}>
      <StockManagementMetrics />
      <StockHomeDetailCards />
    </div>
  );
}
