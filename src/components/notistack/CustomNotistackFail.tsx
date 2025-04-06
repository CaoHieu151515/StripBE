/* eslint-disable unused-imports/no-unused-vars */
import { NOTISTACK_DURATION } from '@/utils/constants/shared.constant';
import { SnackbarContent, closeSnackbar } from 'notistack';
import React from 'react';
import XCircleOutlineIcon from '../icons/XCircleOutlineIcon';
import XIcon from '../icons/XIcon';
import { CustomNotiStackProps } from './notistack.type';

type CustomNotistackFailProps = CustomNotiStackProps;

const CustomNotistackFail = React.forwardRef<HTMLDivElement, CustomNotistackFailProps>((props, ref) => {
  const {
    id,
    message,
    allowDownload,
    anchorOrigin,
    iconVariant,
    hideIconVariant,
    persist,
    autoHideDuration,
    action,
    ...other
  } = props;
  return (
    <SnackbarContent ref={ref} role='alert' {...other}>
      <div className='shadow-notiStack flex min-w-[288px] max-w-[464px] items-center gap-5 rounded-lg bg-white p-4'>
        <XCircleOutlineIcon className='size-8 shrink-0 text-red-500' />
        <p className='text-base font-medium leading-6 text-gray-800'>{message}</p>
        {autoHideDuration >= NOTISTACK_DURATION && (
          <div className='h-full'>
            <button className='group' onClick={() => closeSnackbar(id)}>
              <XIcon className='h-5 w-5 shrink-0 text-gray-400 group-hover:text-gray-600' />
            </button>
          </div>
        )}
      </div>
    </SnackbarContent>
  );
});

const CustomNotistackFailMemo = React.memo(CustomNotistackFail);

export default CustomNotistackFailMemo;
