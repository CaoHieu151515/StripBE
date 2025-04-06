import { ReactElement } from 'react';
import { SideBarGroupTitleCode } from './SideBarGroupTitleCode.type';

export type SideBarGroupTemplate = {
  title: SideBarGroupTitleCode;
  icon: ReactElement;
  items?: { title: SideBarGroupTitleCode; icon: ReactElement }[];
};
