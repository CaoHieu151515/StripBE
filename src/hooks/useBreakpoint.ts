import screensConfig from '@/utils/theme/screens.config';
import { useMediaQuery } from 'react-responsive';

const breakpoints = screensConfig;

export function useBreakpoint(breakpointKey: 'sm' | 'md' | 'lg' | 'xl' | '2xl') {
  const bool = useMediaQuery({
    query: `(min-width: ${breakpoints[breakpointKey]})`,
  });
  const capitalizedKey = breakpointKey[0].toUpperCase() + breakpointKey.substring(1);
  type Key = `is${Capitalize<any>}`;
  return {
    [`is${capitalizedKey}`]: bool,
  } as Record<Key, boolean>;
}

/*
const { isSm } = useBreakpoint('sm');
const { isMd } = useBreakpoint('md');
const { isLg } = useBreakpoint('lg');
console.log(isSm, isMd, isLg);
return (
    <div>
      {isSm && (
        Visible for sm: (min-width: 640px)
        <div>Content</div>
      )}

      {isMd && (
        Visible for md: (min-width: 768px)
        <div>Content</div>
      )}
    </div>
);
*/
