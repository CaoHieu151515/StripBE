import { useQuery } from 'react-query';
import { userApi } from './users.api';
import { useLocation } from 'react-router-dom';
import queryString from 'query-string';
import { PAGE_SIZE } from '@/utils/constants/shared.constant';

const useUsersData = () => {
  const location = useLocation();
  const params = queryString.parse(location.search);
  const page = +(params.page ?? 1);

  const fetchUserDataFunction = async () => {
    try {
      const response = await userApi.getAll({
        firstName: params.firstName as string,
        lastName: params.lastName as string,
        page: page - 1,
        size: PAGE_SIZE,
      });
      return response;
    } catch (e) {
      console.log(e);
      throw e;
    }
  };

  const queryKey = ['usersData', params.firstName, params.lastName, page];

  const {
    data: UserData,
    refetch: refreshUserData,
    isLoading,
    ...rest
  } = useQuery(queryKey, fetchUserDataFunction, {
    onError: (err) => console.log('error at hook', err),
    keepPreviousData: true,
  });

  // useEffect(() => {
  //   refreshUserData();
  // }, [page, refreshUserData]);

  return {
    UserData,
    refreshUserData,
    isLoading, // Đảm bảo isLoading được trả về đúng khi page thay đổi
    ...rest,
  };
};

export default useUsersData;
