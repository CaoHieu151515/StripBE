import { ButtonHTMLAttributes } from 'react';
import { twMerge } from 'tailwind-merge';
import EyeOutlineIcon from '../icons/EyeOutlineIcon';

type CustomTableActionsButtonProps = ButtonHTMLAttributes<HTMLButtonElement> & {
  label: string;
  labelClassName?: string;
};

export default function CustomTableActionsButton({
  label,
  labelClassName,
  className,
  ...props
}: CustomTableActionsButtonProps) {
  return (
    <button
      className={twMerge(
        'pack-bg-white-gray900 flex items-center justify-center overflow-hidden rounded-lg border shadow-[0_1px_4px_0_rgba(0,0,0,0.25)] hover:border-gray-400 max-lg:w-full dark:hover:border-gray-500',
        className,
      )}
      {...props}>
      <div className='flex w-full items-center justify-center gap-2 p-2'>
        {/* <span
          className={twMerge(
            'pack-text-gray700-gray400 text-sm font-medium leading-none max-sm:text-xs',
            labelClassName,
          )}>
          {label}
        </span> */}
        <EyeOutlineIcon className='size-5 h-4 w-4 shrink-0 cursor-pointer text-gray-800 max-sm:h-4 max-sm:w-4' />
      </div>
    </button>
  );
}
