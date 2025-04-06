import { SVGProps } from 'react';
import { twMerge } from 'tailwind-merge';

const CirclePlusIcon = ({ className, ...props }: SVGProps<SVGSVGElement>) => (
  <svg
    className={twMerge('default-icon', className)}
    viewBox='0 0 20 20'
    fill='none'
    xmlns='http://www.w3.org/2000/svg'
    {...props}>
    <path
      d='M9.99992 5.67847V14.3216M5.67834 10H14.3215M19.1666 10C19.1666 15.0627 15.0625 19.1667 9.99992 19.1667C4.93731 19.1667 0.833252 15.0627 0.833252 10C0.833252 4.93743 4.93731 0.833374 9.99992 0.833374C15.0625 0.833374 19.1666 4.93743 19.1666 10Z'
      stroke='currentColor'
      strokeWidth='1.5'
      strokeLinecap='round'
      strokeLinejoin='round'
    />
  </svg>
);
export default CirclePlusIcon;
