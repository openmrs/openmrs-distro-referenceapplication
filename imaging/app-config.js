window.config = {
  routerBasename: "/",
  // Internationalization / locale settings
  i18n: {
    defaultLocale: "es",
    supportedLocales: ["es", "en"]
  },
  // Legacy/alternate key some OHIF builds check
  defaultLanguage: "es",
  extensions: [],
  modes: [],
  showStudyList: true,
  maxNumberOfWebWorkers: 3,
  showWarningMessageForCrossOrigin: true,
  showCPUFallbackMessage: true,
  showLoadingIndicator: true,
  strictZSpacingForVolumeViewport: true,
  dataSources: [
    {
      namespace: "@ohif/extension-default.dataSourcesModule.dicomweb",
      sourceName: "dicomweb",
      configuration: {
        friendlyName: "PeruHCE Orthanc",
        name: "orthanc",
        wadoUriRoot: "/wado",
        qidoRoot: "/dicom-web",
        wadoRoot: "/dicom-web",
        qidoSupportsIncludeField: false,
        supportsReject: false,
        imageRendering: "wadors",
        thumbnailRendering: "wadors",
        enableStudyLazyLoad: true,
        supportsFuzzyMatching: false,
        supportsWildcard: true,
        omitQuotationForMultipartRequest: true
      }
    }
  ],
  defaultDataSourceName: "dicomweb"
};
