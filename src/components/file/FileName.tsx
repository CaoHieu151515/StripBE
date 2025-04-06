import { ImgHTMLAttributes, useMemo, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { twJoin } from 'tailwind-merge';
import FilePenOutlineIcon from '../icons/PenOutlineIcon';
import XCircleOutlineIcon from '../icons/XCircleOutlineIcon';
import CustomConfirmModal from '../modal/CustomConfirmModal';

type FileNameProps = ImgHTMLAttributes<HTMLImageElement> & {
  name: string;
  percentage?: number; // from 0 to 100
  onDelete: () => void;
};

export function FileName({ name, percentage = 0, onDelete }: FileNameProps) {
  const { t: tCommon } = useTranslation('common');

  const [openDeleteModal, setOpenDeleteModal] = useState(false);
  const isReady = useMemo(() => percentage === 100, [percentage]);

  return (
    <div className='flex w-full items-center justify-between gap-10 overflow-hidden'>
      <div className='flex w-full items-start gap-4 overflow-hidden'>
        <FilePenOutlineIcon className='h-10 w-10 text-gray-300 dark:text-gray-700' />

        <div className='flex w-full flex-col gap-3 overflow-hidden'>
          <div className='flex items-center gap-2'>
            <span className='pack-text-gray900-white no-translate truncate text-base font-medium'>{name}</span>
            {isReady && (
              <button onClick={() => setOpenDeleteModal(true)} type='button'>
                <XCircleOutlineIcon className='h-2.5 w-2.5 text-gray-300 hover:text-primary-700 dark:text-gray-700 dark:hover:text-primary-500' />
              </button>
            )}
          </div>

          <div
            className={twJoin(
              'h-[6px] w-full overflow-hidden rounded-sm bg-gray-200',
              isReady ? 'invisible' : 'visible',
            )}>
            <div
              className='h-full rounded-sm bg-primary-700 transition-all dark:bg-primary-500'
              style={{ width: `${percentage}%` }}></div>
          </div>
        </div>
      </div>

      <CustomConfirmModal
        open={openDeleteModal}
        setOpen={setOpenDeleteModal}
        title={tCommon('component.uploader.sure_delete_file')}
        message={tCommon('component.uploader.delete_file_message')}
        onConfirm={onDelete}
      />
    </div>
  );
}
