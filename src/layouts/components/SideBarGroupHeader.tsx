import ChervonRightIcon from '@/components/icons/ChervonRightIcon';
import { ReactElement, useMemo } from 'react';
import { Link } from 'react-router-dom';
import { twMerge } from 'tailwind-merge';
import { SideBarLinkProps } from './SideBarGroupItem';
import SideBarIcon from './SideBarIcon';

type SideBarGroupHeaderProps = {
  title: string;
  icon: ReactElement;
  isFocused?: boolean;
  showArrow?: boolean;
  expanded?: boolean;
  iconOnly?: boolean;
  buttonProps?: { onClick: () => void };
  linkProps?: SideBarLinkProps;
};

export default function SideBarGroupHeader({
  title,
  icon,
  isFocused = false,
  showArrow = false,
  expanded = false,
  iconOnly = false,
  buttonProps,
  linkProps,
}: SideBarGroupHeaderProps) {
  const containerClassName = useMemo(
    () =>
      twMerge(
        'flex w-full overflow-hidden rounded-lg py-2',
        isFocused ? 'text-primary-500' : 'hover:bg-primary-100 group',
        iconOnly ? 'items-center justify-center px-1.5' : 'justify-between gap-2.5 px-2.5',
      ),
    [iconOnly, isFocused],
  );

  const Icon = () => {
    return (
      <div className='flex h-6 items-center justify-center'>
        <SideBarIcon
          icon={icon}
          className={twMerge(isFocused ? 'text-primary-400' : 'text-gray-400 group-hover:text-gray-800')}
        />
      </div>
    );
  };

  const Content = () => (
    <div className={twMerge('flex w-full gap-4', iconOnly && 'w-fit items-center justify-center')}>
      <Icon />

      {!iconOnly && (
        <div className='flex w-[136px] items-center justify-start gap-2'>
          <span className={twMerge('text-start text-sm font-medium capitalize')}>{title}</span>
        </div>
      )}
    </div>
  );

  if (!showArrow && linkProps)
    return (
      <Link title={title} to={linkProps.to} className={containerClassName} target={linkProps.target ?? '_self'} replace>
        <Content />
      </Link>
    );

  return (
    <button title={title} className={containerClassName} {...buttonProps}>
      <Content />
      {!iconOnly && !!showArrow && (
        <div
          className={twMerge('transition-ease-out-200 flex h-6 items-center justify-center', expanded && 'rotate-180')}>
          <ChervonRightIcon className='size-3 shrink-0 -rotate-90 text-gray-400' />
        </div>
      )}
    </button>
  );
}
