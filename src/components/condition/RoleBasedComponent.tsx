import { Role } from '@/@types/dto/entity/role';
import { RootState } from '@/data';
import { GlobalState } from '@/data/global/global.slice';
import { useAppSelector } from '@/hooks/reduxHook';
import React, { PropsWithChildren } from 'react';

type Props = {
  allowedRoles: Role[];
  component?: React.ReactNode | string;
};

const RoleBasedComponent = ({ children, allowedRoles, component }: PropsWithChildren<Props>) => {
  // const userInfo: UserGetMeDTO | undefined = useAppSelector(
  //   (state: RootState) => state.auth.userInfo
  // );

  const { userInfo }: GlobalState = useAppSelector((state: RootState) => state.global);
  // console.log('userInfo', userInfo);

  const hasAccess = userInfo?.roles?.some((role) => allowedRoles.includes(role as Role));

  return <>{hasAccess ? (component ?? children) : null}</>;
};

export default RoleBasedComponent;
