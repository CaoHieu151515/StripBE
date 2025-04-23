import { useQuery } from 'react-query';
import { userApi } from './users.api';
import { useLocation } from 'react-router-dom';
import queryString from 'query-string';

const useUsersData = () => {
  const location = useLocation();
  const params = queryString.parse(location.search);

  const fetchUserDataFunction = async () => {
    try {
      const response = await userApi.getAll({
        firstName: params.firstName as string,
        lastName: params.lastName as string,
      });
      return response?.content;
    } catch (e) {
      console.log(e);
      throw e;
    }
  };

  const queryKey = ['users', params];

  const {
    data: UserData,
    refetch: refreshUserData,
    ...rest
  } = useQuery(queryKey, fetchUserDataFunction, {
    onError: (err) => console.log('error at hook', err),
  });

  return {
    UserData,
    refreshUserData,
    ...rest,
  };
};

export default useUsersData;
