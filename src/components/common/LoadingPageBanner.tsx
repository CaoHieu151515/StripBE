import { LoadingOutlined } from '@ant-design/icons';
import { Spin } from 'antd';
import { HTMLAttributes } from 'react';
import { twMerge } from 'tailwind-merge';

type LoadingPageBannerProps = Pick<HTMLAttributes<HTMLDivElement>, 'className'> & {
  title?: string;
  message?: string;
  disabledFullScreen?: boolean;
  size?: 'small' | 'default' | 'large';
};

export default function LoadingPageBanner({
  title,
  message,
  className,
  disabledFullScreen = false,
  size = 'default',
}: LoadingPageBannerProps) {
  return (
    <div
      className={twMerge(
        'flex flex-col items-center justify-center',
        disabledFullScreen ? 'my-14' : 'h-screen w-screen bg-white',
        className,
      )}>
      <Spin indicator={<LoadingOutlined spin className='text-primary-500' />} size={size} />
      {title && <div className='mt-24 text-xl font-semibold'>{title}</div>}

      {message && <div className='mt-10 text-sm font-normal'>{message}</div>}
    </div>
  );
}
