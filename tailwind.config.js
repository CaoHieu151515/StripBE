/** @type {import('tailwindcss').Config} */

import colors from './src/utils/theme/colors.config';
import screens from './src/utils/theme/screens.config';
import fontSize from './src/utils/theme/fontSize.config';

export default {
  content: ['./index.html', './src/**/*.{js,jsx,ts,tsx,css}'],
  theme: {
    extend: {
      screens,
      fontSize,
      colors,
      minWidth: {
        sidebar: '220px',
        'sidebar-collapsed': '60px',
      },
      maxWidth: {
        pc: '1440px',
      },
      spacing: {
        'navbar-sm': '48px',
        navbar: '60px',
        sidebar: '220px',
        'sidebar-collapsed': '60px',
      },
      boxShadow: {
        dashboardCard: '0px 1px 11.2px -1px #00000047, 0px 1px 4px 0px #00000026;',
        table: '0px 1px 2px -1px #0000001A, 0px 1px 3px 0px #0000001A',
      },
    },
  },
  plugins: [],
};
