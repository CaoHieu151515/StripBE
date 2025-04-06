import { saveBlob } from '@/helpers/handle-file/DownloadPDF';
import { getErrorMessage } from '@/utils/common.helper';
import { STORAGE } from '@/utils/constants/shared.constant';
import { HTTP_METHOD } from '@/utils/enum/http-method.enum';
import Cookie from 'js-cookie';
import { enqueueSnackbar } from 'notistack';
import { useState } from 'react';

type DownloadExcelOptions = {
  endpoint: string;
  method?: HTTP_METHOD;
  params?: Record<string, any>;
  fileName: string;
  fileType: string;
  successMessage?: string;
  errorMessage?: string;
};

export const useDownloadExcel = () => {
  const [isDownloading, setIsDownloading] = useState(false);

  const downloadExcel = async ({
    endpoint,
    method = HTTP_METHOD.GET,
    params,
    fileName,
    fileType,
    successMessage,
    errorMessage,
  }: DownloadExcelOptions) => {
    setIsDownloading(true);

    try {
      const queryParams = new URLSearchParams(params).toString();
      const apiEndpoint = `${endpoint}${queryParams ? `?${queryParams}` : ''}`;
      const accessToken = Cookie.get(STORAGE.ACCESS_TOKEN);

      const response = await fetch(apiEndpoint, {
        method,
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      });

      if (response.ok) {
        const blob = await response.blob();
        saveBlob(blob, fileType, fileName, true);
        enqueueSnackbar({ message: successMessage, variant: 'success' });
      } else {
        enqueueSnackbar({ message: errorMessage, variant: 'error' });
      }
    } catch (error) {
      enqueueSnackbar({ message: getErrorMessage(error), variant: 'error' });
    } finally {
      setIsDownloading(false);
    }
  };

  return { isDownloading, downloadExcel };
};
