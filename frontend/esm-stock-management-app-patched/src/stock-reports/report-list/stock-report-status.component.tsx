import React from 'react';
import styles from './stock-reports.scss';
import { formatDate, parseDate } from '@openmrs/esm-framework';
import { BatchJobStatusCancelled, parseParametersToMap } from '../../core/api/types/BatchJob';
import { useTranslation } from 'react-i18next';

interface StockReportStatusProps {
  model: any;
}

const StockReportStatus = (props: StockReportStatusProps) => {
  const { t } = useTranslation();
  let executionStateMap: React.ReactNode;

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
    executionStateMap = displayParameterMap(props.model?.uuid, parseParametersToMap(props.model?.executionState));
  } catch (ex) {
    console.error(ex);
  }
  return (
    <div className={styles.statusContainer}>
      {/* cancelled */}
      {props.model?.status === BatchJobStatusCancelled && (
        <div>
          <span className={styles.textHeading}>{t('cancelled', 'Cancelled')}:</span>
          <div className={styles.statusDescriptions}>
            <span className={styles.text}>
              {formatDate(parseDate(props.model?.cancelledDate.toString()), {
                time: true,
                mode: 'standard',
              })}
              <p>{props.model?.cancelReason}</p>
            </span>
          </div>
        </div>
      )}
      {/* start time */}
      {props.model?.startTime && (
        <div>
          <span className={styles.textHeading}>{t('started', 'Started')}:</span>
          <div className={styles.statusDescriptions}>
            <span className={styles.text}>
              {formatDate(parseDate(props.model?.startTime.toString()), {
                time: true,
                mode: 'standard',
              })}
            </span>
          </div>
        </div>
      )}
      {/* endTime */}
      {props.model?.endTime && (
        <div>
          <span className={styles.textHeading}>{t('ended', 'Ended')}:</span>
          <div className={styles.statusDescriptions}>
            <span className={styles.text}>
              {formatDate(parseDate(props.model?.endTime.toString()), {
                time: true,
                mode: 'standard',
              })}
            </span>
          </div>
        </div>
      )}
      {/* expiration */}
      {props.model?.expiration && (
        <div>
          <span className={styles.textHeading}>{t('expires', 'Expires')}:</span>
          <div className={styles.statusDescriptions}>
            <span className={styles.text}>
              {formatDate(parseDate(props.model?.expiration.toString()), {
                time: true,
                mode: 'standard',
              })}
            </span>
          </div>
        </div>
      )}
      {/* executionMap */}
      {executionStateMap && (
        <div>
          <span className={styles.textHeading}>{t('executionState', 'Execution State')}:</span>
          <div className={styles.statusDescriptions}>
            <span className={styles.text}>{executionStateMap}</span>
          </div>
        </div>
      )}
      {/* completedDate */}
      {props.model?.completedDate && (
        <div>
          <span className={styles.textHeading}>{t('completed', 'Completed')}:</span>
          <div className={styles.statusDescriptions}>
            <span className={styles.text}>
              {formatDate(parseDate(props.model?.completedDate.toString()), {
                time: true,
                mode: 'standard',
              })}
            </span>
          </div>
        </div>
      )}
      {/* exitMessage */}
      {props.model?.exitMessage && (
        <div>
          <span className={styles.textHeading}>{t('exitMessage', 'Message')}:</span>
          <div className={styles.statusDescriptions}>
            <span className={styles.text}>
              <p>{props.model?.exitMessage}</p>
            </span>
          </div>
        </div>
      )}
    </div>
  );
};

export default StockReportStatus;
