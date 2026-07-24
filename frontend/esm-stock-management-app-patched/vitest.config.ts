import { fileURLToPath } from 'node:url';
import { defineConfig } from 'vitest/config';

const r = (relativePath: string) => fileURLToPath(new URL(relativePath, import.meta.url));

export default defineConfig({
  resolve: {
    alias: [{ find: /^.*\.s?css$/, replacement: 'identity-obj-proxy' }],
    mainFields: ['module', 'main'],
  },
  test: {
    environment: 'jsdom',
    clearMocks: true,
    testTimeout: 30000,
    setupFiles: ['./tools/setup-tests.ts'],
    exclude: ['**/node_modules/**', '**/e2e/**', '**/dist/**'],
    server: {
      deps: {
        inline: [/@openmrs/],
      },
    },
    fakeTimers: {
      toFake: [
        'setTimeout',
        'clearTimeout',
        'setInterval',
        'clearInterval',
        'setImmediate',
        'clearImmediate',
        'requestAnimationFrame',
        'cancelAnimationFrame',
        'Date',
      ],
    },
    alias: {
      '@openmrs/esm-framework/src/internal': '@openmrs/esm-framework/mock',
      '@openmrs/esm-framework': '@openmrs/esm-framework/mock',
      'react-i18next': r('./__mocks__/react-i18next.js'),
      '@mocks': r('./__mocks__/index.ts'),
    },
  },
});
