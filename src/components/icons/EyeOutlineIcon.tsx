import { SVGProps } from 'react';
import { twMerge } from 'tailwind-merge';

const EyeOutlineIcon = ({ className, ...props }: SVGProps<SVGSVGElement>) => (
  <svg
    width='20'
    height='16'
    viewBox='0 0 20 16'
    fill='none'
    xmlns='http://www.w3.org/2000/svg'
    className={twMerge('default-icon', className)}
    {...props}>
    <path
      d='M1.36053 8.27014C1.29948 8.08697 1.29942 7.88866 1.36036 7.70545C2.58734 4.01692 6.06674 1.35706 10.1674 1.35706C14.2661 1.35706 17.7442 4.01444 18.9727 7.7003C19.0337 7.88348 19.0338 8.08179 18.9729 8.265C17.7459 11.9535 14.2665 14.6134 10.1658 14.6134C6.0671 14.6134 2.58903 11.956 1.36053 8.27014Z'
      stroke='currentColor'
      strokeWidth='1.32563'
      strokeLinecap='round'
      strokeLinejoin='round'
    />
    <path
      d='M12.8179 7.98522C12.8179 9.44948 11.6309 10.6365 10.1667 10.6365C8.70241 10.6365 7.5154 9.44948 7.5154 7.98522C7.5154 6.52097 8.70241 5.33396 10.1667 5.33396C11.6309 5.33396 12.8179 6.52097 12.8179 7.98522Z'
      stroke='currentColor'
      strokeWidth='1.32563'
      strokeLinecap='round'
      strokeLinejoin='round'
    />
  </svg>
);
export default EyeOutlineIcon;
