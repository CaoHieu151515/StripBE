import { SVGProps } from 'react';
import { twMerge } from 'tailwind-merge';

const InformationCircleOutlineIcon = ({ className, ...props }: SVGProps<SVGSVGElement>) => (
  <svg
    className={twMerge('default-icon', className)}
    viewBox='0 0 21 21'
    fill='none'
    xmlns='http://www.w3.org/2000/svg'
    {...props}>
    <path
      d='M11.6028 14.3794H10.6028V10.3794H9.60278M10.6028 6.37939H10.6128M19.6028 10.3794C19.6028 11.5613 19.37 12.7316 18.9177 13.8235C18.4654 14.9155 17.8025 15.9076 16.9667 16.7434C16.131 17.5791 15.1389 18.242 14.0469 18.6943C12.955 19.1466 11.7847 19.3794 10.6028 19.3794C9.42089 19.3794 8.25056 19.1466 7.15863 18.6943C6.0667 18.242 5.07455 17.5791 4.23882 16.7434C3.40309 15.9076 2.74016 14.9155 2.28787 13.8235C1.83557 12.7316 1.60278 11.5613 1.60278 10.3794C1.60278 7.99245 2.55099 5.70326 4.23882 4.01543C5.92665 2.32761 8.21583 1.37939 10.6028 1.37939C12.9897 1.37939 15.2789 2.32761 16.9667 4.01543C18.6546 5.70326 19.6028 7.99245 19.6028 10.3794Z'
      stroke='currentColor'
      strokeWidth='2'
      strokeLinecap='round'
      strokeLinejoin='round'
    />
  </svg>
);
export default InformationCircleOutlineIcon;
