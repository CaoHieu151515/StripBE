import { useBreakpoint } from '@/hooks/useBreakpoint';
import { LAYOUT_SIDEBAR_GROUP_TEMPLATES } from '@/utils/constants/layout.constant';
import { useEffect, useState } from 'react';
import { Outlet, useLocation, useNavigate } from 'react-router-dom';
import NavBar from './components/NavBar';
import SideBar from './components/SideBar';

export default function MainLayout() {
  const navigate = useNavigate();
  const location = useLocation();

  const { isXl: screenIsLargerThanXl } = useBreakpoint('xl');

  const [openSideBar, setOpenSideBar] = useState<boolean>(true);
  const [focusedTitles, setFocusedTitles] = useState<[string | undefined, string | undefined]>([undefined, undefined]);

  useEffect(() => {
    if (!screenIsLargerThanXl) setOpenSideBar(false);
    else setOpenSideBar(true);
  }, [screenIsLargerThanXl, location]);

  useEffect(() => {
    const tokens = location.pathname.replace(/^\/+|\/+$/g, '').split('/');
    const foundGroup = LAYOUT_SIDEBAR_GROUP_TEMPLATES.find((group) => group.title === tokens[0]);
    setFocusedTitles([tokens[0], undefined]);
    if (foundGroup) {
      const foundGroupItems = foundGroup.items;
      if (!foundGroupItems?.length) {
        if (!tokens[1]) setFocusedTitles([tokens[0], undefined]);
      } else {
        if (tokens[1]) {
          const foundGroupItem = foundGroupItems.find((item) => item.title === tokens[1]);
          if (foundGroupItem) {
            setFocusedTitles([tokens[0], tokens[1]]);
          } else {
            setFocusedTitles([tokens[0], undefined]);
          }
        }
      }
    } else {
      setFocusedTitles([undefined, undefined]);
    }
  }, [navigate, location]);

  return (
    <>
      <NavBar />
      <SideBar
        className={screenIsLargerThanXl ? 'z-[48] pb-navbar' : 'pb-32'}
        open={openSideBar}
        groups={LAYOUT_SIDEBAR_GROUP_TEMPLATES}
        focusedTitles={focusedTitles}
        onChangeOpen={() => setOpenSideBar((previous) => !previous)}
        alwaysOpen={screenIsLargerThanXl}
      />
      <div className='ml-sidebar mt-navbar bg-gray-100 p-4 max-xl:ml-sidebar-collapsed max-lg:mt-navbar-sm'>
        <Outlet />
      </div>
    </>
  );
}
