import { SVGProps } from 'react';
import { twMerge } from 'tailwind-merge';

const ChevronDownIcon = ({ className, strokeWidth = '2', ...props }: SVGProps<SVGSVGElement>) => (
  <svg
    className={twMerge('default-icon', className)}
    viewBox='0 0 24 25'
    fill='none'
    xmlns='http://www.w3.org/2000/svg'
    {...props}>
    <path
      d='M19 9.5L12 16.5L5 9.5'
      stroke='currentColor'
      strokeWidth={strokeWidth}
      strokeLinecap='round'
      strokeLinejoin='round'
    />
  </svg>
);
export default ChevronDownIcon;
