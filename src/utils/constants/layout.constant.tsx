import AccountIcon from '@/components/icons/AccountIcon';
import CardIdIcon from '@/components/icons/CardIdIcon';
import CarIcon from '@/components/icons/CarIcon';
import ChartPieIcon from '@/components/icons/ChartPieIcon';
import ClipBoardIcon from '@/components/icons/ClipBoardIcon';
import CommentIcon from '@/components/icons/CommentIcon';
import CubeIcon from '@/components/icons/CubeIcon';
import MemberIcon from '@/components/icons/MemberIcon';
import MoneyCheckIcon from '@/components/icons/MoneyCheckIcon';
import UserGroupIcon from '@/components/icons/UserGroupIcon';
import WalletIcon from '@/components/icons/WalletIcon';
import { SideBarGroupTemplate } from '@/layouts/types/SideBarGroupTemplate.type';

export const LAYOUT_SIDEBAR_GROUP_TEMPLATES: SideBarGroupTemplate[] = [
  { title: 'dashboard', icon: <ChartPieIcon /> },
  {
    title: 'user',
    icon: <UserGroupIcon />,
    items: [
      { title: 'view-user-account', icon: <AccountIcon /> },
      { title: 'censor-driver-request', icon: <CardIdIcon /> },
      { title: 'staff', icon: <MemberIcon /> },
    ],
  },
  { title: 'trip', icon: <CarIcon /> },
  { title: 'censor-vehicle', icon: <ClipBoardIcon /> },
  {
    title: 'feedback',
    icon: <CommentIcon />,
  },
  {
    title: 'package',
    icon: <CubeIcon />,
  },
  {
    title: 'wallet',
    icon: <WalletIcon />,
  },
  // {
  //   title: 'rate-driver',
  //   icon: <RatingIcon />,
  // },
  // {
  //   title: 'report',
  //   icon: <ListCheckIcon />,
  // },
  {
    title: 'transaction-history',
    icon: <MoneyCheckIcon />,
  },
];

export const LAYOUT_SIDEBAR_GROUP_TEMPLATES_STAFF: SideBarGroupTemplate[] = [
  { title: 'dashboard', icon: <ChartPieIcon /> },
  {
    title: 'user',
    icon: <UserGroupIcon />,
    items: [
      { title: 'view-user-account', icon: <AccountIcon /> },
      { title: 'censor-driver-request', icon: <CardIdIcon /> },
    ],
  },
  { title: 'trip', icon: <CarIcon /> },
  { title: 'censor-vehicle', icon: <ClipBoardIcon /> },
  {
    title: 'feedback',
    icon: <CommentIcon />,
  },
  {
    title: 'package',
    icon: <CubeIcon />,
  },
  {
    title: 'wallet',
    icon: <WalletIcon />,
  },
  // {
  //   title: 'rate-driver',
  //   icon: <RatingIcon />,
  // },
  // {
  //   title: 'report',
  //   icon: <ListCheckIcon />,
  // },
  {
    title: 'transaction-history',
    icon: <MoneyCheckIcon />,
  },
];
