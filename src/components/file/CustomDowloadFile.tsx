import { Button } from 'antd';
import { enqueueSnackbar } from 'notistack';
import { useTranslation } from 'react-i18next';
import { twMerge } from 'tailwind-merge';
import DownloadFileIcon from '../icons/DownloadFileIcon';

type CustomDowloadFileProps = {
  label: string;
  urlDownload: string;
  fileKey: string;
  className: string;
  disabled: boolean;
};

export default function CustomDowloadFile({
  label,
  urlDownload,
  fileKey,
  className,
  disabled,
}: Partial<CustomDowloadFileProps>) {
  const { t: tCommon } = useTranslation('common');

  const handleDownloadFile = async () => {
    if (!urlDownload) {
      enqueueSnackbar({
        message: tCommon('component.download.failed'),
        variant: 'error',
      });
      return;
    }

    try {
      // Fetch the file content
      const response = await fetch(urlDownload);
      if (!response.ok) {
        enqueueSnackbar({
          message: tCommon('component.download.failed'),
          variant: 'error',
        });
      }
      const a = window.document.createElement('a');
      a.setAttribute('href', response.url);
      a.setAttribute('download', fileKey || '');
      a.click();
      window.open(response.url, '_blank');
      URL.revokeObjectURL(response.url);
    } catch (error) {
      console.error('Error downloading file:', (error as Error).message);
    }
  };

  return (
    <div>
      <Button
        onClick={() => handleDownloadFile()}
        type='default'
        className={twMerge(['group flex w-fit justify-center px-4 py-5 text-base font-semibold', className])}
        disabled={disabled}>
        {<DownloadFileIcon className='h-4 w-4 text-gray-500 duration-200 group-hover:text-main-blue' />}
        <span className='text-sm font-normal'>{label ? label : tCommon('button.download')}</span>
      </Button>
    </div>
  );
}
