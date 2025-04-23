import { RootState } from '@/data';
import { GlobalState } from '@/data/global/global.slice';
import { useAppSelector } from '@/hooks/reduxHook';
import { useBreakpoint } from '@/hooks/useBreakpoint';
import {
  LAYOUT_SIDEBAR_GROUP_TEMPLATES,
  LAYOUT_SIDEBAR_GROUP_TEMPLATES_STAFF,
} from '@/utils/constants/layout.constant';
import { useEffect, useMemo, useState } from 'react';
import { Outlet, useLocation, useNavigate } from 'react-router-dom';
import NavBar from './components/NavBar';
import SideBar from './components/SideBar';
import { SideBarGroupTemplate } from './types/SideBarGroupTemplate.type';

export default function MainLayout() {
  const navigate = useNavigate();
  const location = useLocation();

  const { isXl: screenIsLargerThanXl } = useBreakpoint('xl');
  const { userInfo }: GlobalState = useAppSelector((state: RootState) => state.global);
  const [openSideBar, setOpenSideBar] = useState<boolean>(true);
  const [focusedTitles, setFocusedTitles] = useState<[string | undefined, string | undefined]>([undefined, undefined]);

  const sidebarGroupTemplate = useMemo<SideBarGroupTemplate[]>(() => {
    if (!userInfo?.roles) return [];

    if (userInfo.roles.includes('ROLE_ADMIN')) return LAYOUT_SIDEBAR_GROUP_TEMPLATES;
    if (userInfo.roles.includes('ROLE_STAFF')) return LAYOUT_SIDEBAR_GROUP_TEMPLATES_STAFF;
    return LAYOUT_SIDEBAR_GROUP_TEMPLATES;
  }, [userInfo?.roles]);

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
        groups={sidebarGroupTemplate}
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
