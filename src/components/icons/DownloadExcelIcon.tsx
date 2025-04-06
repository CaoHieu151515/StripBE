import { SVGProps } from 'react';
import { twMerge } from 'tailwind-merge';

const DownloadExcelIcon = ({ className, ...props }: SVGProps<SVGSVGElement>) => (
  <svg
    version='1.0'
    xmlns='http://www.w3.org/2000/svg'
    width='48.000000pt'
    height='48.000000pt'
    viewBox='0 0 48.000000 48.000000'
    preserveAspectRatio='xMidYMid meet'
    className={twMerge('default-icon', className)}
    {...props}>
    <g transform='translate(0.000000,48.000000) scale(0.100000,-0.100000)' fill='currentColor' stroke='none'>
      <path
        d='M80 240 l0 -210 136 0 136 0 -26 34 c-33 43 -33 56 -1 56 22 0 25 4
   25 35 0 31 3 35 25 35 25 0 25 1 25 78 l0 78 -53 52 -53 52 -107 0 -107 0 0
   -210z m300 103 c0 -2 -20 -3 -45 -3 l-45 0 0 47 0 47 45 -44 c25 -24 45 -45
   45 -47z m-151 -60 l13 -28 14 28 c9 16 23 27 35 27 20 0 20 0 -2 -40 l-23 -40
   23 -40 22 -40 -21 0 c-12 0 -25 10 -32 25 -6 14 -14 25 -18 25 -4 0 -12 -11
   -18 -25 -7 -15 -20 -25 -32 -25 -21 0 -21 0 2 41 l23 41 -22 39 -21 39 22 0
   c15 0 27 -9 35 -27z'
      />
      <path
        d='M370 136 c0 -30 -4 -35 -26 -38 l-26 -3 40 -45 41 -45 35 40 c41 46
   43 55 16 55 -16 0 -20 7 -20 35 0 32 -2 35 -30 35 -28 0 -30 -3 -30 -34z'
      />
    </g>
  </svg>
);
export default DownloadExcelIcon;
