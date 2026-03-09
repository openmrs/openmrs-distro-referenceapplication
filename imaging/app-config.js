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
        wadoUriRoot: "http://hii1sc-dev.inf.pucp.edu.pe:8043/wado",
        qidoRoot: "http://hii1sc-dev.inf.pucp.edu.pe:8043/dicom-web",
        wadoRoot: "http://hii1sc-dev.inf.pucp.edu.pe:8043/dicom-web",
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
