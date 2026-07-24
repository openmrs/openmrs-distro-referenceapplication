import React from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { useLeftNav } from '@openmrs/esm-framework';
import Dashboard from './dashboard/home-dashboard.component';

const Root: React.FC = () => {
  const spaBasePath = `${window.spaBase}/stock-management`;

  useLeftNav({
    name: 'stock-page-dashboard-slot',
    basePath: spaBasePath,
  });

  return (
    <main className="omrs-main-content">
      <BrowserRouter basename={window.spaBase}>
        <Routes>
          <Route path="/stock-management" element={<Dashboard />} />
          <Route path="/stock-management/:dashboard/*" element={<Dashboard />} />
        </Routes>
      </BrowserRouter>
    </main>
  );
};

export default Root;
