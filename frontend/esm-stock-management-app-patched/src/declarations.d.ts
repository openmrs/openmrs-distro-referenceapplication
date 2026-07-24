declare module '*.css';
declare module '*.scss';
declare module '*.png';

declare interface RequireContext {
  keys(): string[];
  (id: string): unknown;
  <T>(id: string): T;
  resolve(id: string): string;
  id: string;
}

declare namespace NodeJS {
  interface Require {
    context(directory: string, useSubdirectories?: boolean, regExp?: RegExp, mode?: string): RequireContext;
  }
}
