import React from 'react';
import { parseParametersToMap } from '../../core/api/types/BatchJob';
interface StockReportParametersProps {
  model: any;
}

const StockReportParameters = (props: StockReportParametersProps) => {
  let parameterMap: React.ReactNode;

  const displayParameterMap = (
    batchJobUuid: string,
    parameterMap: { [key: string]: { [key: string]: string } } | null,
  ): React.ReactNode => {
    if (!parameterMap) {
      return null;
    }
    const objectKeys: string[] = Object.keys(parameterMap);
    if (objectKeys.length === 0) {
      return null;
    }
    return objectKeys.map((key, index) => {
      const displayField: string = parameterMap[key]['description'] ?? key;
      const displayValue: string = parameterMap[key]['display'] ?? parameterMap[key]['value'];
      return (
        <div key={`${batchJobUuid}-param-${index}`}>
          {displayField}: {displayValue}
        </div>
      );
    });
  };

  try {
    parameterMap = displayParameterMap(
      props.model?.uuid,
      parseParametersToMap(props.model?.parameters, ['param.report']),
    );
  } catch (ex) {
    console.error(ex);
  }

  return <span>{parameterMap}</span>;
};

export default StockReportParameters;
