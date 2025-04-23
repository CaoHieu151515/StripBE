import { useQuery } from 'react-query';
import { userApi } from './users.api';

export const useUserDetailData = (id?: string) => {
  const getProductUnitDetailFunction = async () => {
    const response = await userApi.getDetail(id ?? '');
    return response;
  };

  const queryKey = ['users', id];

  const {
    data: userDetailData,
    refetch: refreshUserDetailData,
    ...rest
  } = useQuery(queryKey, getProductUnitDetailFunction);

  return {
    userDetailData,
    refreshUserDetailData,
    ...rest,
  };
};
