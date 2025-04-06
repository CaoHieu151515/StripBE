import ChervonRightIcon from '@/components/icons/ChervonRightIcon';
import { useRef } from 'react';
import { useLocation } from 'react-router-dom';
import { twJoin, twMerge } from 'tailwind-merge';
import { SideBarGroupTemplate } from '../types/SideBarGroupTemplate.type';
import SideBarGroup from './SideBarGroup';

type SideBarProps = {
  className?: string;
  open: boolean;
  groups: SideBarGroupTemplate[];
  focusedTitles: [string | undefined, string | undefined];
  onChangeOpen: () => void;
  alwaysOpen?: boolean;
};

export default function SideBar({
  className,
  open,
  groups,
  focusedTitles,
  onChangeOpen,
  alwaysOpen = false,
}: SideBarProps) {
  const sidebarRef = useRef<HTMLDivElement | null>(null);
  const location = useLocation();

  return (
    <div
      ref={sidebarRef}
      className={twMerge(
        'no-scrollbar fixed z-[900] flex h-full items-start justify-center overflow-y-scroll bg-white px-1 pt-3',
        className,
        open ? 'w-sidebar' : 'w-sidebar-collapsed',
        alwaysOpen
          ? 'custom-shadow-sidebar'
          : 'shadow-[0_4.66px_6.99px_0_rgba(0,0,0,0.05),2.93px_11.64px_17.47px_-3.49px_rgba(0,0,0,0.33)]',
      )}
      onMouseLeave={() => {
        if (!alwaysOpen && open) {
          setTimeout(() => {
            onChangeOpen();
          }, 200);
        }
      }}>
      <div className='flex w-full flex-col gap-7 pb-[42px]'>
        {!alwaysOpen && (
          <div className='mx-auto block'>
            <button
              onClick={onChangeOpen}
              className='grid h-9 w-10 shrink-0 place-items-center rounded-lg bg-gray-100 bg-opacity-75 enabled:hover:bg-gray-200 enabled:hover:bg-opacity-80'>
              <ChervonRightIcon
                className={twJoin('transition-ease-out-200 h-6 w-auto text-gray-500', open && 'rotate-180')}
              />
            </button>
          </div>
        )}

        <div className='flex flex-col gap-[15px]'>
          {groups.map((group, index) => (
            <div key={index}>
              <SideBarGroup
                group={group}
                isFocused={focusedTitles[0] === group.title && location.pathname.includes(group.title)}
                focusedItemTitle={focusedTitles[1] ?? focusedTitles[0]}
                defaultExpanded
                iconOnly={!open}
                onChangeOpen={onChangeOpen}
              />
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
