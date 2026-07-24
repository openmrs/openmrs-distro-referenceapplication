import { devices, type PlaywrightTestConfig } from '@playwright/test';
import { config as dotenvConfig } from 'dotenv';
import { resolve } from 'node:path';
dotenvConfig({ path: resolve(process.cwd(), '.env') });
dotenvConfig();

// See https://playwright.dev/docs/test-configuration.
const config: PlaywrightTestConfig = {
  testDir: './e2e/specs',
  timeout: 3 * 60 * 1000,
  expect: {
    timeout: 40 * 1000,
  },
  fullyParallel: true,
  forbidOnly: !!process.env.CI,
  retries: 0,
  reporter: process.env.CI ? [['junit', { outputFile: 'results.xml' }], ['html']] : [['html']],
  globalSetup: require.resolve('./e2e/core/global-setup'),
  use: {
    baseURL: `${process.env.E2E_BASE_URL}/spa/stock-management`,
    storageState: 'e2e/storageState.json',
    video: 'retain-on-failure',
  },
  projects: [
    {
      name: 'chromium',
      use: {
        ...devices['Desktop Chrome'],
      },
    },
  ],
};

export default config;
