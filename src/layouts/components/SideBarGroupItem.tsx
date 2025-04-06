import { HTMLAttributeAnchorTarget, ReactElement, useMemo } from 'react';
import { Link } from 'react-router-dom';
import { twMerge } from 'tailwind-merge';
import SideBarIcon from './SideBarIcon';

export type SideBarLinkProps = { target?: HTMLAttributeAnchorTarget; to: string };

type SideBarGroupItemProps = {
  title: string;
  icon: ReactElement;
  isFocused?: boolean;
  linkProps: SideBarLinkProps;
};

export default function SideBarGroupItem({ title, icon, isFocused = false, linkProps }: SideBarGroupItemProps) {
  const className = useMemo<string>(() => (isFocused ? 'font-bold text-main-blue' : ''), [isFocused]);

  return (
    <Link
      title={title}
      className='group flex min-h-[42px] w-full items-center justify-start gap-3 overflow-hidden rounded-lg py-[9px] pl-7 pr-[23px] hover:bg-gray-100'
      to={linkProps.to}
      target={linkProps.target ?? '_self'}
      replace>
      <div className='flex h-6 w-6 items-start justify-center'>
        <SideBarIcon
          icon={icon}
          className={twMerge(className, isFocused ? 'text-main-blue' : 'text-gray-400 group-hover:text-gray-800')}
        />
      </div>

      <p className={twMerge('w-[124px] text-start text-sm font-medium capitalize', className)}>{title}</p>
    </Link>
  );
}
